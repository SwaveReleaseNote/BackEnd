package com.swave.urnr.user.request;

import lombok.Builder;
import lombok.Data;

/*
회원가입상에서 Email / Username / Password 세개 모두 필요하기 떄문에, 모두 입력받음.
이후 첫 로그인시 Data를 받아야 함.

 */
@Data
@Builder
public class EmailCheckRequestDto {
    private String email;
    private String test;
}

