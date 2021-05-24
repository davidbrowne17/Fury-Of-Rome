package net.davidbrowne.furyofrome;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import net.davidbrowne.furyofrome.Screens.IntroScreen;
import net.davidbrowne.furyofrome.Screens.MainMenuScreen;
import net.davidbrowne.furyofrome.Screens.PlayScreen;
import net.davidbrowne.furyofrome.Tools.AdHandler;

public class Game extends com.badlogic.gdx.Game implements GestureDetector.GestureListener {
	public static SpriteBatch batch;
	public static int level=1;
	public static final int V_WIDTH = 350;
	public static final int V_HEIGHT = 140;
	public static final float PPM = 100;
	public static final short GROUND_BIT =1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT=4;
	public static final short BOX_BIT=8;
	public static final short DESTROYED_BIT=16;
	public static final short ATTACK_BIT=32;
	public static final short ENEMY_BIT = 64;
	public static final short BULLET_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short BLOCK_BIT = 512;
	public static final short NPC_BIT = 1024;
	public static final short INTERACT_BIT = 2048;
	public static final short LEVEL_END_BIT = 4096;
	public static final short COIN_BIT = 8192;
	public static final short SPEAR_BIT = 16384;
	public static final short SPIKE_BIT = (short) 32768;
	public static final short NPC_DETECTOR_BIT = (short) 32769;
	private GestureDetector gestureDetector;
	private float volume= 0.5f;
	private float soundVolume=0.5f;
	public boolean doubleTapped=false;
	public boolean swippedUp=false;
	public AssetManager manager;
	AdHandler handler;
	public Music music;
	boolean toggle = true;

	public Game(AdHandler handler) {
		this.handler = handler;
		handler.showAds(true);
	}
	public Game() {

	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/sounds/click.ogg", Sound.class);
		manager.load("audio/sounds/splat.wav", Sound.class);
		manager.load("audio/sounds/coin.wav", Sound.class);
		manager.load("audio/sounds/swing.mp3", Sound.class);
		manager.load("audio/music/gameover.wav", Music.class);
		manager.load("audio/music/music1.mp3", Music.class);
		manager.load("audio/music/music2.mp3", Music.class);
		manager.finishLoading();
		setScreen(new IntroScreen(this,manager));
		gestureDetector = new GestureDetector(20, 40, 0.5f, 2, 0.15f, this);
		Gdx.input.setInputProcessor(gestureDetector);

	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public void resetInputProcessor(){
		Gdx.input.setInputProcessor(gestureDetector);
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}

	@Override
	public void render () {
		super.render();
		System.out.println("FPS: "+Gdx.graphics.getFramesPerSecond());
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (count == 2) {
			System.out.println("Double tap!");
			doubleTapped=true;
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(Math.abs(velocityX)>Math.abs(velocityY)){
			if(velocityX>0){
				//swiped right
				return false;
			}else{
				//swiped left
				return false;
			}
		}else{
			if(velocityY>0){
				//swiped down
				return false;
			}else{
				//swiped up
				swippedUp=true;
				return true;
			}
		}
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {

	}
}