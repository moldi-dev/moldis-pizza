package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.entity.Pizza;
import org.moldidev.moldispizza.entity.User;
import org.moldidev.moldispizza.exception.InvalidInputException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.ImageDTOMapper;
import org.moldidev.moldispizza.repository.ImageRepository;
import org.moldidev.moldispizza.repository.PizzaRepository;
import org.moldidev.moldispizza.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImageServiceImplementation implements ImageService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PizzaRepository pizzaRepository;
    private final ImageDTOMapper imageDTOMapper;

    public ImageServiceImplementation(ImageRepository imageRepository, ImageDTOMapper imageDTOMapper, PizzaRepository pizzaRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.imageDTOMapper = imageDTOMapper;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ImageDTO save(MultipartFile image) {
        checkIfImageIsValid(image);

        try {
            byte[] imageData = image.getBytes();

            Instant now = Instant.now();

            Image newImage = new Image();

            newImage.setName(now.toString() + " " + image.getOriginalFilename());
            newImage.setType(image.getContentType());
            newImage.setData(imageData);

            return imageDTOMapper.apply(imageRepository.save(newImage));
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be saved: " + e.getMessage());
        }
    }

    @Override
    public ImageDTO findById(Long imageId) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by id " + imageId));

        return imageDTOMapper.apply(foundImage);
    }

    @Override
    public Image findImageEntityById(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by id " + imageId));
    }

    @Override
    public ImageDTO findByName(String name) {
        Image foundImage = imageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by name " + name));

        return imageDTOMapper.apply(foundImage);
    }

    @Override
    public ImageDTO findByUserId(Long userId) {
        Image foundImage = imageRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by user id" + userId));

        return imageDTOMapper.apply(foundImage);
    }

    @Override
    public List<ImageDTO> findAll() {
        List<Image> images = imageRepository.findAll();

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found");
        }

        return images
                .stream()
                .map(imageDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> findAllByType(String type) {
        List<Image> images = imageRepository.findAllByType(type);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found by type " + type);
        }

        return images
                .stream()
                .map(imageDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> findAllByPizzaId(Long pizzaId) {
        List<Image> images = imageRepository.findAllByPizzaId(pizzaId);

        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found by pizza id " + pizzaId);
        }

        return images
                .stream()
                .map(imageDTOMapper::apply)
                .collect(Collectors.toList());
    }

    @Override
    public ImageDTO updateById(Long imageId, MultipartFile image) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by id " + imageId));

        checkIfImageIsValid(image);

        try {
            Instant now = Instant.now();

            foundImage.setName(now.toString() + " " + image.getOriginalFilename());
            foundImage.setType(image.getContentType());
            foundImage.setData(image.getBytes());

            return imageDTOMapper.apply(imageRepository.save(foundImage));
        }

        catch (IOException e) {
            throw new RuntimeException("Image could not be updated: " + e.getMessage());
        }
    }

    @Override
    public ImageDTO updateByName(String name, MultipartFile image) {
        Image foundImage = imageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by name " + name));

        checkIfImageIsValid(image);

        try {
            Instant now = Instant.now();

            foundImage.setName(now.toString() + " " + image.getOriginalFilename());
            foundImage.setType(image.getContentType());
            foundImage.setData(image.getBytes());

            return imageDTOMapper.apply(imageRepository.save(foundImage));
        }

        catch (IOException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage());
        }
    }

    @Override
    public ImageDTO updateByUserId(Long userId, MultipartFile image) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found by id " + userId));

        checkIfImageIsValid(image);

        Optional<Image> foundImage = imageRepository.findByUserId(userId);

        try {
            if (foundImage.isPresent()) {
                Instant now = Instant.now();

                foundImage.get().setName(now.toString() + " " + image.getOriginalFilename());
                foundImage.get().setType(image.getContentType());
                foundImage.get().setData(image.getBytes());

                return imageDTOMapper.apply(imageRepository.save(foundImage.get()));
            }

            else {
                Instant now = Instant.now();

                Image newImage = new Image();

                newImage.setName(now.toString() + " " + image.getOriginalFilename());
                newImage.setType(image.getContentType());
                newImage.setData(image.getBytes());

                Image insertedImage = imageRepository.save(newImage);

                foundUser.setImage(insertedImage);

                return imageDTOMapper.apply(insertedImage);
            }
        }

        catch (IOException e) {
            throw new RuntimeException("Error updating image: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long imageId) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by id " + imageId));

        Optional<User> userWithImage = userRepository.findByImageId(imageId);

        if (userWithImage.isPresent()) {
            userWithImage.get().setImage(null);

            userRepository.save(userWithImage.get());
        }

        Optional<Pizza> pizzaWithImage = pizzaRepository.findByImageId(imageId);

        if (pizzaWithImage.isPresent()) {
            List<Image> pizzaImages = pizzaWithImage.get().getImages();

            pizzaImages.remove(foundImage);

            pizzaWithImage.get().setImages(pizzaImages);

            pizzaRepository.save(pizzaWithImage.get());
        }

        imageRepository.delete(foundImage);
    }

    @Override
    public void deleteByName(String name) {
        Image foundImage = imageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by name " + name));

        Optional<User> userWithImage = userRepository.findByImageName(name);

        if (userWithImage.isPresent()) {
            userWithImage.get().setImage(null);

            userRepository.save(userWithImage.get());
        }

        Optional<Pizza> pizzaWithImage = pizzaRepository.findByImageName(name);

        if (pizzaWithImage.isPresent()) {
            List<Image> pizzaImages = pizzaWithImage.get().getImages();

            pizzaImages.remove(foundImage);

            pizzaWithImage.get().setImages(pizzaImages);

            pizzaRepository.save(pizzaWithImage.get());
        }

        imageRepository.delete(foundImage);
    }

    private void checkIfImageIsValid(MultipartFile image) {
        if (image == null) {
            throw new InvalidInputException("The image can't be null");
        }

        else if (image.getSize() > 5 * 1024 * 1024) {
            throw new InvalidInputException("The image size can't be greater than 5MB");
        }
    }
}
