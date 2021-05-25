package com.spinalcraft.orerepository;

import com.spinalcraft.orerepository.Commands.CarpalCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class OreRepository extends JavaPlugin {

    private MarketManager manager;
    private Economy econ;

    @Override
    public void onEnable() {
        setupEcon();
        manager = new MarketManager(econ);
        //manager.initialize();
        getCommand("cr").setExecutor(new CarpalCommand(manager));
    }

    private void setupEcon()
    {
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        if(econ != null)
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Carpal Repository] found Economy");
        else
            this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Carpal Repository] did not find Economy");
    }

    @Override
    public void onDisable() {
        manager.save();
    }
}
