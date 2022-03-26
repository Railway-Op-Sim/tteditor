package net.danielgill.ros.tteditor;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.event.Event;
import net.danielgill.ros.timetable.event.StopEvent;
import net.danielgill.ros.timetable.service.Service;
import net.danielgill.ros.timetable.service.ServiceInvalidException;

public class StationOrderController implements Initializable {
    @FXML private ListView<String> lv;
    @FXML private CheckBox repeats;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(App.stations == null) {
            rebuildStations();
            writeStations();
        } else if(!checkSame()) {
            rebuildStations();
            writeStations();
        } else {
            writeStations();
        }
    }

    @FXML
    private void moveUp() {
        String str = lv.getSelectionModel().getSelectedItem();
        int index = App.stations.indexOf(str);
        App.stations.remove(str);
        App.stations.add(index - 1 <= 0 ? 0 : index - 1, str);
        writeStations();
        lv.getSelectionModel().select(str);
    }

    @FXML
    private void moveDown() {
        String str = lv.getSelectionModel().getSelectedItem();
        int index = App.stations.indexOf(str);
        App.stations.remove(str);
        App.stations.add(index + 1 > App.stations.size() ? index : index + 1, str);
        writeStations();
        lv.getSelectionModel().select(str);
    }

    @FXML
    private void cancelClick() {
        Stage stage = (Stage) lv.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void doneClick() throws ServiceInvalidException {
        Stage stage = (Stage) lv.getScene().getWindow();
        stage.close();
        Scene scene = new Scene(TimetableGraph.getChart(App.ttb, repeats.isSelected()), 625, 400);
        stage = new Stage();
        stage.setTitle("Graphical Timetable");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    private boolean checkSame() {
        List<String> serStations = getStations();
        if(serStations.size() != App.stations.size()) {
            return false;
        }
        for(String stationA : App.stations) {
            for(String stationB : serStations) {
                if(!serStations.contains(stationA)) {
                    return false;
                }
                if(!App.stations.contains(stationB)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void rebuildStations() {
        App.stations = new ArrayList<>();
        for(Service s : App.ttb.getServices()) {
            for(Event e : s.getEvents()) {
                if(e instanceof StopEvent) {
                    StopEvent evt = (StopEvent) e;
                    if(!App.stations.contains(evt.getLoc().toString())) {
                        App.stations.add(evt.getLoc().toString());
                    }
                }
            }
        }
    }

    private void writeStations() {
        lv.getItems().clear();
        for(String station : App.stations) {
            lv.getItems().add(station);
        }
    }

    private List<String> getStations() {
        List<String> output = new ArrayList<>();
        for(Service s : App.ttb.getServices()) {
            for(Event e : s.getEvents()) {
                if(e instanceof StopEvent) {
                    StopEvent evt = (StopEvent) e;
                    if(!output.contains(evt.getLoc().toString())) {
                        output.add(evt.getLoc().toString());
                    }
                }
            }
        }
        return output;
    }
    
}
