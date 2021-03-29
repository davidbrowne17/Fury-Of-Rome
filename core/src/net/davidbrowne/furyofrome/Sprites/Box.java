package net.davidbrowne.furyofrome.Sprites;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;


public class Box extends InteractiveTileObject {
    private AssetManager manager;
    public Box(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        this.manager = manager;
        fixture.setUserData(this);
        setCategoryFilter(Game.BOX_BIT);
    }

    @Override
    public void onHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        if(!screen.getWorld().isLocked())
            screen.getWorld().destroyBody(fixture.getBody());
        getObjectCell().setTile(null);
        screen.getManager().get("audio/sounds/break.wav", Sound.class).play(screen.getGame().getSoundVolume());
    }

    @Override
    public void onHeadHit() {


    }
}
