package net.danielgill.ros.tteditor;

import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import net.danielgill.ros.timetable.Timetable;
import net.danielgill.ros.timetable.event.Event;
import net.danielgill.ros.timetable.event.StopEvent;
import net.danielgill.ros.timetable.service.Service;
import net.danielgill.ros.timetable.time.Time;

public class TimetableGraph {
    public static LineChart<Number, String> getChart(Timetable ttb) {
        NumberAxis timeAxis = new NumberAxis();
        CategoryAxis stationAxis = new CategoryAxis();
        timeAxis.setLabel("Minutes after Start Time");
        
        for(String s : App.stations) {
            stationAxis.getCategories().add(s);
        }

        LineChart<Number, String> lc = new LineChart<>(timeAxis, stationAxis);

        for(Service s : ttb.getServices()) {
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            series = getSeriesFromService(series, s, ttb);
            lc.getData().add(series);
        }

        ObservableList<String> c = FXCollections.observableArrayList(stationAxis.getCategories());
        Collections.sort(c, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                for(String station : App.stations) {
                    if(station.equals(o1)) {
                        return -1;
                    } else if(station.equals(o2)) {
                        return 1;
                    }
                }
                return 0;
            }
            
        });
        stationAxis.setCategories(c);
        stationAxis.setAutoRanging(true);

        return lc;
    }

    private static XYChart.Series<Number, String> getSeriesFromService(XYChart.Series<Number, String> series, Service s, Timetable ttb) {
        for(Event e : s.getEvents()) {
            if(e.getType() == "stop") {
                StopEvent evt = (StopEvent) e;
                for(Time t : evt.getTimes()) {
                    int mins = t.getMinutes() - ttb.getStartTime().getMinutes();
                    series.getData().add(new XYChart.Data<>(mins, evt.getLoc().toString()));
                }
            }
        }
        return series;
    }
}
