package top.fusuccess.aidemo.demos.module.ai.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.fusuccess.aidemo.demos.common.ApiResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class AIController {
    Logger logger = LoggerFactory.getLogger(AIController.class);

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String API_KEY = "fk232614-PAlcNBJomvveP7lXpL0pMCkAAy6oh5pw";  // 替换为你从API2D获取的API Key
    private static final String API_URL = "https://openai.api2d.net/v1/chat/completions";

    /**
     * 处理AI请求
     * @param requestBody
     * @return
     */
    @RequestMapping("/ai")
    public ResponseEntity<?>  hello(@RequestBody Map<String, String> requestBody) {
        try {
            String prompt = requestBody.get("prompt");
            logger.info("收到请求: {}", prompt);

            String cachedResponse = redisTemplate.opsForValue().get(prompt);
            String response = (cachedResponse != null) ? cachedResponse : callAiAPI(prompt);

            if (response == null) {
                redisTemplate.opsForValue().set(prompt, "EMPTY", 60, TimeUnit.SECONDS); // 空值缓存
            } else {
                redisTemplate.opsForValue().set(prompt, response, 30, TimeUnit.MINUTES);  // 缓存30分钟
            }
            return ResponseEntity.ok().body(new ApiResponse("success", response));
        } catch (Exception e) {
            logger.error("请求出错: {}", e.getMessage());
            return ResponseEntity.ok().body(new ApiResponse("error", "服务器内部错误，请稍后再试"));
        }
    }




    /**
     * 调用AI API
     * @param prompt 提示词
     * @return API响应
     */
    public String callAiAPI(String prompt) {
        HttpClient client = HttpClient.newHttpClient();
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", "gpt-3.5-turbo");

        // 设置消息体
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);

        // 包装消息
        bodyMap.put("messages", new Object[]{message});
        bodyMap.put("safe_mode", false);

        try {
            // 转换成JSON
            String body = new ObjectMapper().writeValueAsString(bodyMap);

            // 创建POST请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            // 发送请求
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);  // 解析 JSON 字符串
            return jsonResponse.get("choices")
                    .get(0)
                    .get("message")
                    .get("content")
                    .asText();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
