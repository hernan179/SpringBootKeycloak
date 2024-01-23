package com.api.rest.util;

import org.keycloak.admin.client.KeycloakBuilder;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.jboss.resteasy.client.jaxrs.ClientHttpEngine;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.context.annotation.Bean;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Invocation.Builder;
import jakarta.ws.rs.core.Configuration;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;

public class KeycloakProvider {
  private static final String SERVER_URL = "http://localhost:9090";
  private static final String REALM_NAME = "spring-boot-realm-dev";
  private static final String REALM_MASTER = "master";
  private static final String ADMIN_CLI = "admin-cli";
  private static final String USER_CONSOLE = "admin";
  private static final String PASSWD_CONSOLE = "admin";
  private static final String CLIENT_SECRECT = "pB6Ahq1AErXKC4upd8W63uiqtrnNSvQS";
  
  
  public static RealmResource getRealmResource() {
	  Keycloak keycloak = KeycloakBuilder.builder()
	  .serverUrl(SERVER_URL)
	  .realm(REALM_MASTER)
	  .clientId(ADMIN_CLI)
	  .username(USER_CONSOLE)
	  .password(PASSWD_CONSOLE)
	  .clientSecret(CLIENT_SECRECT)
	  .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
	.build();  
	  return keycloak.realm(REALM_NAME);
	  
  }
  public static UsersResource getUserResource() {
	  RealmResource realmResource = getRealmResource();
	  return realmResource.users();
  }
}
