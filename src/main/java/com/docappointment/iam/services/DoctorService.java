package com.docappointment.iam.services;

import com.docappointment.iam.dto.DoctorDTO;
import com.docappointment.iam.dto.NewDoctorDTO;
import com.docappointment.iam.dto.PaginatedDoctorsDTO;

import java.util.List;

public interface DoctorService {

    DoctorDTO createDoctor(NewDoctorDTO doctorDTO);

    DoctorDTO getDoctorById(int doctorId);

    PaginatedDoctorsDTO getAllDoctors(int pageNumber, int pageSize);

    String deleteDoctor(int id);

    DoctorDTO updateDoctorDetails(DoctorDTO doctorDTO, int id);

    List<String> getAllSpecializations();
}
