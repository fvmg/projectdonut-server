package com.conestoga.projectdonut.repository;

import com.conestoga.projectdonut.entity.Game;
import com.conestoga.projectdonut.entity.Job;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobRepository extends JpaRepository<Job, Integer> {

    @Query(nativeQuery = true, value = "select gj.game_id from game_jobs gj where gj.jobs_id = :jobId")
    Integer getGameId(@Param("jobId") int jobId);

}
