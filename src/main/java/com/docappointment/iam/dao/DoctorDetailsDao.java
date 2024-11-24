package com.docappointment.iam.dao;

import com.docappointment.iam.entities.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorDetailsDao extends JpaRepository<DoctorDetails, Integer> {

    Optional<DoctorDetails> findById(int id);
}
