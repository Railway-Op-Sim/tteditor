package net.danielgill.ros.tteditor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.event.CdtEvent;
import net.danielgill.ros.timetable.event.Event;
import net.danielgill.ros.timetable.event.FerEvent;
import net.danielgill.ros.timetable.event.FnsEvent;
import net.danielgill.ros.timetable.event.FnshsEvent;
import net.danielgill.ros.timetable.event.FnsshEvent;
import net.danielgill.ros.timetable.event.FrhEvent;
import net.danielgill.ros.timetable.event.FrhshEvent;
import net.danielgill.ros.timetable.event.FspEvent;
import net.danielgill.ros.timetable.event.JboEvent;
import net.danielgill.ros.timetable.event.PassEvent;
import net.danielgill.ros.timetable.event.RspEvent;
import net.danielgill.ros.timetable.event.SfsEvent;
import net.danielgill.ros.timetable.event.SnsEvent;
import net.danielgill.ros.timetable.event.SnsfshEvent;
import net.danielgill.ros.timetable.event.SnsshEvent;
import net.danielgill.ros.timetable.event.SntEvent;
import net.danielgill.ros.timetable.event.SntshEvent;
import net.danielgill.ros.timetable.event.StopEvent;
import net.danielgill.ros.timetable.location.Location;
import net.danielgill.ros.timetable.location.NamedLocation;
import net.danielgill.ros.timetable.location.StartLocation;
import net.danielgill.ros.timetable.reference.Reference;
import net.danielgill.ros.timetable.time.Time;

public class EventController implements Initializable {
    @FXML private ChoiceBox<String> eventBox;

