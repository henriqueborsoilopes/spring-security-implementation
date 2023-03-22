package com.security.springboot.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.security.springboot.components.JwtTokenEnhancer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	
	@Value("${jwt.duration}")
	private Integer jwtDuration;
	
	private BCryptPasswordEncoder passwordEncoder;
	private JwtAccessTokenConverter accessTokenConverter;
	private JwtTokenStore jwtTokenStore;
	private AuthenticationManager authenticationManager;
	private JwtTokenEnhancer jwtTokenEnhancer;
	
	public AuthorizationServerConfig(BCryptPasswordEncoder passwordEncoder, JwtAccessTokenConverter accessTokenConverter, JwtTokenStore jwtTokenStore, AuthenticationManager authenticationManager, JwtTokenEnhancer jwtTokenEnhancer) {
		this.passwordEncoder = passwordEncoder;
		this.accessTokenConverter = accessTokenConverter;
		this.jwtTokenStore = jwtTokenStore;
		this.authenticationManager = authenticationManager;
		this.jwtTokenEnhancer = jwtTokenEnhancer;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
		.withClient(clientId)
		.secret(passwordEncoder.encode(clientSecret))
		.scopes("read", "write")
		.authorizedGrantTypes("password")
		.accessTokenValiditySeconds(jwtDuration);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain chain = new TokenEnhancerChain();
		chain.setTokenEnhancers(Arrays.asList(accessTokenConverter, jwtTokenEnhancer));
		
		 endpoints.authenticationManager(authenticationManager)
		 .tokenStore(jwtTokenStore)
		 .accessTokenConverter(accessTokenConverter)
		 .tokenEnhancer(chain);
	}
}
