package com.conestoga.projectdonut.jobs;

import com.conestoga.projectdonut.service.GameService;
import com.conestoga.projectdonut.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RecommendationSchedule {

    @Autowired
    private GameService gameService;

    @Autowired
    private JobService jobService;

    @Scheduled(fixedDelay = 120 * 1000)
    public void recommendGame() {
        gameService.recommendGame();
    }

    @Scheduled(fixedDelay = 120 * 1000)
    public void recommendJob() {
        jobService.recommendJob();
    }

}
