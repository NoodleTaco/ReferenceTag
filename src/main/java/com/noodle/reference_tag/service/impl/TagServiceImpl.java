package com.noodle.reference_tag.service.impl;

import com.noodle.reference_tag.domain.TagEntity;
import com.noodle.reference_tag.repository.TagRepository;
import com.noodle.reference_tag.service.TagService;

import java.util.List;
import java.util.Optional;

public class TagServiceImpl implements TagService {

    /**
     * Holds the reference to the repository that will be injected
     */
    private final TagRepository tagRepository;

    /**
     * Constructor that will instantiate the repository
     * @param tagRepository The Tag Repository that will be injected
     */
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Saves a tagEntity to the DB
     * @param tagEntity The Tag Entity to be saved
     * @return A copy of the Tag Entity
     */
    @Override
    public TagEntity save(TagEntity tagEntity) {
        return tagRepository.save(tagEntity);
    }

    /**
     * Attempts to return a Tag correlating to a provided id, wrapping it in an Optional container
     * @param id The id to be searched for
     * @return The Optional container that holds the entity or is empty
     */
    @Override
    public Optional<TagEntity> findTagById(Long id) {
        return tagRepository.findById(id);
    }

    /**
     * Return the list of all tags
     * @return The list of all tags
     */
    @Override
    public List<TagEntity> findAllTags() {
        return tagRepository.findAll();
    }

    /**
     * Return whether a given tag exists in the db
     * @param id The id of the tag being searched for
     * @return Whether the tag exists
     */
    @Override
    public boolean isExists(Long id) {
        return tagRepository.existsById(id);
    }

    /**
     * Delete the provided tag
     * @param id The id of the tag to be deleted
     */
    @Override
    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }
}
