package com.noodle.reference_tag.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image_tag")
/**
 * Entity Class representing the image_tag table in the db
 */
public class ImageTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_tag_id_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private ImageEntity image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    

}
