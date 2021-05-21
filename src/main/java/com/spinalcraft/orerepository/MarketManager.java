package com.spinalcraft.orerepository;

import com.spinalcraft.orerepository.Util.ConfigReader;
import org.bukkit.Material;

import java.util.*;

public class MarketManager {

    private Logic logic;
    private Market market;
    private Set<RepositoryItem> repoItems;
    private Map<Material, Material> oreMap;

    private int globalPriceCut;

    public MarketManager()
    {
        logic = new Logic();
        market = new Market();
        repoItems = new HashSet<>();
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
        market.addAmount(amount, material);
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
}
