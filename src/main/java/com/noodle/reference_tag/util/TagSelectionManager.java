package com.noodle.reference_tag.util;

import org.springframework.stereotype.Component;
import com.noodle.reference_tag.domain.TagEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagSelectionManager {
    private final List<TagEntity> selectedTags = new ArrayList<>();

    public boolean addTag(TagEntity tag) {
        if (!selectedTags.contains(tag)) {
            selectedTags.add(tag);
            return true;
        }
        else{
            return false;
        }
    }

    public void removeTag(TagEntity tag) {
        selectedTags.remove(tag);
    }

    public List<TagEntity> getSelectedTags() {
        return new ArrayList<>(selectedTags);
    }

    public List<Long> getSelectedTagIds() {
        return selectedTags.stream()
                .map(TagEntity::getId)
                .collect(Collectors.toList());
    }

    public void clearSelection() {
        selectedTags.clear();
    }
}