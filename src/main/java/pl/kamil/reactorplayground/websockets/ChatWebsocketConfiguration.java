package pl.kamil.reactorplayground.websockets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

//Excerpt From: Josh Long. “Reactive Spring”.
@Configuration
class ChatWebsocketConfiguration {
  private final Map<String, Connection> sessions = new ConcurrentHashMap<>();
  private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

  private final ObjectMapper objectMapper;

  ChatWebsocketConfiguration(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
//    var module = new SimpleModule();
//    module.addSerializer(Message.class, new JsonSerializer<>() {
//      @Override
//      public void serialize(Message value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        gen.writeStartObject();
//        gen.writeStringField("clientId", value.clientId());
//        gen.writeStringField("text", value.text());
//        gen.writeStringField("when", DateTimeFormatter.BASIC_ISO_DATE.format(value.when()));
//        gen.writeEndObject();
//      }
//    });
//    objectMapper.registerModule(module);
  }

  @Bean
  WebSocketHandler chatWsh() {
    var messagesToBroadcast = Flux.<Message>create(sink -> {
      var submit = Executors.newSingleThreadExecutor().submit(() -> {
        while (true) {
          try {
            sink.next(this.messages.take());
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      });
      sink.onCancel(() -> submit.cancel(true));
    })
        .share();

    return session -> {

      var sessionId = session.getId();

      System.out.println("Connected " + sessionId);
      this.sessions.put(sessionId, new Connection(sessionId, session));

      var in = session.receive()
          .map(WebSocketMessage::getPayloadAsText)
          .map(this::messageFromJson)
          .map(msg -> new Message(sessionId, msg.text(), Instant.now()))
          .map(this.messages::offer)
          .doFinally(st -> {
            if (st.equals(SignalType.ON_COMPLETE)) {
              this.sessions.remove(sessionId);
              System.out.println("Disconnected " + sessionId);
            }
          });

      var out = messagesToBroadcast
          .map(this::jsonFromMessage)
          .map(session::textMessage);

      return session.send(out).and(in);
    };
  }

  @Bean
  HandlerMapping chatHm() {
    return new SimpleUrlHandlerMapping() {
      {
        this.setUrlMap(Map.of("/ws/chat", chatWsh()));
        this.setOrder(2);
      }
    };
  }

  private Message messageFromJson(String json) {
    try {
      return this.objectMapper.readValue(json, Message.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private String jsonFromMessage(Message msg) {
    try {
      return this.objectMapper
          .writeValueAsString(msg);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
