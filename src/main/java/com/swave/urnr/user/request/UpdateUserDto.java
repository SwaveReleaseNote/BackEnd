package com.swave.urnr.user.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserDto {
    private String email;

    private String password;
    private String department;
    private String name;
}

