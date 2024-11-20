package com.docappointment.iam.dao;

import com.docappointment.iam.entities.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientDetailsDao extends JpaRepository<PatientDetails, Integer> {

    Optional<PatientDetails> findById(int id);
}
