package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.domain.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
