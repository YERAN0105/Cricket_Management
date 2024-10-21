package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.PractiseSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PractiseSessionRepository extends JpaRepository<PractiseSession, Long> {
//    List<PractiseSession> findByAssignedCoach_CoachId(Long coachId);
//    List<PractiseSession> findByUnder(String under);
@Query("SELECT ps FROM PractiseSession ps JOIN ps.coaches c WHERE c.coachId = :coachId")
List<PractiseSession> findAllByCoachId(@Param("coachId") Long coachId);
}

