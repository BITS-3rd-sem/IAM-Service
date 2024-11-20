package com.docappointment.iam.dto;

import com.docappointment.iam.entities.User;
import com.docappointment.iam.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields like "gender"
public class UserDTO {

    public int userId;

    public String name;

    public String email;

    public Role role;

    public UserDTO(int userId, String name, String email, Role role) {
        super();
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public UserDTO(User user) {
        super();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }

    public UserDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
