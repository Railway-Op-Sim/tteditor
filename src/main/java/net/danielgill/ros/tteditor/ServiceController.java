package net.danielgill.ros.tteditor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.data.Data;
import net.danielgill.ros.timetable.data.DataTemplate;
import net.danielgill.ros.timetable.event.Event;
import net.danielgill.ros.timetable.service.Repeat;

public class ServiceController implements Initializable {
    @FXML private TextField serviceRef;
    @FXML private TextField serviceDesc;
    @FXML private Button cancelButton;

    @FXML private TableView<Event> eventsView;
    @FXML private TableColumn<Event, String> eventsColumn;

    @FXML private CheckBox repeatCheckBox;
    @FXML private TextField interval;
    @FXML private TextField increment;
    @FXML private TextField repeats;

    @FXML private ChoiceBox<String> dataBox;
    @FXML private TextField startSpeed;
    @FXML private TextField dataField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceRef.setText(App.editing.getRef().toString());
        serviceDesc.setText(App.editing.getDescription());
        eventsColumn.setCellValueFactory(new PropertyValueFactory<Event, String>("contextualDescription"));
        eventsView.getItems().setAll(parseEventList());

        Repeat r = App.editing.getRepeat();
        if(r.getNumberOfRepeats() == 0) {
            interval.setDisable(true);
            increment.setDisable(true);
            repeats.setDisable(true);
            repeatCheckBox.setSelected(false);
        } else {
            repeatCheckBox.setSelected(true);
            interval.setDisable(false);
            increment.setDisable(false);
            repeats.setDisable(false);
            interval.setText(String.valueOf(r.getInterval()));
            increment.setText(String.valueOf(r.getIncrement()));
            repeats.setText(String.valueOf(r.getNumberOfRepeats()));
        }

        dataBox.getItems().add("CUSTOM DATA");

        for(DataTemplate dt : App.templates) {
            dataBox.getItems().add(dt.getLabel());
        }

        dataBox.setValue("CUSTOM DATA");

        if(App.editing.getData() != null) {
            dataField.setText(App.editing.getData().toString());
        }

        App.sc = this;
    }

    @FXML
    private void openDataTemplate() throws IOException {
        if(dataField.getText().equals("")) {
            App.currentData = null;
        } else {
            App.currentData = this.getDataFromString(dataField.getText());
        }
        Stage stage = new Stage();
        Scene scene = new Scene(loadFXML("data_template"));
        stage.setTitle("Data Template");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void updateEventList() {
        eventsView.getItems().setAll(parseEventList());
    }
    
    private List<Event> parseEventList() {
        return App.editing.getEvents();
    }

    @FXML
    private void saveClick() {
        App.editing.setRef(serviceRef.getText());
        App.editing.setDescription(serviceDesc.getText());
        if(repeatCheckBox.isSelected()) {
            int intervalInt = Integer.parseInt(interval.getText());
            int incrementInt = Integer.parseInt(increment.getText());
            int repeatsInt = Integer.parseInt(repeats.getText());
            App.editing.setRepeat(intervalInt, incrementInt, repeatsInt);
        } else {
            App.editing.setRepeat(new Repeat(0, 0, 0));
        }

        if(dataBox.getValue().equalsIgnoreCase("CUSTOM DATA")) {
            if(dataField.getText() == null || dataField.getText().isEmpty()) {

            } else {
                App.editing.setData(getDataFromString(dataField.getText()));
            }
        } else {
            for(DataTemplate dt : App.templates) {
                if(dataBox.getValue().equalsIgnoreCase(dt.getLabel())) {
                    Data d = new Data(Integer.parseInt(startSpeed.getText()), dt.getData());
                    App.editing.setData(d);
                }
            }
        }

        App.pc.updateServices();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void repeatEnable() {
        if(repeatCheckBox.isSelected()) {
            interval.setDisable(false);
            increment.setDisable(false);
            repeats.setDisable(false);
        } else {
            interval.setDisable(true);
            increment.setDisable(true);
            repeats.setDisable(true);
        }
    }

    @FXML
    private void addEvent() throws IOException {
        Scene scene = new Scene(loadFXML("event"));
        Stage stage = new Stage();
        stage.setTitle("Add Event");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private Data getDataFromString(String dataString) {
        String[] dataParts = dataString.split(";");
        return new Data(Integer.parseInt(dataParts[0]), Integer.parseInt(dataParts[1]), Integer.parseInt(dataParts[2]), Integer.parseInt(dataParts[3]), Integer.parseInt(dataParts[4]));
    }

    @FXML
    private void deleteEvent() {
        Event e = eventsView.getSelectionModel().getSelectedItem();
        App.editing.getEvents().remove(e);
        updateEventList();
    }

    @FXML
    private void moveUp() {
        Event e = eventsView.getSelectionModel().getSelectedItem();
        int index = App.editing.getEvents().indexOf(e);
        App.editing.getEvents().remove(e);
        App.editing.getEvents().add(index - 1 <= 0 ? 0 : index - 1, e);
        updateEventList();
    }

    @FXML
    private void moveDown() {
        Event e = eventsView.getSelectionModel().getSelectedItem();
        int index = App.editing.getEvents().indexOf(e);
        App.editing.getEvents().remove(e);
        App.editing.getEvents().add(index + 1 > App.editing.getEvents().size() ? index : index + 1, e);
        updateEventList();
    }
}
