module ru.kosarev.cleint.client {
    requires javafx.controls;
    requires javafx.fxml;


    opens ru.kosarev.cleint.client to javafx.fxml;
    exports ru.kosarev.cleint.client;
}