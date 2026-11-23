package com.philipniron.applicationservice.service;

import com.philipniron.applicationservice.dto.JobApplicationRequest;
import com.philipniron.applicationservice.dto.JobApplicationResponse;
import com.philipniron.applicationservice.exception.ResourceNotFoundException;
import com.philipniron.applicationservice.mapper.JobApplicationMapper;
import com.philipniron.applicationservice.model.ApplicationStatus;
import com.philipniron.applicationservice.model.JobApplication;
import com.philipniron.applicationservice.repository.JobApplicationRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository repository;
    private final JobApplicationMapper mapper;

    public JobApplicationServiceImpl(JobApplicationRepository repository, JobApplicationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public JobApplicationResponse create(JobApplicationRequest request) {
        JobApplication saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAll(ApplicationStatus status) {
        List<JobApplication> entities = (status == null) ? repository.findAll() : repository.findByStatus(status);
        return entities.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public JobApplicationResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));

    }

    @Override
    public JobApplicationResponse update(Long id, JobApplicationRequest request) {
        JobApplication entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public JobApplicationResponse updateStatus(Long id, ApplicationStatus status) {
        JobApplication entity = findEntity(id);
        entity.setStatus(status);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findEntity(id));
    }

    private JobApplication findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Job application not found with id "+ id));
    }
}
