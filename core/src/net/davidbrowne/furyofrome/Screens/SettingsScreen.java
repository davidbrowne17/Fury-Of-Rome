package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Game;


public class SettingsScreen implements Screen {
    private Game game;
    private int V_WIDTH=1600,V_HEIGHT=1200;
    private AssetManager manager;
    private Texture background;
    public Stage stage;
    private Viewport viewport;
    private Skin mySkin;
    private Slider slider,slider1;
    private Label volume,volume1;
    public SettingsScreen(final Game game, final AssetManager manager) {
        this.game=game;
        this.manager=manager;
        background=new Texture(Gdx.files.internal("background.png"));
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(V_WIDTH,V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        volume = new Label(String.format("Music volume: "), mySkin);
        volume.setFontScale(3f);
        slider = new Slider(0f,1f,0.01f,false,mySkin);
        slider.setValue(game.getVolume());
        slider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!slider.isDragging()){
                    game.setVolume(slider.getValue());
                    manager.get("audio/sounds/coin.wav", Sound.class).play(game.getVolume());
                    game.music.setVolume(game.getVolume());
                }
            }
        });

        volume1 = new Label(String.format("Sounds volume: "), mySkin);
        volume1.setFontScale(3f);
        slider1 = new Slider(0f,1f,0.01f,false,mySkin);
        slider1.setValue(game.getSoundVolume());
        slider1.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!slider1.isDragging()){
                    game.setSoundVolume(slider1.getValue());
                    manager.get("audio/sounds/coin.wav", Sound.class).play(game.getSoundVolume());
                }
            }
        });
        Container<Slider> container=new Container<Slider>(slider);
        container.setTransform(true);   // for enabling scaling and rotation
        container.size(300, 100);
        container.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container.setScale(1);  //scale according to your requirement

        Container<Slider> container1=new Container<Slider>(slider1);
        container1.setTransform(true);   // for enabling scaling and rotation
        container1.size(300, 100);
        container1.setOrigin(container.getWidth() / 2, container.getHeight() / 2);
        container1.setScale(1);  //scale according to your requirement

        Button button = new TextButton("Main Menu",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MainMenuScreen(game,manager));
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                dispose();
                return true;
            }
        });
        Window window = new Window("",mySkin);
        window.add(volume);
        Window window1 = new Window("",mySkin);
        window1.add(volume1);
        final CheckBox checkBox = new CheckBox(" Fullscreen ",mySkin);
        checkBox.setChecked(Gdx.graphics.isFullscreen());
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                Gdx.graphics.setContinuousRendering(checkBox.isChecked());
                Boolean fullScreen = Gdx.graphics.isFullscreen();
                Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
                if (fullScreen)
                    Gdx.graphics.setWindowedMode(currentMode.width/2, currentMode.height/2);
                else
                    Gdx.graphics.setFullscreenMode(currentMode);
            }
        });
        Window window3 = new Window("",mySkin);
        window3.add(checkBox);
        table.add(window).center().expandY();
        table.add(container);
        table.row();
        table.add(window1).center().expandY();
        table.add(container1);
        table.row();
        if(Gdx.app.getType()!= Application.ApplicationType.Android){
            table.add(window3).center();
            table.row();
        }
        table.add(button).center().padTop(100).expandY().padLeft(220);
        stage.addActor(table);

    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0, V_WIDTH,V_HEIGHT);
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        mySkin.dispose();
    }
}
