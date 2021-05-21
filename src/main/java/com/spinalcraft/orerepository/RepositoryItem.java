package com.spinalcraft.orerepository;

import org.bukkit.Material;

public class RepositoryItem {

    private Material material;
    private int defaultAmount;
    private int defaultPrice;
    private int minPrice;
    private int maxPrice;
    private int stepSize;
    private int stepPrice;
    private int saleModifier;

    public RepositoryItem()
    {

    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(int defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(int defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getStepSize() {
        return stepSize;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public int getStepPrice() {
        return stepPrice;
    }

    public void setStepPrice(int stepPrice) {
        this.stepPrice = stepPrice;
    }

    public int getSaleModifier() {
        return saleModifier;
    }

    public void setSaleModifier(int saleModifier) {
        this.saleModifier = saleModifier;
    }
}
