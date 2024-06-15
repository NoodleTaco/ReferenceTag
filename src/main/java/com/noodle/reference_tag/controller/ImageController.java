package com.noodle.reference_tag.controller;

import com.noodle.reference_tag.domain.ImageEntity;
import com.noodle.reference_tag.service.ImageService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class ImageController {

    private final ImageService imageService;

    @FXML
    private ListView<ImageEntity> imageListView;

    @FXML
    private TextField imagePathField;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @FXML
    public void initialize() {
        refreshImageList();
    }

    @FXML
    public void onAddImage() {
        String path = imagePathField.getText();
        if (!path.isEmpty()) {
            ImageEntity newImage = new ImageEntity();
            newImage.setPath(path);
            imageService.save(newImage);
            imagePathField.clear();
            refreshImageList();
        }
    }

    private void refreshImageList() {
        imageListView.getItems().setAll(imageService.findAllImages());
    }
}