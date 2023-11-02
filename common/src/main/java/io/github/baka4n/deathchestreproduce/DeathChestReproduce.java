package io.github.baka4n.deathchestreproduce;

import dev.architectury.event.Event;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.concurrent.atomic.AtomicInteger;

public class DeathChestReproduce
{
	public static final String MOD_ID = "deathchestreproduce";


	public static void init() {

		EntityEvent.LIVING_DEATH.register((entity, source) -> {
			if (entity instanceof PlayerEntity player) {
				if (!player.getWorld().getGameRules().get(GameRules.KEEP_INVENTORY).get()) {
					double x = player.getX();
					double y = player.getY();
					double z = player.getZ();
					PlayerInventory inventory = player.getInventory();
					DefaultedList<ItemStack> itemStacks = allInAll(inventory.main, inventory.offHand, inventory.armor);
					for (ItemStack itemStack : itemStacks) {
						if (allChestBlockEntityGen(player, itemStack, (int) x, (int) y, (int) z, itemStacks)) {
							return EventResult.interruptTrue();
						}
					}
					inventory.clear();
				}

			}
			return EventResult.pass();
		});
	}

	public static boolean allChestBlockEntityGen(PlayerEntity player, ItemStack itemStack, int x, int y, int z, DefaultedList<ItemStack> itemStacks) {

		if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ChestBlock block) {
			World world = player.getWorld();
			BlockPos pos = new BlockPos(x, y, z);
			BlockState defaultState = block.getDefaultState();
			defaultState.with(FacingBlock.FACING, player.getHorizontalFacing());
			world.setBlockState(pos, defaultState);
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ChestBlockEntity chestBlockEntity) {
				boolean b = false;//delete one chest
				for (int i = 0; i < itemStacks.size(); i++) {
					ItemStack stack = itemStacks.get(i);
					if (i < chestBlockEntity.size()) {
						if (!b && stack.getItem() instanceof BlockItem bi && bi.getBlock().equals(blockItem.getBlock())) {
							if (stack.getCount() > 1) {
								stack.setCount(stack.getCount() - 1);
							} else {
								stack = ItemStack.EMPTY;
							}
							b = true;
						}
						if (!stack.isEmpty()) {
							chestBlockEntity.setStack(i, stack);
						}
					} else {
						player.dropStack(stack);
					}
				}
			}
			return true;
		}
		return false;
	}

	@SafeVarargs
	public static DefaultedList<ItemStack> allInAll(DefaultedList<ItemStack>... lists) {
		AtomicInteger integer = new AtomicInteger();
		for (DefaultedList<ItemStack> list : lists) integer.addAndGet(list.size());
		DefaultedList<ItemStack> tmp = DefaultedList.ofSize(integer.get(), ItemStack.EMPTY);
		for (DefaultedList<ItemStack> list : lists) tmp.addAll(list);
		return tmp;
	}
}
