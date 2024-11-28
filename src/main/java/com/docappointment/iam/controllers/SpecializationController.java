package com.docappointment.iam.controllers;

import com.docappointment.iam.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/specialization")
public class SpecializationController {

    @Autowired
    DoctorService doctorService;

    @GetMapping
    private ResponseEntity<List<String>> getAllSpecializations() {
        return ResponseEntity.ok().body(doctorService.getAllSpecializations());
    }
}
