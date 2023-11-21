package com.projeto_pi.controllers;

import com.projeto_pi.dtos.UsuarioDto;
import com.projeto_pi.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "usuarioId") UUID usuarioId) {
        return ResponseEntity.ok().body(service.selectOne(usuarioId));
    }

    @PutMapping
    public ResponseEntity<?> put(@RequestParam(name = "usuarioId") UUID usuarioId, @Valid @RequestBody UsuarioDto dto) {
        return ResponseEntity.ok().body(service.update(usuarioId, dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "usuarioId") UUID usuarioId) throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("accepted", service.delete(usuarioId));
        return ResponseEntity.ok().body(response);
    }

}
