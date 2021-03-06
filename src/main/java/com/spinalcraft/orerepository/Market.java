package com.spinalcraft.orerepository;

import org.bukkit.Material;

import java.util.*;

public class Market {

    private Set<MarketItem> marketItems;

    public Market()
    {
        marketItems = new HashSet<>();
    }

    /**
     * Creates a new market item with the material and the amount of the items
     * @param amount
     * @param material
     * @return If the item is successfully added to the market
     */
    public boolean addMarketItem(int amount, Material material)
    {
        MarketItem item = new MarketItem();
        item.setAmount(amount);
        item.setMaterial(material);
        return marketItems.add(item);
    }

    /**
     * Adds all market items to the market
     * @param items
     * @return
     */
    public boolean addMarketItems(MarketItem[] items)
    {
        return marketItems.addAll(Arrays.asList(items));
    }

    /**
     * Gets the amount of an object if it is in the market
     * @param material
     * @return Amount of a given item
     */
    public int getAmount(Material material)
    {
        for(MarketItem item: marketItems)
        {
            if(item.equals(material))
            {
                return item.getAmount();
            }
        }
        return 0;
    }

    /**
     * Adds a given amount to an item
     * @param amount
     * @param material
     * @return If the item is in the market
     */
    public boolean addAmount(int amount, Material material)
    {
        for(MarketItem item: marketItems)
        {
            if(item.equals(material))
            {
                item.addAmount(amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a given amount from an item
     * @param amount
     * @param material
     * @return If the item has at least the amount and exists in the market
     */
    public boolean removeAmount(int amount, Material material)
    {
        for(MarketItem item: marketItems)
        {
            if(item.equals(material))
            {
                if(item.getAmount() < amount)
                    return false;
                item.removeAmount(amount);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all items from the market
     * Used to backup and save item information
     * @return The set of all market items
     */
    public Set<MarketItem> getMarketItems()
    {
        return this.marketItems;
    }

    /**
     * Gets all materials which are currently being used in the market
     * @return
     */
    public List<String> getMarketMaterials()
    {
        List<String> materials = new LinkedList<>();
        for(MarketItem item: marketItems)
        {
            materials.add(item.getMaterial().name());
        }
        return materials;
    }

    /**
     * Gets a specific item from the market
     * @param material
     * @return
     */
    public MarketItem getMarketItem(Material material)
    {
        for(MarketItem item: marketItems)
        {
            if(item.getMaterial().equals(material))
                return item;
        }
        return null;
    }

    /**
     * Removes all items from the market
     */
    public void removeMarket()
    {
        marketItems.clear();
    }

}
