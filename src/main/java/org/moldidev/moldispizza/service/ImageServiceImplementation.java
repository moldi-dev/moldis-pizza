package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.moldidev.moldispizza.exception.InvalidArgumentException;
import org.moldidev.moldispizza.exception.ResourceNotFoundException;
import org.moldidev.moldispizza.mapper.ImageDTOMapper;
import org.moldidev.moldispizza.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ImageServiceImplementation implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageDTOMapper imageDTOMapper;

    public ImageServiceImplementation(ImageRepository imageRepository, ImageDTOMapper imageDTOMapper) {
        this.imageRepository = imageRepository;
        this.imageDTOMapper = imageDTOMapper;
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
            throw new RuntimeException("Error saving image: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(Long imageId) {
        Image foundImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by id " + imageId));

        imageRepository.delete(foundImage);
    }

    @Override
    public void deleteByName(String name) {
        Image foundImage = imageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found by name " + name));

        imageRepository.delete(foundImage);
    }

    private boolean checkIfImageIsValid(MultipartFile image) {
        if (image == null) {
            throw new InvalidArgumentException("The image can't be null");
        }

        return true;
    }
}
