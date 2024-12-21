package edu.utility;

import java.math.BigDecimal;

public class Constants {

    public static final BigDecimal CONNECTION_PRICE = new BigDecimal("100");
    public static final String PUBLIC_ADDRESS = System.getenv("STELLAR_PUBLIC_KEY");
    public static final Long MONTH_LENGTH_IN_MILLISECONDS = 2592000000L;
    public static final Long DAY_LENGTH_IN_MILLISECONDS = 86400000L;

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
