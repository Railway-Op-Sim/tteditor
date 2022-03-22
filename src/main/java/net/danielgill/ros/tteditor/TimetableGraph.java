package net.danielgill.ros.tteditor;

import java.util.ArrayList;
import java.util.List;

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
        setStationCategories(stationAxis, ttb);

        LineChart<Number, String> lc = new LineChart<>(timeAxis, stationAxis);

        for(Service s : ttb.getServices()) {
            XYChart.Series<Number, String> series = new XYChart.Series<>();
            series = getSeriesFromService(series, s, ttb);
            lc.getData().add(series);
        }

        return lc;
    }

    private static void setStationCategories(CategoryAxis stationAxis, Timetable ttb) {
        List<String> stations = new ArrayList<>();
        for(Service s : ttb.getServices()) {
            List<String> knownStations = new ArrayList<>();
            for(Event e : s.getEvents()) {
                if(e.getType() == "stop") {
                    StopEvent evt = (StopEvent) e;
                    knownStations.add(evt.getLoc().toString());
                }
            }
            int count = 0;
            for(Event e : s.getEvents()) {
                if(e.getType() == "stop") {
                    StopEvent evt = (StopEvent) e;
                    String prev = count <= 0 ? null : knownStations.get(count - 1);
                    String next = count - 1 >= knownStations.size() ? null : knownStations.get(count);
                    System.out.println(stations + " " + evt.getLoc().toString() + " " + prev + " " + next);
                    stations = placeStation(stations, evt.getLoc().toString(), prev, next);
                    count++;
                }
                
            }
        }
        stationAxis.getCategories().addAll(stations);
    }

    private static List<String> placeStation(List<String> stations, String station, String prev, String next) {
        if(stations.size() <= 1 && !stations.contains(station)) {
            stations.add(station);
            return stations;
        }
        if(stations.contains(station)) {
            return stations;
        }
        if(prev == null || next == null) {
            return stations;
        }
        for(int i = 0; i < stations.size(); i++) {
            if(stations.get(i).equals(prev)) {
                if(i + 1 == stations.size()) {
                    stations.add(station);
                    return stations;
                }
                if(stations.get(i + 1).equals(next)) {
                    stations.add(i + 1, station);
                    return stations;
                }
            } else if(stations.get(i).equals(next)) {
                if(stations.get(i + 1).equals(prev)) {
                    stations.add(i, station);
                    return stations;
                }
            }
            /*
            if(prev != null && stations.get(i).equals(prev)) {
                if(next == null) {
                    return stations;
                } else if(stations.subList(i, stations.size()).contains(next)) {
                    stations.add(i + 1, station);
                    return stations;
                }
            }
            if(next != null && stations.get(i).equals(next)) {
                if(prev == null) {
                    return stations;
                } else if(stations.subList(0, i).contains(prev)) {
                    stations.add(i, station);
                    return stations;
                }
            }
            */
        }
        return stations;
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
