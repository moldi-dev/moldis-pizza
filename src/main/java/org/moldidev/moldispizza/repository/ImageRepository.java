package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String name);
    List<Image> findAllByType(String type);
}
