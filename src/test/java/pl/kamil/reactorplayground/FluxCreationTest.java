package pl.kamil.reactorplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FluxCreationTest {
  @Test
  void canBeEmpty() {
    Flux.empty()
        .as(StepVerifier::create)
        .verifyComplete();
  }

  @Test
  void canHaveManyElements() {
    Flux.fromIterable(List.of("a", "b", "c"))
        .as(StepVerifier::create)
        .expectNext("a")
        .expectNext("b")
        .expectNext("c")
        .verifyComplete();
  }

  @Test
  void canBeInfinite() {
    StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ZERO, Duration.ofMillis(100)))
        .expectNext(0L)
        .expectNoEvent(Duration.ofMillis(100))
        .expectNext(1L)
        .expectNoEvent(Duration.ofMillis(100))
        .expectNext(2L)
        .expectNoEvent(Duration.ofMillis(100))
        .thenCancel() //Cancels the infinite stream of events
        .verify();
  }

  @Test
  void canBeFromRange() {
    Flux.range(100, 3)
        .as(StepVerifier::create)
        .expectNext(100)
        .expectNext(101)
        .expectNext(102)
        .verifyComplete();
  }

  @Test
  void canEndWithError() {
    Flux.error(new RuntimeException("OMG!"))
        .as(StepVerifier::create)
        .expectErrorSatisfies(e -> assertThat(e).isInstanceOf(RuntimeException.class).hasMessage("OMG!"))
        .verify();
  }
}
