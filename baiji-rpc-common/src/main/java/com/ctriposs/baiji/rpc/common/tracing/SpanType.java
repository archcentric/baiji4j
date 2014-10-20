package com.ctriposs.baiji.rpc.common.tracing;

/**
 * Created by yqdong on 2014/10/20.
 */
public enum SpanType {
    OTHER(0),
    URL(1),
    WEB_SERVICE(2),
    SQL(3),
    MEMCACHED(4);

    private final int value;

    private SpanType(int value) {
        this.value = value;
    }

    /**
     * Get the integer value of this enum value, as defined in the Thrift IDL.
     */
    public int getValue() {
        return value;
    }

    /**
     * Find a the enum type by its integer value, as defined in the Thrift IDL.
     *
     * @return null if the value is not found.
     */
    public static SpanType findByValue(int value) {
        switch (value) {
            case 0:
                return OTHER;
            case 1:
                return URL;
            case 2:
                return WEB_SERVICE;
            case 3:
                return SQL;
            case 4:
                return MEMCACHED;
            default:
                return null;
        }
    }
}
