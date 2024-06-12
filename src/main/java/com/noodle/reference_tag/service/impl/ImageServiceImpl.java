package com.noodle.reference_tag.service.impl;

import com.noodle.reference_tag.domain.ImageEntity;
import com.noodle.reference_tag.repository.ImageRepository;
import com.noodle.reference_tag.service.ImageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    /**
     * Holds the reference to the repository that will be injected
     */
    private final ImageRepository imageRepository;

    /**
     * Constructor that will instantiate the repository
     * @param imageRepository The Image Repository that will be injected
     */
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Saves an ImageEntity to the DB
     * @param imageEntity The Image Entity to be saved
     * @return A copy of the Image Entity
     */
    @Override
    public ImageEntity save(ImageEntity imageEntity) {
        return imageRepository.save(imageEntity);
    }

    /**
     * Attempts to return an Image correlating to a provided id, wrapping it in an Optional container
     * @param id The id to be searched for
     * @return The Optional container that holds the entity or is empty
     */
    @Override
    public Optional<ImageEntity> findImageById(Long id) {
        return imageRepository.findById(id);
    }

    /**
     * Returns a list of all images in the DB
     * @return A list of all images in the DB
     */
    @Override
    public List<ImageEntity> findAllImages() {
        return imageRepository.findAll();
    }

    /**
     * Determines whether a given image exists in the DB
     * @param id The id of the image to be searched for
     * @return Whether the image exists
     */
    @Override
    public boolean isExists(Long id) {
        return imageRepository.existsById(id);
    }


    /**
     * Delete the provided image
     * @param id The id of the image to be deleted
     */
    @Override
    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }
}
