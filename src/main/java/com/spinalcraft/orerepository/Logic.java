package com.spinalcraft.orerepository;

public class Logic {

    /**
     * Gets the current price for the item based on availability
     * @param mItem
     * @param rItem
     * @return
     */
    public float getCurrentBuyPrice(MarketItem mItem, RepositoryItem rItem)
    {
        // If item is new then max price is 0
        // Also if max price is 0 then the most an item should be is 0
        if(rItem.getMaxPrice() == 0)
            return 0;
        // Default cost of the item
        int defaultCost = rItem.getDefaultPrice();

        // Difference between default amount of item and the current amount
        // If there is more steps should be positive, otherwise should be negative
        // default: 50 current: 25 || 25 - 50 = -25 step: 3 -25 / 3 = -8
        int steps = -(mItem.getAmount() - rItem.getDefaultAmount()) / rItem.getStepSize();

        // Current price based on default cost and the amount of steps
        float modifiedCost = defaultCost + (rItem.getStepPrice() * steps);

        // If the modified price is lower than the min price or greater than the max price make it the min or max price
        if(modifiedCost < rItem.getMinPrice())
            modifiedCost = rItem.getMinPrice();
        else  if(modifiedCost > rItem.getMaxPrice())
                modifiedCost = rItem.getMaxPrice();

        // Apply the sale modifier to the cost
        return modifiedCost *= ((float)rItem.getSaleModifier() / 100);
    }

    /**
     * Gets the current price if you were to sell the given item
     * @param mItem
     * @param rItem
     * @param globalPriceCut
     * @return
     */
    public float getCurrentSellPrice(MarketItem mItem, RepositoryItem rItem, int globalPriceCut)
    {
        // Get the current buy price
        float value = getCurrentBuyPrice(mItem, rItem);

        return value *= ((float) globalPriceCut / 100);
    }


}
