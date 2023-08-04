package com.swave.urnr.util.kafka;

public enum NotificationEnum {



    POST("post"),
    MENTION("mention"),
    COMMENT("comment");

    private final String value;

    NotificationEnum (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Optional: Create a static method to parse string and get corresponding enum value.
    public static NotificationEnum  fromString(String value) {
        for (NotificationEnum item : NotificationEnum.values()) {
            if (item.value.equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Invalid value for NotificationEnum " + value);
    }
}

