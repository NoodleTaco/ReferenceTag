package com.noodle.reference_tag.service;

import com.noodle.reference_tag.domain.ImageTagEntity;
import com.noodle.reference_tag.domain.TagEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageTagService {

    @Transactional
    ImageTagEntity associateTagWithImage(Long imageId, Long tagId);

    void removeTagFromImage(Long imageId, Long tagId);

    List<TagEntity> getTagsForImage(Long imageId);


}
