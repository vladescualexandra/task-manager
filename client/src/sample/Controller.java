package sample;

import common.Settings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import java.io.IOException;


public class Controller{

    @FXML
    TextField text;

    @FXML
    ListView list;

    @FXML
    Button submit;

    Client client;


    public void initialize() throws IOException {
        submit.setOnAction(onSubmitEvent());

        client = new Client(Settings.HOST,
                Settings.PORT, message -> {
            System.out.println(message);
        });

//        stage.setOnHiding(closeEvent());


    }


    private EventHandler<ActionEvent> onSubmitEvent() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    client.send(text.getText().trim());
                    text.setText("");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };
    }


    public void shutdown() {
        try {
            client.send("Client disconnected.");
            client.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
