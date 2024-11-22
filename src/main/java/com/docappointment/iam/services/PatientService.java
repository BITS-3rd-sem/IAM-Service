package com.docappointment.iam.services;

import com.docappointment.iam.dto.PatientDTO;

import java.util.List;

public interface PatientService {

    PatientDTO getPatientById(int patientId);

    List<PatientDTO> getAllPatients();

    String deletePatient(int id);

    PatientDTO updatePatientDetails(PatientDTO patientDTO, int id);
}
