package org.moldidev.moldispizza.service;

import org.moldidev.moldispizza.dto.ImageDTO;
import org.moldidev.moldispizza.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public interface ImageService {
    ImageDTO save(MultipartFile image);

    String findById(Long imageId);
    String findByUrl(String url);
    String findByUserId(Long userId);
    Page<ImageDTO> findAll(int page, int size);
    Page<ImageDTO> findAllByType(String type, int page, int size);
    List<ImageDTO> findAllByPizzaId(Long pizzaId);

    ImageDTO updateById(Long imageId, MultipartFile image);

    void delete(Image image);
    void deleteById(Long imageId);
}
