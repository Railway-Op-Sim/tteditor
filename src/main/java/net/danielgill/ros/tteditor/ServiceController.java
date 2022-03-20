package net.danielgill.ros.tteditor;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.event.Event;

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
    }
    
    private List<Event> parseEventList() {
        return App.editing.getEvents();
    }

    @FXML
    private void saveClick() {
        App.editing.setRef(serviceRef.getText());
        App.editing.setDescription(serviceDesc.getText());
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
}
