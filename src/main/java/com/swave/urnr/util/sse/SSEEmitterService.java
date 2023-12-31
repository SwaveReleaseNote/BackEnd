package com.swave.urnr.util.sse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

public interface SSEEmitterService {

    SseEmitter subscribeEmitter(String id, String firstMessage);

    SseEmitter getEmitter(String id);

    SseEmitter getEmitterByHTTP(HttpServletRequest request);

    void publishMessageToAllSseEmitter(@RequestBody String message);



    void publishMessageToSpeficEmitter(String uid, String message);

    void publishAlarmToEmitter(String uid, String message, SSETypeEnum type);
}
