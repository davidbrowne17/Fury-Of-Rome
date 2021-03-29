package net.davidbrowne.furyofrome.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private AssetManager manager;
    //tiled property id in tiled + 1
    private final int BLUE_SKY =6;

    public Brick(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        this.manager=manager;
        this.map = screen.getMap();
        tileSet = map.getTileSets().getTileSet("tiles");
        fixture.setUserData(this);
        setCategoryFilter(Game.BRICK_BIT);
    }

    @Override
    public void onHit() {

    }

    @Override
    public void onHeadHit() {

    }
}