    @FXML private TextField eventTime;
    @FXML private TextField arrivalTime;
    @FXML private TextField departureTime;
    @FXML private ChoiceBox<String> namedLocation;
    @FXML private TextField startLocation;
    @FXML private TextField exitLocation;
    @FXML private TextField otherReference;
    @FXML private TextField shuttleReference;
    @FXML private TextField feederRefernce;
    @FXML private TextField finishReference;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eventBox.setOnAction(this::setFields);
        for(Event e : EventRegistry.EVENTS_LIST) {
            eventBox.getItems().add("[" + e.getType() + "] " + e.getDescription());
        }
        if(App.locations == null) {
            return;
        }
        for(String loc : App.locations) {
            namedLocation.getItems().add(loc);
        }
    }

    @FXML
    public void cancelClick() {
        Stage stage = (Stage) eventBox.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addEvent() {
        App.editing.addEvent(getEventFromInput());
        App.sc.updateEventList();
        Stage stage = (Stage) eventBox.getScene().getWindow();
        stage.close();
    }

    private Event getEventFromInput() {
        String eventString = (String) eventBox.getValue();
        if(eventString == null) {
            return null;
        }
        Event event = getEventFromString(eventString);

        if(event == null) {
            return null;
        } else if(event instanceof SntEvent) {
            return new SntEvent(new Time(eventTime.getText()), new StartLocation(startLocation.getText()));
        } else if(event instanceof SnsEvent) {
            return new SnsEvent(new Time(eventTime.getText()), new Reference(otherReference.getText()));
        } else if(event instanceof SfsEvent) {
            return new SfsEvent(new Time(eventTime.getText()), new Reference(otherReference.getText()));
        } else if(event instanceof StopEvent) {
            if(arrivalTime.getText().isEmpty() || arrivalTime.getText() == null) {
                return new StopEvent(new NamedLocation(namedLocation.getValue()), new Time(departureTime.getText()));
            } else if(departureTime.getText().isEmpty() || departureTime.getText() == null) {
                return new StopEvent(new Time(arrivalTime.getText()), new NamedLocation(namedLocation.getValue()));
            } else {
                return new StopEvent(new Time(arrivalTime.getText()), new Time(departureTime.getText()), new NamedLocation(namedLocation.getValue()));
            }
        } else if(event instanceof PassEvent) {
            return new PassEvent(new Time(eventTime.getText()), new NamedLocation(namedLocation.getValue()));
        } else if(event instanceof CdtEvent) {
            return new CdtEvent(new Time(eventTime.getText()));
        } else if(event instanceof JboEvent) {
            return new JboEvent(new Time(eventTime.getText()), new Reference(otherReference.getText()));
        } else if(event instanceof FerEvent) {
            return new FerEvent(new Time(eventTime.getText()), new Location(exitLocation.getText()));
        } else if(event instanceof FnsEvent) {
            return new FnsEvent(new Time(eventTime.getText()), new Reference(otherReference.getText()));
        } else if(event instanceof FrhEvent) {
            return new FrhEvent();
        } else if(event instanceof FspEvent) {
            return new FspEvent(new Time(eventTime.getText()), new Reference(otherReference.getText()));
        } else if(event instanceof RspEvent) {
            return new RspEvent(new Time(eventTime.getText()), new Reference(otherReference.getText()));
        } else if(event instanceof SntshEvent) {
            return new SntshEvent(new Time(eventTime.getText()), new StartLocation(startLocation.getText()), new Reference(shuttleReference.getText()));
        } else if(event instanceof SnsshEvent) {
            return new SnsshEvent(new Time(eventTime.getText()), new Reference(shuttleReference.getText()), new Reference(feederRefernce.getText()));
        } else if(event instanceof SnsfshEvent) {
            return new SnsfshEvent(new Time(eventTime.getText()), new Reference(shuttleReference.getText()));
        } else if(event instanceof FnsshEvent) {
            return new FnsshEvent(new Time(eventTime.getText()), new Reference(shuttleReference.getText()), new Reference(finishReference.getText()));
        } else if(event instanceof FrhshEvent) {
            return new FrhshEvent(new Time(eventTime.getText()), new Reference(shuttleReference.getText()));
        } else if(event instanceof FnshsEvent) {
            return new FnshsEvent(new Time(eventTime.getText()), new Reference(shuttleReference.getText()));
        } else {
            return null;
        }
    }

    @FXML
    public void setFields(ActionEvent actionEvent) {
        disableAll();
        String eventString = (String) eventBox.getValue();
        if(eventString == null) {
            return;
        }

        Event event = getEventFromString(eventString);

        if(event == null) {
            return;
        } else if(event instanceof SntEvent) {
            eventTime.setDisable(false);
            startLocation.setDisable(false);
        } else if(event instanceof SnsEvent) {
            eventTime.setDisable(false);
            otherReference.setDisable(false);
        } else if(event instanceof SfsEvent) {
            eventTime.setDisable(false);
            otherReference.setDisable(false);
        } else if(event instanceof StopEvent) {
            arrivalTime.setDisable(false);
            departureTime.setDisable(false);
            namedLocation.setDisable(false);
        } else if(event instanceof PassEvent) {
            eventTime.setDisable(false);
            namedLocation.setDisable(false);
        } else if(event instanceof CdtEvent) {
            eventTime.setDisable(false);
        } else if(event instanceof JboEvent) {
            eventTime.setDisable(false);
            otherReference.setDisable(false);
        } else if(event instanceof FerEvent) {
            eventTime.setDisable(false);
            exitLocation.setDisable(false);
        } else if(event instanceof FnsEvent) {
            eventTime.setDisable(false);
            otherReference.setDisable(false);
        } else if(event instanceof FrhEvent) {
            
        } else if(event instanceof FspEvent) {
            eventTime.setDisable(false);
            otherReference.setDisable(false);
        } else if(event instanceof RspEvent) {
            eventTime.setDisable(false);
            otherReference.setDisable(false);
        } else if(event instanceof SntshEvent) {
            eventTime.setDisable(false);
            startLocation.setDisable(false);
            shuttleReference.setDisable(false);
        } else if(event instanceof SnsshEvent) {
            eventTime.setDisable(false);
            shuttleReference.setDisable(false);
            feederRefernce.setDisable(false);
        } else if(event instanceof SnsfshEvent) {
            eventTime.setDisable(false);
            shuttleReference.setDisable(false);
        } else if(event instanceof FnsshEvent) {
            eventTime.setDisable(false);
            shuttleReference.setDisable(false);
            finishReference.setDisable(false);
        } else if(event instanceof FrhshEvent) {
            eventTime.setDisable(false);
            shuttleReference.setDisable(false);
        } else if(event instanceof FnshsEvent) {
            eventTime.setDisable(false);
            shuttleReference.setDisable(false);
        }
    }

    private Event getEventFromString(String eventString) {
        Event event = null;
        for(Event e : EventRegistry.EVENTS_LIST) {
            if(eventString.equals("[" + e.getType() + "] " + e.getDescription())) {
                event = e;
            }
        }
        return event;
    }

    private void disableAll() {
        eventTime.setDisable(true);
        arrivalTime.setDisable(true);
        departureTime.setDisable(true);
        namedLocation.setDisable(true);
        startLocation.setDisable(true);
        exitLocation.setDisable(true);
        otherReference.setDisable(true);
        shuttleReference.setDisable(true);
        feederRefernce.setDisable(true);
        finishReference.setDisable(true);
    }
    
}
