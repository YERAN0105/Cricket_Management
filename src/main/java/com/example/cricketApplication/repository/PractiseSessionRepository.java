package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.PractiseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PractiseSessionRepository extends JpaRepository<PractiseSession, Long> {
    // Custom query to get practice sessions for a player through the player's teams
    @Query("SELECT ps FROM PractiseSession ps " +
            "JOIN ps.team t " +
            "JOIN t.players p " +
            "WHERE p.playerId = :playerId")
    List<PractiseSession> findPractiseSessionsByPlayerId(Long playerId);

//    List<PractiseSession> findByAssignedCoach_CoachId(Long coachId);
//    List<PractiseSession> findByUnder(String under);
@Query("SELECT ps FROM PractiseSession ps JOIN ps.coaches c WHERE c.coachId = :coachId")
List<PractiseSession> findAllByCoachId(@Param("coachId") Long coachId);

}

