package pl.kamil.reactorplayground.infrastructure;

import org.springframework.stereotype.Repository;
import pl.kamil.reactorplayground.domain.Customer;
import pl.kamil.reactorplayground.domain.CustomerRepository;
import reactor.core.publisher.Flux;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {
  @Override
  public Flux<Customer> findAll() {
    return Flux.empty();
  }
}
