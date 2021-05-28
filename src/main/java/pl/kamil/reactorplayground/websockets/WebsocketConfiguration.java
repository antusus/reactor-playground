package pl.kamil.reactorplayground.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import pl.kamil.reactorplayground.IntervalMessageProducer;

import java.util.Map;

import static reactor.core.publisher.SignalType.ON_COMPLETE;

//Excerpt From: Josh Long. “Reactive Spring”.
@Configuration
public class WebsocketConfiguration {
  @Bean
  HandlerMapping echoHm() {
    return new SimpleUrlHandlerMapping(Map.of("/ws/echo", echoWsh()), 10);
  }

  @Bean
  WebSocketHandler echoWsh() {
    return session -> {
      var out = IntervalMessageProducer.produce()
          .doOnNext(System.out::println)
          .map(session::textMessage)
          .doFinally(signalType -> System.out.println("Outbound connection " + signalType));

      var in = session.receive()
          .map(WebSocketMessage::getPayloadAsText)
          .doFinally(signalType -> {
            System.out.println("Inbound connection " + signalType);
            if (ON_COMPLETE == signalType) {
              session.close().subscribe();
            }
          })
          .doOnNext(System.out::println);

      return session.send(out).and(in);
    };
  }
}
