package com.cecilireid.springchallenges.repositories;

import com.cecilireid.springchallenges.models.Status;
import com.cecilireid.springchallenges.models.CateringJob;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CateringJobRepository extends CrudRepository<CateringJob, Long> {
    List<CateringJob> findAll();

    List<CateringJob> findByStatus(Status status);

    boolean existsByStatus(Status status);
}
