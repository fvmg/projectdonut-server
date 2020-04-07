package com.conestoga.projectdonut.controller;

import com.conestoga.projectdonut.dto.JobApplicationDto;
import com.conestoga.projectdonut.dto.JobDto;
import com.conestoga.projectdonut.entity.Job;
import com.conestoga.projectdonut.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/createJob")
    public Job createJob(@RequestParam("jobData") String jobData,
            @RequestParam("gameId") String gameId)
            throws IOException {
        Job job = new ObjectMapper().readValue(jobData, Job.class);
        if (job.getId() > 0) {
            return jobService.updateJob(job);
        }
        return jobService.saveJob(job, gameId);
    }

    @GetMapping("/get")
    public Job getJob(@RequestParam int id) {
        return jobService.getJob(id);
    }

    @GetMapping("/getApplications")
    public List<JobApplicationDto> getApplications(@RequestParam int id) {
        return jobService.getApplications(id);
    }

    @GetMapping("/getAll")
    public List<Job> getJobs(@RequestParam(value = "gameId") String gameId) {
        return jobService.getAll(gameId);
    }

    @GetMapping("/getAllList")
    public List<JobDto> getAllList() {
        return jobService.getAllList();
    }

    @PostMapping("/applyJob")
    public ResponseEntity<?> applyJob(@RequestParam(value = "resume") MultipartFile resume,
            @RequestParam(value = "coverLetter", required = false) MultipartFile coverLetter,
            @RequestParam("jobId") String jobId, @RequestParam("userId") String userId) throws IOException {
        jobService.applyJob(jobId, userId, resume, coverLetter);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getForYouJobs")
    public List<JobDto> getForYouJobs(@RequestParam(value = "userId") int userId) {
        return jobService.getForYouJobs(userId);
    }

}
