package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.domain.ImageTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageTagRepository extends JpaRepository<ImageTagEntity, Long> {

    Optional<ImageTagEntity> findByImageIdAndTagId(Long imageId, Long tagId);

    @Query("SELECT it FROM ImageTagEntity it WHERE it.image.id = :imageId")
    List<ImageTagEntity> findByImageId(@Param("imageId") Long imageId);

    List<ImageTagEntity> findByImage_IdAndTag_Id(Long imageId, Long tagId);
}
