package net.davidbrowne.furyofrome.Sprites;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;


public class LevelEnd extends InteractiveTileObject {
    private AssetManager manager;
    public LevelEnd(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        this.manager=manager;
        this.map = screen.getMap();
        fixture.setDensity(0);
        fixture.setUserData(this);
        setCategoryFilter(Game.LEVEL_END_BIT);
    }

    @Override
    public void onHit() {
        screen.finishLevel();
    }

    @Override
    public void onHeadHit() {

    }
}
