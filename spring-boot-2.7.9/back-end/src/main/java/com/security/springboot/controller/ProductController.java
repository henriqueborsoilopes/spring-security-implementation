package com.security.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/post1")
	public ResponseEntity<Void> post1() {
		return ResponseEntity.ok().build();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
	@PostMapping("/post2")
	public ResponseEntity<Void> post2() {
		return ResponseEntity.ok().build();
	}
}
