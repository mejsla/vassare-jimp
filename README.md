# vassare-jimp

En vässare om JavaFX.

## Code-A-Long: Paintbrush

* Lägg till en `imageview`
* Lägg till ett `menuitem`, döp till "Open"
* Knyt `action handler` till menyvalet
* Fixa `NullPointerException` genom att knyta `fx:id` på imageview
* Sätt `fitWidth` & `fitHeight` till 0
* Arrange - wrap in `scrollpane`

I `PrimaryController` lägg till

```java
    @FXML
    private ScrollPane imageScroller ;

```

git checkout step 2