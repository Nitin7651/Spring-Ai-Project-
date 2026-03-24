package com.ollama.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final MessageWindowChatMemory chatMemory;

    @Value("classpath:/prompts/user-prompt.st")
    private Resource userResource;

    public ChatServiceImpl(ChatClient chatClient, MessageWindowChatMemory chatMemory) {
        this.chatClient = chatClient;
        this.chatMemory = chatMemory;
    }

    @Override
    public String chat(String query, String userId) {
        return chatClient.prompt()
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()
                )
                .user(u -> u.text(userResource).param("topic", "General").param("query", query))
                .call()
                .content();
    }

    @Override
    public Flux<String> streamChat(String query, String userId) {
        return chatClient.prompt()
                .advisors(
                        PromptChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()
                )
                .user(u -> u.text(userResource).param("topic", "General").param("query", query))
                .stream()
                .content();
    }
}