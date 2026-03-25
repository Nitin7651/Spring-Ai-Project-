package com.ollama.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;

import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    /**
     * MessageWindowChatMemory backed by PostgreSQL via JdbcChatMemoryRepository.
     * Keeps the last 10 messages per user (conversationId set per-request in service).
     * JdbcChatMemoryRepository is auto-configured by Spring AI JDBC starter.
     */
    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();
    }

    /**
     * ChatClient with system prompt only.
     * PromptChatMemoryAdvisor is applied per-request in ChatServiceImpl
     * using the caller's userId as conversationId.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are a helpful and polite AI assistant. Always provide accurate and concise information.")
                .build();
    }
}
