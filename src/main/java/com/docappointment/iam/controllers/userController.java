package com.docappointment.iam.controllers;

import com.docappointment.iam.dto.LoginDTO;
import com.docappointment.iam.dto.LoginResponseDTO;
import com.docappointment.iam.enums.Role;
import com.docappointment.iam.entities.User;
import com.docappointment.iam.dto.UserDTO;
import com.docappointment.iam.exceptions.AuthHeaderNotFoundException;
import com.docappointment.iam.services.impl.JwtService;
import com.docappointment.iam.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class userController {

	@Autowired
	private UserService UserServiceObj;

	@Autowired
	private JwtService jwtService;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody User user) {
		user.setRole(Role.PATIENT);
		return ResponseEntity.ok().body(this.UserServiceObj.registerUser(user));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginDTO user) {
		LoginResponseDTO response = this.UserServiceObj.loginUser(user);
		return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + response.getToken())
				.body(response);
	}

	private User getUserFromAuthHeader(String authHeader) {
		String email = getEmailFromAuthHeader(authHeader);
		return UserServiceObj.getUserDetails(email);
	}

	private String getEmailFromAuthHeader(String authHeader) {
		if (!authHeader.contains("Bearer "))
			throw new AuthHeaderNotFoundException();
		String email = jwtService.extractUsername(authHeader.substring(7));
		return email;
	}

	@GetMapping("/token/validate")
	public User validateToken(@RequestHeader String authorization) {
		return getUserFromAuthHeader(authorization);
	}
}
