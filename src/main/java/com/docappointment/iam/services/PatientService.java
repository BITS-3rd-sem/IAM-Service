package com.docappointment.iam.services;

import com.docappointment.iam.dto.PaginatedPatientsDTO;
import com.docappointment.iam.dto.PatientDTO;

import java.util.List;

public interface PatientService {

    PatientDTO getPatientById(int patientId);

    PaginatedPatientsDTO getAllPatients(int pageNumber, int pageSize);

    String deletePatient(int id);

    PatientDTO updatePatientDetails(PatientDTO patientDTO, int id);
}
