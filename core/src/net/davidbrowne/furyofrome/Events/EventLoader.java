package net.davidbrowne.furyofrome.Events;

import java.util.ArrayList;

public class EventLoader {
    private ArrayList<Event> events;
    //loads all the events so they can be traversed and used with the script that has the corresponding id
    public EventLoader(){
        events = new ArrayList<Event>();
        events.add(new Event01());
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
