package top.fusuccess.aidemo.demos.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
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
    @ResponseBody
    public Map<String, Object>  hello(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> result = new HashMap<>();
        try {
            String prompt = requestBody.get("prompt");
            logger.info("收到请求: {}", prompt);
            String cachedResponse = redisTemplate.opsForValue().get(prompt);
            if (cachedResponse != null) {
                result.put("status", "success");
                result.put("message", "成功");
                result.put("data", cachedResponse);
                return result;
            }
            String response = callAiAPI(prompt);
            redisTemplate.opsForValue().set(prompt, response, 30, TimeUnit.MINUTES);  // 缓存30分钟
            // 假设解析返回的 JSON 格式
            result.put("status", "success");
            result.put("message", "成功");
            result.put("data", response);
        } catch (Exception e) {
            logger.error("请求出错: {}", e.getMessage());
            result.put("status", "error");
            result.put("message", "请求失败，请重试！");
            result.put("data", e.getMessage());
        }
        return result;
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
