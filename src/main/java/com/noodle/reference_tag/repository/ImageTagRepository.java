package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.domain.ImageTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageTagRepository extends JpaRepository<ImageTagEntity, Long> {

    Optional<ImageTagEntity> findByImageIdAndTagId(Long imageId, Long tagId);

    List<ImageTagEntity> findByImageId(Long imageId);
}
