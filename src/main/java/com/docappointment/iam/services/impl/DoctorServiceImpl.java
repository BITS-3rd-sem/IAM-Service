package com.docappointment.iam.services.impl;

import com.docappointment.iam.entities.PatientDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.docappointment.iam.dao.DoctorDetailsDao;
import com.docappointment.iam.dao.UserDao;
import com.docappointment.iam.dto.DoctorDTO;
import com.docappointment.iam.dto.NewDoctorDTO;
import com.docappointment.iam.dto.UserDTO;
import com.docappointment.iam.entities.DoctorDetails;
import com.docappointment.iam.entities.User;
import com.docappointment.iam.enums.Role;
import com.docappointment.iam.services.DoctorService;
import com.docappointment.iam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if (user.isPresent()) {
            DoctorDetails doctorDetails1 = doctorDetails.orElseGet(DoctorDetails::new);
            return mergeUserAndDetails(user.get(), doctorDetails1);
        }

        return null;
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<DoctorDTO> doctors = new ArrayList<>();
        List<User> users = userDao.findAllByRole(Role.DOCTOR);
        for (User user: users) {
            Optional<DoctorDetails> doctorDetails = doctorDetailsDao.findById(user.getUserId());
            doctorDetails.ifPresent(details -> doctors.add(mergeUserAndDetails(user, details)));
        }

        return doctors;
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
        try {
            userDao.deleteById(id);
            doctorDetailsDao.deleteById(id);
        } catch (Exception e) {
            return "Failed to delete doctor";
        }

        return "Deleted successfully";
    }

    @Override
    public DoctorDTO updateDoctorDetails(DoctorDTO doctorDTO, int id) {
        if (userDao.existsById(id) && doctorDetailsDao.existsById(id)) {
            ObjectMapper objectMapper = new ObjectMapper();

            Optional<User> user = userDao.findById(id);

            DoctorDetails doctorDetails = objectMapper.convertValue(doctorDTO, DoctorDetails.class);

            DoctorDetails savedDoctorDetails = doctorDetailsDao.save(doctorDetails);
            if (user.isPresent())
                return mergeUserAndDetails(user.get(), savedDoctorDetails);
        }

        return null;
    }
}
