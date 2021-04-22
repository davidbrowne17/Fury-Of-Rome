package net.davidbrowne.furyofrome.Events;

import net.davidbrowne.furyofrome.Screens.PlayScreen;

public class Event01 extends Event {

    public Event01() {
        setEventId(1);
    }

    @Override
    public void run(PlayScreen screen) {
        screen.getPlayer().setLevel(screen.getPlayer().getLevel()+1);
    }
}
