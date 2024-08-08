package com.example.testAi.chat;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.example.testAi.chat.ChatService.jsonConverter;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/chat")
    @ResponseBody
    public Map<String, Object> generate(@RequestParam(value = "message", defaultValue = "스프링 부트 개발자 되기  ") String message) {
        String responseText = chatService.generate(message);
        return jsonConverter(responseText);
    }
}