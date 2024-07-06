package org.moldidev.moldispizza.repository;

import org.moldidev.moldispizza.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByVerificationToken(String verificationToken);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);

    @Query(value = "SELECT u.* FROM public.users AS u " +
            "JOIN public.images AS i ON u.image_id = i.image_id " +
            "WHERE i.image_id = :image_id", nativeQuery = true)
    Optional<User> findByImageId(@Param("image_id") Long imageId);

    @Query(value = "SELECT u.* FROM public.users AS u " +
            "JOIN public.images AS i ON u.image_id = i.image_id " +
            "WHERE i.url = :image_url", nativeQuery = true)
    Optional<User> findByImageUrl(@Param("image_url") String imageUrl);
}
