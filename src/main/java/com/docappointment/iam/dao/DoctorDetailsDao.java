package com.docappointment.iam.dao;

import com.docappointment.iam.entities.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorDetailsDao extends JpaRepository<DoctorDetails, Integer> {

    Optional<DoctorDetails> findById(int id);

    @Query("SELECT DISTINCT d.specialization FROM DoctorDetails d")
    List<String> findDistinctSpecializations();
}
