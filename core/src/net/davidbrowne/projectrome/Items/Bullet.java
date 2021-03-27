package net.davidbrowne.projectrome.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import net.davidbrowne.projectrome.Game;
import net.davidbrowne.projectrome.Screens.PlayScreen;
import net.davidbrowne.projectrome.Sprites.Player;


public class Bullet extends Item {
    private Array<TextureRegion> frames;
    private Animation<TextureRegion> fireAnimation;
    private float stateTime;
    private boolean destroyed;
    private boolean setToDestroy;
    private boolean fireRight;
    public Bullet(PlayScreen screen, float x, float y, boolean fireRight){
        super(screen, x, y);
        this.fireRight = fireRight;
        frames = new Array<TextureRegion>();
        frames.add(screen.getAtlas().findRegion("axe"));
        fireAnimation = new Animation(0.2f, frames);
        setRegion(fireAnimation.getKeyFrame(0));
        setBounds(x, y, 10 /  Game.PPM, 10 / Game.PPM);
    }

    @Override
    public void defineItem() {
        /*
        BodyDef bdef = new BodyDef();
        bdef.position.set(fireRight ? getX() + 0.1f / Game.PPM : getX() - 0.1f / Game.PPM, getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        if(!screen.getWorld().isLocked())
            body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / Game.PPM);
        fdef.filter.categoryBits = Game.BULLET_BIT;
        fdef.filter.maskBits = Game.GROUND_BIT |
                Game.BRICK_BIT |
                Game.ENEMY_BIT |
                Game.BOX_BIT;
        fdef.shape = shape;
        fdef.restitution = 0f;
        fdef.friction = 0f;
        body.createFixture(fdef).setUserData(this);
        body.setGravityScale(0);
        shape.dispose();

         */
    }

    @Override
    public void use(Player player) {

    }

    @Override
    public void update(float dt){
        super.update(dt);
        if(!destroyed){
        stateTime += dt;
        setRegion(fireAnimation.getKeyFrame(stateTime, true));
        if(body!=null){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        if(!fireRight)
            body.setLinearVelocity(new Vector2(2.3f, 0f));
        else
            body.setLinearVelocity(new Vector2(-2.3f, 0f));

        if( fireRight&& !isFlipX()){
            flip(true, false);
        }
        else if(!fireRight &&isFlipX()){
            flip(true, false);
        }
        }
        if(stateTime > 1f||(setToDestroy) && !destroyed) {

            destroyed = true;
            float delay = 0.6f;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    screen.getPlayer().setCanFire(true);
                }
            }, delay);

        }
        }

    }

    public void setToDestroy(){
        setToDestroy = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}