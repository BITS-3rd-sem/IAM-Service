package com.docappointment.iam.services;

import com.docappointment.iam.dto.DoctorDTO;
import com.docappointment.iam.dto.NewDoctorDTO;

import java.util.List;

public interface DoctorService {

    DoctorDTO createDoctor(NewDoctorDTO doctorDTO);

    DoctorDTO getDoctorById(int doctorId);

    List<DoctorDTO> getAllDoctors();

    String deleteDoctor(int id);

    DoctorDTO updateDoctorDetails(DoctorDTO doctorDTO, int id);
}
