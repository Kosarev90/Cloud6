package ru.kosarev.cloud6;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private String homeDir;

    private byte[] buf;
    @FXML
    public ListView<String> ClientView;
    @FXML
    public ListView<String> ServerView;
    private Network network;
    public TextField textView;

    public void sendMessage(ActionEvent actionEvent) throws IOException {
        String msg = textView.getText();
        network.writeMessage(msg);
        textView.clear();
    }

    private void ReadLoop() {
        try {
            while (true) {
                String command = network.readString();
                if (command.equals("#list")) {
                    Platform.runLater(() -> ServerView.getItems().clear());
                    int len = network.readInt();
                    for (int i = 0; i < len; i++) {
                        String file = network.readString();
                        Platform.runLater(() -> ServerView.getItems().add(file));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Connected lost");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        {
            try {
                buf = new byte[256];
                homeDir = System.getProperty("user.home");
                ClientView.getItems().clear();
                ClientView.getItems().addAll(getFiles(homeDir));
                network = new Network(8190);
                Thread readThread = new Thread(this::ReadLoop);
                readThread.setDaemon(true);
                readThread.start();

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
        assert list != null;
        return Arrays.asList(list);
    }

    public void upload(ActionEvent actionEvent) throws IOException {
        network.getOs().writeUTF("#file#");
        String file = ClientView.getSelectionModel().getSelectedItem();
        network.getOs().writeUTF(file);
        File toSend = Path.of(homeDir).resolve(file).toFile();
        network.getOs().writeLong(toSend.length());
        try (FileInputStream fis = new FileInputStream(toSend)) {
            while (fis.available() > 0) {
                int read = fis.read(buf);
                network.getOs().write(buf, 0, read);
            }
        }
        network.getOs().flush();
    }

    public void download(ActionEvent actionEvent) {

    }
}