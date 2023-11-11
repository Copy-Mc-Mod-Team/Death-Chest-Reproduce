package io.github.baka4n.deathchestreproduce.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.baka4n.deathchestreproduce.DeathChestReproduce;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DeathChestReproduce.MOD_ID)
public class DeathChestReproduceForge {
    public DeathChestReproduceForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(DeathChestReproduce.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        DeathChestReproduce.init();

    }
}