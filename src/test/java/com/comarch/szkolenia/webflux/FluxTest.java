package com.comarch.szkolenia.webflux;

import com.comarch.szkolenia.webflux.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FluxTest {

    @Test
    public void test1() throws InterruptedException {
        List<User> users = List.of(
                new User(1, "janusz", "janusz123", "Janusz", "Kowalski"),
                new User(2, "wieksiek", "wieksiek123", "Wiesiek", "Kowalski2"),
                new User(3, "zbyszek", "zbyszek123", "Zbyszek", "Kowalski3"),
                new User(4, "mateusz", "mateusz123", "Mateusz", "Kowalski4"),
                new User(5, "karol", "karol123", "Karol", "Kowalski5")
        );

        Flux<User> flux = Flux.fromIterable(users)
                .delayElements(Duration.ofSeconds(1))
                .map(user -> {
                    if(user.getId() == 3) {
                        throw new RuntimeException();
                    }
                    return user;
                });

        /*flux.subscribe(user -> {
            System.out.println(user);
            if(user.getName().equals("Wiesiek")) {
                Assertions.fail();
            }
        });*/

        StepVerifier.create(flux)
                .expectNext(new User(1, "janusz", "janusz123", "Janusz", "Kowalski"))
                .expectNext(new User(2, "wieksiek", "wieksiek123", "Wiesiek", "Kowalski2"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException)
//                .expectNextCount(1)
//                .expectNextMatches(u -> u.getId() == 4)
//                .expectNoEvent(Duration.ofMillis(900))
                //.expectNextCount(1)
//                .verifyComplete();
                .verify();
    }

    @Test
    public void webClient() throws InterruptedException {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080")
                .build();

        Flux<User> userFlux = webClient
                .get()
                .uri("/test1")
                .retrieve()
                .bodyToFlux(User.class);
        userFlux.subscribe(System.out::println);
        Thread.sleep(10000);
    }
}
