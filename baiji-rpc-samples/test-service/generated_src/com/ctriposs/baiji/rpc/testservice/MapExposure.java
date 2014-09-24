package com.ctriposs.baiji.rpc.testservice;

public enum MapExposure {

    PRE_CHECKOUT(0),
    DURING_CHECKOUT(1);

    private final int value;

    MapExposure(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Baiji IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Baiji IDL.
     */
    public static MapExposure findByValue(int value) {
        switch (value) {
            case 0:
                return PRE_CHECKOUT;
            case 1:
                return DURING_CHECKOUT;
            default:
                return null;
        }
    }
}
