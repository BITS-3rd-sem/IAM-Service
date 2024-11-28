package com.docappointment.iam.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedPatientsDTO {
    int pageNumber;
    int pageSize;
    int totalPages;
    int totalPatients;
    List<PatientDTO> patients;
}
