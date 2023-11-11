package io.github.baka4n.deathchestreproduce;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class DeathChestReproduce
{
	public static final String MOD_ID = "deathchestreproduce";


	public static void init() {
//		TickEvent.SERVER_PRE.register(instance -> {
//			instance.sendMessage(Text.empty().append("test"));
//		});

		EntityEvent.LIVING_DEATH.register((entity, source) -> {
			if (entity instanceof PlayerEntity player) {
				if (!player.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).get()) {
					double x = player.getX();
					double y = player.getY();
					double z = player.getZ();
					boolean b = true;
					PlayerInventory inventory = player.getInventory();
					b = chestSelect(player, inventory, b, (int) x, (int) y, (int) z);
					chestSaveOrDrop(player, b, (int) x, (int) y, (int) z, inventory);
				}

			}
			return EventResult.pass();
		});
	}

	private static void chestSaveOrDrop(PlayerEntity player, boolean b, int x, int y, int z, PlayerInventory inventory) {
		if (!b) {
			BlockEntity blockEntity = player.getWorld().getBlockEntity(new BlockPos(x, y, z));
			if (blockEntity instanceof ChestBlockEntity chestEntity) {
				for (int i = 0; i < inventory.size(); i++) {
					ItemStack stack = inventory.getStack(i);
					if (i < chestEntity.size()) {
						chestEntity.setStack(i, stack);
					} else {
						player.dropStack(stack);
					}
				}
				inventory.clear();
			}
		}
	}

	private static boolean chestSelect(PlayerEntity player, PlayerInventory inventory, boolean b, int x, int y, int z) {
		for (int i = 0; i < inventory.size(); i++) {
			if (b) {
				ItemStack stack = inventory.getStack(i);
				if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ChestBlock chest) {
					b = false;
					player.getWorld().setBlockState(new BlockPos(x, y, z), chest.getDefaultState().with(ChestBlock.FACING, player.getHorizontalFacing()));
					if (stack.getCount() > 1) {
						stack.setCount(stack.getCount() - 1);
						inventory.setStack(i, stack);
					} else {
						inventory.setStack(i, ItemStack.EMPTY);
					}
				}
			}
		}
		return b;
	}
}
