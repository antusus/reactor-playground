package pl.kamil.reactorplayground;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

//Excerpt From: Josh Long. “Reactive Spring”.
public class IntervalMessageProducer {

  public static Flux<String> produce(int c) {
    return produce().take(c);
  }

  public static Flux<String> produce() {
    return doProduceCountAndStrings().map(CountAndString::message);
  }

  private static Flux<CountAndString> doProduceCountAndStrings() {
    var counter = new AtomicLong();
    return Flux
        .interval(Duration.ofSeconds(1))
        .map(i -> new CountAndString(counter.incrementAndGet()));
  }

}