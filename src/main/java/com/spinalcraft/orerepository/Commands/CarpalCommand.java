package com.spinalcraft.orerepository.Commands;

import com.spinalcraft.orerepository.MarketManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CarpalCommand implements TabExecutor {

    private MarketManager manager;
    private String reloadMessage = "Reloading configuration for the Carpal Repository";
    private String missingValue = ChatColor.RED + "Missing a valid value for the global price cut";
    private String globalPriceCutValid = "Global Price Cut has been changed to ";

    public CarpalCommand(MarketManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0].equals("reload"))
            return reload(sender);
        if(args[0].equals("globalcut"))
            return globalCut(sender, args);
        if(args[0].equals("sale"))
            return sale(sender, args);
        if(args[0].equals("buy"))
            return sale(sender, args);
        if(args[0].equals("sell"))
            return sale(sender, args);
        if(args[0].equals("price"))
            return price();
        if(args[0].equals("stock"))
            return stock();
        sender.sendMessage("Carpal Repository is a server repository in which players can sell specific raw ores, materials, etc... for some form of currency");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("reload", "globalcut", "sale", "buy", "sell");
        if(args.length == 2 && args[1].equals("sale"))
            return manager.getMarketMaterials();
        return null;
    }

    private boolean reload(CommandSender sender)
    {
        if(sender instanceof Player)
            sender.sendMessage(reloadMessage);
        sender.getServer().getConsoleSender().sendMessage(reloadMessage);
        return manager.reload();
    }

    private boolean globalCut(CommandSender sender, String[] args)
    {
        int value = 0;
        if(args.length < 3)
        {
            sender.sendMessage(missingValue);
            return true;
        }
        try
        {
            value = Integer.parseInt(args[2]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(missingValue);
            return true;
        }
        manager.updateGlobalPriceCut(value);
        if(sender instanceof Player)
            sender.sendMessage(globalPriceCutValid + value);
        sender.getServer().getConsoleSender().sendMessage(sender.getName() + ": " + globalPriceCutValid + value);
        return true;
    }

    private boolean sale(CommandSender sender, String[] args)
    {
        return true;
    }

    private boolean buy()
    {
        return true;
    }

    private boolean sell()
    {
        return true;
    }

    private boolean price()
    {
        return true;
    }

    private boolean stock()
    {
        return true;
    }
}
