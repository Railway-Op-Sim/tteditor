package net.danielgill.ros.tteditor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import net.danielgill.ros.timetable.event.Event;

public class EventController implements Initializable {
    @FXML private ChoiceBox<String> eventBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(Event e : EventRegistry.EVENTS_LIST) {
            eventBox.getItems().add("[" + e.getType() + "] " + e.getDescription());
        }
    }
    
}
