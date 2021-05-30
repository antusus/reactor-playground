package pl.kamil.reactorplayground.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kamil.reactorplayground.domain.Customer;
import pl.kamil.reactorplayground.domain.CustomerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomersController {
  private final CustomerRepository customerRepository;

  public CustomersController(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @GetMapping
  Flux<Customer> findAll() {
    return customerRepository.findAll();
  }
}
