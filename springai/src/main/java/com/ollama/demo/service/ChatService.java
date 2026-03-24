package com.ollama.demo.service;

import reactor.core.publisher.Flux;

public interface ChatService {

    String chat(String query, String userId);

    Flux<String> streamChat(String query, String userId);
}
