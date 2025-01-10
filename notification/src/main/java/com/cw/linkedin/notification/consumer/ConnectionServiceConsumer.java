package com.cw.linkedin.notification.consumer;

import com.cw.linkedin.connection_service.event.AcceptConnectionRequestEvent;
import com.cw.linkedin.connection_service.event.SendConnectionRequestEvent;
import com.cw.linkedin.notification.service.SendNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final SendNotification sendNotification;


    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        String message = String.format("Your have received a connection request from user with id : %d", sendConnectionRequestEvent.getSenderId());
        sendNotification.send(sendConnectionRequestEvent.getReceiverId(), message);
    }

    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        String message = String.format("Your connection request has been accepted by the user with id : %d", acceptConnectionRequestEvent.getReceiverId());
        sendNotification.send(acceptConnectionRequestEvent.getSenderId(), message);
    }
}
