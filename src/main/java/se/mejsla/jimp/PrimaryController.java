package se.mejsla.jimp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PrimaryController {

    @FXML
    private void initialize() {
    }

    @FXML
    public void handleApplicationQuitAction(ActionEvent event) {
        App.getWindow().hide();
    }

    @FXML
    public void handleFileOpenAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        File file = fileChooser.showOpenDialog(App.getWindow());
        if (file != null) {
            try {
                InputStream imageIS = Files.newInputStream(file.toPath());
                final Image image = new Image(imageIS);
                // imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
