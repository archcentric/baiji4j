package com.ctriposs.baiji.fastjson;

public enum EnumType {

    THIRD(0),
    FIRST(1),
    SECOND(2);

    private final int _value;

    EnumType(int value) {
        _value = value;
    }

    public int getValue() {
        return _value;
    }

    public static EnumType findByValue(int value) {
        switch (value) {
            case 0:
                return THIRD;
            case 1:
                return FIRST;
            case 2:
                return SECOND;
            default:
                return null;
        }
    }
}
