package com.mygdx.game.Box2D;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy {

    Body enemy;
    Batch b;
    World w;
    Animation<TextureRegion> enemyAnimation;
    boolean isDestroyed = false;

    public Enemy(Batch batch, World world, Animation<TextureRegion> animation, float x, float y) {

        //DEFINE BODY
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));//pixel coords work if use ortho camera 50x50
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        //CREATE BODY
        enemy = world.createBody(bodyDef);

        //CREATE SHAPE
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(5, 5);

        //CREATE FIXTURE
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        //ATTACH FIXTURE TO BODY
        enemy.createFixture(fixtureDef);

        b = batch;
        w = world;
        enemyAnimation = animation;

        shape.dispose();
    }

    public void display(float elapsedTime) {
        if(isDestroyed) {
            return;
        }

        if(enemy.getLinearVelocity().x != 0 || enemy.getLinearVelocity().y != 0) {
            w.destroyBody(enemy);
            isDestroyed = true;
            return;
        }

        enemy.setTransform(new Vector2((float)(enemy.getPosition().x - .1), enemy.getPosition().y), 0);

        float enemyPosX = enemy.getPosition().x;
        float enemyPosY = enemy.getPosition().y;

        b.draw(enemyAnimation.getKeyFrame(elapsedTime, true), enemyPosX, enemyPosY, 5, 5);
    }
}
