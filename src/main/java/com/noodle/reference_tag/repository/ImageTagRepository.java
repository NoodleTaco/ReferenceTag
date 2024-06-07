package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.domain.ImageTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageTagRepository extends JpaRepository<ImageTagEntity, Long> {
}
