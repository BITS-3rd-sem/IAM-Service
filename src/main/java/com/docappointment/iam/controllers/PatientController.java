package com.docappointment.iam.controllers;

import com.docappointment.iam.dto.PaginatedPatientsDTO;
import com.docappointment.iam.dto.PatientDTO;
import com.docappointment.iam.services.PatientService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
@CrossOrigin
public class PatientController {
    
    @Autowired
    PatientService patientService;

    @GetMapping
    private ResponseEntity<PaginatedPatientsDTO> getPatients(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "20") int pageSize) {
        return ResponseEntity.ok().body(patientService.getAllPatients(pageNumber, pageSize));
    }

    @GetMapping ("/{id}")
    private ResponseEntity<PatientDTO> getPatientById(@PathVariable int id) {
        return ResponseEntity.ok().body(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    @RolesAllowed({"ADMIN", "PATIENT"})
    private ResponseEntity<PatientDTO> updatePatientById(@RequestBody PatientDTO patientDTO, @PathVariable int id) {
        return ResponseEntity.ok().body(patientService.updatePatientDetails(patientDTO, id));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"ADMIN", "PATIENT"})
    private ResponseEntity<String> deletePatientById(@PathVariable int id) {
        return ResponseEntity.ok().body(patientService.deletePatient(id));
    }
}
