package com.api.rest.controller;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import com.api.rest.service.IKeycloakService;

@RestController
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin_client_role')")
public class KeycloakController {
	
	@Autowired
	private IKeycloakService keycloakService;
	

	
	@GetMapping("/search")
	public ResponseEntity<?> findAllUsers(){
		return ResponseEntity.ok(keycloakService.findAllUsers());
	}
	
	@GetMapping("/search/{username}")
	public ResponseEntity<?> findAllUserByName(@PathVariable String username){
		return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
	}
	
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException{
		String response = keycloakService.crateUser(userDTO);
		//https://www.youtube.com/watch?v=zR3igUft1KA
		
			return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);
	}
	
	@PutMapping("/update/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable String userId,@RequestBody UserDTO userDTO) throws URISyntaxException{
		keycloakService.updateUser(userId,userDTO);
		//https://www.youtube.com/watch?v=zR3igUft1KA
		
			return ResponseEntity.ok("User updated successfully");
	}
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId) throws URISyntaxException{
		keycloakService.deleteUser(userId);
		//https://www.youtube.com/watch?v=zR3igUft1KA
		
			return ResponseEntity.noContent().build();
	}
}
