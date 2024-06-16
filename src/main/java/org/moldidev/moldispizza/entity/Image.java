package org.moldidev.moldispizza.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "images")
@Entity
@Data
public class Image extends Auditable {

    @Column(name = "image_id", updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name = "url", unique = true)
    private String url;

    @Column(name = "type")
    private String type;
}
