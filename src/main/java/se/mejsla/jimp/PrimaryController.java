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
import javafx.scene.input.MouseEvent;
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

        final StackPane imageLayers = new StackPane(bottomImageLayer, new ImageView(paintedLayer), topToolIndicatorLayer);

        registerMouseListeners(height, width, paintedLayer.getPixelWriter(), topToolIndicatorLayer, topToolIndicatorLayer.getGraphicsContext2D(), imageLayers);

        imageScroller.setContent(imageLayers);
    }

    private void registerMouseListeners(int height, int width, PixelWriter pixelWriter, Canvas topToolIndicatorLayer, GraphicsContext toolGC, StackPane imageLayers) {
        imageLayers.setOnMouseEntered(mouseEvent -> {
            imageLayers.setCursor(Cursor.NONE);
        });
        imageLayers.setOnMouseExited(mouseEvent -> {
            imageLayers.setCursor(Cursor.DEFAULT);
        });

        topToolIndicatorLayer.setOnMouseMoved(event -> {
            clearCanvas(width, height, toolGC);
            toolGC.setStroke(Color.rgb(255, 255, 255, 1.0d));
            paintTool(toolGC, event);
        });

        topToolIndicatorLayer.setOnMouseDragged(event -> {
            clearCanvas(width, height, toolGC);
            toolGC.setStroke(Color.RED);
            paintTool(toolGC, event);
            final double x = event.getX();
            final double y = event.getY();
            applyPaint((int) x, (int) y, event.isShiftDown() || event.isControlDown(), pixelWriter);
        });

        topToolIndicatorLayer.setOnMousePressed((event -> {
            clearCanvas(width, height, toolGC);
            toolGC.setStroke(Color.YELLOW);
            paintTool(toolGC, event);
            final double x = event.getX();
            final double y = event.getY();
            applyPaint((int) x, (int) y, event.isShiftDown() || event.isControlDown(), pixelWriter);
        }));
    }

    private void paintTool(GraphicsContext toolGC, MouseEvent event) {
        final double x = event.getX();
        final double y = event.getY();

        final int diameter = brushSizeProperty.intValue();
        final int radius = diameter / 2;
        toolGC.setLineDashes(2);
        toolGC.strokeOval(x - radius, y - radius, diameter, diameter);
    }

    public void applyPaint(final double x, final double y, boolean erase, PixelWriter pixelWriter) {
        final int diameter = this.brushSizeProperty.intValue();
        final int radius = diameter / 2;

        final int xoffset = (int) (x - radius);
        final int yoffset = (int) (y - radius);
        final int radiusSquared = radius * radius;
        for (int xc = 0; xc < diameter; xc++) {
            for (int yc = 0; yc < diameter; yc++) {
                int xcc = xc - radius;
                int ycc = yc - radius;
                final int distanceSquard = xcc * xcc + ycc * ycc;
                if (distanceSquard < radiusSquared) {
                    final int ximage = xc + xoffset;
                    final int yimage = yc + yoffset;
                    if ((ximage > -1) && (yimage > -1)) {
                        final Color fillColor;
                        if (erase) {
                            fillColor = Color.color(0, 0, 0, 0);
                        } else {
                            fillColor = Color.PAPAYAWHIP;
                        }
                        pixelWriter.setColor(ximage, yimage, fillColor);
                    }
                }
            }
        }
    }

    private static void clearCanvas(int width, int height, GraphicsContext toolGC) {
        toolGC.clearRect(0, 0, width, height);
    }

}
