package com.example.app.cosmos;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends CosmosRepository<User, String> {
    Flux<User> findByName(String name);
}
