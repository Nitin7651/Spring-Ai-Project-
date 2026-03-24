package com.ollama.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    /**
     * Shared in-memory repository — stores every user's conversation
     * history keyed by their userId (conversationId).
     */
    @Bean
    public InMemoryChatMemoryRepository chatMemoryRepository() {
        return new InMemoryChatMemoryRepository();
    }

    /**
     * Shared MessageWindowChatMemory — keeps the last 10 messages per
     * conversation.  The conversationId is set per-request in the service.
     */
    @Bean
    public MessageWindowChatMemory chatMemory(InMemoryChatMemoryRepository repository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(10)
                .build();
    }

    /**
     * ChatClient with system prompt only.
     * PromptChatMemoryAdvisor is NOT added here — it is applied
     * per-request in ChatServiceImpl using the caller's userId.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a helpful and polite AI assistant. Always provide accurate and concise information.")
                .build();
    }
}
