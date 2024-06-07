package com.noodle.reference_tag.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
