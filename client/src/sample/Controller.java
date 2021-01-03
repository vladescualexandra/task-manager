package sample;

import common.Settings;
import common.Transport;
import common.data.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    @FXML
    Button add;
    @FXML
    MenuButton status;

    @FXML
    MenuButton severity;

    @FXML
    TableView<Task> list;


    Client client;
    List<Task> taskList = new ArrayList<>();


    public void initialize() throws IOException {


        setSeverityFilter();
        setStatusFilter();
        setColumns();
        ContextMenu cm = new ContextMenu();
        MenuItem mi = new MenuItem("Delete");
        mi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("delete item: " + list.getSelectionModel().getSelectedItem().toString());
            }
        });
        cm.getItems().add(mi);

        list.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    cm.show(list, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                } else {
                    cm.hide();
                }
            }
        });

        client = new Client(Settings.HOST,
                Settings.PORT, message -> {

            Task task = Task.convertToTask(message);

            taskList.add(task);

            ObservableList<Task> obsList = FXCollections.observableArrayList(taskList);
            list.setItems(obsList);

        });
    }

    private void setSeverityFilter() {
        for (int i = 0; i < severity.getItems().size(); i++) {
            severity.getItems().get(i).setOnAction(filterSeverityEvent(i));
        }
    }

    private void setStatusFilter() {
        for (int i = 0; i < status.getItems().size(); i++) {
            status.getItems().get(i).setOnAction(filterStatusEvent(i));
        }
    }

    private EventHandler<ActionEvent> filterSeverityEvent(int i) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("clicked severity " + i);
            }
        };
    }

    private EventHandler<ActionEvent> filterStatusEvent(int i) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("clicked status " + i);
            }
        };
    }


    private void setColumns() {
        String[] columns = {"id", "summary", "description", "severity", "status"};

        for (String s : columns) {
            TableColumn<Task, String> column = new TableColumn<>(s);
            column.setCellValueFactory(new PropertyValueFactory<>(s));
            list.getColumns().add(column);

        }
    }


//    private EventHandler<ActionEvent> onSubmitEvent() {
//        return new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//
//                try {
//                    client.send(text.getText().trim());
//                    text.setText("");
//                    taskList.clear();
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        };
//    }


    public void shutdown() {
        try {
            client.send("Client disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();

    }
}
