package net.danielgill.ros.tteditor;

import java.util.Arrays;
import java.util.List;

import net.danielgill.ros.timetable.event.CdtEvent;
import net.danielgill.ros.timetable.event.Event;
import net.danielgill.ros.timetable.event.FerEvent;
import net.danielgill.ros.timetable.event.FnsEvent;
import net.danielgill.ros.timetable.event.FnshsEvent;
import net.danielgill.ros.timetable.event.FnsshEvent;
import net.danielgill.ros.timetable.event.FrhEvent;
import net.danielgill.ros.timetable.event.FrhshEvent;
import net.danielgill.ros.timetable.event.FspEvent;
import net.danielgill.ros.timetable.event.JboEvent;
import net.danielgill.ros.timetable.event.PassEvent;
import net.danielgill.ros.timetable.event.RspEvent;
import net.danielgill.ros.timetable.event.SfsEvent;
import net.danielgill.ros.timetable.event.SnsEvent;
import net.danielgill.ros.timetable.event.SnsfshEvent;
import net.danielgill.ros.timetable.event.SnsshEvent;
import net.danielgill.ros.timetable.event.SntEvent;
import net.danielgill.ros.timetable.event.SntshEvent;
import net.danielgill.ros.timetable.event.StopEvent;

public class EventRegistry {
    public static final List<Event> EVENTS_LIST = Arrays.asList(
        new SntEvent(null, null),
        new SnsEvent(null, null),
        new SfsEvent(null, null),
        new StopEvent(null, null, null),
        new PassEvent(null, null),
        new CdtEvent(null),
        new JboEvent(null, null),
        new FerEvent(null, null),
        new FnsEvent(null, null),
        new FrhEvent(),
        new FspEvent(null, null),
        new RspEvent(null, null),
        new SntshEvent(null, null, null),
        new SnsshEvent(null, null, null),
        new SnsfshEvent(null, null),
        new FnsshEvent(null, null, null),
        new FrhshEvent(null, null),
        new FnshsEvent(null, null)
    );
}
