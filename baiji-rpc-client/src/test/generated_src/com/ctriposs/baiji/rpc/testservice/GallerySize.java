package com.ctriposs.baiji.rpc.testservice;

public enum GallerySize {

    SMALL(0),
    MEDIUM(1),
    LARGE(2);

    private final int value;

    GallerySize(int value) {
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
    public static GallerySize findByValue(int value) {
        switch (value) {
            case 0:
                return SMALL;
            case 1:
                return MEDIUM;
            case 2:
                return LARGE;
            default:
                return null;
        }
    }
}
