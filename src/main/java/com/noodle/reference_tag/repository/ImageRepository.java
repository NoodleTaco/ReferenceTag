package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.domain.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

}
