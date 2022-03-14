package net.danielgill.ros.tteditor;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.reference.Reference;
import net.danielgill.ros.timetable.service.Service;

public class PrimaryController implements Initializable {
    @FXML private TableView<Service> serviceView;
    @FXML private TableColumn<Service, String> refColumn;
    @FXML private TableColumn<Service, String> descColumn;
    
    @FXML private void updateServices(Event event) {
        System.out.println(serviceView.getColumns().toString());
        serviceView.getItems().add(new Service(new Reference("1A01"), "Test"));
        System.out.println(serviceView.getItems());
    }

    @FXML
    private void openService(Event event) throws IOException {
        Service s = serviceView.getSelectionModel().getSelectedItem();
        System.out.println(s.getRef());

        Scene scene = new Scene(loadFXML("service"));
        Stage stage = new Stage();
        stage.setTitle("Editing Service");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("ref"));
        descColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("description"));
        serviceView.getItems().setAll(parseServiceList());
    }

    private List<Service> parseServiceList() {
        List<Service> s = new ArrayList<>();
        s.add(new Service(new Reference("1A01"), "Test"));
        s.add(new Service(new Reference("1A02"), "Test2"));
        return s;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
