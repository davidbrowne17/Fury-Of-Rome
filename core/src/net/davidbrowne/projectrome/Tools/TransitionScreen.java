package net.davidbrowne.projectrome.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import net.davidbrowne.projectrome.Game;

public class TransitionScreen implements Screen {
    private Screen currentScreen;
    private Screen nextScreen;

    private Game game;

    // Once this reaches 1.0f the next scene is shown
    private float alpha = 0;
    // true if fade in, false if fade out
    private boolean fadeDirection = true;
    private ShapeRenderer shapeRenderer;

    public TransitionScreen(Screen current, Screen next, Game game) {
        this.currentScreen = current;
        this.nextScreen = next;

        // I temporarily change the screen to next because if I call render() on it without calling the create() function
        // there will be crashed caused by using null variables
        game.setScreen(next);
        game.setScreen(current);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        if (fadeDirection == true) {
            currentScreen.render(Gdx.graphics.getDeltaTime());
        } else {
            nextScreen.render(Gdx.graphics.getDeltaTime());
        }

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        if (alpha >= 1) {
            fadeDirection = false;
        }
        else if (alpha <= 0 && fadeDirection == false) {
            game.setScreen(nextScreen);
            dispose();
        }
        alpha += fadeDirection == true ? 0.01 : -0.01;
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
        shapeRenderer.dispose();
        currentScreen.dispose();
    }
}