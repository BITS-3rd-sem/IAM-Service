package com.docappointment.iam.services.impl;

import com.docappointment.iam.dao.PatientDetailsDao;
import com.docappointment.iam.dao.UserDao;
import com.docappointment.iam.dto.PaginatedPatientsDTO;
import com.docappointment.iam.dto.PatientDTO;
import com.docappointment.iam.entities.PatientDetails;
import com.docappointment.iam.entities.User;
import com.docappointment.iam.enums.Role;
import com.docappointment.iam.exceptions.InvalidDataException;
import com.docappointment.iam.exceptions.ResourceNotFoundException;
import com.docappointment.iam.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (user.isPresent() && user.get().getRole().equals(Role.PATIENT)) {
            PatientDetails patientDetails1 = patientDetails.orElseGet(PatientDetails::new);
            return mergeUserAndDetails(user.get(), patientDetails1);
        } else {
            throw new ResourceNotFoundException("Invalid patient id");
        }

    }

    @Override
    public PaginatedPatientsDTO getAllPatients(int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            throw new InvalidDataException("pageNumber cannot be negative");
        }

        if (pageSize < 0) {
            throw new InvalidDataException("pageSize cannot be negative");
        }

        if (pageSize > 200) {
            throw new InvalidDataException("pageSize cannot be more than 200");
        }

        List<PatientDTO> patients = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc("userId")));
        Page<User> paginatedUsers = userDao.findByRole(Role.PATIENT, pageable);
        List<User> users = paginatedUsers.getContent();

        for (User user: users) {
            Optional<PatientDetails> patientDetails = patientDetailsDao.findById(user.getUserId());
            if (patientDetails.isPresent()) {
                patients.add(mergeUserAndDetails(user, patientDetails.get()));
            } else {
                patients.add(mergeUserAndDetails(user, new PatientDetails()));
            }
        }

        PaginatedPatientsDTO response = convertPaginatedDataToDTO(paginatedUsers);
        response.setPatients(patients);
        return response;
    }

    private PatientDTO mergeUserAndDetails(User user, PatientDetails patientDetails) {
        ObjectMapper objectMapper = new ObjectMapper();

        PatientDTO patientDTO = objectMapper.convertValue(patientDetails, PatientDTO.class);
        patientDTO.setUserId(user.getUserId());
        patientDTO.setname(user.getName());
        patientDTO.setEmail(user.getEmail());
        patientDTO.setRole(user.getRole());

        return patientDTO;
    }

    @Override
    public String deletePatient(int id) {
        Optional<User> user = userDao.findById(id);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("Invalid patient id");
        }

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
        if (userDao.existsById(id)) {
            ValidatePatientDetails(patientDTO);
            ObjectMapper objectMapper = new ObjectMapper();

            Optional<User> user = userDao.findById(id);

            PatientDetails patientDetails = objectMapper.convertValue(patientDTO, PatientDetails.class);

            float height = (float) Math.round(patientDetails.getHeight() * 100) / 100;
            patientDetails.setHeight(height);

            float weight = (float) Math.round(patientDetails.getWeight() * 100) / 100;
            patientDetails.setWeight(weight);

            PatientDetails savedPatientDetails = patientDetailsDao.save(patientDetails);
            if (user.isPresent())
                return mergeUserAndDetails(user.get(), savedPatientDetails);
        } else {
            throw new ResourceNotFoundException("Invalid patient id");
        }

        return null;
    }

    private PaginatedPatientsDTO convertPaginatedDataToDTO(Page<User> paginatedData) {
        PaginatedPatientsDTO dto = new PaginatedPatientsDTO();
        dto.setPageNumber(paginatedData.getPageable().getPageNumber());
        dto.setPageSize(paginatedData.getPageable().getPageSize());
        dto.setTotalPages(paginatedData.getTotalPages());
        dto.setTotalPatients((int) paginatedData.getTotalElements());

        return dto;
    }

    private void ValidatePatientDetails(PatientDTO patientDTO) {
         if (patientDTO.getAge() < 1) {
             throw new InvalidDataException("Invalid age value");
         }

        if (patientDTO.getHeight() < 1f) {
            throw new InvalidDataException("Invalid height value");
        }

        if (patientDTO.getWeight() < 1f) {
            throw new InvalidDataException("Invalid weight value");
        }

        String regex = "^\\+91\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(patientDTO.getPhoneNo());

        if (!matcher.matches()) {
            throw new InvalidDataException("Invalid phone no value, accepatble format: +91XXXXXXXXXX");
        }
    }
}
