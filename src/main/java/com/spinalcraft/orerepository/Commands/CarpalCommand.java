package com.spinalcraft.orerepository.Commands;

import com.spinalcraft.orerepository.MarketManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CarpalCommand implements TabExecutor {

    private MarketManager manager;
    private String reloadMessage = "Reloading configuration for the Carpal Repository";
    private String missingValue = ChatColor.RED + "Missing a valid value for the global price cut";
    private String globalPriceCutValid = "Global Price Cut has been changed to ";
    private String cantSell = "The item cannot be sold at this time";
    private String cantBuy = "The item cannot be bought at this time";
    private String invalidPlayer = "The chosen player is not online";
    private String invalidMaterial = "The material is not a valid item";

    public CarpalCommand(MarketManager manager)
    {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            sender.sendMessage("Carpal Repository is a server repository in which players can sell specific raw ores, materials, etc... for some form of currency");
            return true;
        }
        if(args[0].equals("reload"))
            return reload(sender);
        if(args[0].equals("globalcut"))
            return globalCut(sender, args);
        if(args[0].equals("sale"))
            return sale(sender, args);
        if(args[0].equals("buy"))
            return buy(sender, args);
        if(args[0].equals("sell"))
            return sell(sender, args);
        if(args[0].equals("price"))
            return price(sender, args);
        if(args[0].equals("stock"))
            return stock(sender, args);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("reload", "globalcut", "sale", "buy", "sell", "stock", "price");
        if(args.length == 2 && (args[0].equals("sale") || args[0].equals("stock") || args[0].equals("price")))
            return manager.getMarketMaterials();
        if(args.length == 3 && (args[0].equals("sell") || args[0].equals("buy")))
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

    private boolean buy(CommandSender sender, String[] args)
    {
        if(args.length < 4)
        {
            sender.sendMessage(cantSell);
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if(player == null)
        {
            sender.sendMessage(invalidPlayer);
            return true;
        }
        Material material = Material.matchMaterial(args[2]);
        if(material == null)
        {
            sender.sendMessage(invalidMaterial);
            return true;
        }
        int amount = 0;
        try
        {
            amount = Integer.parseInt(args[3]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(missingValue);
            return true;
        }

        float itemValue = (float)amount * manager.getSellPrice(material);
        if(!manager.hasEnoughCurrency(player, itemValue))
        {
            sender.sendMessage("You do not have enough money to purchase this item");
            return true;
        }
        manager.buy(material, amount);
        manager.withdraw(player, itemValue);
        player.getInventory().addItem(new ItemStack(material, amount));
        sender.sendMessage("You bought " + amount + " " + args[2] + " for " + itemValue + " from the ore repository");
        return true;
    }

    private boolean sell(CommandSender sender, String[] args)
    {
        if(args.length < 4)
        {
            sender.sendMessage(cantSell);
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if(player == null)
        {
            sender.sendMessage(invalidPlayer);
            return true;
        }
        Material material = Material.matchMaterial(args[2]);
        if(material == null)
        {
            sender.sendMessage(invalidMaterial);
            return true;
        }
        int amount = 0;
        try
        {
            amount = Integer.parseInt(args[3]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(missingValue);
            return true;
        }

        float itemValue = (float)amount * manager.getSellPrice(material);
        manager.sell(material, amount);
        manager.deposit(player, itemValue);
        sender.sendMessage("You sold " + amount + " " + args[2] + " for " + itemValue + " to the ore repository");
        return true;
    }

    private boolean price(CommandSender sender, String[] args)
    {
        if(args.length < 2)
            return false;
        Material material = Material.matchMaterial(args[1]);
        if(material == null)
        {
            sender.sendMessage(invalidMaterial);
            return true;
        }

        float buyPrice = manager.getBuyPrice(material);
        float sellPrice = manager.getSellPrice(material);
        sender.sendMessage(material.name() + ": Buy[" + buyPrice + "] Sell[" + sellPrice + "]");
        return true;
    }

    private boolean stock(CommandSender sender, String[] args)
    {
        if(args.length < 2)
            return false;
        Material material = Material.matchMaterial(args[1]);
        if(material == null)
        {
            sender.sendMessage(invalidMaterial);
            return true;
        }

        sender.sendMessage(material.name() + ": Stock[" + manager.getCurrentStock(material) + "]");
        return true;
    }
}
