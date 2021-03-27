package net.davidbrowne.projectrome.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import net.davidbrowne.projectrome.Screens.PlayScreen;


public class Hud implements Disposable {
    private int width;
    private final int totalHealth=100;
    private final int totalBarWidth=50;
    private Texture heart;
    private Texture emptyHeart;
    public Stage stage;
    private Viewport viewport;
    private int V_WIDTH=600,V_HEIGHT=300;
    public static int score=0;
    private Preferences prefs;
    private static Label scoreLabel;
    private Label axeLabel;
    private Skin mySkin;
    private PlayScreen screen;

    public Hud(SpriteBatch sb, PlayScreen screen){
        this.screen=screen;
        prefs = Gdx.app.getPreferences("RomeGamePrefs");
        score = prefs.getInteger("score");
        mySkin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        heart = new Texture(Gdx.files.internal("gradient.png"));
        viewport = new FitViewport(V_WIDTH, V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        scoreLabel = new Label(String.format("Score: %04d",screen.getPlayer().getScore()), mySkin);
        scoreLabel.setFontScale(0.5f);
        axeLabel = new Label(String.format("Health: %03d", screen.getPlayer().getHealth()), mySkin);
        axeLabel.setFontScale(0.5f);
        width = screen.getPlayer().getHealth() / totalHealth * totalBarWidth;
        table.add(scoreLabel).center().padTop(10).padRight(20);
        table.add(axeLabel).center().padTop(10);
        stage.addActor(table);

    }

    public void update(float dt){
        axeLabel.setText(String.format("Health: %03d", screen.getPlayer().getHealth()));
        scoreLabel.setText(String.format("Score: %04d", screen.getPlayer().getScore()));

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
