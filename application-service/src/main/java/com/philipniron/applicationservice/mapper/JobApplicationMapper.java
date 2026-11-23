package com.philipniron.applicationservice.mapper;

import com.philipniron.applicationservice.dto.JobApplicationRequest;
import com.philipniron.applicationservice.dto.JobApplicationResponse;
import com.philipniron.applicationservice.model.JobApplication;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {
    public JobApplication toEntity(JobApplicationRequest request){
        return JobApplication.builder()
                .company(request.company())
                .jobTitle(request.jobTitle())
                .jobUrl(request.jobUrl())
                .status(request.status())
                .appliedDate(request.appliedDate())
                .cvVersion(request.cvVersion())
                .notes(request.notes())
                .build();
    }

    public JobApplicationResponse toResponse(JobApplication entity){
        return new JobApplicationResponse(
                entity.getId(),
                entity.getCompany(),
                entity.getJobTitle(),
                entity.getJobUrl(),
                entity.getStatus(),
                entity.getAppliedDate(),
                entity.getCvVersion(),
                entity.getNotes()
        );
    }

    public void updateEntity(JobApplication entity, JobApplicationRequest request){
        entity.setCompany(request.company());
        entity.setJobTitle(request.jobTitle());
        entity.setJobUrl(request.jobUrl());
        entity.setStatus(request.status());
        entity.setAppliedDate(request.appliedDate());
        entity.setCvVersion(request.cvVersion());
        entity.setNotes(request.notes());
    }
}

