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

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping(value = "/id={id}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> findById(@PathVariable("id") Long imageId) {
        ImageDTO result = imageService.findById(imageId);
        String imagePath = result.url();
        Path path = Paths.get(imagePath);

        try {
            byte[] imageBytes = Files.readAllBytes(path);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be fetched");
        }
    }

    @GetMapping(value = "/url={url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> findByUrl(@PathVariable("url") String url) {
        ImageDTO result = imageService.findByUrl(url);
        String imagePath = result.url();
        Path path = Paths.get(imagePath);

        try {
            byte[] imageBytes = Files.readAllBytes(path);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be fetched");
        }
    }

    @GetMapping("/user-id={user_id}")
    public ResponseEntity<byte[]> findByUserId(@PathVariable("user_id") Long userId) {
        ImageDTO result = imageService.findByUserId(userId);
        String imagePath = result.url();
        Path path = Paths.get(imagePath);

        try {
            byte[] imageBytes = Files.readAllBytes(path);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be fetched");
        }
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

    @PatchMapping
    public ResponseEntity<HTTPResponse> updateById(@RequestParam("id") Long imageId, @RequestBody MultipartFile updatedImage) {
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

    @DeleteMapping
    public ResponseEntity<HTTPResponse> deleteById(@RequestParam("id") Long imageId) {
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
