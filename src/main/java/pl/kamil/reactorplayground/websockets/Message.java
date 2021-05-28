package pl.kamil.reactorplayground.websockets;

import java.time.Instant;

record Message(String clientId, String text, Instant when) {
}
