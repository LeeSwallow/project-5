package com.example.testAi.chat;

import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.util.List;
import java.util.Map;

public class CustomMessage implements Message {
    private final String message;
    private final MessageType messageType;

    public CustomMessage(String message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String getContent() {
        return message;
    }

    @Override
    public List<Media> getMedia(String... dummy) {
        return null;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }
}
