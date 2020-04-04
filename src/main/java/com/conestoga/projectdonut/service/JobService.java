package com.conestoga.projectdonut.service;

import com.conestoga.projectdonut.entity.Job;
import com.conestoga.projectdonut.repository.JobRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private GameService gameService;

    public Job updateJob(Job job) {
        Job updateJob = jobRepository.getOne(job.getId());
        updateJob.setName(job.getName());
        updateJob.setDescription(job.getDescription());
        updateJob.setRequirements(job.getRequirements());
        updateJob.setBenefits(job.getBenefits());
        return jobRepository.save(updateJob);
    }

    public Job saveJob(Job job, String gameId) {
        job = jobRepository.save(job);
        gameService.saveJob(job, gameId);
        return job;
    }

    public Job getJob(int jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        return job;
    }

    public List<Job> getAll(String gameId) {
        List<Job> jobs = jobRepository.findAll();
        List<Job> finalJobs = new ArrayList<>();
        if (gameId != null && jobs != null) {
            for (Job job : jobs) {
                if (gameService.containsJob(gameId, job)) {
                    finalJobs.add(job);
                }
            }
        } else {
            finalJobs = jobs;
        }
        return finalJobs;
    }
}
