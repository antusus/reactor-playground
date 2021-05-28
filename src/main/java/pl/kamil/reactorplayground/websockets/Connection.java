package pl.kamil.reactorplayground.websockets;

import org.springframework.web.reactive.socket.WebSocketSession;

record Connection(String id, WebSocketSession session) {
}
