package com.comarch.szkolenia.webflux.dao;

import com.comarch.szkolenia.webflux.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends R2dbcRepository<User, Integer> {
    Flux<User> findByNameContainingOrSurnameContaining(String namePattern, String surnamePattern);

    default Flux<User> findByNameContainingOrSurnameContaining(String pattern) {
        return this.findByNameContainingOrSurnameContaining(pattern, pattern);
    }
}
