package com.swave.urnr.util.sse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SSEEmitterService {

    SseEmitter subscribeEmitter(String id, String firstMessage);

    SseEmitter getEmitter(String id);

    void publishMessageToAllSseEmitter(@RequestBody String message);



    void publishMessageToSpeficEmitter(String message, String uid);
}
