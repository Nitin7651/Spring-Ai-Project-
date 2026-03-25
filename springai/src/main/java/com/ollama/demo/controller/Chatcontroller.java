package com.ollama.demo.controller;

import com.ollama.demo.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class Chatcontroller {

    private static final Logger log = LoggerFactory.getLogger(Chatcontroller.class);

    private final ChatService chatService;

    public Chatcontroller(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chat(
            @RequestParam(value = "q") String query,
            @RequestHeader("userId") String userId) {

        log.info("[/chat] userId={} | query={}", userId, query);
        return ResponseEntity.ok(chatService.chat(query, userId));
    }

    @GetMapping(value = "/stream-chat", produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamchat(
            @RequestParam(value = "q") String query,
            @RequestHeader("userId") String userId) {

        log.info("[/stream-chat] userId={} | query={}", userId, query);
        return chatService.streamChat(query, userId);
    }
}
