package com.mygdx.wanderingpaw.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.wanderingpaw.handlers.Animation;
import com.mygdx.wanderingpaw.handlers.B2DVars;
import com.mygdx.wanderingpaw.states.Play;


/**
 * Attaches animated sprites to box2d bodies
 */
public class B2DSprite {

    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;

    public B2DSprite(Body body) {
        this.body = body;
        animation = new Animation();
    }

    public void setAnimation(TextureRegion reg, float delay) {

        setAnimation(new TextureRegion[]{reg}, delay);
    }

    public void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update(float dt) {

        animation.update(dt);
    }

    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(animation.getFrame(),
                (body.getPosition().x * B2DVars.PPM - width / 2),
                (int) (body.getPosition().y * B2DVars.PPM - height / 2));
        sb.end();
    }

    //customized rendering for player
    public void playerRender(SpriteBatch sb) {
        if (Play.right) {
            sb.begin();
            sb.draw(animation.getFrame(),
                    (body.getPosition().x * B2DVars.PPM - width / 2),
                    (int) (body.getPosition().y * B2DVars.PPM - height / 2));
            sb.end();
        } else if(Play.left) {
            sb.begin();
            sb.draw(animation.getFrame(), (float) (Play.left ? ((body.getPosition().x * B2DVars.PPM - width / 2)+width) : (body.getPosition().x * B2DVars.PPM - width / 2)), (float) (body.getPosition().y * B2DVars.PPM - width / 2), Play.left ? -width : width, height);

            sb.end();
        }
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

}