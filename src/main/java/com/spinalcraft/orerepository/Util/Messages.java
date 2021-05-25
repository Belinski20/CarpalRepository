package com.spinalcraft.orerepository.Util;

public class Messages {

    public static String reloadMessage = "Reloading configuration for the Carpal Repository";
    public static String globalPriceCutValid = "Global Price Cut has been changed to ";
    public static String globalCutInvalidParameters = "Usage - /cr globalcut <value>";
    public static String globalCutInvalidValue = "The value used for the global cut is not a valid number";
    public static String saleInvalidParameters = "Usage - /cr sale <item material> <value>";
    public static String invalidMaterial = "The given material type is not a valid material type";
    public static String saleInvalidValue = "The value used for the sale is not a valid number";
    public static String salePriceChange = "Sale for Material updated to Value % off";
    public static String buyInvalidParameters = "Usage - /cr buy <user> <item material> <amount>";
    public static String invalidPlayer = "The given player either is offline or does not exist";
    public static String buyInvalidValue = "The amount of items to buy is not a value number";
    public static String notEnoughMoney = "You do not have enough money to purchase this item";
    public static String successfulPurchase = "You bought Amount Material for Value from the Carpal Repository";
    public static String buyNotEnoughStock = "There is not enough items in the Carpal Repository to complete this purchase";
    public static String sellInvalidParameters = "Usage - /cr sell <user> <item material> <amount>";
    public static String sellCutInvalidValue = "The amount of items to sell is not a value number";
    public static String successfulSell = "You sold Amount Material for Value to the Carpal Repository";
    public static String priceInvalidParameters = "Usage - /cr price <item material>";
    public static String priceBuySell = "Material: Buy[cost] Sell[value]";
    public static String stockInvalidParameters = "Usage - /cr stock <item material>";
    public static String stockCheck = "Material: Stock[Amount]";
}
