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

    @FXML TextField search;
    @FXML Label error;
    @FXML MenuButton add_status;
    @FXML MenuButton add_severity;
    @FXML TextField add_description;
    @FXML TextField add_summary;
    @FXML Button add;
    @FXML MenuButton status;
    @FXML MenuButton severity;
    @FXML TableView<Task> list;

    Client client;
    List<Task> taskList = new ArrayList<>();
    List<Task> filteredList;

    public static final int CODE_DISCONNECT = 0;
    public static final int CODE_INSERT = 1;
    public static final int CODE_UPDATE = 2;
    public static final int CODE_DELETE = 3;

    public static final String CODE_CLEAR = "clear";


    public void initialize() throws IOException {
        initComponents();
        client = new Client(Settings.HOST,
                Settings.PORT, displayTasks());
    }

    private void initComponents() {
        setSeverityFilter();
        setStatusFilter();
        setColumns();
        setContextMenu();
        add.setOnAction(insertEvent());
        search.setOnAction(actionEvent -> filterList());
    }

    private Client.ClientCallback displayTasks() {
        return message -> {
            if (message.equalsIgnoreCase(CODE_CLEAR)) {
                taskList.clear();
            }

            Task task = Task.convertToTask(message);
            taskList.add(task);
            ObservableList<Task> obsList = FXCollections.observableArrayList(taskList);

            if (list != null && list.getScene() != null) {
                list.setItems(obsList);
            }
        };
    }


    private EventHandler<ActionEvent> insertEvent() {
        return actionEvent -> {
            if (validateTask()) {
                addTask();
            }
        };
    }

    private EventHandler<ActionEvent> updateEvent() {
        return actionEvent -> populateView(list.getSelectionModel().getSelectedItem());
    }

    private EventHandler<ActionEvent> deleteEvent() {
        return actionEvent -> {
            try {
                client.send(String.valueOf(CODE_DELETE) + list.getSelectionModel().getSelectedItem().getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private boolean validateTask() {
        if (!add_summary.getText().equalsIgnoreCase("")) {
            if (!add_description.getText().equalsIgnoreCase("")) {
                return true;
            } else {
                error.setVisible(true);
                error.setText("Empty description.");
            }
        } else {
            error.setVisible(true);
            error.setText("Empty summary.");
        }
        return false;
    }

    private void addTask() {
        Task task = new Task();
        task.setSummary(add_summary.getText().trim());
        task.setDescription(add_description.getText().trim());
        task.setSeverity(add_severity.getText());
        task.setStatus(add_status.getText());

        try {
            client.send(CODE_INSERT + task.toString());
            cleanView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void populateView(Task selectedItem) {
        add_summary.setText(selectedItem.getSummary());
        add_description.setText(selectedItem.getDescription());
        add_severity.setText(selectedItem.getSeverity().toString());
        add_status.setText(selectedItem.getStatus().toString());
        add.setText("update");
        add.setOnAction(updateTask());
    }

    private EventHandler<ActionEvent> updateTask() {
        return actionEvent -> {
            try {
                Task task = list.getSelectionModel().getSelectedItem();
                task.setSummary(add_summary.getText().trim());
                task.setDescription(add_description.getText().trim());
                task.setSeverity(add_severity.getText());
                task.setStatus(add_status.getText());
                client.send(CODE_UPDATE + task.toString());
                cleanView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void cleanView() {
        add_summary.setText("");
        add_description.setText("");
        add_severity.setText("severity");
        add_status.setText("status");
        add.setText("add a new task");
        add.setOnAction(insertEvent());
        error.setVisible(false);
    }

    private void setContextMenu() {
        ContextMenu cm = new ContextMenu();

        MenuItem delete = new MenuItem("Delete");
        MenuItem edit = new MenuItem("Edit");

        delete.setOnAction(deleteEvent());
        edit.setOnAction(updateEvent());

        cm.getItems().add(delete);
        cm.getItems().add(edit);

        list.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                cm.show(list, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                cm.hide();
            }
        });
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
        return actionEvent -> add_severity.setText(convertSeverity(i));
    }

    private EventHandler<ActionEvent> addStatusEvent(int i) {
        return actionEvent -> add_status.setText(convertStatus(i));
    }


    private EventHandler<ActionEvent> filterSeverityEvent(int i) {
        return actionEvent -> {
            severity.setText(convertSeverity(i));
            filterList();
        };
    }

    private EventHandler<ActionEvent> filterStatusEvent(int i) {
        return actionEvent -> {
            status.setText(convertStatus(i));
            filterList();
        };
    }

    private void filterList() {
        filteredList = new ArrayList<>(taskList);

        for (Task task : taskList) {

            if (!search.getText().trim().equals("")) {
                if (!task.getSummary().contains(search.getText())
                        && !task.getDescription().contains(search.getText())) {
                    filteredList.remove(task);
                }
            }

            if (!severity.getText().equals("no severity")
                    && !severity.getText().equals("severity")) {
                if (!task.getSeverity().toString().equals(severity.getText())) {
                    filteredList.remove(task);
                }
            }

            if (!status.getText().equals("no status")
                    && !status.getText().equals("status")) {
                if (!task.getStatus().toString().equals(status.getText())) {
                    filteredList.remove(task);
                }
            }
        }

        ObservableList<Task> obsList = FXCollections.observableArrayList(filteredList);
        list.setItems(obsList);


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
            client.send(String.valueOf(CODE_DISCONNECT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
    }
}
