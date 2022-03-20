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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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

        App.sc = this;
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
}
