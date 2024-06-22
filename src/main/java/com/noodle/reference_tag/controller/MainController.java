package com.noodle.reference_tag.controller;

import com.noodle.reference_tag.config.StageInitializer;
import com.noodle.reference_tag.domain.ImageEntity;
import com.noodle.reference_tag.domain.TagEntity;
import com.noodle.reference_tag.service.ImageService;
import com.noodle.reference_tag.service.ImageTagService;
import com.noodle.reference_tag.service.TagService;
import com.noodle.reference_tag.util.NotificationUtil;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.text.html.Option;
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

    //TODO: Going to have to manually create the cascade functionality when deleting tags or images
    
    private final TagService tagService;
    private final StageInitializer stageInitializer;
    private final ImageService imageService;

    private final ImageTagService imageTagService;

    @Autowired
    public MainController(TagService tagService, StageInitializer stageInitializer, ImageService imageService, ImageTagService imageTagService) {
        this.tagService = tagService;
        this.stageInitializer = stageInitializer;
        this.imageService = imageService;
        this.imageTagService = imageTagService;
    }

    //FXML Properties

    @FXML
    private ListView<TagEntity> allTagListView;

    @FXML
    private TilePane imageTilePane;

    @FXML
    private ImageView selectedImageView;

    @FXML
    private Label selectedImageNameLabel;

    @FXML
    private ListView<TagEntity> selectedImageTagListView;


    //Properties holding selected elements in display
    private ImageEntity selectedImage;



    //TODO: Make DB Operations work on Separate Thread
    public void initialize() {
        refreshAllTagListView();
        refreshImageTilePane();

        //Set on click callback for image display
        imageTilePane.setOnMouseClicked(event -> {
            Node clickedNode = event.getPickResult().getIntersectedNode();
            if (clickedNode instanceof ImageView) {
                ImageView clickedImageView = (ImageView) clickedNode;
                String imagePath = (String) clickedImageView.getUserData();
                selectedImage = imageService.findByPath(imagePath).orElse(null);
                if (selectedImage != null) {
                    updateSelectedImageDetails();
                } else {
                    System.out.println("No image found for path: " + imagePath);
                }
            }
        });
    }

    private void updateSelectedImageDetails() {
        if (selectedImage != null) {
            selectedImageView.setImage(new Image(getClass().getResourceAsStream("/" + selectedImage.getPath())));
            selectedImageNameLabel.setText(Paths.get(selectedImage.getPath()).getFileName().toString());
            updateImageTags();
        }
    }

    private void updateImageTags() {
        List<TagEntity> tags = imageTagService.getTagsForImage(selectedImage.getId());
        selectedImageTagListView.getItems().setAll(tags);
    }

    /**
     * Called when the Create Tag Button is activated
     *
     */
    @FXML
    public void onCreateTagAction(){

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

    @FXML
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
    @FXML
    public void refreshAllTagListView(){
        List<TagEntity> allTags = tagService.findAllTags();
        allTagListView.getItems().setAll(allTags);
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
                thumbnailView.setUserData(imageEntity.getPath()); // Store the path as user data
                imageTilePane.getChildren().add(thumbnailView);
            } catch (Exception e) {
                // Handle any exceptions that occur while loading the image
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void addTagToImage() {
        if (selectedImage == null) {
            NotificationUtil.showNotification("No image selected.", stageInitializer.getPrimaryStage());
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Tag to Image");
        dialog.setHeaderText("Enter the name of the tag to add:");
        dialog.setContentText("Tag Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(tagName -> {
            if (!tagName.isEmpty()) {
                Optional<TagEntity> foundTag = tagService.findByName(tagName);
                if(foundTag.isPresent()){
                    TagEntity tag = foundTag.get();
                    System.out.println("Tag Found: " + tag);

                    if(imageTagService.findByImageIdAndTagId(selectedImage.getId(), tag.getId()).isEmpty()){
                        imageTagService.associateTagWithImage(selectedImage.getId(), tag.getId());
                        updateImageTags();
                        NotificationUtil.showNotification("Tag added to image.", stageInitializer.getPrimaryStage());
                    }
                    else{
                        NotificationUtil.showNotification("Tag Already Associated with Image.", stageInitializer.getPrimaryStage());
                    }

                }
                else{
                    NotificationUtil.showNotification("Tag Addition Failed.", stageInitializer.getPrimaryStage());
                }

                System.out.println("Image Entity to String: " + selectedImage);

            }
        });
    }

    @FXML
    public void removeTagFromImage(){
        if (selectedImage == null) {
            NotificationUtil.showNotification("No image selected.", stageInitializer.getPrimaryStage());
            return;
        }

        TagEntity selectedTag = selectedImageTagListView.getSelectionModel().getSelectedItem();
        if (selectedTag == null) {
            NotificationUtil.showNotification("No tag selected.", stageInitializer.getPrimaryStage());
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Tag Removal");
        confirmDialog.setHeaderText("Are you sure you want to remove this tag?");
        confirmDialog.setContentText("Tag: " + selectedTag.getName());

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            imageTagService.removeTagFromImage(selectedImage.getId(), selectedTag.getId());
            updateImageTags();
            NotificationUtil.showNotification("Tag removed from image.", stageInitializer.getPrimaryStage());
        }
    }


}
