package com.ollama.demo.service;

import org.jspecify.annotations.Nullable;
import reactor.core.publisher.Flux;

public interface ChatService {

    String chat(String query);

    default Flux<String> streamChat(String query) {
        //use streaming api to return a stream of responses
    }
}
