package com.swave.urnr.util.sse;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
public class SSEEmitterServiceImpl implements SSEEmitterService{

    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SseEmitter subscribeEmitter(String id, String firstMessage){
        SseEmitter emitter = new SseEmitter();
        CLIENTS.put(id, emitter);

        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        try {
            emitter.send("data: " +firstMessage+ "\n\n",MediaType.TEXT_EVENT_STREAM);
        } catch (Exception e) {
            log.warn("disconnected id : {}", id);
        }
        return emitter;
    }


    @Override
    public SseEmitter getEmitter(String id){
        SseEmitter emitter = new SseEmitter();
        CLIENTS.put(id, emitter);
        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        return emitter;
    }


    @Override
    public void publishMessageToSseEmitter(String message) {
        Set<String> deadIds = new HashSet<>();
        log.info("MSG : "+message);

        try {
            // JSON 객체를 문자열로 변환
            String jsonMsg = objectMapper.writeValueAsString(message);
            log.info("HERE IS MSG: "+jsonMsg);
            log.info("HERE IS TEST MSG: "+message);

            CLIENTS.forEach((id, emitter) -> {
                try {
//                    MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
                    emitter.send("data: " +message+ "\n\n");
                } catch (Exception e) {
                    deadIds.add(id);
                    log.warn("disconnected id : {}", id);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        deadIds.forEach(CLIENTS::remove);
    }


    @Override
    public void publishMessageToCetrainEmitter( String uid,String message) {
        Set<String> deadIds = new HashSet<>();

        SseEmitter emitter = CLIENTS.get(uid);
        log.info("PV : "+uid);
        log.info("MSG : "+message);
        if( emitter != null)
        {
            try {
                emitter.send("data: " + message + "\n\n", MediaType.TEXT_EVENT_STREAM);
            } catch (Exception e) {
                deadIds.add(uid);
                log.warn("disconnected id : {}", uid);
            }
        }
        else {
            log.info("NO USER on PV : "+ uid);
        }
        deadIds.forEach(CLIENTS::remove);
    }


}
