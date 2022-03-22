package net.danielgill.ros.tteditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.Timetable;
import net.danielgill.ros.timetable.TimetableInvalidException;
import net.danielgill.ros.timetable.parse.ParseTimetable;
import net.danielgill.ros.timetable.reference.Reference;
import net.danielgill.ros.timetable.service.Service;
import net.danielgill.ros.timetable.service.ServiceInvalidException;
import net.danielgill.ros.timetable.time.Time;

public class PrimaryController implements Initializable {
    @FXML private TableView<Service> serviceView;
    @FXML private TableColumn<Service, String> refColumn;
    @FXML private TableColumn<Service, String> descColumn;
    @FXML private TextField startTimeTextField;
    @FXML private Label statNumber;

    @FXML
    private void openService(Event event) throws IOException {
        Service s = serviceView.getSelectionModel().getSelectedItem();
        if(s == null) {

        } else {
            App.editing = s;
            Scene scene = new Scene(loadFXML("service"));
            Stage stage = new Stage();
            stage.setTitle("Editing Service");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void openGraph() throws IOException {
        Scene scene = new Scene(loadFXML("station_order"));
        Stage stage = new Stage();
        stage.setTitle("Graphical Timetable");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void createService() {
        App.ttb.addService(new Service(new Reference("0000"), "EMPTY"));
        updateServices();
    }

    @FXML
    private void deleteService() {
        Service s = serviceView.getSelectionModel().getSelectedItem();
        if(s == null) {

        } else {
            Alert a = new Alert(AlertType.CONFIRMATION);
            a.setTitle("Delete Service?");
            a.setHeaderText("Delete Service " + s.getRef());
            a.setContentText("Are you sure you want to delete this service?");

            Optional<ButtonType> result = a.showAndWait();
            if(result.get() == ButtonType.OK) {
                App.ttb.getServices().remove(s);
            } else {

            }
        }
        updateServices();
    }

    @FXML
    private void validateTimetable() {
        boolean valid = true;
        try {
            Timetable output = new Timetable(new Time(startTimeTextField.getText()));
            for(Service s : App.ttb.getServices()) {
                output.addService(s);
            }
            App.ttb = output;
            output.validate();
        } catch(TimetableInvalidException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error in Validation");
            a.setHeaderText("Error in Validation");
            a.setContentText(e.getMessage());
            a.show();
            valid = false;
        } finally {
            if(valid) {
                Alert a = new Alert(AlertType.INFORMATION);
                a.setTitle("Validation Successful");
                a.setHeaderText("Timetable validation was successful.");
                a.show();
            }
        }
    }

    @FXML
    private void openTTB() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Timetable Files (.ttb)", "*.ttb"));
        Stage stage = (Stage) serviceView.getScene().getWindow();
        File ttbFile = fc.showOpenDialog(stage);
        try {
            App.ttb = ParseTimetable.parseTimetable(ttbFile);
        } catch (FileNotFoundException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Cannot find File");
            a.setContentText("The timetable file cannot be finded, please try again.");
            a.show();
        } finally {
            updateServices();
            startTimeTextField.setText(App.ttb.getStartTime().toString());
        }
    }

    @FXML
    private void saveTTB() {
        Timetable output = new Timetable(new Time(startTimeTextField.getText()));
        for(Service s : App.ttb.getServices()) {
            output.addService(s);
        }
        App.ttb = output;

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Timetable Files (.ttb)", "*.ttb"));
        Stage stage = (Stage) serviceView.getScene().getWindow();
        File file = fc.showSaveDialog(stage);
        if(file != null) {
            try {
                output.validate();
                PrintWriter writer = new PrintWriter(file);
                writer.print(output.getTextTimetable());
                writer.close();
                Alert a = new Alert(AlertType.INFORMATION);
                a.setTitle("Saved Successfully");
                a.setHeaderText("The timetable was saved successfully.");
                a.show();
            } catch (FileNotFoundException e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Cannot find File");
                a.setContentText("The timetable file cannot be finded, please try again.");
                a.show();
            } catch (TimetableInvalidException e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error in Validation");
                a.setHeaderText("Error in validation, cannot save file.");
                a.setContentText(e.getMessage());
                a.show();
            } catch (ServiceInvalidException e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Error in Validation");
                a.setHeaderText("Error in validation, cannot save file.");
                a.setContentText(e.getMessage());
                a.show();
            }
            
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("ref"));
        descColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));
        serviceView.getItems().setAll(parseServiceList());
        startTimeTextField.setText(App.ttb.getStartTime().toString());
        statNumber.setText("0");
        App.pc = this;
    }

    public void updateServices() {
        serviceView.getItems().setAll(parseServiceList());
        statNumber.setText(String.valueOf(App.ttb.getServices().size()));
    }

    private List<Service> parseServiceList() {
        return App.ttb.getServices();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
