package com.swave.urnr.redis;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Set;

@Controller
public class RedisController {
//레디스 테스트

    @Autowired
    RedisService redisService;


    @Operation(summary="레디스 테스트1", description="레디스 테스트")
    @PostMapping(value = "/redis/test/setString")
    @ResponseBody
    public void setValue(String testkey, String testvalue){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        redisService.setValues(testkey,testvalue);
    }


    @Operation(summary="레디스 테스트2", description="레디스 테스트")
    @GetMapping(value = "/redis/test/getString")
    @ResponseBody
    public String getValue(String testkey){
        return redisService.getValues(testkey);
    }



    @Operation(summary="레디스 테스트3", description="레디스 테스트")
    @PostMapping(value = "/redis/test/setSets")
    @ResponseBody
    public void setSets(String testkey,String... testvalues){
        redisService.setSets(testkey,testvalues);
    }


    @Operation(summary="레디스 테스트4", description="레디스 테스트")
    @GetMapping(value = "/redis/test/getSets")
    @ResponseBody
    public Set getSets(String key){
        return redisService.getSets(key);
    }

}