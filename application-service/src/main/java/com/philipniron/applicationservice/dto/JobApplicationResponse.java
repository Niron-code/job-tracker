package com.philipniron.applicationservice.dto;

import com.philipniron.applicationservice.model.ApplicationStatus;


import java.time.LocalDate;

public record JobApplicationResponse(
        Long id,
        String company,
        String jobTitle,
        String jobUrl,
        ApplicationStatus status,
        LocalDate appliedDate,
        String cvVersion,
        String notes
) {
}
