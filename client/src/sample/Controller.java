package sample;

import common.Settings;
import common.data.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Controller {
    @FXML
    MenuButton add_status;
    @FXML
    MenuButton add_severity;
    @FXML
    TextField add_description;
    @FXML
    TextField add_summary;
    @FXML
    TextField add_id;
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
        setContextMenu();

        add.setOnAction(addTaskEvent());

        client = new Client(Settings.HOST,
                Settings.PORT, message -> {

            if (message.equalsIgnoreCase("clear")) {
                taskList.clear();
            }

            Task task = Task.convertToTask(message);
            taskList.add(task);
            ObservableList<Task> obsList = FXCollections.observableArrayList(taskList);

            if (list != null && list.getScene() != null) {
                list.setItems(obsList);
            }

        });
    }


    private EventHandler<ActionEvent> addTaskEvent() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (add_id.getText() != null || !add_id.getText().equalsIgnoreCase("")) {
                    if (!add_summary.getText().equalsIgnoreCase("")) {
                        if (!add_description.getText().equalsIgnoreCase("")) {
                            Task task = new Task();
                            task.setId(add_id.getText().trim());
                            task.setSummary(add_summary.getText().trim());
                            task.setDescription(add_description.getText().trim());
                            task.setSeverity(add_severity.getText());
                            task.setStatus(add_status.getText());

                            try {
                                client.send("1" + task.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        };
    }

    /*
       -1 - disconnect
        0 - refresh list
        1 - insert
        2 - update
        3 - delete
     */

    private void setContextMenu() {
        ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(deleteEvent());
        cm.getItems().add(delete);

        MenuItem edit = new MenuItem("Edit");
        edit.setOnAction(updateEvent(cm));
        cm.getItems().add(edit);

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
    }

    private EventHandler<ActionEvent> deleteEvent() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    client.send("3" + list.getSelectionModel().getSelectedItem().getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private EventHandler<ActionEvent> updateEvent(ContextMenu cm) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                populateView(list.getSelectionModel().getSelectedItem());


            }
        };
    }

    private void populateView(Task selectedItem) {
        add_id.setText(selectedItem.getId());
        add_summary.setText(selectedItem.getSummary());
        add_description.setText(selectedItem.getDescription());
        add_severity.setText(selectedItem.getSeverity().toString());
        add_status.setText(selectedItem.getStatus().toString());
        add.setText("update");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Task task = list.getSelectionModel().getSelectedItem();
                    task.setSummary(add_summary.getText().trim());
                    task.setDescription(add_description.getText().trim());
                    task.setSeverity(add_severity.getText());
                    task.setStatus(add_status.getText());
                    client.send("2" + task.toString());
                    cleanView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void cleanView() {
        add_id.setText("");
        add_summary.setText("");
        add_description.setText("");
        add_severity.setText("severity");
        add_status.setText("status");
        add.setText("add a new task");
    }


    private void setSeverityFilter() {
        for (int i = 0; i < severity.getItems().size(); i++) {
            severity.getItems().get(i).setOnAction(filterSeverityEvent(i));
        }

        for (int i = 0; i < add_status.getItems().size(); i++) {
            add_severity.getItems().get(i).setOnAction(addSeverityEvent(i));
        }
    }

    private void setStatusFilter() {
        for (int i = 0; i < status.getItems().size(); i++) {
            status.getItems().get(i).setOnAction(filterStatusEvent(i));
        }

        for (int i = 0; i < add_status.getItems().size(); i++) {
            add_status.getItems().get(i).setOnAction(addStatusEvent(i));
        }
    }

    private EventHandler<ActionEvent> addSeverityEvent(int i) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                add_severity.setText(convertSeverity(i));
            }
        };
    }

    private EventHandler<ActionEvent> addStatusEvent(int i) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                add_status.setText(convertStatus(i));
            }
        };
    }

    private EventHandler<ActionEvent> filterSeverityEvent(int i) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                severity.setText(convertSeverity(i));
            }
        };
    }

    private EventHandler<ActionEvent> filterStatusEvent(int i) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                status.setText(convertStatus(i));
            }
        };
    }

    private String convertStatus(int i) {
        return switch (i) {
            case 3 -> "no status";
            case 2 -> "DONE";
            case 1 -> "IN PROGRESS";
            default -> "NOT STARTED";
        };
    }

    private String convertSeverity(int i) {
        return switch (i) {
            case 3 -> "no severity";
            case 2 -> "URGENT";
            case 1 -> "IMPORTANT";
            default -> "TRIVIAL";
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

    public void shutdown() {
        try {
            client.send("-1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();

    }
}
