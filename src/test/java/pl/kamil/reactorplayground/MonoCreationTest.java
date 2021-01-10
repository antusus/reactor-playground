package pl.kamil.reactorplayground;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;

class MonoCreationTest {
  @Test
  void canBeEmpty() {
    Mono.empty().as(StepVerifier::create).verifyComplete();
  }

  @Test
  void createAndVerify() {
    // You can assert values with StepVerifier
    Mono.just(1).as(StepVerifier::create)
        .assertNext(i -> assertThat(i).isEqualTo(1))
        .verifyComplete();
  }

  @Test
  void createAndSubscribe() {
    // You can assert values
    Mono.just(2).subscribe(
        i -> assertThat(i).isEqualTo(2),
        e -> Assertions.fail("Should not return error")
    );
  }

  @Test
  void handleErrorAndVerify() {
    // You can assert errors with StepVerifier
    Mono.error(new RuntimeException("OMG!!!"))
        .as(StepVerifier::create)
        .verifyErrorSatisfies(e -> assertThat(e).hasMessage("OMG!!!"));
  }

  @Test
  void handleErrorAndSubscribe() {
    // You can assert errors
    Mono.error(new RuntimeException("OMG!"))
        .subscribe(
            o -> Assertions.fail("Should not be called"),
            e -> assertThat(e).isInstanceOf(RuntimeException.class).hasMessage("OMG!")
        );
  }

  @Test
  void delayGeneration() {
    // You can delay generation
    StepVerifier.withVirtualTime(() -> Mono.just(3).delayElement(ofMillis(500)))
        .expectSubscription()
        .expectNoEvent(ofMillis(500))
        .expectNext(3)
        .verifyComplete();
  }

  @Test
  void mergeMonos() {
    // You can merge only with the same type and after merging you will get Flux
    Mono.just(4).mergeWith(Mono.just(5))
    .as(StepVerifier::create)
    .assertNext(i -> assertThat(i).isEqualTo(4))
    .assertNext(i -> assertThat(i).isEqualTo(5))
    .verifyComplete();
  }

  @Test
  void zipMonos() {
    // You can ZIP different types
    Mono.just(6).zipWith(Mono.just("A"))
        .as(StepVerifier::create)
        .assertNext(t -> {
          assertThat(t.getT1()).isEqualTo(6);
          assertThat(t.getT2()).isEqualTo("A");
        })
        .verifyComplete();
  }

  //TODO: test subscription
}