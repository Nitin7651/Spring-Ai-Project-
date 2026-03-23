package com.ollama.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    /**
     * A pre-configured ChatClient with:
     *  - defaultSystem: system instructions applied at the builder level,
     *    never stored in memory.
     *  - PromptChatMemoryAdvisor: appends the last 10 messages as plain text
     *    inside the prompt — fully compatible with Google Gemini.
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(10)
                .build();

        return builder
                .defaultSystem("You are a helpful and polite AI assistant. Always provide accurate and concise information.")
                .defaultAdvisors(
                        PromptChatMemoryAdvisor.builder(memory)
                                .conversationId("default")
                                .build()
                )
                .build();
    }
}

