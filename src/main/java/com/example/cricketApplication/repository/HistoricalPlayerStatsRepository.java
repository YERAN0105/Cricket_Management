package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.HistoricalPlayerStats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricalPlayerStatsRepository extends JpaRepository<HistoricalPlayerStats, Long> {
    List<HistoricalPlayerStats> findByPlayer_PlayerId(Long playerId);
}
