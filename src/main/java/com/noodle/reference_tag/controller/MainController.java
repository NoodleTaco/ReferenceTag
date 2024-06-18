package com.noodle.reference_tag.controller;

import com.noodle.reference_tag.config.StageInitializer;
import com.noodle.reference_tag.domain.ImageEntity;
import com.noodle.reference_tag.domain.TagEntity;
import com.noodle.reference_tag.service.ImageService;
import com.noodle.reference_tag.service.TagService;
import com.noodle.reference_tag.util.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {


    private final TagService tagService;
    private final StageInitializer stageInitializer;
    private final ImageService imageService;

    @Autowired
    public MainController(TagService tagService, StageInitializer stageInitializer, ImageService imageService) {
        this.tagService = tagService;
        this.stageInitializer = stageInitializer;
        this.imageService = imageService;
    }

    //FXML Properties

    @FXML
    private ListView<TagEntity> allTagListView;

    @FXML
    private TilePane imageTilePane;


    public void initialize(){
        refreshAllTagListView();
        refreshImageTilePane();
    }

    /**
     * Called when the Create Tag Button is activated
     *
     */
    public void onCreateTagAction(){
        //TODO: Make DB Operations work on Separate Thread
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Tag");
        dialog.setHeaderText("Enter the name for the new tag:");
        dialog.setContentText("Tag Name:");

        Optional<String> result = dialog.showAndWait();
        //If something was entered
        result.ifPresent(tagName -> {
            if (!tagName.isEmpty()) {
                Optional<TagEntity> existingTag = tagService.findByName(tagName);

                //Tag with the same name already exists
                if(existingTag.isPresent()){
                    NotificationUtil.showNotification("A tag with this name already exists.", stageInitializer.getPrimaryStage());
                }
                else{
                    TagEntity newTag = new TagEntity();
                    newTag.setName(tagName);
                    tagService.save(newTag);
                    NotificationUtil.showNotification("Tag created successfully.", stageInitializer.getPrimaryStage());
                    refreshAllTagListView();
                }

            }
        });
    }

    public void onAddImageButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stageInitializer.getPrimaryStage());

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            Optional<ImageEntity> existingImage = imageService.findByPath(imagePath);
            if (existingImage.isPresent()) {
                NotificationUtil.showNotification("Image already exists in the database.", stageInitializer.getPrimaryStage());
            } else {
                try {
                    // Get the path to the "images" directory within the project's resources
                    Path resourcesPath = Paths.get("src", "main", "resources");
                    Path imagesDir = resourcesPath.resolve("images");

                    // Create the "images" directory if it doesn't exist
                    Files.createDirectories(imagesDir);

                    // Copy the image file to the "images" directory
                    Path destinationPath = imagesDir.resolve(selectedFile.getName());
                    Files.copy(Paths.get(imagePath), destinationPath, StandardCopyOption.REPLACE_EXISTING);

                    // Store the relative path to the "images" directory
                    Path relativePath = Paths.get("images", selectedFile.getName());

                    ImageEntity newImage = new ImageEntity();
                    newImage.setPath(relativePath.toString()); // Save the relative path
                    imageService.save(newImage);
                    NotificationUtil.showNotification("Image added successfully.", stageInitializer.getPrimaryStage());
                    refreshImageTilePane();
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace
                }
            }
        }
    }


    /**
     * Populate the Tag List View using information in the DB
     */
    public void refreshAllTagListView(){
        List<TagEntity> allTags = tagService.findAllTags();
        allTagListView.getItems().setAll(allTags);
        imageService.deleteImageById(2L);
    }

    private void refreshImageTilePane() {
        imageTilePane.getChildren().clear();
        List<ImageEntity> images = imageService.findAllImages();
        for (ImageEntity imageEntity : images) {
            try {
                // Load the image as a resource using the stored relative path
                Image image = new Image(getClass().getResourceAsStream("/" + imageEntity.getPath()));
                ImageView thumbnailView = new ImageView(image);
                thumbnailView.setFitHeight(100);
                thumbnailView.setFitWidth(100);
                imageTilePane.getChildren().add(thumbnailView);
            } catch (Exception e) {
                // Handle any exceptions that occur while loading the image
                e.printStackTrace();
            }
        }
    }


}
