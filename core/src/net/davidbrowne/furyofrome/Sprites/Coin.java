package net.davidbrowne.furyofrome.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;

import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;


public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private AssetManager manager;
    //tiled property id in tiled + 1
    public Coin(PlayScreen screen, MapObject object, AssetManager manager) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tiles");
        this.manager = manager;
        fixture.setUserData(this);
        fixture.setSensor(true);
        setCategoryFilter(Game.COIN_BIT);
    }

    @Override
    public void onHit() {
        setCategoryFilter(Game.DESTROYED_BIT);
        getObjectCell().setTile(null);
        //manager.get("audio/sounds/coin.wav", Sound.class).play(screen.getGame().getSoundVolume());
        screen.getPlayer().addGold(5);
    }

    @Override
    public void onHeadHit() {

    }

}
