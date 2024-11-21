package com.docappointment.iam.services.impl;

import com.docappointment.iam.dao.PatientDetailsDao;
import com.docappointment.iam.dao.UserDao;
import com.docappointment.iam.dto.PatientDTO;
import com.docappointment.iam.entities.PatientDetails;
import com.docappointment.iam.entities.User;
import com.docappointment.iam.enums.Role;
import com.docappointment.iam.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    UserDao userDao;

    @Autowired
    PatientDetailsDao patientDetailsDao;

     @Override
     public PatientDTO getPatientById(int patientId) {
        Optional<User> user = userDao.findById(patientId);
        Optional<PatientDetails> patientDetails = patientDetailsDao.findById(patientId);

        if (user.isPresent() && patientDetails.isPresent()) {
            return mergeUserAndDetails(user.get(), patientDetails.get());
        }

        return null;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<PatientDTO> patients = new ArrayList<>();
        List<User> users = userDao.findAllByRole(Role.PATIENT);
        for (User user: users) {
            Optional<PatientDetails> patientDetails = patientDetailsDao.findById(user.getUserId());
            if (patientDetails.isPresent()) {
                patients.add(mergeUserAndDetails(user, patientDetails.get()));
            } else {
                patients.add(mergeUserAndDetails(user, new PatientDetails()));
            }
        }

        return patients;
    }

    private PatientDTO mergeUserAndDetails(User user, PatientDetails patientDetails) {
        ObjectMapper objectMapper = new ObjectMapper();

        PatientDTO patientDTO = objectMapper.convertValue(patientDetails, PatientDTO.class);
        patientDTO.setname(user.getName());
        patientDTO.setEmail(user.getEmail());
        patientDTO.setRole(user.getRole());

        return patientDTO;
    }

    @Override
    public String deletePatient(int id) {
        try {
            userDao.deleteById(id);
            patientDetailsDao.deleteById(id);
        } catch (Exception e) {
            return "Failed to delete patient";
        }

        return "Deleted successfully";
    }

    @Override
    public PatientDTO updatePatientDetails(PatientDTO patientDTO, int id) {
        if (userDao.existsById(patientDTO.getUserId())) {
            ObjectMapper objectMapper = new ObjectMapper();

            Optional<User> user = userDao.findById(id);

            PatientDetails patientDetails = objectMapper.convertValue(patientDTO, PatientDetails.class);

            PatientDetails savedPatientDetails = patientDetailsDao.save(patientDetails);
            if (user.isPresent())
                return mergeUserAndDetails(user.get(), savedPatientDetails);
        }

        return null;
    }
}
