package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Table(name = "images")
@Entity
@Data
public class Image {
    @Column(name = "image_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name = "name", unique = true)
    @NotNull(message = "The image name can't be null")
    @NotEmpty(message = "The image name can't be empty")
    private String name;

    @Column(name = "type")
    @NotNull(message = "The image type can't be null")
    @NotEmpty(message = "The image type can't be empty")
    private String type;

    @Column(name = "data")
    @Lob
    private byte[] data;
}
