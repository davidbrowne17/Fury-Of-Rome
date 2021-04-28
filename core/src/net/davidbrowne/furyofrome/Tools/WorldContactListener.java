package net.davidbrowne.furyofrome.Tools;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import net.davidbrowne.furyofrome.Game;
import net.davidbrowne.furyofrome.Items.Bullet;
import net.davidbrowne.furyofrome.Items.Item;
import net.davidbrowne.furyofrome.Sprites.Box;
import net.davidbrowne.furyofrome.Sprites.Coin;
import net.davidbrowne.furyofrome.Sprites.Enemy;
import net.davidbrowne.furyofrome.Sprites.InteractiveTileObject;
import net.davidbrowne.furyofrome.Sprites.LevelEnd;
import net.davidbrowne.furyofrome.Sprites.Player;
import net.davidbrowne.furyofrome.Sprites.Spear;
import net.davidbrowne.furyofrome.Sprites.Spike;


public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData()=="head"||fixB.getUserData()=="head"){
            Fixture head = fixA.getUserData()=="head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            if(object.getUserData()!=null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onHeadHit();
            }
        }
        if(fixA.getUserData()!=null && Coin.class.isAssignableFrom(fixA.getUserData().getClass())){
            ((Coin)fixA.getUserData()).onHit();
        }else if(fixB.getUserData()!=null && Coin.class.isAssignableFrom(fixB.getUserData().getClass())){
            ((Coin)fixB.getUserData()).onHit();
        }
        enemyCollisions(fixA,fixB,cDef);
        itemCollisions(fixA,fixB,cDef);
        levelEndCollision(fixA,fixB,cDef);
    }

    public void levelEndCollision(Fixture fixA, Fixture fixB, int cDef){
        switch(cDef){
            case Game.LEVEL_END_BIT | Game.PLAYER_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.LEVEL_END_BIT) {
                        ((LevelEnd)(fixA.getUserData())).onHit();
                    }
                    else {
                        ((LevelEnd)(fixB.getUserData())).onHit();
                    }
                }
                break;
            default:
                break;
        }
    }

    public void itemCollisions(Fixture fixA, Fixture fixB, int cDef){
        switch(cDef){
            case Game.BULLET_BIT | Game.BRICK_BIT:
            case Game.BULLET_BIT | Game.BOX_BIT:
                if(fixA.getFilterData().categoryBits==Game.BULLET_BIT)
                    ((Bullet)(fixA.getUserData())).destroy();
                else
                    ((Bullet)(fixB.getUserData())).destroy();
                break;
            case Game.ITEM_BIT | Game.BRICK_BIT:
            case Game.ITEM_BIT | Game.BOX_BIT:
                if(fixA.getFilterData().categoryBits==Game.ITEM_BIT)
                    ((Item)(fixA.getUserData())).reverseVelocity(true,false);
                else
                    ((Item)(fixB.getUserData())).reverseVelocity(true,false);
                break;
            case Game.ITEM_BIT | Game.PLAYER_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.ITEM_BIT)
                        ((Item)(fixA.getUserData())).use((Player) fixB.getUserData());
                    else
                        ((Item)(fixB.getUserData())).use((Player) fixA.getUserData());
                }
                break;
            case Game.BOX_BIT | Game.ATTACK_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.BOX_BIT)
                        ((Box)(fixA.getUserData())).onHit();
                    else
                        ((Box)(fixB.getUserData())).onHit();
                }
                break;
            case Game.SPEAR_BIT | Game.PLAYER_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.SPEAR_BIT)
                        ((Spear)(fixA.getUserData())).onHit();
                    else
                        ((Spear)(fixB.getUserData())).onHit();
                }
                break;
            case Game.SPIKE_BIT | Game.PLAYER_BIT:
                if(fixA.getUserData()!= "head"&&fixB.getUserData()!= "head"){
                    if(fixA.getFilterData().categoryBits==Game.SPIKE_BIT)
                        ((Spike)(fixA.getUserData())).onHit();
                    else
                        ((Spike)(fixB.getUserData())).onHit();
                }
                break;

    }
}

    public void enemyCollisions(Fixture fixA, Fixture fixB, int cDef){
        switch(cDef){
            case Game.ENEMY_BIT | Game.PLAYER_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT)
                    ((Enemy)(fixA.getUserData())).hitOnHead();
                else
                    ((Enemy)(fixB.getUserData())).hitOnHead();
                break;
            case Game.NPC_BIT | Game.INTERACT_BIT:
                if(fixA.getFilterData().categoryBits==Game.NPC_BIT)
                    ((Enemy)(fixA.getUserData())).hitOnHead();
                else
                    ((Enemy)(fixB.getUserData())).hitOnHead();
                break;
            case Game.ENEMY_BIT | Game.BLOCK_BIT:
            case Game.ENEMY_BIT | Game.BOX_BIT:
            case Game.ENEMY_BIT | Game.BRICK_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT)
                    ((Enemy)(fixA.getUserData())).reverseVelocity(true,false);
                else
                    ((Enemy)(fixB.getUserData())).reverseVelocity(true,false);
                break;
            case Game.ENEMY_BIT | Game.ENEMY_BIT:
                ((Enemy)(fixA.getUserData())).reverseVelocity(true,false);
                ((Enemy)(fixB.getUserData())).reverseVelocity(true,false);
                break;
            case Game.ATTACK_BIT | Game.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT){
                    ((Enemy)(fixA.getUserData())).OnHit();
                }
                else{
                    ((Enemy)(fixB.getUserData())).OnHit();
                }
                break;
            case Game.BULLET_BIT | Game.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits==Game.ENEMY_BIT){
                    ((Enemy)(fixA.getUserData())).OnHit();
                    ((Bullet)(fixB.getUserData())).destroy();}
                else{
                    ((Bullet)(fixA.getUserData())).destroy();
                    ((Enemy)(fixB.getUserData())).OnHit();
                }
            default:
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
