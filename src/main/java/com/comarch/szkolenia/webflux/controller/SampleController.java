package com.comarch.szkolenia.webflux.controller;

import com.comarch.szkolenia.webflux.dao.UserRepository;
import com.comarch.szkolenia.webflux.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/user", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public class SampleController {

    private final UserRepository userRepository;

    @GetMapping(path = "/test1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getUsers() {
        Flux<User> userFlux = Flux.just(
                new User(1, "janusz", "janusz123", "Janusz", "Kowalski"),
                new User(2, "wieksiek", "wieksiek123", "Wieksiek", "Kowalski2"),
                new User(3, "zbyszek", "zbyszek123", "Zbyszek", "Kowalski3"),
                new User(4, "mateusz", "mateusz123", "Mateusz", "Kowalski4"),
                new User(5, "karol", "karol123", "Karol", "Kowalski5")
        );

        return userFlux.delayElements(Duration.ofSeconds(2));
    }

    @GetMapping(path = "/test2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<User> getUser() {
        Mono<User> userMono = Mono.just(
                new User(1, "janusz", "janusz123", "Janusz", "Kowalski"));

        return userMono.delayElement(Duration.ofSeconds(1));
    }

    @GetMapping(path = "/test3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> test3() {
        Flux<User> userFlux = Flux.just(
                new User(1, "janusz", "janusz123", "Janusz", "Kowalski"),
                new User(2, "wieksiek", "wieksiek123", "Wieksiek", "Kowalski2"),
                new User(3, "zbyszek", "zbyszek123", "Zbyszek", "Kowalski3"),
                new User(4, "mateusz", "mateusz123", "Mateusz", "Kowalski4"),
                new User(5, "karol", "karol123", "Karol", "Kowalski5")
        )
                .delayElements(Duration.ofSeconds(1))
                .map(user -> {
            if(user.getName().equals("Mateusz")) {
                throw new RuntimeException();
            }
            user.setName(user.getName() + " - verified");
            return user;
        });

        userFlux.subscribe(
                user -> System.out.println("Wytemintowany user: " + user),
                System.out::println,
                () -> System.out.println("cos")
        );
        return userFlux;
    }

    @GetMapping("/test4")
    public User user() {
        return new User(1, "janusz", "janusz123", "Janusz", "Kowalski");
    }

    @GetMapping(path = "/test5")
    public Flux<User> getUserByPattern() {
        Flux<User> userFlux = this.userRepository
                .findByNameContainingOrSurnameContaining("n")
                .delayElements(Duration.ofSeconds(1));
        /*userFlux.subscribe();
        userFlux.subscribe();
        userFlux.subscribe();*/
        return userFlux;
    }

    @GetMapping(path = "/test6")
    public Mono<User> cos() {
        return this.userRepository.findById(1).delayElement(Duration.ofSeconds(5));
    }

    @GetMapping("test7")
    public Mono<User> cos2() {
        User user = new User();
        return Mono.just(user);
    }

    @GetMapping("/test8")
    public Mono<User> save() {
        User user = new User(0, "karol", "karol", "Karol", "Fajny");
        return this.userRepository.save(user);
    }

    public void kafka() {
        Flux<String> stringFlux = Flux.just("A", "B", "C error", "D");

        stringFlux.map(s -> s)
                .flatMap(s -> Mono.just(s))
                .filter(s -> s.length()>5)
                .doOnNext(System.out::println);

        Mono<List<String>> listMono = stringFlux.collectList();
        listMono.subscribe(System.out::println);
    }
}
