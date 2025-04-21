package top.fusuccess.aidemo.demos.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AIController {

    private static final String API_KEY = "fk232614-ESysqzDLm5SuVLgoCAFt8zfCRFQhQTdB";  // 替换为你从API2D获取的API Key
    private static final String API_URL = "https://openai.api2d.net/v1/chat/completions";


    /**
     * 处理AI请求
     * @param requestBody
     * @return
     */
    @RequestMapping("/ai")
    @ResponseBody
    public String hello(@RequestBody Map<String, String> requestBody) {
        String prompt = requestBody.get("prompt");
        if (prompt == null || prompt.isEmpty()) {
            return "请提供提示词";
        }

        System.out.println("AI prompt: " + prompt);

        String response = callAiAPI(prompt);
        System.out.println("AI Response: " + response);
        return response;
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
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
