package se.mejsla.jimp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
}
