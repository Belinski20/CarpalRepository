package com.spinalcraft.orerepository.Util;

import com.spinalcraft.orerepository.Market;
import com.spinalcraft.orerepository.MarketItem;
import com.spinalcraft.orerepository.RepositoryItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigReader {

    /**
     * Gets all the items from the market file
     * @return
     */
    public static MarketItem[] getMarketItems(Plugin plugin) throws IOException {
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "market.yml");
        if(file.createNewFile())
        {
            config = YamlConfiguration.loadConfiguration(file);
            config.set(Material.IRON_ORE.name(), 64);
            config.save(file);
        }
        config = YamlConfiguration.loadConfiguration(file);
        Set<String> names = config.getKeys(false);
        MarketItem[] items = new MarketItem[names.size()];
        int index = 0;
        for(String name: names)
        {
            Material material = Material.matchMaterial(name);
            if(material == null)
            {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "Item " + name + " in the market config is not a valid material");
                continue;
            }
            MarketItem item = new MarketItem();
            item.setMaterial(material);
            item.setAmount(config.getInt(name));
            items[index++] = item;
        }
        return items;
    }

    /**
     * Gets all the repository items from the repo file
     * @return
     */
    public static RepositoryItem[] getRepositoryItems(Plugin plugin) throws IOException {
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "repository.yml");
        if(file.createNewFile())
        {
            config = YamlConfiguration.loadConfiguration(file);
            config.set("global_price_cut", 20);
            config.set(Material.IRON_ORE.name() + ".default_price", 50);
            config.set(Material.IRON_ORE.name() + ".default_amount", 64);
            config.set(Material.IRON_ORE.name() + ".min_price", 1);
            config.set(Material.IRON_ORE.name() + ".max_price", 150);
            config.set(Material.IRON_ORE.name() + ".step_amount", 3);
            config.set(Material.IRON_ORE.name() + ".step_price", 1);
            config.set(Material.IRON_ORE.name() + ".sale_modifier", 25);
            config.save(file);
        }
        config = YamlConfiguration.loadConfiguration(file);
        Set<String> names = config.getKeys(false);
        RepositoryItem[] items = new RepositoryItem[names.size() - 1];
        int index = 0;
        for(String name: names)
        {
            if(name.equals("global_price_cut"))
                continue;
            Material material = Material.matchMaterial(name);
            if(material == null)
            {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Item " + name + " in the repository config is not a valid material");
                continue;
            }
            RepositoryItem item = new RepositoryItem();
            item.setMaterial(material);
            item.setDefaultPrice(config.getInt(name + ".default_price"));
            item.setDefaultAmount(config.getInt(name + ".default_amount"));
            item.setMinPrice(config.getInt(name + ".min_price"));
            item.setMaxPrice(config.getInt(name + ".max_price"));
            item.setStepSize(config.getInt(name + ".step_amount"));
            item.setStepPrice(config.getInt(name + ".step_price"));
            item.setSaleModifier(config.getInt(name + ".sale_modifier"));
            items[index++] = item;
        }
        return items;
    }

    /**
     * Gets the mapping between ores from the mapping file
     * @return
     */
    public static Map<Material, Material> getOreMap(Plugin plugin) throws IOException {
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "map.yml");
        String[] materials = new String[2];
        materials[0] = Material.OAK_PLANKS.name();
        materials[1] = Material.DARK_OAK_PLANKS.name();
        if(file.createNewFile())
        {
            config = YamlConfiguration.loadConfiguration(file);
            config.set(Material.OAK_PLANKS.name(), materials);
            config.save(file);
        }
        config = YamlConfiguration.loadConfiguration(file);
        Set<String> names = config.getKeys(false);
        Map<Material, Material> items = new HashMap<>();
        for(String name: names)
        {
            Material material = Material.matchMaterial(name);
            if(material == null)
            {
                plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Item " + name + " in the repository config is not a valid material");
                continue;
            }
            for(String child: config.getStringList(name))
            {
                Material childMaterial = Material.matchMaterial(child);
                if(material == null)
                {
                    plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Item " + child + " in the Map config is not a valid material");
                    continue;
                }
                if(items.containsKey(childMaterial))
                {
                    plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Item " + child + " is mapped twice in the Map config");
                    continue;
                }
                items.put(childMaterial, material);
            }
        }
        return items;
    }

    /**
     *
     * @return
     */
    public static int getGlobalPriceCut(Plugin plugin)
    {
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "repository.yml");
        config = YamlConfiguration.loadConfiguration(file);
        return config.getInt("global_price_cut");
    }

    /**
     * Saves the items in the market and the repository information
     * @param market
     */
    public static void save(Market market, Set<RepositoryItem> repoItems, Plugin plugin) throws IOException {
        FileConfiguration config;
        File file = new File(plugin.getDataFolder(), "market.yml");
        config = YamlConfiguration.loadConfiguration(file);

        for(MarketItem mItem : market.getMarketItems())
            config.set(mItem.getMaterial().name(), mItem.getAmount());

        config.save(file);

        file = new File(plugin.getDataFolder(), "repository.yml");
        config = YamlConfiguration.loadConfiguration(file);

        for(RepositoryItem rItem : repoItems)
            config.set(rItem.getMaterial().name() + ".sale_modifier", rItem.getSaleModifier());

        config.save(file);
    }

}
