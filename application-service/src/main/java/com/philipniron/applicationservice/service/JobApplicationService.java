package com.philipniron.applicationservice.service;

import com.philipniron.applicationservice.dto.JobApplicationRequest;
import com.philipniron.applicationservice.dto.JobApplicationResponse;
import com.philipniron.applicationservice.model.ApplicationStatus;

import java.util.List;

public interface JobApplicationService {
    JobApplicationResponse create(JobApplicationRequest request);
    List<JobApplicationResponse> getAll(ApplicationStatus status);
    JobApplicationResponse getById(Long id);
    JobApplicationResponse update(Long id, JobApplicationRequest request);
    JobApplicationResponse updateStatus(Long id, ApplicationStatus status);
    void delete(Long id);
}
