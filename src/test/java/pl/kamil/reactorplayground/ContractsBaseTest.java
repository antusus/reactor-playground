package pl.kamil.reactorplayground;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.kamil.reactorplayground.application.CustomersController;
import pl.kamil.reactorplayground.domain.Customer;
import pl.kamil.reactorplayground.domain.CustomerRepository;
import reactor.core.publisher.Flux;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class ContractsBaseTest {

  @MockBean
  private CustomerRepository customerRepository;

  @BeforeEach
  void setUp() {
    Mockito.doReturn(
        Flux.just(new Customer(1L, "Jane"), new Customer(2L, "John"))
    ).when(customerRepository).findAll();
    RestAssuredWebTestClient.standaloneSetup(new CustomersController(customerRepository));
  }

  @Configuration
  @Import(ReactiveApplication.class)
  public static class TestConfiguration {

  }
}
