package com.cw.linkedin.notification.consumer;

import com.cw.linkedin.notification.client.ConnectionsClient;
import com.cw.linkedin.notification.dto.PersonDto;
import com.cw.linkedin.notification.entity.Notification;
import com.cw.linkedin.notification.repository.NotificationRepository;
import com.cw.linkedin.notification.service.SendNotification;
import com.cw.linkedin.post_service.event.PostCreatedEvent;
import com.cw.linkedin.post_service.event.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceConsumer {
    private final ConnectionsClient connectionsClient;
    private final SendNotification sendNotification;


    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Send notifications : handlePostCreated, {}", postCreatedEvent);
        List<PersonDto> connections = connectionsClient.getFirstConnections(postCreatedEvent.getCreatorId());
        for(PersonDto connection : connections) {
            sendNotification.send(connection.getUserId(), "Your connection " + postCreatedEvent.getCreatorId() + " has created a post. Check it out.");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Send notifications : handlePostLiked, {}", postLikedEvent);
        String message = String.format("Your post, %d has been liked by % d", postLikedEvent.getPostId(), postLikedEvent.getLikedByUserId());
        sendNotification.send(postLikedEvent.getCreatorId(), message);
    }
    
}
