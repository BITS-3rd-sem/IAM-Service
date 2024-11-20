package com.docappointment.iam.services;

import com.docappointment.iam.dto.LoginDTO;
import com.docappointment.iam.dto.LoginResponseDTO;
import com.docappointment.iam.dto.UserDTO;
import com.docappointment.iam.entities.User;

import java.util.List;

public interface UserService {

	public List<UserDTO> getUsers();
	public UserDTO getUsers(int userId);
	public UserDTO registerUser(User user);
	public LoginResponseDTO loginUser(LoginDTO user);
	public User getUserDetails(String email);

}
