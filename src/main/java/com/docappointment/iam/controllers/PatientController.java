package com.docappointment.iam.controllers;

import com.docappointment.iam.dto.PatientDTO;
import com.docappointment.iam.services.PatientService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {
    
    @Autowired
    PatientService patientService;

    @GetMapping
    private List<PatientDTO> getPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping ("/{id}")
    private PatientDTO getPatientById(@PathVariable int id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    @RolesAllowed({"ADMIN", "PATIENT"})
    private PatientDTO updatePatientById(@RequestBody PatientDTO patientDTO, @PathVariable int id) {
        return patientService.updatePatientDetails(patientDTO, id);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"ADMIN", "PATIENT"})
    private String deletePatientById(@PathVariable int id) {
        return patientService.deletePatient(id);
    }
}
