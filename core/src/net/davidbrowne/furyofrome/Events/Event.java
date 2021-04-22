package net.davidbrowne.furyofrome.Events;

import net.davidbrowne.furyofrome.Screens.PlayScreen;

public abstract class Event {
    private int eventId;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public abstract void run(PlayScreen screen);
}
