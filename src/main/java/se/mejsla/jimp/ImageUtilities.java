package se.mejsla.jimp;

import javafx.beans.property.IntegerProperty;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ImageUtilities {

    public static void clearCanvas(int width, int height, GraphicsContext toolGC) {
        toolGC.clearRect(0, 0, width, height);
    }

    public static void paintTool(GraphicsContext toolGC, int diameter, MouseEvent event) {
        final double x = event.getX();
        final double y = event.getY();

        final int radius = diameter / 2;
        toolGC.setLineDashes(2);
        toolGC.strokeOval(x - radius, y - radius, diameter, diameter);
    }

    public static void applyPaint(
            final double x,
            final double y,
            final int diameter,
            final boolean erase,
            final PixelWriter pixelWriter) {
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

    public static void registerMouseListeners(
            final int height,
            final int width,
            final IntegerProperty brushSizeProperty,
            final PixelWriter pixelWriter,
            final Canvas topToolIndicatorLayer,
            final GraphicsContext toolGC,
            final StackPane imageLayers) {
        imageLayers.setOnMouseEntered(mouseEvent -> {
            imageLayers.setCursor(Cursor.NONE);
        });
        imageLayers.setOnMouseExited(mouseEvent -> {
            imageLayers.setCursor(Cursor.DEFAULT);
        });

        topToolIndicatorLayer.setOnMouseMoved(event -> {
            ImageUtilities.clearCanvas(width, height, toolGC);
            toolGC.setStroke(Color.rgb(255, 255, 255, 1.0d));
            ImageUtilities.paintTool(toolGC, brushSizeProperty.intValue(), event);
        });

        topToolIndicatorLayer.setOnMouseDragged(event -> {
            ImageUtilities.clearCanvas(width, height, toolGC);
            toolGC.setStroke(Color.RED);
            ImageUtilities.paintTool(toolGC, brushSizeProperty.intValue(), event);
            final double x = event.getX();
            final double y = event.getY();
            ImageUtilities.applyPaint(
                    (int) x,
                    (int) y,
                    brushSizeProperty.intValue(),
                    event.isShiftDown() || event.isControlDown(),
                    pixelWriter);
        });

        topToolIndicatorLayer.setOnMousePressed((event -> {
            ImageUtilities.clearCanvas(width, height, toolGC);
            toolGC.setStroke(Color.YELLOW);
            ImageUtilities.paintTool(toolGC, brushSizeProperty.intValue(), event);
            final double x = event.getX();
            final double y = event.getY();
            ImageUtilities.applyPaint(
                    (int) x,
                    (int) y,
                    brushSizeProperty.intValue(),
                    event.isShiftDown() || event.isControlDown(),
                    pixelWriter);
        }));
    }
}
