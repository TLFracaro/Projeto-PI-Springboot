package com.projeto_pi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_pi.dtos.UsuarioDto;
import com.projeto_pi.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {
    
    @Autowired
    private UsuarioService service;
    
    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "usuarioId", required = true) UUID usuarioId) throws Exception {
        return ResponseEntity.ok().body(service.selectOne(usuarioId));
    }
    
    @PutMapping
    public ResponseEntity<?> put(@RequestParam(name = "usuarioId", required = true) UUID usuarioId, @Valid @RequestBody UsuarioDto dto) throws Exception {
        return ResponseEntity.ok().body(service.update(usuarioId, dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "usuarioId", required = true) UUID usuarioId) throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("accepted", service.delete(usuarioId));
        return ResponseEntity.ok().body(response);
    }

}
