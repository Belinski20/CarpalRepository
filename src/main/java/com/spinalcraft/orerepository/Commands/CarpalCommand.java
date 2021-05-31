package com.spinalcraft.orerepository.Commands;

import com.spinalcraft.orerepository.MarketManager;
import com.spinalcraft.orerepository.Util.ConfigReader;
import com.spinalcraft.orerepository.Util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CarpalCommand implements TabExecutor {

    private MarketManager manager;

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
            if(sender.hasPermission("Cr.reload.admin"))
                return reload(sender);
        if(args[0].equals("globalcut"))
            if(sender.hasPermission("Cr.globalcut.admin"))
                return globalCut(sender, args);
        if(args[0].equals("sale"))
            if(sender.hasPermission("Cr.sale.admin"))
                return sale(sender, args);
        if(args[0].equals("buy"))
            if(sender.hasPermission("Cr.buy"))
                return buy(sender, args);
        if(args[0].equals("sell"))
            if(sender.hasPermission("Cr.sell"))
                return sell(sender, args);
        if(args[0].equals("price"))
            if(sender.hasPermission("Cr.price"))
                return price(sender, args);
        if(args[0].equals("stock"))
            if(sender.hasPermission("Cr.stock"))
                return stock(sender, args);
        if(args[0].equals("additem"))
            if(sender.hasPermission("Cr.add.admin")) {
                try {
                    return addItem(sender, args);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1)
            return Arrays.asList("reload", "globalcut", "sale", "buy", "sell", "stock", "price", "additem");
        if(args.length == 2 && (args[0].equals("sale") || args[0].equals("stock") || args[0].equals("price")))
            return manager.getMarketMaterials();
        if(args.length == 3 && (args[0].equals("sell") || args[0].equals("buy")))
            return manager.getMarketMaterials();
        return null;
    }

    private boolean reload(CommandSender sender)
    {
        if(sender instanceof Player)
            sender.sendMessage(Messages.reloadMessage);
        sender.getServer().getConsoleSender().sendMessage(Messages.reloadMessage);
        return manager.reload();
    }

    private boolean globalCut(CommandSender sender, String[] args)
    {
        int value = 0;
        if(args.length < 2)
        {
            sender.sendMessage(Messages.globalCutInvalidParameters);
            return true;
        }
        try
        {
            value = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(Messages.globalCutInvalidValue);
            return true;
        }
        manager.updateGlobalPriceCut(value);
        if(sender instanceof Player)
            sender.sendMessage(Messages.globalPriceCutValid + value);
        sender.getServer().getConsoleSender().sendMessage(sender.getName() + ": " + Messages.globalPriceCutValid + value);
        return true;
    }

    private boolean sale(CommandSender sender, String[] args)
    {
        int value = 0;
        if(args.length < 3)
        {
            sender.sendMessage(Messages.saleInvalidParameters);
            return true;
        }
        Material material = Material.matchMaterial(args[1]);
        if(material == null)
        {
            sender.sendMessage(Messages.invalidMaterial);
            return true;
        }
        try
        {
            value = Integer.parseInt(args[2]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(Messages.saleInvalidValue);
            return true;
        }

        Material material2 = manager.getMappedMaterial(material);

        manager.updateSalePrice(material2, value);
        String message = Messages.salePriceChange.replace("Material", material.name()).replace("Value", String.valueOf(value));
        if(sender instanceof Player)
            sender.sendMessage(message);
        sender.getServer().getConsoleSender().sendMessage(message);
        return true;
    }

    private boolean buy(CommandSender sender, String[] args)
    {
        if(args.length < 4)
        {
            sender.sendMessage(Messages.buyInvalidParameters);
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null)
        {
            sender.sendMessage(Messages.invalidPlayer);
            return true;
        }

        Material material = Material.matchMaterial(args[2]);
        if(material == null)
        {
            sender.sendMessage(Messages.invalidMaterial);
            return true;
        }

        int amount = 0;
        try
        {
            amount = Integer.parseInt(args[3]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(Messages.buyInvalidValue);
            return true;
        }

        if(manager.getCurrentStock(material) < amount)
        {
            sender.sendMessage(Messages.buyNotEnoughStock);
            return true;
        }

        float itemValue = (float)amount * manager.getSellPrice(material);
        if(!manager.hasEnoughCurrency(player, itemValue))
        {
            sender.sendMessage(Messages.notEnoughMoney);
            return true;
        }

        material = manager.getMappedMaterial(material);

        manager.buy(material, amount);
        manager.withdraw(player, itemValue);
        player.getInventory().addItem(new ItemStack(material, amount));
        String message = Messages.successfulPurchase.replace("Amount", String.valueOf(amount)).replace("Material", material.name()).replace("Value", String.valueOf(itemValue));
        if(sender instanceof Player)
            if(sender.equals(player))
                sender.sendMessage(message);
            else
            {
                sender.sendMessage(message.replace("You bought", "You forced " + player.getName() + " to buy "));
                player.sendMessage(message);
            }
        sender.getServer().getConsoleSender().sendMessage(message.replace("You", player.getName()));
        return true;
    }

    private boolean sell(CommandSender sender, String[] args)
    {
        if(args.length < 4)
        {
            sender.sendMessage(Messages.sellInvalidParameters);
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null)
        {
            sender.sendMessage(Messages.invalidPlayer);
            return true;
        }

        Material material = Material.matchMaterial(args[2]);
        if(material == null)
        {
            sender.sendMessage(Messages.invalidMaterial);
            return true;
        }

        int amount = 0;
        try
        {
            amount = Integer.parseInt(args[3]);
        }
        catch(NumberFormatException e)
        {
            sender.sendMessage(Messages.sellCutInvalidValue);
            return true;
        }

        Material material2 = manager.getMappedMaterial(material);

        float itemValue = (float)amount * manager.getSellPrice(material2);
        manager.sell(material2, amount);
        manager.deposit(player, itemValue);
        String message = Messages.successfulSell.replace("Amount", String.valueOf(amount)).replace("Material", material.name()).replace("Value", String.valueOf(itemValue));
        if(sender instanceof Player)
            if(sender.equals(player))
                sender.sendMessage(message);
            else
            {
                sender.sendMessage(message.replace("You sold", "You forced " + player.getName() + " to sell "));
                player.sendMessage(message);
            }
        sender.getServer().getConsoleSender().sendMessage(message.replace("You", player.getName()));
        return true;
    }

    private boolean price(CommandSender sender, String[] args)
    {
        if(args.length < 2)
        {
            sender.sendMessage(Messages.priceInvalidParameters);
            return true;
        }

        Material material = Material.matchMaterial(args[1]);
        if(material == null)
        {
            sender.sendMessage(Messages.invalidMaterial);
            return true;
        }

        Material material2 = manager.getMappedMaterial(material);

        float buyPrice = manager.getBuyPrice(material2);
        float sellPrice = manager.getSellPrice(material2);
        String message = Messages.priceBuySell.replace("Material", material.name()).replace("cost", String.valueOf(buyPrice)).replace("value", String.valueOf(sellPrice));
        sender.sendMessage(message);
        return true;
    }

    private boolean stock(CommandSender sender, String[] args)
    {
        if(args.length < 2)
        {
            sender.sendMessage(Messages.stockInvalidParameters);
            return true;
        }

        Material material = Material.matchMaterial(args[1]);
        if(material == null)
        {
            sender.sendMessage(Messages.invalidMaterial);
            return true;
        }

        Material material2 = manager.getMappedMaterial(material);

        String message = Messages.stockCheck.replace("Material", material.name()).replace("Amount", String.valueOf(manager.getCurrentStock(material2)));
        sender.sendMessage(message);
        return true;
    }

    private boolean addItem(CommandSender sender, String[] args) throws IOException {
        if(args.length < 2)
        {
            sender.sendMessage(Messages.addItemInvalidParameters);
            return true;
        }

        Material material = Material.matchMaterial(args[1]);
        if(material == null)
        {
            sender.sendMessage(Messages.invalidMaterial);
            return true;
        }

        if(ConfigReader.generateItemTemplate(material, manager.getPlugin()))
        {
            if(sender instanceof Player)
                sender.sendMessage(Messages.successfulItemAdd.replace("Material", material.name()));
            sender.getServer().getConsoleSender().sendMessage(Messages.successfulItemAdd.replace("Material", material.name()));
        }
        else
        {
            sender.sendMessage(Messages.itemAlreadyExists.replace("Material", material.name()));
        }
        return true;
    }
}
