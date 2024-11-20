package com.docappointment.iam.services.impl;

import com.docappointment.iam.dto.LoginDTO;
import com.docappointment.iam.dto.LoginResponseDTO;
import com.docappointment.iam.dto.UserDTO;
import com.docappointment.iam.entities.User;
import com.docappointment.iam.exceptions.ResourceAlreadyExistsException;
import com.docappointment.iam.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.docappointment.iam.dao.UserDao;
import com.docappointment.iam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao UserDaoObj; 
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authManager;

	/**
	 * Retrieves a list of all users.
	 *
	 * This method retrieves a list of all users from the UserDao. Each user is converted to a UserDTO object and
	 * added to the resulting list of UserDTOs.
	 *
	 * @return a list of UserDTO objects representing all users
	 */
	@Override
	public List<UserDTO> getUsers() {
		List<User> users = UserDaoObj.findAll();
		List<UserDTO> usersDto = new ArrayList<>();
		for (User user : users) {
			usersDto.add(new UserDTO(user));
		}
		return usersDto;
	}
	
	/**
	 * Registers a new user.
	 *
	 * This method registers a new user by saving the provided User object to the UserDao. Before saving, it checks if
	 * a user with the same email already exists in the database. If so, a ResourceAlreadyExistsException is thrown.
	 * The user's password is encoded using the configured PasswordEncoder. The method returns the UserDTO object
	 * representing the registered user.
	 *
	 * @param user the User object representing the user to be registered
	 * @return the UserDTO object representing the registered user
	 * @throws ResourceAlreadyExistsException if a user with the same email already exists
	 */
	@Override
	public UserDTO registerUser(User user) {
		ObjectMapper objectMapper = new ObjectMapper();
		Optional<User> existingUser  = UserDaoObj.findByEmail(user.getEmail());
		if(existingUser.isPresent()) {
			throw new ResourceAlreadyExistsException("User with this email id already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		UserDaoObj.save(user);
		UserDTO response = objectMapper.convertValue(user, UserDTO.class);
		return response;
	}

	/**
	 * Retrieves the details of a specific user by their user ID.
	 *
	 * This method retrieves the user with the specified user ID from the UserDao. If the user does not exist, a
	 * ResourceNotFoundException is thrown. The retrieved user is converted to a UserDTO object and returned.
	 *
	 * @param userId the ID of the user to retrieve
	 * @return the UserDTO object representing the retrieved user
	 * @throws ResourceNotFoundException if the user with the specified ID does not exist
	 */
	@Override
	public UserDTO getUsers(int userId) {
		User user = UserDaoObj.findById(userId).orElseThrow(()-> new ResourceNotFoundException("The user doesn't exist"));
		UserDTO userDto = new UserDTO(user);
		return userDto;
	}


	/**
	 * Authenticates a user and generates a login response.
	 *
	 * This method authenticates a user by validating the provided login credentials using the AuthenticationManager.
	 * If the authentication is successful, the corresponding user is retrieved from the UserDao. If the user does not
	 * exist, a ResourceNotFoundException is thrown. A JWT token is generated using the JwtService and included in the
	 * LoginResponse object along with the user details. The LoginResponse object is then returned.
	 *
	 * @param request the Login object containing the user's login credentials
	 * @return a LoginResponse object containing the user details and JWT token
	 * @throws ResourceNotFoundException if the user does not exist
	 */
	public LoginResponseDTO loginUser(LoginDTO request) {
		authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		User user = UserDaoObj.findByEmail(request.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User doesn't exist"));
		
		String token = jwtService.generateToken(user);

		LoginResponseDTO resp = new LoginResponseDTO();
		UserDTO userDto = new UserDTO(user);
		
		resp.setStatus("success");
		resp.setMessage("Login successful");
		resp.setUser(userDto);
		resp.setToken(token);

		return resp;
	}
	
	/**
	 * Retrieves the details of a user based on their email.
	 *
	 * This method retrieves the user with the specified email from the UserDao. If the user does not exist, a
	 * ResourceNotFoundException is thrown. The retrieved user is returned.
	 *
	 * @param email the email of the user to retrieve
	 * @return the User object representing the retrieved user
	 * @throws ResourceNotFoundException if the user with the specified email does not exist
	 */
	public User getUserDetails(String email) {
		Optional<User> user = UserDaoObj.findByEmail(email);
		if (user.isEmpty())
			throw new ResourceNotFoundException("User not found");
		return user.get();
	}

}
