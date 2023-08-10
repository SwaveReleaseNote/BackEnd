package com.swave.urnr.util.sse;

public enum SSETypeEnum {
    ALARM("Alarm"),
    NORMAL("Normal"),
    TOKEN("Token");

    private final String value;

    SSETypeEnum (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Optional: Create a static method to parse string and get corresponding enum value.
    public static SSETypeEnum fromString(String value) {
        for ( SSETypeEnum item :  SSETypeEnum.values()) {
            if ( item.value.equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Invalid value for SSETypeEnum " + value);
    }
}

