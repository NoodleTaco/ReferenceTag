package com.noodle.reference_tag.controller;

import com.noodle.reference_tag.config.StageInitializer;
import com.noodle.reference_tag.domain.ImageEntity;
import com.noodle.reference_tag.domain.TagEntity;
import com.noodle.reference_tag.service.ImageService;
import com.noodle.reference_tag.service.TagService;
import com.noodle.reference_tag.util.NotificationUtil;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                    //TODO: Refresh UI After This
                }

            }
        });
    }

    public void onAddImageButtonAction(){
        //TODO: Does not work
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(stageInitializer.getPrimaryStage());

        if(selectedFile != null){
            String imagePath = selectedFile.getAbsolutePath();
            Optional<ImageEntity> existingImage = imageService.findByPath(imagePath);
            if (existingImage.isPresent()) {
                NotificationUtil.showNotification("Image already exists in the database.", stageInitializer.getPrimaryStage());
            }
            else{
                try {
                    // Copy the image file to a designated folder within your application
                    String appImagesFolder = "images";
                    Files.copy(Paths.get(imagePath), Paths.get(appImagesFolder, selectedFile.getName()), StandardCopyOption.REPLACE_EXISTING);

                    ImageEntity newImage = new ImageEntity();
                    newImage.setPath(Paths.get(appImagesFolder, selectedFile.getName()).toString());
                    imageService.save(newImage);
                    NotificationUtil.showNotification("Image added successfully.", stageInitializer.getPrimaryStage());
                    // TODO: Refresh UI After This
                } catch (IOException e) {
                    System.out.println("Error adding image: " + e.getMessage());
                }
            }
        }
    }
}
