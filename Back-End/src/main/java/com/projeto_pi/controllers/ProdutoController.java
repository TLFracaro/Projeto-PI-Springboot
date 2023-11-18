package com.projeto_pi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto_pi.dtos.ProdutoDto;
import com.projeto_pi.services.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping("/todos")
    public ResponseEntity<?> getAll() throws Exception {
        return ResponseEntity.ok().body(service.selectAll());
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "produtoId", required = true) UUID produtoId) throws Exception {
        return ResponseEntity.ok().body(service.selectOne(produtoId));
    }

    @PostMapping
    public ResponseEntity<?> post(@Valid @RequestBody ProdutoDto dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(dto));
    }

    @PutMapping
    public ResponseEntity<?> put(@RequestParam(name = "produtoId", required = true) UUID produtoId, @Valid @RequestBody ProdutoDto dto) throws Exception {
        return ResponseEntity.ok().body(service.update(produtoId, dto));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "produtoId", required = true) UUID produtoId) throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("accepted", service.delete(produtoId));
        return ResponseEntity.ok().body(response);
    }
}
