package com.ctriposs.baiji.rpc.common.types;

/**
 * SeverityCodeType - Type declaration to be used by other schema.
 * This code identifies the severity of an API error.
 * A code indicates whether there is an API- level error or warning that needs to be communicated to the client.
 */
public enum SeverityCodeType {

    /**
     * (out) The request that triggered the error was not processed successfully.
     * When a serious framework, validation or service-level error occurs, the error is returned instead of the business data.
     */
    ERROR(0),

    /**
     * (out) The request was processed successfully, but something occurred that may affect your application or the user.
     * For example, Baiji service may have changed a value the user sent in.
     * In this case, Baiji service returns a normal, successful response and also returns the warning.
     */
    WARNING(1);

    private final int value;

    SeverityCodeType(int value) {
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
    public static SeverityCodeType findByValue(int value) {
        switch (value) {
            case 0:
                return ERROR;
            case 1:
                return WARNING;
            default:
                return null;
        }
    }
}
