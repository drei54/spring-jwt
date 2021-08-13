package com.atan.test.jwt.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class MyService {

	private final WebClient webClient;

	public MyService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://dev3.dansmultipro.co.id/").build();
	}

	public Mono<Object[]> positionRestCall() {
		return this.webClient.get().uri("/api/recruitment/positions.json")
						.retrieve().bodyToMono(Object[].class);
	}

	public Mono<Object> detailRestCall(String id) {
		return this.webClient.get().uri("/api/recruitment/positions/{id}", id)
						.retrieve().bodyToMono(Object.class);
	}

}