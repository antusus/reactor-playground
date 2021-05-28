package pl.kamil.reactorplayground.sse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pl.kamil.reactorplayground.IntervalMessageProducer;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

//Excerpt From: Josh Long. “Reactive Spring”.
@Configuration
class SseConfiguration {

  private final String countPathVariable = "count";

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route()
        .GET("/sse/{" + this.countPathVariable + "}", this::handleSse)
        .build();
  }

  Mono<ServerResponse> handleSse(ServerRequest r) {
    var countPathVariable = Integer.parseInt(r.pathVariable(this.countPathVariable));
    var publisher = IntervalMessageProducer.produce(countPathVariable)
        .doOnComplete(() -> System.out.println("completed"));

    return ServerResponse
        .ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(publisher, String.class);

  }
}