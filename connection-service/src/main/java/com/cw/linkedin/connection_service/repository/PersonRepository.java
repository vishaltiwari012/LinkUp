package com.cw.linkedin.connection_service.repository;

import com.cw.linkedin.connection_service.entities.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {
    Optional<Person> getByName(String name);
    @Query("MATCH (personA:Person) -[:CONNECTED_TO]- (personB:Person) "+
            "WHERE personA.userId = $userId RETURN personB")
    List<Person> getFirstDegreeConnections(Long userId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) "+
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId "+
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]->(p2:Person) "+
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId "+
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person), (p2:Person) "+
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId "+
            "CREATE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) "+
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId "+
            "DELETE r "+
            "CREATE (p1)-[:CONNECTED_TO]->(p2)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) "+
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId "+
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);

//    @Query("MATCH (sender:Person {userId: $senderId}), (receiver:Person {userId: $receiverId}) " +
//            "MERGE (sender)-[:CONNECTED]->(receiver) " +
//            "RETURN sender, receiver")
//    void connectIfNotExists(Long senderId, Long receiverId);

    @Query("MATCH (p:Person {userId: $userId}) RETURN COUNT(p) > 0 AS userExists")
    boolean existsByUserId(Long userId);
}
