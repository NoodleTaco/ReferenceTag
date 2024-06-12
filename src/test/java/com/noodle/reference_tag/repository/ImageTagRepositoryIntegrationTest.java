package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.TestDataUtil;
import com.noodle.reference_tag.domain.ImageEntity;
import com.noodle.reference_tag.domain.ImageTagEntity;
import com.noodle.reference_tag.domain.TagEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImageTagRepositoryIntegrationTest {

    private final ImageTagRepository underTest;

    @Autowired
    private final ImageRepository imageRepository;

    @Autowired
    private final TagRepository tagRepository;

    @Autowired
    public ImageTagRepositoryIntegrationTest(ImageTagRepository underTest, ImageRepository imageRepository, TagRepository tagRepository) {
        this.underTest = underTest;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
    }

    @Test
    public void testThatImageTagCanBeCreatedAndRecalled(){
        ImageEntity imageEntityA = TestDataUtil.createTestImageA();
        TagEntity tagEntityA = TestDataUtil.createTestTagA();

        ImageTagEntity imageTagEntityA = TestDataUtil.createTestImageTag(imageEntityA, tagEntityA);

        underTest.save(imageTagEntityA);


        Optional<ImageTagEntity> result = underTest.findByImageIdAndTagId(imageEntityA.getId(), tagEntityA.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(imageTagEntityA);
    }

    @Test
    public void testThatImageTagCanBeDeleted(){
        ImageEntity imageEntityA = TestDataUtil.createTestImageA();
        TagEntity tagEntityA = TestDataUtil.createTestTagA();

        ImageTagEntity imageTagEntityA = TestDataUtil.createTestImageTag(imageEntityA, tagEntityA);

        underTest.save(imageTagEntityA);

        ImageTagEntity foundImageTag = underTest.findByImageIdAndTagId(imageEntityA.getId(), tagEntityA.getId())
                .orElseThrow(() -> new RuntimeException("Image-Tag association not found"));

        underTest.delete(foundImageTag);

        Optional<ImageTagEntity> result = underTest.findByImageIdAndTagId(imageEntityA.getId(), tagEntityA.getId());
        assertThat(result).isEmpty();
    }

    @Test
    @Transactional
    public void testThatMultipleImageTagsAssociatedWithImageCanBeRecalled() {
        // Create entities without setting the ID
        ImageEntity imageEntityA = TestDataUtil.createTestImageA();
        TagEntity tagEntityA = TestDataUtil.createTestTagA();
        TagEntity tagEntityB = TestDataUtil.createTestTagB();
        TagEntity tagEntityC = TestDataUtil.createTestTagC();

        // Save ImageEntity and TagEntity instances first
        imageRepository.saveAndFlush(imageEntityA);
        tagRepository.saveAndFlush(tagEntityA);
        tagRepository.saveAndFlush(tagEntityB);
        tagRepository.saveAndFlush(tagEntityC);

        // Use the managed entities to create ImageTagEntity instances
        ImageTagEntity imageTagEntityA = TestDataUtil.createTestImageTag(imageEntityA, tagEntityA);
        ImageTagEntity imageTagEntityB = TestDataUtil.createTestImageTag(imageEntityA, tagEntityB);
        ImageTagEntity imageTagEntityC = TestDataUtil.createTestImageTag(imageEntityA, tagEntityC);

        // Save ImageTagEntity instances
        underTest.saveAndFlush(imageTagEntityA);
        underTest.saveAndFlush(imageTagEntityB);
        underTest.saveAndFlush(imageTagEntityC);

        // Retrieve and assert
        List<ImageTagEntity> result = underTest.findByImageId(imageEntityA.getId());

        assertThat(result)
                .hasSize(3)
                .containsExactlyInAnyOrder(imageTagEntityA, imageTagEntityB, imageTagEntityC);
    }
}
