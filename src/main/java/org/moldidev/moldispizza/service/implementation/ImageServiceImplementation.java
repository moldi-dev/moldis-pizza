package org.moldidev.moldispizza.service.implementation;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.ObjectNotValidException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.ImageDTOMapper;
import org.moldidev.moldispizza.repository.ImageRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.moldidev.moldispizza.service.ImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImplementation implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PizzaRepository pizzaRepository;
    private final ImageDTOMapper imageDTOMapper;

    @Override
    public ImageDTO save(MultipartFile image) {
        HashSet<String> violations = new HashSet<>();

        if (image.getSize() > 1024 * 1024 * 5) {
            violations.add("The image size must not exceed 5MB");
            throw new ObjectNotValidException(violations);
        }

        try {
            Image newImage = new Image();

            String filename = LocalDateTime.now() + " " + image.getOriginalFilename();
            String saveDirectory = "src/main/resources/images/";
            Path savePath = Paths.get(saveDirectory + filename);

            Files.createDirectories(savePath.getParent());

            Files.copy(image.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            newImage.setUrl(savePath.toString());
            newImage.setType(image.getContentType());

            return imageDTOMapper.apply(imageRepository.save(newImage));
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be saved: " + e.getMessage());
        }
    }

    @Override
    public String findById(Long imageId) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id doesn't exist"));

        Path path = Paths.get(foundImage.getUrl());

        try {
            byte[] imageBytes = Files.readAllBytes(path);
            return Base64.encodeBase64String(imageBytes);
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be fetched");
        }
    }

    @Override
    public String findByUrl(String url) {
        Image foundImage = imageRepository.findByUrl(url)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided url doesn't exist"));

        Path path = Paths.get(foundImage.getUrl());

        try {
            byte[] imageBytes = Files.readAllBytes(path);
            return Base64.encodeBase64String(imageBytes);
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be fetched");
        }
    }

    @Override
    public String findByUserId(Long userId) {
        Image foundImage = imageRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided user id doesn't exist"));

        Path path = Paths.get(foundImage.getUrl());

        try {
            byte[] imageBytes = Files.readAllBytes(path);
            return Base64.encodeBase64String(imageBytes);
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be fetched");
        }
    }

    @Override
    public Page<ImageDTO> findAll(int page, int size) {
        Page<Image> images = imageRepository.findAll(PageRequest.of(page, size));

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images exist");
        }

        return images.map(imageDTOMapper);
    }

    @Override
    public Page<ImageDTO> findAllByType(String type, int page, int size) {
        Page<Image> images = imageRepository.findAllByType(type, PageRequest.of(page, size));

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images exist by the provided type");
        }

        return images.map(imageDTOMapper);
    }

    @Override
    public List<ImageDTO> findAllByPizzaId(Long pizzaId) {
        List<Image> images = imageRepository.findAllByPizzaId(pizzaId);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images exist by the provided pizza id");
        }

        return images
                .stream()
                .map(imageDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public ImageDTO updateById(Long imageId, MultipartFile updatedImage) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id doesn't exist"));

        HashSet<String> violations = new HashSet<>();

        if (updatedImage.getSize() > 1024 * 1024 * 5) {
            violations.add("The image size must not exceed 5MB");
            throw new ObjectNotValidException(violations);
        }

        try {
            String filename = LocalDateTime.now() + " " + updatedImage.getOriginalFilename();
            String saveDirectory = "src/main/resources/images/";
            Path savePath = Paths.get(saveDirectory + filename);

            Files.createDirectories(savePath.getParent());

            Path oldImagePath = Paths.get(foundImage.getUrl());
            Files.deleteIfExists(oldImagePath);

            Files.copy(updatedImage.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

            foundImage.setUrl(savePath.toString());
            foundImage.setType(updatedImage.getContentType());

            return imageDTOMapper.apply(imageRepository.save(foundImage));
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be updated: " + e.getMessage());
        }
    }

    @Override
    public void delete(Image image) {
        Optional<User> userWithImage = userRepository.findByImageUrl(image.getUrl());
        Optional<Pizza> pizzaWithImage = pizzaRepository.findByImageUrl(image.getUrl());

        if (userWithImage.isPresent()) {
            userWithImage.get().setImage(null);
            userRepository.save(userWithImage.get());
        }

        if (pizzaWithImage.isPresent()) {
            pizzaWithImage.get().getImages().remove(image);
            pizzaRepository.save(pizzaWithImage.get());
        }

        Path path = Paths.get(image.getUrl());

        try {
            Files.deleteIfExists(path);
        }

        catch (IOException e) {
            throw new RuntimeException("Failed to delete the image: " + e.getMessage());
        }

        imageRepository.delete(image);
    }

    @Override
    public void deleteById(Long imageId) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("The image by the provided id doesn't exist"));

        Optional<User> userWithImage = userRepository.findByImageId(imageId);
        Optional<Pizza> pizzaWithImage = pizzaRepository.findByImageId(imageId);

        if (userWithImage.isPresent()) {
            userWithImage.get().setImage(null);
            userRepository.save(userWithImage.get());
        }

        if (pizzaWithImage.isPresent()) {
            pizzaWithImage.get().getImages().remove(foundImage);
            pizzaRepository.save(pizzaWithImage.get());
        }

        Path path = Paths.get(foundImage.getUrl());

        try {
            Files.deleteIfExists(path);
        }

        catch (IOException e) {
            throw new RuntimeException("Failed to delete the image: " + e.getMessage());
        }

        imageRepository.delete(foundImage);
    }
}
