module net.danielgill.ros {
    requires transitive javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive net.danielgill.ros.timetable;

    opens net.danielgill.ros.tteditor to javafx.fxml;

    exports net.danielgill.ros.tteditor;
}