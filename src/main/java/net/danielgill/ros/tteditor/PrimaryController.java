package net.danielgill.ros.tteditor;

import java.io.IOException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.reference.Reference;
import net.danielgill.ros.timetable.service.Service;

public class PrimaryController implements Initializable {
    @FXML private TableView<Service> serviceView;
    @FXML private TableColumn<Service, String> refColumn;
    @FXML private TableColumn<Service, String> descColumn;

    @FXML
    private void openService(Event event) throws IOException {
        Service s = serviceView.getSelectionModel().getSelectedItem();
        if(s == null) {

        } else {
            Scene scene = new Scene(loadFXML("service"));
            Stage stage = new Stage();
            stage.setTitle("Editing Service");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
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
        Alert a = new Alert(AlertType.ERROR);
        a.setTitle("Error in Validation");
        a.setHeaderText("Error in Validation");
        a.setContentText("The timetable did not validate.");
        a.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("ref"));
        descColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));
        serviceView.getItems().setAll(parseServiceList());
    }

    public void updateServices() {
        serviceView.getItems().setAll(parseServiceList());
    }

    private List<Service> parseServiceList() {
        return App.ttb.getServices();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
