module se.mejsla.jimp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;

    opens se.mejsla.jimp to javafx.fxml;
    exports se.mejsla.jimp;
}