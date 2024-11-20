package com.docappointment.iam.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)  // Ignore unknown fields like "gender"
public class LoginDTO {

    @NotEmpty(message = "Email is required")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Email is not valid.")
    public String email;

    @NotEmpty(message = "Password is required")
    public String password;

    public LoginDTO(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }

    public LoginDTO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
