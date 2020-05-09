# vassare-jimp

En vässare om JavaFX.

## Paintbrush tools

* Lägg till en VBox till höger
* Lägg till en `Label` och sätt texten till "Brush size" inuti VBoxen
* Lägg till ett `TextField`, klicka ur `Editable` i properties
* Lägg till en horisontell `Slider`

I `PrimaryController` lägg till:

```
    private final IntegerProperty brushSizeProperty = new SimpleIntegerProperty(50);

    @FXML
    private Slider brushSizeSlider;

    @FXML
    private TextField brushSizeField ;
```

* Knyt `fx:id` till controllerna

Knyt de olika egenskaperna till varandra:

``` 
    @FXML
    private void initialize() {
        brushSizeSlider.valueProperty().bindBidirectional(brushSizeProperty);

        brushSizeProperty.addListener((prop, oldValue, newValue) -> {
            brushSizeField.setText("" + newValue);
        });
        brushSizeField.setText("" + brushSizeProperty.getValue());
    }
```