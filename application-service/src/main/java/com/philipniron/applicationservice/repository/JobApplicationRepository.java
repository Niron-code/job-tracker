package com.philipniron.applicationservice.repository;

import com.philipniron.applicationservice.model.ApplicationStatus;
import com.philipniron.applicationservice.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    List<JobApplication> findByStatus(ApplicationStatus status);
}
