package com.ollama.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    @Value("classpath:/prompts/user-prompt.st")
    private Resource userResource;

    public ChatServiceImpl(ChatClient chatClient, ChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    @Override
    public String chat(String query, String userId) {
        log.debug("chat() called | userId={} | query={}", userId, query);
        String response = chatClient.prompt()
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()
                )
                .user(u -> u.text(userResource).param("topic", "General").param("query", query))
                .call()
                .content();
        log.debug("chat() response | userId={} | response={}", userId, response);
        return response;
    }

    @Override
    public Flux<String> streamChat(String query, String userId) {
        log.info("streamChat() started | userId={} | query={}", userId, query);
        return chatClient.prompt()
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()
                )
                .user(u -> u.text(userResource).param("topic", "General").param("query", query))
                .stream()
                .content()
                .doOnComplete(() -> log.info("streamChat() completed | userId={}", userId))
                .doOnError(e -> log.error("streamChat() error | userId={} | error={}", userId, e.getMessage()));
    }
}
