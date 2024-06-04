package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT u.* FROM public.users AS u " +
            "JOIN public.images AS i ON u.image_id = i.image_id " +
            "WHERE i.image_id = :image_id", nativeQuery = true)
    Optional<User> findByImageId(@Param("image_id") Long imageId);

    @Query(value = "SELECT u.* FROM public.users AS u " +
            "JOIN public.images AS i ON u.image_id = i.image_id " +
            "WHERE i.name = :image_name", nativeQuery = true)
    Optional<User> findByImageName(@Param("image_name") String imageName);
}
