package com.example.bill.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Helpers used with the application
 */
public class BillHelper {

    /**
     * Util method for rounding number to an increment
     * @param value - the BigDecimal whose value is rounded
     * @param increment - specific increment to round the value
     * @param roundingMode - rounding mode to apply
     * @return the rounded BigDecimal
     */
    public static BigDecimal round(BigDecimal value, BigDecimal increment, RoundingMode roundingMode) {
        if (increment.signum() == 0) {
            // Prevent division by 0
            return value;
        } else {
            BigDecimal divided = value.divide(increment, 0, roundingMode);
            return divided.multiply(increment);
        }
    }


}
