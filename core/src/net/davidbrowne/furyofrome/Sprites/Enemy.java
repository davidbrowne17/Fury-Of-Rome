package net.davidbrowne.furyofrome.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Timer;

import net.davidbrowne.furyofrome.Screens.PlayScreen;


public abstract class Enemy extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;
    protected boolean destroyed;
    private boolean attacking;
    private boolean setToDestroy;
    public Enemy (PlayScreen screen,float x, float y){
        this.screen = screen;
        world = screen.getWorld();
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(0,0);
        b2body.setActive(false);
    }
    public void Attack(){

        attacking = true;
       // screen.getManager().get("audio/sounds/swing.mp3", Sound.class).play(screen.getGame().getSoundVolume());
        if(!setToDestroy&&!destroyed)
            screen.getPlayer().die();
        Timer timer = new Timer();
        if (attacking) {
            Timer.Task task1 = timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    attacking = false;
                }
            }, 0.5f);


        }
    }
    public abstract void update(float dt);
    protected abstract void defineEnemy();
    public abstract void hitOnHead();
    public abstract void OnHit();
    public void reverseVelocity(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }
}
