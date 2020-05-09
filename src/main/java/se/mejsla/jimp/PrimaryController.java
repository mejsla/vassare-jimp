package se.mejsla.jimp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PrimaryController {

    private final IntegerProperty brushSizeProperty = new SimpleIntegerProperty(50);

    @FXML
    private ImageView imageView ;

    @FXML
    private ScrollPane imageScroller;

    @FXML
    private Slider brushSizeSlider;

    @FXML
    private TextField brushSizeField ;

    @FXML
    private void initialize() {
        brushSizeSlider.valueProperty().bindBidirectional(brushSizeProperty);

        brushSizeProperty.addListener((prop, oldValue, newValue) -> {
            brushSizeField.setText("" + newValue);
        });
        brushSizeField.setText("" + brushSizeProperty.getValue());
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
                imageView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
