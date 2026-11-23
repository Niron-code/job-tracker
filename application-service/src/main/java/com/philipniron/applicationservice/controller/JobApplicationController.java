package com.philipniron.applicationservice.controller;

import com.philipniron.applicationservice.dto.JobApplicationRequest;
import com.philipniron.applicationservice.dto.JobApplicationResponse;
import com.philipniron.applicationservice.model.ApplicationStatus;
import com.philipniron.applicationservice.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(@Valid @RequestBody JobApplicationRequest request){
        JobApplicationResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<JobApplicationResponse> getAll(@RequestParam(required = false) ApplicationStatus status){
        return service.getAll(status);
    }

    @GetMapping("/{id}")
    public JobApplicationResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse update(@PathVariable Long id, @Valid @RequestBody JobApplicationRequest request){
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public JobApplicationResponse updateStatus(@PathVariable Long id, @Valid @RequestParam ApplicationStatus status) {
        return service.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
