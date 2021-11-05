package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Axe {

    private Body axe;
    private Batch b;
    private Animation<TextureRegion> axeAnimation;

    public Axe(Batch batch, World world, Animation<TextureRegion> animation, float x, float y) {

        //DEFINE BODY
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));//pixel coords work if use ortho camera 50x50
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //CREATE BODY
        axe = world.createBody(bodyDef);

        //CREATE SHAPE
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(.005f);

        //CREATE FIXTURE
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = .1f;
        fixtureDef.restitution = .8f;
        fixtureDef.friction = .5f;
        fixtureDef.shape = circleShape;

        //ATTACH FIXTURE TO BODY
        axe.createFixture(fixtureDef);

        b = batch;
        axeAnimation = animation;

        circleShape.dispose();
    }

    public void display(float elapsedTime) {
        b.draw(axeAnimation.getKeyFrame(elapsedTime, true), axe.getPosition().x, axe.getPosition().y, 5, 5);
    }

    public void throwAxe(float forceX, float forceY) {
        axe.applyForceToCenter(forceX, forceY, true);
    }
}
