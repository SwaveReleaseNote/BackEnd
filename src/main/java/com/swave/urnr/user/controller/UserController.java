package com.swave.urnr.user.controller;

import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.user.responsedto.UserEntityResponseDTO;
import com.swave.urnr.user.requestdto.*;
import com.swave.urnr.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Api(tags = "UserController")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin(origins = "http://61.109.214.110:80", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserService userService;


    @Cacheable(value="getUserInformationList")
    @Operation(summary="사용자 정보 리스트 반환", description="관리자의 정보와 사용자들의 정보 리스트를 반환합니다.")
    @GetMapping("/users")
    public ManagerResponseDTO getUserInformationList(HttpServletRequest request) {
        return userService.getUserInformationList(request);
    }


    @Cacheable(value="getCurrentUserInformation")
    @Operation(summary="사용자 계정 정보 반환", description="사용자의 계정 정보를 반환합니다.")
    @GetMapping("/user")
    public ResponseEntity<UserResponseDTO> getCurrentUserInformation(HttpServletRequest request)  {
        return userService.getCurrentUserInformation(request);
    }

    @Operation(summary="사용자 계정 생성", description="사용자 정보를 생성합니다.")
    @PostMapping("/user")
    public ResponseEntity<UserEntityResponseDTO> createAccountByEmail(@RequestBody @Valid UserRegisterRequestDTO request) {
        return userService.createAccountByEmail(request);
    }


    @Operation(summary="사용자 계정 수정", description="로그인한 사용자로부터 받은 정보로 사용자의 계정 정보를 수정합니다.")
    @PutMapping("/user")
    @SecurityRequirement(name = "Authorization")
    public  ResponseEntity<String> updateUser(HttpServletRequest request, @RequestBody UserUpdateAccountRequestDTO requestDto)  {
        return userService.updateUser(request, requestDto);
    }


    @Operation(summary="사용자 계정 일부 수정", description="로그인한 사용자로부터 받은 정보로 사용자의 계정 정보를 일부 수정합니다.")
    @PatchMapping("/user")
    @SecurityRequirement(name = "JWT 토큰")
    public  ResponseEntity<UserEntityResponseDTO> initDepartment(HttpServletRequest request, @RequestBody UserDepartmentRequestDTO requestDto) {
        return userService.patchUserInformation(request, requestDto);
    }

    @Operation(summary="사용자 계정 삭제", description="토큰에 해당하는 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        return userService.deleteUser(request);
    }


    @Operation(summary="인증 이메일 전송", description="받은 이메일 주소로 난수가 포함된 이메일을 보내며, 난수를 반환합니다.")
    @PostMapping("/user/validation")
    public ResponseEntity<String> validateByEmail(@RequestBody @Valid UserValidateEmailDTO request)  {
        return userService.getValidationCode(request);
    }


    @Operation(summary="임시 비밀번호 반환", description="받은 이메일 주소로 임시 비밀번호가 포함된 이메일을 보냅니다.")
    @PostMapping("/user/temporary-password")
    public ResponseEntity<String> getTemporaryEmail(@RequestBody @Valid UserValidateEmailDTO request) {
        return userService.setTemporaryPassword(request);
    }


    @Operation(summary="이메일 로그인을 통한 토큰 반환", description="입력된 이메일 계정과 비밀번호가 동일하면 토큰값을 반환합니다.")
    @PostMapping("/user/login-by-email")
    public  ResponseEntity<SseEmitter> getTokenByLogin(@RequestBody UserLoginServerRequestDTO requestDto)  {
        return userService.getTokenByLogin(requestDto);
    }
    @Operation(summary="oAuth 로그인을 통한 토큰 반환", description="Oauth 기능을 기반으로 사용자의 계정을 로그인합니다.")
    @PostMapping("/user/login-by-oauth")
    public ResponseEntity<SseEmitter> getTokenByOauth(@RequestParam("code") String code, @RequestParam("provider") String provider) {
        return userService.getTokenByOauth(code, provider);
    }
    @Operation(summary="사용자 로그인 여부 확인", description="현재 사용자가 로그인하고 있으면 true 아니면 false가 나옵니다")
    @PatchMapping("/user/status")
    public boolean updateStatus(HttpServletRequest request,  @RequestBody Map<String, Object> requestBody)  {
        return userService.updateLoginState(request, (Boolean) requestBody.get("loginState"));
    }


    @Operation(summary="샘플 계정 생성", description="(테스트 전용 코드) 임시 사용자 계정 6개를 생성합니다. ")
    @PostMapping("/user/sample")
    public void createSampleAccount() {
        userService.createSampleAccount();
    }


    @Operation(summary="샘플 계정 삭제", description="(테스트 전용 코드) 임시 사용자 계정 6개를 삭제시킵니다. ")
    @DeleteMapping("/user/sample")
    public void deleteSampleAccount() {
        userService.deleteSampleAccount();
    }

    @Operation(summary="사용자 password 변경", description="사용자 password 변경합니다.")
    @PatchMapping("/user/password")
    @SecurityRequirement(name = "JWT 토큰")
    public ResponseEntity<String> updatePassword(HttpServletRequest request, @RequestBody UserUpdateAccountRequestDTO requestDto)  {
        return userService.updatePassword(request, requestDto);
        
    }

}
