package com.swave.releasenotesharesystem.Util.type;

import lombok.Data;

@Data
public enum UserRole {
    Subscriber("구독자"), Developer("개발자"), Manager("관리자");

    private final String value;
    UserRole(String value) {
        this.value = value;
    }
}
