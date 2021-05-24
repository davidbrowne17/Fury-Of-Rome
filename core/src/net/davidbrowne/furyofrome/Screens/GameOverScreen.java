package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Game;


public class GameOverScreen implements Screen {
    private Game game;
    private int V_WIDTH=1600,V_HEIGHT=1200,level;
    private AssetManager manager;
    private Texture background;
    public Stage stage;
    private Viewport viewport;
    private Skin mySkin;
    private Label gameOver;
    public GameOverScreen(final Game game, final AssetManager manager, final int level) {
        this.game=game;
        this.manager=manager;
        this.level=level;
        game.music.stop();
        Gdx.input.setCursorCatched(false);
        background=new Texture(Gdx.files.internal("background.png"));
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        viewport = new StretchViewport(V_WIDTH,V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        game.music = manager.get("audio/music/gameover.wav", Music.class);
        game.music.setLooping(false);
        game.music.setVolume(game.getVolume());
        game.music.play();
        gameOver = new Label(String.format(" Game Over! "), mySkin);
        gameOver.setFontScale(3f);
        Button button = new TextButton("Retry",mySkin,"default");
        button.setTransform(true);
        button.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.music.stop();
                game.setScreen(new PlayScreen(game,manager,level));
                dispose();
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        Button button1 = new TextButton("Main Menu",mySkin,"default");
        button1.setTransform(true);
        button1.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.music.stop();
                game.setScreen(new MainMenuScreen(game,manager));
                dispose();
                manager.get("audio/sounds/click.ogg", Sound.class).play(game.getSoundVolume());
                return true;
            }
        });
        Window window = new Window("",mySkin);
        window.add(gameOver);
        table.add(window).center().expandX().padTop(100);
        table.row();
        table.add(button).center().padTop(200).expandX();
        table.row();
        table.add(button1).center().padTop(50).expandX();
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