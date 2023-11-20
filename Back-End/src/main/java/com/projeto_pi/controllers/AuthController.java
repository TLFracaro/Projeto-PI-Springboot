package com.projeto_pi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_pi.dtos.AuthDto;
import com.projeto_pi.dtos.UsuarioDto;
import com.projeto_pi.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Map<String, String> RESPONSE = new LinkedHashMap<>();

    @Autowired
    private AuthService service;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDto dto) throws Exception {
        RESPONSE.put("token", service.login(dto));
        return ResponseEntity.ok().body(RESPONSE);
    }
    
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody UsuarioDto dto) throws Exception {
        RESPONSE.put("token", service.registrar(dto));
        return ResponseEntity.ok().body(RESPONSE);
    }
}
