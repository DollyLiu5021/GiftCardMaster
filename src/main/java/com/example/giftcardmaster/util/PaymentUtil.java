package com.example.giftcardmaster.util;

public class PaymentUtil {

    public static String getCurrencyByContryCode(String countryCode) {
        String currency;
        switch (countryCode) {
            case "US":
                currency = "USD";
                break;
            case "CA":
                currency = "CAD";
                break;
            case "CN":
                currency = "RMB";
                break;
            default:
                currency = "USD";
        }
        return currency;
    }
}
