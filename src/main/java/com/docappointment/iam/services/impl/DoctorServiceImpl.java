package com.docappointment.iam.services.impl;

import com.docappointment.iam.dao.DoctorDetailsDao;
import com.docappointment.iam.dao.UserDao;
import com.docappointment.iam.dto.DoctorDTO;
import com.docappointment.iam.dto.NewDoctorDTO;
import com.docappointment.iam.dto.PaginatedDoctorsDTO;
import com.docappointment.iam.dto.UserDTO;
import com.docappointment.iam.entities.DoctorDetails;
import com.docappointment.iam.entities.User;
import com.docappointment.iam.enums.Role;
import com.docappointment.iam.exceptions.InvalidDataException;
import com.docappointment.iam.exceptions.ResourceNotFoundException;
import com.docappointment.iam.services.DoctorService;
import com.docappointment.iam.services.UserService;
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
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    UserDao userDao;

    @Autowired
    DoctorDetailsDao doctorDetailsDao;

    @Autowired
    UserService userService;

    @Override
    public DoctorDTO createDoctor(NewDoctorDTO doctorDTO) {
        ValidateDoctorDetails(doctorDTO);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.convertValue(doctorDTO, User.class);
        user.setRole(Role.DOCTOR);
        UserDTO savedUser = userService.registerUser(user);

        DoctorDetails doctorDetails = objectMapper.convertValue(doctorDTO, DoctorDetails.class);
        doctorDetails.setUserId(savedUser.getUserId());

        DoctorDetails savedDoctorDetails = doctorDetailsDao.save(doctorDetails);

        return mergeUserAndDetails(objectMapper.convertValue(savedUser,User.class), savedDoctorDetails);
    }

     @Override
     public DoctorDTO getDoctorById(int doctorId) {
        Optional<User> user = userDao.findById(doctorId);
        Optional<DoctorDetails> doctorDetails = doctorDetailsDao.findById(doctorId);

        if (user.isPresent() && user.get().getRole().equals(Role.DOCTOR)) {
            DoctorDetails doctorDetails1 = doctorDetails.orElseGet(DoctorDetails::new);
            return mergeUserAndDetails(user.get(), doctorDetails1);
        } else {
            throw new ResourceNotFoundException("Invalid doctor id");
        }
    }

    @Override
    public PaginatedDoctorsDTO getAllDoctors(int pageNumber, int pageSize, String specialization) {
        if (pageNumber < 0) {
            throw new InvalidDataException("pageNumber cannot be negative");
        }

        if (pageSize < 0) {
            throw new InvalidDataException("pageSize cannot be negative");
        }

        if (pageSize > 200) {
            throw new InvalidDataException("pageSize cannot be more than 200");
        }

        List<DoctorDTO> doctors = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc("userId")));

        Page<User> paginatedUsers = userDao.findByRole(Role.DOCTOR, pageable);

        List<User> users = paginatedUsers.getContent();

        for (User user: users) {
            Optional<DoctorDetails> doctorDetails = doctorDetailsDao.findById(user.getUserId());
            if (specialization == null) {
                doctorDetails.ifPresent(details -> doctors.add(mergeUserAndDetails(user, details)));
            } else if (specialization.isBlank()) {
                throw new InvalidDataException("specialization cannot be blank");
            } else {
                doctorDetails.ifPresent(details -> {
                    if (specialization.equals(details.getSpecialization())) {
                        doctors.add(mergeUserAndDetails(user, details));
                    }
                });
            }
        }

        PaginatedDoctorsDTO response = convertPaginatedDataToDTO(paginatedUsers);
        response.setDoctors(doctors);
        return response;
    }

    private DoctorDTO mergeUserAndDetails(User user, DoctorDetails doctorDetails) {
        ObjectMapper objectMapper = new ObjectMapper();

        DoctorDTO doctorDTO = objectMapper.convertValue(doctorDetails, DoctorDTO.class);
        doctorDTO.setUserId(user.getUserId());
        doctorDTO.setname(user.getName());
        doctorDTO.setEmail(user.getEmail());
        doctorDTO.setRole(user.getRole());

        return doctorDTO;
    }

    @Override
    public String deleteDoctor(int id) {
        Optional<User> user = userDao.findById(id);
        Optional<DoctorDetails> doctorDetails = doctorDetailsDao.findById(id);

        if (user.isEmpty() || doctorDetails.isEmpty()) {
            throw new ResourceNotFoundException("Invalid doctor id");
        }

        try {
            userDao.deleteById(id);
            doctorDetailsDao.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete doctor");
        }

        return "Deleted successfully";
    }

    @Override
    public DoctorDTO updateDoctorDetails(DoctorDTO doctorDTO, int id) {
        if (userDao.existsById(id) && doctorDetailsDao.existsById(id)) {
            ValidateDoctorDetails(doctorDTO);
            ObjectMapper objectMapper = new ObjectMapper();

            Optional<User> user = userDao.findById(id);

            DoctorDetails doctorDetails = objectMapper.convertValue(doctorDTO, DoctorDetails.class);
            doctorDetails.setUserId(user.get().getUserId());
            DoctorDetails savedDoctorDetails = doctorDetailsDao.save(doctorDetails);
            if (user.isPresent())
                return mergeUserAndDetails(user.get(), savedDoctorDetails);
        } else {
            throw new ResourceNotFoundException("Invalid doctor id");
        }

        return null;
    }

    private PaginatedDoctorsDTO convertPaginatedDataToDTO(Page<User> paginatedData) {
        PaginatedDoctorsDTO dto = new PaginatedDoctorsDTO();
        dto.setPageNumber(paginatedData.getPageable().getPageNumber());
        dto.setPageSize(paginatedData.getPageable().getPageSize());
        dto.setTotalPages(paginatedData.getTotalPages());
        dto.setTotalDoctors((int) paginatedData.getTotalElements());

        return dto;
    }

    @Override
    public List<String> getAllSpecializations() {
        return doctorDetailsDao.findDistinctSpecializations();
    }

    private void ValidateDoctorDetails(NewDoctorDTO doctorDTO) {
        if (doctorDTO.getExperience() < 0) {
            throw new InvalidDataException("Invalid experience value");
        }

        if (doctorDTO.getFees() < 0) {
            throw new InvalidDataException("Invalid Fees value");
        }

        if (doctorDTO.getRating() < 0) {
            throw new InvalidDataException("Invalid rating value");
        }

        if (doctorDTO.getSpecialization() == null || doctorDTO.getSpecialization().isBlank()) {
            throw new InvalidDataException("Invalid specialization value");
        }

        if (doctorDTO.getLicense() == null || doctorDTO.getLicense().isBlank()) {
            throw new InvalidDataException("Invalid Licence value");
        }

        if (doctorDTO.getRole().equals(Role.DOCTOR)) {
            throw new InvalidDataException("Invalid role value");
        }

        if (doctorDTO.getDegrees().isEmpty()) {
            throw new InvalidDataException("Invalid degrees value");
        }

        if (doctorDTO.getKnownLanguages().isEmpty()) {
            throw new InvalidDataException("Invalid known languages value");
        }

        if (doctorDTO.getname() == null || doctorDTO.getname().isBlank()) {
            throw new InvalidDataException("Invalid name value");
        }

        if (doctorDTO.getEmail() == null || doctorDTO.getEmail().isBlank()) {
            throw new InvalidDataException("Invalid email value");
        }

        String regex = "^\\+91\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(doctorDTO.getPhoneNo());

        if (matcher.matches()) {
            throw new InvalidDataException("Invalid phone no value");
        }
    }

    private void ValidateDoctorDetails(DoctorDTO doctorDTO) {
        if (doctorDTO.getExperience() < 0) {
            throw new InvalidDataException("Invalid experience value");
        }

        if (doctorDTO.getFees() < 0) {
            throw new InvalidDataException("Invalid Fees value");
        }

        if (doctorDTO.getRating() < 0) {
            throw new InvalidDataException("Invalid rating value");
        }

        if (doctorDTO.getSpecialization() == null || doctorDTO.getSpecialization().isBlank()) {
            throw new InvalidDataException("Invalid specialization value");
        }

        if (doctorDTO.getLicense() == null || doctorDTO.getLicense().isBlank()) {
            throw new InvalidDataException("Invalid Licence value");
        }

        if (!doctorDTO.getRole().equals(Role.DOCTOR)) {
            throw new InvalidDataException("Invalid role value");
        }

        if (doctorDTO.getDegrees().isEmpty()) {
            throw new InvalidDataException("Invalid degrees value");
        }

        if (doctorDTO.getKnownLanguages().isEmpty()) {
            throw new InvalidDataException("Invalid known languages value");
        }

        if (doctorDTO.getname() == null || doctorDTO.getname().isBlank()) {
            throw new InvalidDataException("Invalid name value");
        }

        if (doctorDTO.getEmail() == null || doctorDTO.getEmail().isBlank()) {
            throw new InvalidDataException("Invalid email value");
        }

        String regex = "^\\+91\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(doctorDTO.getPhoneNo());

        if (!matcher.matches()) {
            throw new InvalidDataException("Invalid phone no value, accepatble format: +91XXXXXXXXXX");
        }
    }
}
