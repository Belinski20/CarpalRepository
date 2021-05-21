package com.spinalcraft.orerepository.Util;

import com.spinalcraft.orerepository.Market;
import com.spinalcraft.orerepository.MarketItem;
import com.spinalcraft.orerepository.RepositoryItem;
import org.bukkit.Material;

import java.util.Map;

public class ConfigReader {

    /**
     * Gets all the items from the market file
     * @return
     */
    public static MarketItem[] getMarketItems()
    {
        return null;
    }

    /**
     * Gets all the repository items from the repo file
     * @return
     */
    public static RepositoryItem[] getRepositoryItems()
    {
        return null;
    }

    /**
     * Gets the mapping between ores from the mapping file
     * @return
     */
    public static Map<Material, Material> getOreMap()
    {
        return null;
    }

    public static int getGlobalPriceCut()
    {
        return 0;
    }

    public static void save(Market market)
    {

    }

}
