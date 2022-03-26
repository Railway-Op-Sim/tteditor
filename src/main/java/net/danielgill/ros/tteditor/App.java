package net.danielgill.ros.tteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.danielgill.ros.timetable.Timetable;
import net.danielgill.ros.timetable.data.Data;
import net.danielgill.ros.timetable.data.DataTemplate;
import net.danielgill.ros.timetable.data.DataTemplates;
import net.danielgill.ros.timetable.service.Service;
import net.danielgill.ros.timetable.time.Time;

import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Timetable ttb;
    public static Service editing;
    public static PrimaryController pc;
    public static ServiceController sc;
    public static List<String> stations = null;
    public static List<DataTemplate> templates;
    public static Data currentData = null;

    @Override
    public void start(Stage stage) throws IOException {
        ttb = new Timetable(new Time("00:00"));

        DataTemplates dts = new DataTemplates();
        templates = dts.getDataTemplates();

        Scene scene = new Scene(loadFXML("primary"), 600, 400);
        stage.setTitle("ROS Timetable Editor");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}