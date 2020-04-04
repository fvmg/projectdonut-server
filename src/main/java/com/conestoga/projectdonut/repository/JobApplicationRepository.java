package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {

}
