package com.docappointment.iam.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedDoctorsDTO {
    int pageNumber;
    int pageSize;
    int totalPages;
    int totalDoctors;
    List<DoctorDTO> doctors;
}
