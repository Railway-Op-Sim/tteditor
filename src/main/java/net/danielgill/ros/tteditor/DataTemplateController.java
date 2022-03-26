package net.danielgill.ros.tteditor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.data.Data;
import net.danielgill.ros.timetable.data.DataTemplate;

public class DataTemplateController implements Initializable {
    @FXML private TextField name;
    @FXML private TextField maxSpeed;
    @FXML private TextField mass;
    @FXML private TextField maxBrake;
    @FXML private TextField power;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(App.currentData != null) {
            Data data = App.currentData;
            maxSpeed.setText(String.valueOf(data.getMaxSpeed()));
            mass.setText(String.valueOf(data.getMass()));
            maxBrake.setText(String.valueOf(data.getMaxBrake()));
            power.setText(String.valueOf(data.getPower()));
        }
    }

    //button saves the data template and closes the window
    public void save() {
        DataTemplate dt = new DataTemplate(name.getText(),
                Integer.parseInt(maxSpeed.getText()),
                Integer.parseInt(mass.getText()),
                Integer.parseInt(maxBrake.getText()),
                Integer.parseInt(power.getText()), 
                name.getText());
        App.templates.add(dt);
        App.sc.initialize(null, null);
        App.currentData = null;
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    //pressing button closes window
    @FXML
    public void closeWindow() {
        App.currentData = null;
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    //get integers of the text fields
    public int getMaxSpeed() {
        return Integer.parseInt(maxSpeed.getText());
    }
    public int getMass() {
        return Integer.parseInt(mass.getText());
    }
    public int getMaxBrake() {
        return Integer.parseInt(maxBrake.getText());
    }
    public int getPower() {
        return Integer.parseInt(power.getText());
    }
    
}
