package org.moldidev.moldispizza.controller;

import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.service.ImageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<ImageDTO>> findAll() {
        return ResponseEntity.ok(imageService.findAll());
    }

    @GetMapping("/find-all/type={type}")
    public ResponseEntity<List<ImageDTO>> findAllByType(@PathVariable String type) {
        return ResponseEntity.ok(imageService.findAllByType(type));
    }

    @GetMapping("/find/id={image_id}")
    public ResponseEntity<ImageDTO> findById(@PathVariable("image_id") Long imageId) {
        return ResponseEntity.ok(imageService.findById(imageId));
    }

    @GetMapping("/find/id={image_id}/see-image")
    public ResponseEntity<byte[]> findByIdAndSee(@PathVariable("image_id") Long imageId) {
        Image image = imageService.findImageEntityById(imageId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(image.getData(), headers, HttpStatus.OK);
    }

    @GetMapping("/find/name={name}")
    public ResponseEntity<ImageDTO> findByName(@PathVariable String name) {
        return ResponseEntity.ok(imageService.findByName(name));
    }

    @PostMapping("/save")
    public ResponseEntity<ImageDTO> save(@RequestBody MultipartFile image) {
        return ResponseEntity.ok(imageService.save(image));
    }

    @PostMapping("/update/id={image_id}")
    public ResponseEntity<ImageDTO> updateById(@PathVariable("image_id") Long imageId, @RequestBody MultipartFile image) {
        return ResponseEntity.ok(imageService.updateById(imageId, image));
    }

    @PostMapping("/update/name={name}")
    public ResponseEntity<ImageDTO> updateByName(@PathVariable("name") String name, @RequestBody MultipartFile image) {
        return ResponseEntity.ok(imageService.updateByName(name, image));
    }

    @DeleteMapping("/delete/id={image_id}")
    public ResponseEntity<Void> deleteById(@PathVariable("image_id") Long imageId) {
        imageService.deleteById(imageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/name={name}")
    public ResponseEntity<Void> deleteByName(@PathVariable("name") String name) {
        imageService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
