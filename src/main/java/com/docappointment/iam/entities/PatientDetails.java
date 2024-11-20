package com.docappointment.iam.entities;

import com.docappointment.iam.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields like "gender"
public class PatientDetails {

    @Id
    private int userId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Age is required")
    private int age;

    @Column(unique = true)
    @NotEmpty(message = "Phone no Id is required")
    private String phoneNo;

    @NotNull(message = "Weight is required")
    private float weight;

    @NotNull(message = "Height Id is required")
    private float height;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public int getId() {
        return userId;
    }
}
