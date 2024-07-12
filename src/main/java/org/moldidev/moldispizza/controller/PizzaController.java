package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.PizzaDTO;
import org.moldidev.moldispizza.request.admin.PizzaCreateAdminRequest;
import org.moldidev.moldispizza.request.admin.PizzaUpdateDetailsAdminRequest;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.PizzaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pizzas")
@RequiredArgsConstructor
public class PizzaController {

    private final PizzaService pizzaService;

    @GetMapping
    public ResponseEntity<HTTPResponse> findAll(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        Page<PizzaDTO> result = pizzaService.findAll(page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("pizzasDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/id={id}")
    public ResponseEntity<HTTPResponse> findById(@PathVariable("id") Long pizzaId) {
        PizzaDTO result = pizzaService.findById(pizzaId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("pizzaDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/name={name}")
    public ResponseEntity<HTTPResponse> findByName(@PathVariable("name") String pizzaName) {
        PizzaDTO result = pizzaService.findByName(pizzaName);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("pizzaDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HTTPResponse> save(@ModelAttribute PizzaCreateAdminRequest request) {
        PizzaDTO result = pizzaService.save(request);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Pizza created successfully")
                        .data(Map.of("pizzaDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PatchMapping(path = "/admin/id={id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HTTPResponse> updateById(@PathVariable("id") Long pizzaId, @ModelAttribute PizzaUpdateDetailsAdminRequest request) {
        PizzaDTO result = pizzaService.updateById(pizzaId, request);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Pizza updated successfully")
                        .data(Map.of("pizzaDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<HTTPResponse> deleteById(@PathVariable("id") Long pizzaId) {
        pizzaService.deleteById(pizzaId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Pizza deleted successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
