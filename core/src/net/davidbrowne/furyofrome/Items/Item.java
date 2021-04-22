package net.davidbrowne.furyofrome.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Screens.PlayScreen;
import net.davidbrowne.furyofrome.Sprites.Player;


public abstract class Item extends Sprite {
    private static PlayScreen playScreen;
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    public Body body;

    public Item(PlayScreen screen , float x, float y){
        this.screen= screen;
        playScreen=screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        setBounds(getX(),getY(),10/ Game.PPM,10 / Game.PPM);
        defineItem();
        toDestroy=false;
        destroyed=false;
    }
    public void reverseVelocity(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }

    public abstract void defineItem();

    public boolean isDestroyed() {
        return destroyed;
    }

    public abstract void use(Player player);

    public void update(float dt){
    }
    public void destroy(){
        toDestroy=true;
    }


    /**
     * Safe way to remove body from the world. Remember that you cannot have any
     * references to this body after calling this
     *
     * @param body
     *            that will be removed from the physic world
     */

    public static void removeBodySafely(Body body) {
        //to prevent some obscure c assertion that happened randomly once in a blue moon
        if(body != null){
        final Array<JointEdge> list = body.getJointList();
        while (list.size > 0) {
            playScreen.getWorld().destroyJoint(list.get(0).joint);
        }
        // actual remove
        playScreen.getWorld().destroyBody(body);
        }
    }


    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

}
