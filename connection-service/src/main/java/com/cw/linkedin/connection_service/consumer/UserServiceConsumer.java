package com.cw.linkedin.connection_service.consumer;

import com.cw.linkedin.connection_service.entities.Person;
import com.cw.linkedin.connection_service.repository.PersonRepository;
import com.cw.linkedin.user_service.event.AddUserNodeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceConsumer {

    private final PersonRepository personRepository;

    @KafkaListener(topics = "add-user-node-topic")
    @Transactional
    public void handleAddUserNode(AddUserNodeEvent event) {
        log.info("Adding a node of user into database");

        Person person = new Person();
        person.setUserId(event.getUserId());
        person.setName(event.getName());
        person.setEmail(event.getEmail());

        // Save Person node to Neo4j
        personRepository.save(person);

        log.info("Person node created in Neo4j for: {}",event.getName());
    }

}
