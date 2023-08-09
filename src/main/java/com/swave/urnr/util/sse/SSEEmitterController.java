package com.swave.urnr.util.sse;

import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "EmitterController")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/sse")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class SSEEmitterController {
    public final SSEEmitterService sseEmitterService;


    @Operation(summary="Emitter 토큰 구독", description="Userid에 맞는 emitt를 반환합니다.")
    @GetMapping("/emitter/{id}/message/{message}")
    public SseEmitter getEmitterandSubscribe (@PathVariable String id, @PathVariable  String message) {
        return sseEmitterService.subscribeEmitter(id, message);
    }


    @GetMapping("/test")
    public String tempo() {
        return "그래! ";
    }


    @Operation(summary="Emitter 반환", description=" emitter를 반환합니다.")
    @GetMapping("/emitter/c/{id}")
    public SseEmitter getEmitter(@PathVariable String id) {
        return sseEmitterService.getEmitter(id);
    }


    @Operation(summary="Emitter 반환", description=" emitter를 반환합니다.")
    @PostMapping("/message")
    public void postMessages(@RequestBody String message) {
        sseEmitterService.publishMessageToSseEmitter(message);
    }



    @Operation(summary="Emitter 반환", description=" emitter를 반환합니다.")
    @PostMapping("/message/{id}")
    public void postMessage(@PathVariable String id,@RequestBody String message) {
        sseEmitterService.publishMessageToCetrainEmitter(id, message);
    }

}
