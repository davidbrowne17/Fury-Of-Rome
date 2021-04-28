package net.davidbrowne.furyofrome.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;


public class Spear extends InteractiveTileObject {
    private AssetManager manager;
    public Spear(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        this.manager = manager;
        fixture.setUserData(this);
        fixture.setSensor(true);
        setCategoryFilter(Game.SPEAR_BIT);
    }

    @Override
    public void onHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        //screen.getManager().get("audio/sounds/bop.wav", Sound.class).play(screen.getGame().getSoundVolume());
        if(!screen.getWorld().isLocked())
            screen.getWorld().destroyBody(fixture.getBody());
        getObjectCell().setTile(null);
        screen.getPlayer().setSpears(screen.getPlayer().getSpears()+5);

    }

    @Override
    public void onHeadHit() {


    }
}
