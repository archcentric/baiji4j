package com.ctriposs.baiji.rpc.common.types;

/**
 * (out) The request was processed successfully, but something occurred that may affect your application or the user.
 * For example, Baiji service may have changed a value the user sent in.
 * In this case, Baiji service returns a normal, successful response and also returns the warning.
 */
public enum ErrorClassificationCodeType {

    /**
     * Indicates that an error has occurred in the service implementation,
     * such as business logic error or other backend error.
     */
    SERVICE_ERROR(0),

    /**
     * Indicates that an error has occurred because of framework-level request validation failure.
     * This is usually because client consumer has attempted to submit invalid data (or missing data)
     * in the request when making API call.
     */
    VALIDATION_ERROR(1),

    /**
     * Indicates that an error has occurred in the Baiji soa framework (Baiji RPC),
     * such as a serialization/deserialization failure.
     */
    FRAMEWORK_ERROR(2),

    /**
     * Indicates that a Baiji service is unable to meet a specified service level agreement.
     * Typical cases that will cause this error including:
     * 1) continues high service call latency;
     * 2) continues high service call error rate.
     * In these cases, to avoid further service deterioration, the service framework will enter into a self-protecting mode,
     * by tripping the service call circuit and return SLAError to clients.
     * Later, when the situation improves, the service framework will close the service call circuit again and continue to serve the clients.
     */
    SLAERROR(3),

    /**
     * Indicates that a request failed to pass the service security check.
     */
    SECURITY_ERROR(4);

    private final int value;

    ErrorClassificationCodeType(int value) {
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
    public static ErrorClassificationCodeType findByValue(int value) {
        switch (value) {
            case 0:
                return SERVICE_ERROR;
            case 1:
                return VALIDATION_ERROR;
            case 2:
                return FRAMEWORK_ERROR;
            case 3:
                return SLAERROR;
            case 4:
                return SECURITY_ERROR;
            default:
                return null;
        }
    }
}
