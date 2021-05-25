package com.spinalcraft.orerepository;

import com.spinalcraft.orerepository.Util.ConfigReader;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class MarketManager {

    private Logic logic;
    private Market market;
    private Economy econ;
    private Set<RepositoryItem> repoItems;
    private Map<Material, Material> oreMap;

    private int globalPriceCut = 20;

    public MarketManager(Economy econ)
    {
        logic = new Logic();
        market = new Market();
        repoItems = new HashSet<>();
        this.econ = econ;
        setupExampleItems();
    }

    public void setupExampleItems()
    {
        addRepoItem(Material.IRON_ORE, 64, 10, 2, 90, 5, 1, 5);
        addRepoItem(Material.GOLD_ORE, 48, 10, 2, 90, 5, 1, 5);
        addRepoItem(Material.COAL_ORE, 32, 10, 2, 90, 5, 1, 5);
        addRepoItem(Material.DIAMOND_ORE, 16, 10, 2, 90, 5, 1, 5);
    }

    public void addRepoItem(Material material, int defaultAmount, int defaultPrice, int minPrice, int maxPrice, int stepPrice, int stepSize, int saleModifier)
    {
        RepositoryItem item = new RepositoryItem();
        item.setMaterial(material);
        item.setDefaultAmount(defaultAmount);
        item.setDefaultPrice(defaultPrice);
        item.setMaxPrice(maxPrice);
        item.setMinPrice(minPrice);
        item.setStepPrice(stepPrice);
        item.setStepSize(stepSize);
        item.setSaleModifier(saleModifier);
        repoItems.add(item);
    }

    /**
     * Initializes all of the market items and related items
     */
    public void initialize()
    {
        market.addMarketItems(ConfigReader.getMarketItems());
        repoItems.addAll(Arrays.asList(ConfigReader.getRepositoryItems()));
        oreMap = ConfigReader.getOreMap();
        globalPriceCut = ConfigReader.getGlobalPriceCut();
    }

    /**
     * Gets the economy on the server
     * @return
     */
    public Economy getEcon()
    {
        return econ;
    }

    /**
     * Saves all the current market information
     */
    public void save()
    {
        ConfigReader.save(market);
    }

    /**
     * Returns if the reload was successful or not
     * @return
     */
    public boolean reload()
    {
        return true;
    }

    /**
     * Returns a list of all items in the market
     * @return
     */
    public List<String> getMarketMaterials()
    {
        return market.getMarketMaterials();
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

    public void withdraw(Player player, float cost)
    {
        econ.withdrawPlayer(player, cost);
    }

    public void deposit(Player player, float cost)
    {
        econ.depositPlayer(player, cost);
    }
}
