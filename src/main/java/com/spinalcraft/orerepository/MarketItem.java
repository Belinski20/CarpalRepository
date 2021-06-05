package com.spinalcraft.orerepository;

import org.bukkit.Material;

public class MarketItem {

    private Material material;
    private int amount;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount)
    {
        this.amount += amount;
    }

    public void removeAmount(int amount)
    {
        this.amount -= amount;
    }

    @Override
    public boolean equals(Object o) {
        return this.material == o;
    }
}
