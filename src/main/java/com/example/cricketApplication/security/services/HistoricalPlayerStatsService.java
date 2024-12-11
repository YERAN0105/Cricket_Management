package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.HistoricalPlayerStats;
import com.example.cricketApplication.payload.response.HistoricalPlayerStatsResponse;
import com.example.cricketApplication.repository.HistoricalPlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricalPlayerStatsService {

    private final HistoricalPlayerStatsRepository statsRepository;

    @Autowired
    public HistoricalPlayerStatsService(HistoricalPlayerStatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public HistoricalPlayerStats addHistoricalPlayerStats(HistoricalPlayerStats stats) {
        return statsRepository.save(stats);
    }

    public HistoricalPlayerStatsResponse getHistoricalPlayerStatsById(Long id) {
        HistoricalPlayerStats historicalPlayerStats = statsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HistoricalPlayerStats not found with id: " + id));
        return refactorResponse(historicalPlayerStats);
    }

    public List<HistoricalPlayerStatsResponse> getAllHistoricalPlayerStats() {
        List<HistoricalPlayerStats> historicalStatList = statsRepository.findAll();
        List<HistoricalPlayerStatsResponse> responseList = new ArrayList<>();
        for (HistoricalPlayerStats historicalStat : historicalStatList) {
            responseList.add(refactorResponse(historicalStat));
        }
        return responseList;
    }

    public void deleteHistoricalPlayerStats(Long id) {
        if (statsRepository.existsById(id)) {
            statsRepository.deleteById(id);
        } else {
            throw new RuntimeException("HistoricalPlayerStats not found with id: " + id);
        }
    }

    public List<HistoricalPlayerStatsResponse> getHistoricalPlayerStatsByPlayerId(Long playerId) {
        List<HistoricalPlayerStats> statsList = statsRepository.findByPlayer_PlayerId(playerId);
        return statsList.stream()
                .map(this::refactorResponse)
                .collect(Collectors.toList());
    }

    private HistoricalPlayerStatsResponse refactorResponse(HistoricalPlayerStats stats) {
        HistoricalPlayerStatsResponse response = new HistoricalPlayerStatsResponse();
        response.setId(stats.getId());
        response.setInning(stats.getInning());
        response.setRuns(stats.getRuns());
        response.setWickets(stats.getWickets());
        response.setFours(stats.getFours());
        response.setSixers(stats.getSixers());
        response.setFifties(stats.getFifties());
        response.setCenturies(stats.getCenturies());
        response.setBalls(stats.getBalls());
        response.setOvers(stats.getOvers());
        response.setRunsConceded(stats.getRunsConceded());
        response.setNoBalls(stats.getNoBalls());
        response.setWides(stats.getWides());
        response.setMaidens(stats.getMaidens());
        response.setCatches(stats.getCatches());
        response.setStumps(stats.getStumps());
        response.setRunOuts(stats.getRunOuts());
        response.setMatchType(stats.getMatchType());
        response.setPlayerId(stats.getPlayer().getPlayerId());
        response.setCreatedBy(stats.getCreatedBy());
        response.setCreatedOn(stats.getCreatedOn());
        response.setUpdatedBy(stats.getUpdatedBy());
        response.setUpdatedOn(stats.getUpdatedOn());
        return response;
    }

    public HistoricalPlayerStatsResponse updateHistoricalPlayerStats(Long id, HistoricalPlayerStats updatedStats) {
        return statsRepository.findById(id).map(stats -> {
            stats.setInning(updatedStats.getInning());
            stats.setRuns(updatedStats.getRuns());
            stats.setWickets(updatedStats.getWickets());
            stats.setFours(updatedStats.getFours());
            stats.setSixers(updatedStats.getSixers());
            stats.setFifties(updatedStats.getFifties());
            stats.setCenturies(updatedStats.getCenturies());
            stats.setBalls(updatedStats.getBalls());
            stats.setOvers(updatedStats.getOvers());
            stats.setRunsConceded(updatedStats.getRunsConceded());
            stats.setNoBalls(updatedStats.getNoBalls());
            stats.setWides(updatedStats.getWides());
            stats.setMaidens(updatedStats.getMaidens());
            stats.setCatches(updatedStats.getCatches());
            stats.setStumps(updatedStats.getStumps());
            stats.setRunOuts(updatedStats.getRunOuts());
            stats.setMatchType(updatedStats.getMatchType());
            stats.setUpdatedBy(updatedStats.getUpdatedBy());
            stats.setUpdatedOn(updatedStats.getUpdatedOn());
            //stats.setPlayer(updatedStats.getPlayer());
            return refactorResponse(statsRepository.save(stats));
        }).orElseThrow(() -> new RuntimeException("HistoricalPlayerStats not found with id: " + id));
    }
}
