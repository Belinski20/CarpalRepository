package com.spinalcraft.orerepository;

import com.spinalcraft.orerepository.Commands.CarpalCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class OreRepository extends JavaPlugin {

    private MarketManager manager;
    private Economy econ;

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        setupEcon();
        try {
            manager = new MarketManager(econ, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("cr").setExecutor(new CarpalCommand(manager));

        // Saves every 6 hours since plugin started
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> manager.save(), 0, 144000);
    }


    @Override
    public void onDisable() {
        manager.save();
    }

    private void setupEcon()
    {
        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        if(econ != null)
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[Carpal Repository] found an Economy");
        else
            this.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "[Carpal Repository] did not find an Economy");
    }
}
