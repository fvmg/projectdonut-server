package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Job;
import com.conestoga.projectdonut.entity.JobApplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {

    List<JobApplication> findAllByJob(Job job);

}
