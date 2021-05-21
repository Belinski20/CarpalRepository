package com.spinalcraft.orerepository;

import com.spinalcraft.orerepository.Commands.CarpalCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class OreRepository extends JavaPlugin {

    private MarketManager manager;

    @Override
    public void onEnable() {
        manager = new MarketManager();
        manager.initialize();
        getCommand("cr").setExecutor(new CarpalCommand(manager));
    }

    @Override
    public void onDisable() {
        manager.save();
    }
}
