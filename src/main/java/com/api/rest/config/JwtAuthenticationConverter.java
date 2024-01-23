package com.api.rest.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

	@Value("${jwt.auth.converter.principle-attribute}")
	private String principleAtrribute;
	
	@Value("${jwt.auth.converter.resource-id}")
	private String resourceId;

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> authorities = Stream
				.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractResourceRole(jwt).stream())
				.toList();

		return new JwtAuthenticationToken(jwt,authorities,getPrincipalName(jwt));
	}

	private Collection<? extends GrantedAuthority> extractResourceRole(Jwt jwt){
		Map<String,Object> resourceAccess;
		Map<String,Object> resource;
		Collection<String> resourceRoles;
		if(jwt.getClaim("resource_access") == null) {
			return Set.of();
		}
		resourceAccess = jwt.getClaim("resource_access"); 
		
		if(resourceAccess.get(resourceId) == null) {
			return List.of();	
		}
		
		resource = (Map<String,Object>) resourceAccess.get(resourceId);
		
		if(resource.get("roles") == null) {
			return List.of();
		}
		
		resourceRoles = (Collection<String>) resource.get("roles");	
		
		
		return resourceRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_".concat(role)))
				.toList();
		
	}
	
	private String getPrincipalName(Jwt jwt) {
		String claimName = JwtClaimNames.SUB;
		if (principleAtrribute != null) {
			claimName = principleAtrribute;
		}
		return jwt.getClaim(claimName);
	}

}
