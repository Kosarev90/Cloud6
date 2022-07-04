module ru.kosarev.cleint.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.kosarev.cloud6 to javafx.fxml;
    exports ru.kosarev.cloud6;
}