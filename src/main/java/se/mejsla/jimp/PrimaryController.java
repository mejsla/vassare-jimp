package se.mejsla.jimp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class PrimaryController {

    private final IntegerProperty brushSizeProperty = new SimpleIntegerProperty(50);

    @FXML
    private ScrollPane imageScroller;

    @FXML
    private Slider brushSizeSlider;

    @FXML
    private TextField brushSizeField;

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
                onImageLoaded(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onImageLoaded(Image image) {
        final int height = (int) image.getHeight();
        final int width = (int) image.getWidth();

        final ImageView bottomImageLayer = new ImageView(image);
        bottomImageLayer.setFitHeight(0);
        bottomImageLayer.setFitWidth(0);

        final WritableImage paintedLayer = new WritableImage(width, height);
        final Canvas topToolIndicatorLayer = new Canvas(width, height);

        final StackPane imageLayers = new StackPane(
                bottomImageLayer,
                new ImageView(paintedLayer),
                topToolIndicatorLayer);

        ImageUtilities.registerMouseListeners(
                height,
                width,
                brushSizeProperty,
                paintedLayer.getPixelWriter(),
                topToolIndicatorLayer,
                topToolIndicatorLayer.getGraphicsContext2D(),
                imageLayers);

        imageScroller.setContent(imageLayers);
    }
}
