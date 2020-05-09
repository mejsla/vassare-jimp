# vassare-jimp

En vässare om JavaFX.

## Image layers

Nu skall vi skapa 3 lager att rita på. 
* Understa lagret är bilden vi laddade in. Det är en `Image`
* Nästa lager är det vi ritar färg på. Det gör vi till en `WriteableImage` 
* Sen behöver vi ett topplager där vi kan rita ut penselpekaren, där är det enklast att använda en `Canvas`

* Ta bort `ImageView` från FXML-filen och ta bort medlemsvariabeln från controllern
* Skapa en ny metod att anropa när vi har laddat en ny bild `onImageLoaded`

```java

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

        imageScroller.setContent(imageLayers);
    }
```

* Man ritar på en canvas genom att anropa metoder på en `GraphicsContext`. Skapa metoden nedan för att rita ut penseln

```java
    private void paintTool(GraphicsContext toolGC, MouseEvent event) {
        final double x = event.getX();
        final double y = event.getY();

        final int diameter = brushSizeProperty.intValue();
        final int radius = diameter / 2;
        toolGC.setLineDashes(2);
        toolGC.strokeOval(x - radius, y - radius, diameter, diameter);
    }
```

* Man ritar på en WriteableImage genom att anropa metoder på en `PixelWriter`. Skapa metoden nedan:

```java

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
```



* Vi behöver också veta _var_ vi vill rita penseln så vi registrerar en lyssanre på vår canvas för att hålla koll på var musen är.

```java

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
```

och sist så ändrar vi vår `onImageLoaded` att se ut såhär:

```java
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
```

Slut!