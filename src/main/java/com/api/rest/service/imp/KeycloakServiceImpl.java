package com.api.rest.service.imp;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.api.rest.controller.UserDTO;
import com.api.rest.service.IKeycloakService;
import com.api.rest.util.KeycloakProvider;

import jakarta.ws.rs.core.Response;

@Service
public class KeycloakServiceImpl implements IKeycloakService {

	@Override
	public List<UserRepresentation> findAllUsers() {
		// TODO Auto-generated method stub
		return KeycloakProvider.getRealmResource().users().list();
	}

	@Override
	public List<UserRepresentation> searchUserByUsername(String username) {
		// TODO Auto-generated method stub
		return KeycloakProvider.getRealmResource().users().search(username,true);
	}

	@Override
	public String crateUser(UserDTO userDTO) {
		// TODO Auto-generated method stub
		int status = 0;
		UsersResource userResource = KeycloakProvider.getUserResource();
		
		UserRepresentation userRepresentation = new UserRepresentation();  
		userRepresentation.setFirstName(userDTO.getFirstName());
		userRepresentation.setLastName(userDTO.getLastName());
		userRepresentation.setEmail(userDTO.getEmail());
		userRepresentation.setUsername(userDTO.getUsername());
		userRepresentation.setEmailVerified(true);
		userRepresentation.setEnabled(true);
		
		Response response =userResource.create(userRepresentation);
		status = response.getStatus();
		if (status == 201) {
			String path = response.getLocation().getPath();
			String userId = path.substring(path.lastIndexOf("/") + 1);
			
			CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
			credentialRepresentation.setTemporary(false);
			credentialRepresentation.setType(OAuth2Constants.PASSWORD);
			credentialRepresentation.setValue(userDTO.getPassword());
			userResource.get(userId).resetPassword(credentialRepresentation);
			
		    RealmResource realResource = KeycloakProvider.getRealmResource();
		    
		    List<RoleRepresentation> roleRealmRepresentacion  = null;
			
			if(userDTO.getRoles() == null || userDTO.getRoles().isEmpty() ) {
				roleRealmRepresentacion = List.of(realResource.roles().get("user").toRepresentation());
			}else {
				roleRealmRepresentacion = realResource.roles()
						.list()
						.stream()
						.filter(role -> userDTO.getRoles().stream()
								.anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
						.toList();
				
				
			}
			realResource.users()
			.get(userId)
			.roles()
			.realmLevel()
			.add(roleRealmRepresentacion);
			
			return "User created successfully!!!";
			
		}else if (status == 2409) {
			return "User exist already!!!";
		}else {
			return "Error creating user, please contact with the administrator!!!";
		}
	}

	@Override
	public void deleteUser(String userId) {
		// TODO Auto-generated method stub
		KeycloakProvider.getUserResource().get(userId).remove();
		//https://www.youtube.com/watch?v=zR3igUft1KA
	}

	@Override
	public void updateUser(String userId, UserDTO userDTO) {
		// TODO Auto-generated method stub
		
		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setTemporary(false);
		credentialRepresentation.setType(OAuth2Constants.PASSWORD);
		credentialRepresentation.setValue(userDTO.getPassword());
		
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setFirstName(userDTO.getFirstName());
		userRepresentation.setLastName(userDTO.getLastName());
		userRepresentation.setEmail(userDTO.getEmail());
		userRepresentation.setUsername(userDTO.getUsername());
		userRepresentation.setEmailVerified(true);
		userRepresentation.setEnabled(true);
		
		userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));		
		
	
		UserResource userResource = KeycloakProvider.getUserResource().get(userId);
		userResource.update(userRepresentation);
		
	}

}
