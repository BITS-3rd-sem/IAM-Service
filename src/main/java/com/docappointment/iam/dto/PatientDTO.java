package com.docappointment.iam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.docappointment.iam.enums.Gender;

@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields like "gender"
public class PatientDTO extends UserDTO {

    private Gender gender;

    private int age;

    private String phoneNo;

    private float height;

    private float weight;

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
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
}
