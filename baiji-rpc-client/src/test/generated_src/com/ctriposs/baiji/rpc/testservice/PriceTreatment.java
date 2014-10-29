package com.ctriposs.baiji.rpc.testservice;

public enum PriceTreatment {

    STP(0),
    MAP(1);

    private final int value;

    PriceTreatment(int value) {
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
    public static PriceTreatment findByValue(int value) {
        switch (value) {
            case 0:
                return STP;
            case 1:
                return MAP;
            default:
                return null;
        }
    }
}
