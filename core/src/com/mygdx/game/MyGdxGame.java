package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MyGdxGame extends ApplicationAdapter {

	OrthographicCamera camera;
	ExtendViewport viewport;
	TextureAtlas axeAtlas;
	Animation<TextureRegion> axeAnimation;
	World world;
	Axe axe;
	float elapsedTime = 0f;
	SpriteBatch batch;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(50, 50, camera);

		axeAtlas = new TextureAtlas(Gdx.files.internal("axe.atlas"));
		axeAnimation = new Animation<TextureRegion>(.075f, axeAtlas.getRegions());

		batch = new SpriteBatch();

		Box2D.init();
		world = new World(new Vector2(-10, 0), true);
		axe = new Axe(batch, world, axeAnimation, 50, 25);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		world.step(1f / 60f, 8 ,3);//step physics engine

		elapsedTime += Gdx.graphics.getDeltaTime();

		batch.begin();

		axe.display(elapsedTime);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}
}
