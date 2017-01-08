package com.hydroLibCreator.applicationController;

/**
 * Created by SuperMata on 2017/01/08.
 */

import com.hydroLibCreator.action.Creator;
import com.hydroLibCreator.model.AudioLibrary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {


    @FXML private TextField directoryName;
    @FXML private TextField name;
    @FXML private TextField author;
    @FXML private TextField info;
    @FXML private TextField license;

    @FXML private Label fullpath;

    private final AudioLibrary audioLibrary = AudioLibrary.getInstance();
    static {

    }

    @FXML
    protected void handleSelectButtonAction(ActionEvent event) {

        Stage primaryStage = Stage.class.cast(Control.class.cast(event.getSource()).getScene().getWindow());

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if(selectedDirectory == null){
            System.out.println("No Directory selected");


        }else{
            System.out.println(selectedDirectory.getName());
            audioLibrary.setDirectorypath(selectedDirectory.getAbsolutePath());
            directoryName.setText(selectedDirectory.getName());
            fullpath.setText("Full path: "+selectedDirectory.getAbsolutePath());

        }
    }


    @FXML
    protected void handleConveretButtonAction(ActionEvent event) {

        audioLibrary.setName(name.getText().isEmpty() ? name.getPromptText():name.getText());
        audioLibrary.setAuthor(author.getText().isEmpty() ? author.getPromptText():author.getText());
        audioLibrary.setInfo(info.getText().isEmpty() ? info.getPromptText():info.getText());
        audioLibrary.setLicense(license.getText().isEmpty() ? license.getPromptText():license.getText());

        Creator creator = new Creator(audioLibrary);
        creator.createLibrary();

        this.showCompleteMessage();

    }

    protected void showCompleteMessage(){


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Conversion Complete");
        alert.setHeaderText("Library created in : "+audioLibrary.getNewLibPath());


        alert.showAndWait();


    }


}
