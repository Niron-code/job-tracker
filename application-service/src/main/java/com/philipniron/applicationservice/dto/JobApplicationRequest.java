package com.philipniron.applicationservice.dto;

import com.philipniron.applicationservice.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record JobApplicationRequest(
        @NotBlank String company,
        @NotBlank String jobTitle,
        String jobUrl,
        @NotNull ApplicationStatus status,
        LocalDate appliedDate,
        String cvVersion,
        @Size(max = 2000) String notes
) {
}
