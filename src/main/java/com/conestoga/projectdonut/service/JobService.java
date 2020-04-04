package com.conestoga.projectdonut.service;

import com.conestoga.projectdonut.dto.JobDto;
import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.Job;
import com.conestoga.projectdonut.entity.JobApplication;
import com.conestoga.projectdonut.entity.User;
import com.conestoga.projectdonut.repository.GameRepository;
import com.conestoga.projectdonut.repository.JobApplicationRepository;
import com.conestoga.projectdonut.repository.JobRepository;
import com.conestoga.projectdonut.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private GameRepository gameRepository;

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

    public List<JobDto> getAllList() {
        List<Job> jobs = jobRepository.findAll();
        List<JobDto> jobDtos = new ArrayList<>();
        for (Job job : jobs) {
            JobDto jobDto = new JobDto();
            int gameId = jobRepository.getGameId(job.getId());
            jobDto.setGameId(gameId);
            Game game = gameRepository.getOne(gameId);
            jobDto.setGameName(game.getName());
            jobDto.setCoverImage(gameService.getGameImg(gameId));
            jobDto.setName(job.getName());
            jobDto.setDescription(job.getDescription());
            if (jobDto.getDescription().length() > 250) {
                String description = jobDto.getDescription().substring(0, 250) + "...";
                jobDto.setDescription(description);
            }
            jobDtos.add(jobDto);
        }
        return jobDtos;
    }

    public void applyJob(String jobId, String userId, MultipartFile resume, MultipartFile coverLetter) throws IOException {
        Job job = jobRepository.getOne(Integer.parseInt(jobId));
        User user = userRepository.getOne(Integer.parseInt(userId));
        JobApplication jobApplication = new JobApplication();
        jobApplication.setJob(job);
        jobApplication.setUser(user);
        jobApplication.setResume(gameService.compressBytes(resume.getBytes()));
        if (coverLetter != null) {
            jobApplication.setCoverLetter(gameService.compressBytes(coverLetter.getBytes()));
        }
        jobApplicationRepository.save(jobApplication);
    }
}
