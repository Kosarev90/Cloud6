package ru.kosarev.cloud6;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private Network network;
    public TextField textView;
    public ListView listView;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        String msg = textView.getText();
        network.writeMessage(msg);
        textView.clear();
    }

    private void ReadLoop() {
        try {
            while (true) {
                String msg = network.readMessage();
                Platform.runLater(() -> listView.getItems().add(msg));

            }
        } catch (Exception e) {
            System.err.println("Connected lost");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        {
            try {
                network = new Network(8189);
                Thread readThread = new Thread(this::ReadLoop);
                readThread.setDaemon(true);
                readThread.start();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}