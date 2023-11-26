package com.projeto_pi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto_pi.dtos.ProdutoDto;
import com.projeto_pi.services.ProdutoService;
import com.projeto_pi.utils.ValidateImage;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService service;

    private final Validator validator;

    public ProdutoController(ProdutoService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.selectAll());
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam(name = "produtoId") UUID produtoId) {
        return ResponseEntity.ok().body(service.selectOne(produtoId));
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestParam(name = "json") String json, @Valid @NotNull(message = "As imagens devem ser informado") @ValidateImage @RequestParam(name = "imagens") MultipartFile[] imagens) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.insert(processRequestBody(json), imagens));
    }

    @PutMapping
    public ResponseEntity<?> put(@RequestParam(name = "produtoId") UUID produtoId, @RequestParam(name = "json") String json, @Valid @ValidateImage @RequestParam(name = "imagens", required = false) MultipartFile[] imagens) throws Exception {
        return ResponseEntity.ok().body(service.update(produtoId, processRequestBody(json), imagens));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam(name = "produtoId") UUID produtoId) throws Exception {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("accepted", service.delete(produtoId));
        return ResponseEntity.ok().body(response);
    }

    private ProdutoDto processRequestBody(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ProdutoDto dto = mapper.readValue(json, ProdutoDto.class);
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<ProdutoDto> violation : violations) {
                throw new ValidationException(violation.getMessage());
            }
        }
        return dto;
    }
}