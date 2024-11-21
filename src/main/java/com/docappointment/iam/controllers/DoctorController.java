package com.docappointment.iam.controllers;

import com.docappointment.iam.dto.DoctorDTO;
import com.docappointment.iam.services.DoctorService;
import com.docappointment.iam.dto.NewDoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@CrossOrigin
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @GetMapping
    private List<DoctorDTO> getDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping ("/{id}")
    private DoctorDTO getDoctorById(@PathVariable int id) {
        return doctorService.getDoctorById(id);
    }

    @PutMapping("/{id}")
    private DoctorDTO updateDoctorById(@RequestBody DoctorDTO doctorDTO, @PathVariable int id) {
        return doctorService.updateDoctorDetails(doctorDTO, id);
    }

    @DeleteMapping("/{id}")
    private String deleteDoctorById(@PathVariable int id) {
        return doctorService.deleteDoctor(id);
    }

    @PostMapping
    private DoctorDTO createDoctor(@RequestBody NewDoctorDTO doctorDTO) {
        return doctorService.createDoctor(doctorDTO);
    }
}
