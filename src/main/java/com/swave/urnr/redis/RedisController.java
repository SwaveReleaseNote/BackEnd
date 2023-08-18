package com.swave.urnr.redis;

import io.swagger.v3.oas.annotations.Operation;
import org.redisson.RedissonRedLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.redisson.api.RLock;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



@Controller
@Slf4j
@CrossOrigin(origins = "http://61.109.214.110", allowedHeaders = "*", allowCredentials = "true")
public class RedisController {
//레디스 테스트


    private final RedisService redisService;

    private final RedissonClient redissonClient;

    public RedisController(RedisService redisService, RedissonClient redissonClient) {
        this.redisService = redisService;
        this.redissonClient = redissonClient;
    }


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

    // 설문 분석 시작
    // 분산락 적용
    @PostMapping(value = "/redis/create/create")
    public String saveAnalyze(@RequestBody String Id) {
        RedissonRedLock lock = new RedissonRedLock(redissonClient.getLock("/redis/create/create"));

        try {
            if (lock.tryLock()) {
                // transaction
                return "Success";
            } else {
                throw new RuntimeException("Failed to acquire lock.");
            }
        } finally {
            lock.unlock();
        }
    }

    // 분산락 획득 테스트
    @GetMapping("/test-lock")
    public String testLock() {
        RLock lock = redissonClient.getLock("my-lock");

        try {
            if (lock.tryLock()) {
                // 분산락 획득 성공
                // 이곳에서 동시에 실행되면 안 되는 코드를 작성합니다
                Thread.sleep(5000); // 테스트를 위해 5초 동안 대기합니다.

                System.out.println("분산락 획득 성공");
                return "Lock acquired successfully!";
            } else {
                // 분산락 획득 실패
                System.out.println("분산락 획득 실패");
                return "Failed to acquire lock!";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("에러");
            return "Lock acquisition interrupted!";
        } finally {
            lock.unlock();
        }
    }

    // 동시 요청 10개 분산락 테스트
    @GetMapping("/test-concurrent-requests")
    public String testConcurrentRequests() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                String result = testLock();
                System.out.println(result);
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("동시 요청 분산락 획득 실패");
            return "Test interrupted!";
        }

        return "All requests completed!";
    }

}