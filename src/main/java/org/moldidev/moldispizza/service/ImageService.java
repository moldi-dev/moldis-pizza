package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public interface ImageService {
    ImageDTO save(MultipartFile image);

    ImageDTO findById(Long imageId);
    Image findImageEntityById(Long imageId);
    ImageDTO findByName(String name);
    ImageDTO findByUserId(Long userId);
    List<ImageDTO> findAll();
    List<ImageDTO> findAllByType(String type);
    List<ImageDTO> findAllByPizzaId(Long pizzaId);

    ImageDTO updateById(Long imageId, MultipartFile image);
    ImageDTO updateByName(String name, MultipartFile image);
    ImageDTO updateByUserId(Long userId, MultipartFile image);

    void deleteById(Long imageId);
    void deleteByName(String name);
}
