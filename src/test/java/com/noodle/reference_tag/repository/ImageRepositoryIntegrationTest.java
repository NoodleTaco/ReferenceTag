package com.noodle.reference_tag.repository;

import com.noodle.reference_tag.domain.ImageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import javax.swing.text.html.Option;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ImageRepositoryIntegrationTest {

    private ImageRepository underTest;

    @Autowired
    public ImageRepositoryIntegrationTest(ImageRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testThatImageCanBeCreatedAndRecalled(){
        ImageEntity imageEntity = ImageEntity.builder()
                .id(1L)
                .path("/path/to/image.jpg")
                .build();

        underTest.save(imageEntity);
        Optional<ImageEntity> result = underTest.findById(imageEntity.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(imageEntity);
    }
}
