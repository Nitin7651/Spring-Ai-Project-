package com.ollama.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    @Value("classpath:/prompts/system-prompt.st")
    private Resource systemResource;

    @Value("classpath:/prompts/user-prompt.st")
    private Resource userResource;

    public ChatServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String chat(String query) {
        return chatClient.prompt()
                .system(systemResource)
                .user(u -> u.text(userResource).param("topic", "General").param("query", query))
                .call()
                .content();
    }

    @Override
    public Flux<String> streamChat(String query) {
        return chatClient.prompt()
                .system(systemResource)
                .user(u -> u.text(userResource).param("topic", "General").param("query", query))
                .stream()
                .content();
    }
}