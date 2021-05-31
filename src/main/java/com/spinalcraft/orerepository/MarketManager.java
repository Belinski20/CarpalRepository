package com.spinalcraft.orerepository;

import com.spinalcraft.orerepository.Util.ConfigReader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.*;

public class MarketManager {

    private Plugin plugin;
    private Logic logic;
    private Market market;
    private Economy econ;
    private Set<RepositoryItem> repoItems;
    private Map<Material, Material> oreMap;

    private int globalPriceCut;

    public MarketManager(Economy econ, Plugin plugin) throws IOException {
        logic = new Logic();
        market = new Market();
        repoItems = new HashSet<>();
        this.econ = econ;
        this.plugin = plugin;
        initialize();
    }

    /**
     * Initializes all of the market items and related items
     */
    public void initialize() throws IOException {
        market.addMarketItems(ConfigReader.getMarketItems(plugin));
        repoItems.addAll(Arrays.asList(ConfigReader.getRepositoryItems(plugin)));
        oreMap = ConfigReader.getOreMap(plugin);
        globalPriceCut = ConfigReader.getGlobalPriceCut(plugin);
    }

    /**
     * Saves all the current market information
     */
    public void save()
    {
        try {
            ConfigReader.save(market, repoItems, plugin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the plugin
     * @return
     */
    public Plugin getPlugin()
    {
        return plugin;
    }

    /**
     * Returns if the reload was successful or not
     * @return
     */
    public boolean reload()
    {
        save();
        market.removeMarket();
        repoItems.clear();
        oreMap.clear();
        try
        {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Returns a list of all items in the market
     * @return
     */
    public List<String> getMarketMaterials()
    {
        Set<String> names = new HashSet<>();
        names.addAll(market.getMarketMaterials());
        names.addAll(getMappedItems());
        return new LinkedList<>(names);
    }

    public Set<String> getMappedItems()
    {
        Set<String> items = new HashSet<>();
        for(Material material : oreMap.keySet())
        {
            items.add(material.name());
        }
        return items;
    }

    /**
     * Updates the global price cut
     * @param globalPriceCut
     */
    public void updateGlobalPriceCut(int globalPriceCut)
    {
        this.globalPriceCut = globalPriceCut;
    }

    /**
     * Gets the mapped material if it exists
     * @param material
     * @return
     */
    public Material getMappedMaterial(Material material)
    {
        if(oreMap.containsKey(material))
            return oreMap.get(material);
        return material;
    }

    /**
     * Sell a specific item to the repository
     * @param material
     * @param amount
     * @return
     */
    public float sell(Material material, int amount)
    {
        if(market.addAmount(amount, material))
            return logic.getCurrentSellPrice(market.getMarketItem(material), getRepositoryItem(material), globalPriceCut);

        return 0;
    }

    /**
     * Buys a specific amount of an item from the repository
     * @param material
     * @param amount
     * @return
     */
    public float buy(Material material, int amount)
    {
        if(market.getAmount(material) < amount)
            return 0;

        float value = logic.getCurrentBuyPrice(market.getMarketItem(material), getRepositoryItem(material));
        market.removeAmount(amount, material);
        return value;
    }

    /**
     * Gets a specific item from the repository
     * @param material
     * @return
     */
    public RepositoryItem getRepositoryItem(Material material)
    {
        for(RepositoryItem item: repoItems)
        {
            if(item.getMaterial().equals(material))
                return item;
        }
        return null;
    }

    /**
     * Gets the current buy price for the item
     * @param material
     * @return
     */
    public float getBuyPrice(Material material)
    {
        return logic.getCurrentBuyPrice(market.getMarketItem(material), getRepositoryItem(material));
    }

    /**
     * Gets the current sell price for the item
     * @param material
     * @return
     */
    public float getSellPrice(Material material)
    {
        return logic.getCurrentSellPrice(market.getMarketItem(material), getRepositoryItem(material), globalPriceCut);
    }

    /**
     * Updates the sale modifier of a given item
     * @param material
     * @param saleModifier
     */
    public void updateSalePrice(Material material, int saleModifier)
    {
        getRepositoryItem(material).setSaleModifier(saleModifier);
    }

    /**
     * Gets the current stock of the item
     * @param material
     * @return
     */
    public int getCurrentStock(Material material)
    {
        return market.getMarketItem(material).getAmount();
    }

    /**
     * Returns if the player has enough money to cover the cost
     * @param player
     * @param cost
     * @return
     */
    public boolean hasEnoughCurrency(Player player, float cost)
    {
        return econ.getBalance(player) > cost;
    }

    /**
     * Withdraws a specific amount of money from a player
     * @param player
     * @param cost
     */
    public void withdraw(Player player, float cost)
    {
        econ.withdrawPlayer(player, cost);
    }

    /**
     * Deposits a specific amount of money to a player
     * @param player
     * @param cost
     */
    public void deposit(Player player, float cost)
    {
        econ.depositPlayer(player, cost);
    }
}
