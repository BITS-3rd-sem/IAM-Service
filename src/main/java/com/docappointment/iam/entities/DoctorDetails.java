package com.docappointment.iam.entities;

import com.docappointment.iam.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields like "gender"
public class DoctorDetails {

    @Id
    private int userId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @Column(unique = true)
    @NotEmpty(message = "Phone no is required")
    private String phoneNo;

    @NotNull(message = "Years of experience is required")
    private int experience;

    @NotEmpty(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Fees is required")
    private int fees;

    @NotNull(message = "Rating is required")
    private float rating;

    @Column(unique = true)
    @NotEmpty(message = "License is required")
    private String license;

    @ElementCollection
    @NotEmpty(message = "Degrees are required")
    private List<String> degrees;

    @ElementCollection
    @NotEmpty(message = "Known languages are required")
    private List<String> knownLanguages;

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public List<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<String> degrees) {
        this.degrees = degrees;
    }

    public List<String> getKnownLanguages() {
        return knownLanguages;
    }

    public void setKnownLanguages(List<String> knownLanguages) {
        this.knownLanguages = knownLanguages;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public int getUserId() {
        return userId;
    }
}
