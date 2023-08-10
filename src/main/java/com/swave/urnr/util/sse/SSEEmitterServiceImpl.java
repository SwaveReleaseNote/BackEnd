package com.swave.urnr.util.sse;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import com.swave.urnr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class SSEEmitterServiceImpl implements SSEEmitterService{

    private static final Map<String, SseEmitter> CLIENTS = new ConcurrentHashMap<>();

    private final UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public  SseEmitter subscribeEmitter(String id, String firstMessage){


        log.info("SubscribeEmitter : {} "+id);

        SseEmitter emitter = new SseEmitter();
        CLIENTS.put(id, emitter);


        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        try {
            emitter.send("data: " +firstMessage+ "\n\n",MediaType.TEXT_EVENT_STREAM);
        } catch (Exception e) {
            log.warn("disconnected id : {}", id);
        }



        log.info("EV on subscribe : "+String.valueOf(CLIENTS.containsKey(id)));
        return emitter;
    }


    @Override
    public SseEmitter getEmitter(String id){

        log.info("GETEmitter : {} "+id);
        SseEmitter emitter = new SseEmitter();
        CLIENTS.put(id, emitter);
        emitter.onTimeout(() -> CLIENTS.remove(id));
        emitter.onCompletion(() -> CLIENTS.remove(id));
        try {
            emitter.send("data: Emitter started on getEmitter!\n\n",MediaType.TEXT_EVENT_STREAM);
        } catch (Exception e) {
            log.warn("disconnected id : {}", id);
        }

        log.info("EV on getEmitter : "+String.valueOf(CLIENTS.containsKey(id)));
        return emitter;
    }


    @Override
    public void publishMessageToAllSseEmitter(String message) {
        Set<String> deadIds = new HashSet<>();
        log.info("MSG : "+message);

        try {
            // JSON 객체를 문자열로 변환
            String jsonMsg = objectMapper.writeValueAsString(message);
            SSEDataDTO sseDataDTO = new SSEDataDTO(jsonMsg, SSETypeEnum.TOKEN);
            log.info("DATA : {}",sseDataDTO.getData());

            CLIENTS.forEach((id, emitter) -> {
                log.info("Emitter sent to {}",id);
                try {
                    emitter.send(sseDataDTO, MediaType.APPLICATION_JSON);

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
    public void publishMessageToSpeficEmitter(String uid, String message) {
        Set<String> deadIds = new HashSet<>();

        SseEmitter emitter = CLIENTS.get(uid);
        SSEDataDTO sseDataDTO = new SSEDataDTO(message, SSETypeEnum.TOKEN);
        log.info("PV : "+uid);
        log.info("MSG : "+message);
            try {
                emitter.send(sseDataDTO, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                deadIds.add(uid);
                log.warn("disconnected id : {} ", uid);
                log.warn("Exception  : {}", e.toString());
            }
        deadIds.forEach(CLIENTS::remove);
    }




}
