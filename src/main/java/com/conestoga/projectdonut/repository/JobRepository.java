package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Integer> {


}
