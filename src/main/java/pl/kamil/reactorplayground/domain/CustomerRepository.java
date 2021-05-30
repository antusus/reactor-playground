package pl.kamil.reactorplayground.domain;

import reactor.core.publisher.Flux;

public interface CustomerRepository {
  Flux<Customer> findAll();
}
