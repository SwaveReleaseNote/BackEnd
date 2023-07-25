package com.swave.urnr.user.controller;

import com.swave.urnr.user.responsedto.UserListResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.util.common.ResponseDTO;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.requestdto.*;
import com.swave.urnr.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "UserController")
@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;

    @Operation(summary="유저 계정 생성", description="유저 정보를 생성합니다.")
    @PostMapping("/prelogin/create")
    public ResponseEntity<ResponseDTO> createAccountByEmail(@RequestBody @Valid UserRegisterRequestDTO request) {
        return userService.createAccountByEmail(request);
    }

    @Operation(summary="인증 이메일 전송", description="받은 이메일 주소로 난수가 포함된 이메일을 보내며, 난수를 반환합니다.")
    @PostMapping("/prelogin/sendmail")
    public ResponseEntity<String> validateByEmail(@RequestBody @Valid UserValidateEmailDTO request)  {
        return userService.getValidationCode(request);
    }


    @Operation(summary="임시 비밀번호 반환", description="받은 이메일 주소로 임시 비밀번호가 포함된 이메일을 보냅니다.")
    @PostMapping("/prelogin/get-temporary-password")
    public ResponseEntity<String> getTemporaryEmail(@RequestBody @Valid UserValidateEmailDTO request) {
        return userService.setTemporaryPassword(request);
    }

    @Operation(summary="사용자 계정 수정", description="로그인한 사용자로부터 받은 정보로 사용자의 계정 정보를 수정합니다.")
    @PutMapping("/update")
    @SecurityRequirement(name = "Authorization")
    public  ResponseEntity<String> updateUser(HttpServletRequest request, @RequestBody UserUpdateAccountRequestDTO requestDto)  {
        return userService.updateUser(request, requestDto);
    }


    @Operation(summary="사용자 소속 수정", description="로그인한 사용자로부터 받은 정보로 사용자의 계정 정보를 수정합니다.")
    @PatchMapping("/department")
    @SecurityRequirement(name = "JWT 토큰")
    public  ResponseEntity<ResponseDTO> initDepartment(HttpServletRequest request, @RequestBody UserDepartmentRequestDTO requestDto) throws UserNotFoundException {
        return userService.initDepartment(request, requestDto);
    }
    @Operation(summary="이메일 로그인을 통한 토큰 반환", description="입력된 이메일 계정과 비밀번호가 동일하면 토큰값을 반환합니다.")
    @PostMapping("/prelogin/login-by-email")
    public  ResponseEntity<String> getTokenByLogin( @RequestBody UserLoginServerRequestDTO requestDto) throws UserNotFoundException {
        return userService.getTokenByLogin(requestDto);
    }

    @Operation(summary="사용자 계정 삭제", description="토큰에 해당하는 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }

    @Operation(summary="oAuth 로그인을 통한 토큰 반환", description="Oauth 기능을 기반으로 사용자의 계정을 로그인합니다.")
    @PostMapping("/prelogin/login-by-oauth")
    public ResponseEntity getTokenByOauth(@RequestParam("code") String code, @RequestParam("provider") String provider) {
        return userService.getTokenByOauth(code, provider);
    }

    @Operation(summary="사용자 정보 리스트 반환", description="사용자들의 정보 리스트를 반환합니다.")
    @GetMapping("/prelogin/getuserlist")
    public List<UserListResponseDTO>  getUserInformationList() {
        return userService.getUserInformationList();
    }

    @Operation(summary="사용자 계정 정보 반환", description="사용자의 계정 정보를 반환합니다.")
    @GetMapping("/getuser")
    public ResponseEntity<UserResponseDTO> getCurrentUserInformation(HttpServletRequest request)  {
        return userService.getCurrentUserInformation(request);
    }

    @PatchMapping("/updateStatus")
    public boolean updateStatus(HttpServletRequest request,  @RequestBody Map<String, Object> requestBody) throws UserNotFoundException {
        return userService.updateLoginState(request, (boolean) requestBody.get("loginState"));
    }
}
