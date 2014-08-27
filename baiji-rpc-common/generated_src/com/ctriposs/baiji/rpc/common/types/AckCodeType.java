package com.ctriposs.baiji.rpc.common.types;

/**
 * AckCodeType - Type declaration to be used by other schema.
 * This code identifies the acknowledgement code types that Baiji could use to
 * communicate the status of processing a (request) message to a client.
 * This code would be used as part of a response message that contains a framework,
 * validation or service-level acknowledgement element.
 */
public enum AckCodeType {

    /**
     * (out) Request processing succeeded
     */
    SUCCESS(0),

    /**
     * (out) Request processing failed
     */
    FAILURE(1),

    /**
     * (out) Request processing completed with warning information being included in the response message
     */
    WARNING(2),

    /**
     * (out) Request processing completed with warning information being included in the response message
     */
    PARTIAL_FAILURE(3);

    private final int value;

    AckCodeType(int value) {
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
    public static AckCodeType findByValue(int value) {
        switch (value) {
            case 0:
                return SUCCESS;
            case 1:
                return FAILURE;
            case 2:
                return WARNING;
            case 3:
                return PARTIAL_FAILURE;
            default:
                return null;
        }
    }
}
