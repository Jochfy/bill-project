package com.example.bill.helpers;

import lombok.RequiredArgsConstructor;

/**
 * Helper useful for constant messages
 */
public class LabelConstantHelper {

    private LabelConstantHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static final String A = "à";
    public static final String LINE_SEPARATOR = "line.separator";
    public static final String HEAD_OF_BILL = "Facture";
    public static final String TAX_AMOUNT_LABEL = "Montant des taxes";
    public static final String TOTAL_AMOUNT_LABEL = "Total";
    public static final String CURRENCY_SIGN = "€";
    public static final String ASTERISK_SIGN = "*";
    public static final String SPACE = " ";
    public static final String IMPORTED = "importé(s)";
    public static final String COLON_SEPARATION_SIGN = ":";
    public static final String COMMA_SEPARATION_SIGN = ":";
    public static final String EXCLUSIVE_OF_TAXES = "HT";
    public static final String INCLUSIVE_OF_TAXES = "TTC";

}
