package com.ollama.demo.controller;

import com.ollama.demo.service.ChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class Chatcontroller {
    private ChatService chatService;

    private ChatClient chatClient;

    public Chatcontroller(ChatClient.Builder builder, ChatService chatService) {
        this.chatClient = builder.build();
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q") String query) {

        String responseContent = this.chatClient.prompt(query).call().content();
        return ResponseEntity.ok(responseContent);
    }

    @GetMapping(value = "/stream-chat", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamchat(
            @RequestParam(value = "q") String query) {

        return this.chatService.streamChat(query);
    }

}
