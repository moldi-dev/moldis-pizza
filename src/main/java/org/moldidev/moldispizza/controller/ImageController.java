package org.moldidev.moldispizza.controller;

import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.response.HTTPResponse;
import org.moldidev.moldispizza.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<HTTPResponse> findAll(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        Page<ImageDTO> result = imageService.findAll(page.orElse(0), page.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("imagesDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/type={type}")
    public ResponseEntity<HTTPResponse> findAllByType(@PathVariable("type") String type, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        Page<ImageDTO> result = imageService.findAllByType(type, page.orElse(0), size.orElse(10));

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("imagesDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/pizza-id={pizza_id}")
    public ResponseEntity<HTTPResponse> findAllByPizzaId(@PathVariable("pizza_id") Long pizzaId) {
        List<ImageDTO> result = imageService.findAllByPizzaId(pizzaId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .data(Map.of("imagesDTOs", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(value = "/id={id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HTTPResponse> findById(@PathVariable("id") Long imageId) {
        String result = imageService.findById(imageId);

        return ResponseEntity.ok(
                HTTPResponse.builder()
                        .data(Map.of("base64EncodedImage", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping(value = "/url={url}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HTTPResponse> findByUrl(@PathVariable("url") String url) {
        String result = imageService.findByUrl(url);

        return ResponseEntity.ok(
                HTTPResponse.builder()
                        .data(Map.of("base64EncodedImage", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @GetMapping("/user-id={user_id}")
    public ResponseEntity<HTTPResponse> findByUserId(@PathVariable("user_id") Long userId) {
        String result = imageService.findByUserId(userId);

        return ResponseEntity.ok(
                HTTPResponse.builder()
                        .data(Map.of("base64EncodedImage", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HTTPResponse> save(@RequestBody MultipartFile image) {
        ImageDTO result = imageService.save(image);

        return ResponseEntity.created(URI.create("")).body(
                HTTPResponse
                        .builder()
                        .message("Image created successfully")
                        .data(Map.of("imageDTO", result))
                        .status(HttpStatus.CREATED)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PatchMapping("/id={id}")
    public ResponseEntity<HTTPResponse> updateById(@PathVariable("id") Long imageId, @RequestBody MultipartFile updatedImage) {
        ImageDTO result = imageService.updateById(imageId, updatedImage);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Image updated successfully")
                        .data(Map.of("imageDTO", result))
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @DeleteMapping("/id={id}")
    public ResponseEntity<HTTPResponse> deleteById(@PathVariable("id") Long imageId) {
        imageService.deleteById(imageId);

        return ResponseEntity.ok(
                HTTPResponse
                        .builder()
                        .message("Image deleted successfully")
                        .status(HttpStatus.OK)
                        .timestamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
