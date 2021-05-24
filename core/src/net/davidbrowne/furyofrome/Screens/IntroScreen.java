package net.davidbrowne.furyofrome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.furyofrome.Game;


public class IntroScreen implements Screen {
    private Game game;
    private AssetManager manager;
    public Stage stage;
    private Texture background;
    private Viewport viewport;
    public IntroScreen(final Game game, final AssetManager manager) {
        this.game=game;
        this.manager=manager;
        background=new Texture(Gdx.files.internal("devbg.png"));
        viewport = new StretchViewport(Game.V_WIDTH,Game.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,game.batch);
        Gdx.input.setInputProcessor(stage);
        Timer timer = new Timer();
        Timer.Task task = timer.schedule(new Timer.Task() {
            @Override
            public void run () {
                game.setScreen(new MainMenuScreen(game,manager));
                dispose();
            }
        }, 2f);


    }

    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background,0,0, Game.V_WIDTH,game.V_HEIGHT);
        game.batch.end();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        background.dispose();
    }
}
