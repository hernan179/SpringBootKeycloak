package com.api.rest.service;

import java.util.List;
import org.keycloak.representations.idm.UserRepresentation;
import com.api.rest.controller.UserDTO;


public interface IKeycloakService {
   
	List<UserRepresentation> findAllUsers();
	
	List<UserRepresentation> searchUserByUsername(String username);
	
	String crateUser(UserDTO userDTO);
	
	void deleteUser(String userId);
	
	void updateUser(String userId,UserDTO userDTO);
}
