package net.danielgill.ros.tteditor;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServiceController implements Initializable {
    @FXML private TextField serviceRef;
    @FXML private TextField serviceDesc;
    @FXML private Button cancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceRef.setText(App.editing.getRef().toString());
        serviceDesc.setText(App.editing.getDescription());
    }
    
    @FXML
    private void saveClick() {
        
    }

    @FXML
    private void cancelClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
