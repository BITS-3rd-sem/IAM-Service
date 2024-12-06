package com.docappointment.iam.controllers;

import com.docappointment.iam.dto.DoctorDTO;
import com.docappointment.iam.dto.PaginatedDoctorsDTO;
import com.docappointment.iam.services.DoctorService;
import com.docappointment.iam.dto.NewDoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@CrossOrigin
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping
    private ResponseEntity<PaginatedDoctorsDTO> getDoctors(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String specialization) {
        return ResponseEntity.ok().body(doctorService.getAllDoctors(pageNumber,pageSize, specialization));
    }

    @GetMapping ("/{id}")
    private ResponseEntity<DoctorDTO> getDoctorById(@PathVariable int id) {
        return ResponseEntity.ok().body(doctorService.getDoctorById(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<DoctorDTO> updateDoctorById(@RequestBody DoctorDTO doctorDTO, @PathVariable int id) {
        return ResponseEntity.ok().body(doctorService.updateDoctorDetails(doctorDTO, id));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteDoctorById(@PathVariable int id) {
        return ResponseEntity.ok().body(doctorService.deleteDoctor(id));
    }

    @PostMapping
    private ResponseEntity<DoctorDTO> createDoctor(@RequestBody NewDoctorDTO doctorDTO) {
        return ResponseEntity.ok().body(doctorService.createDoctor(doctorDTO));
    }
}
