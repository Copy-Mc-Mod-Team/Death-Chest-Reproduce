package io.github.baka4n.deathchestreproduce.fabric;

import io.github.baka4n.deathchestreproduce.DeathChestReproduce;
import net.fabricmc.api.ModInitializer;

public class DeathChestReproduceFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        DeathChestReproduce.init();
    }
}