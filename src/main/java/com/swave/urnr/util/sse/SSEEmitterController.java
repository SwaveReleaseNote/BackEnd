package com.swave.urnr.util.sse;

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
@CrossOrigin(origins = "http://61.109.214.110:80", allowedHeaders = "*", allowCredentials = "true")
public class SSEEmitterController {
    public final SSEEmitterService sseEmitterService;


    @Operation(summary="SSE Emitter 구독", description="UserId에 맞는 emitter를 반환합니다.")
    @GetMapping("/emitter/{id}/message/{message}")
    public SseEmitter getEmitterBySubscribe (@PathVariable String id, @PathVariable  String message) {
        return sseEmitterService.subscribeEmitter(id, message);
    }



    @Operation(summary="Emitter 반환", description=" emitter를 반환합니다.")
    @GetMapping("/emitter/{id}")
    public SseEmitter getEmitter(@PathVariable String id) {
        return sseEmitterService.getEmitter(id);
    }


    @Operation(summary="Emitter 반환", description=" emitter를 반환합니다.")
    @PostMapping("/message")
    public void postMessages(@RequestBody String message) {
        sseEmitterService.publishMessageToAllSseEmitter(message);
    }



    @Operation(summary="Emitter 반환", description=" emitter를 반환합니다.")
    @PostMapping("/message/{id}")
    public void postMessage(@PathVariable String id,@RequestBody String message) {
        sseEmitterService.publishMessageToSpeficEmitter(id, message);
    }


    @Operation(summary="Emitter request 기반 반환", description="request를 기반으로 emitter를 반환합니다.")
    @PostMapping("/emitter/html")
    public void postMessage(HttpServletRequest request) {
        sseEmitterService.getEmitterByHTTP(request);
    }



}
