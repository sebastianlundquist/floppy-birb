package com.sebastianlundquist.floppybirb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FloppyBirb extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture background;
	private Texture[] birds;
	private Texture topTube;
	private Texture bottomTube;
	private int flopState = 0;
	private int pause = 0;
	private float birdY = 0;
	private float velocity = 0;
	private int gameState = 0;
	private int gap = 400;
	private int maxTubeOffset;
	private float tubeVelocity = 12;
	private int numberOfTubes = 4;
	private float[] tubeX = new float[numberOfTubes];
	private int[] tubeOffsets = new int[numberOfTubes];
	private float distanceBetweenTubes;
	private Random random;
	private Circle birdCircle;
	private Rectangle[] topTubeRectangles;
	private Rectangle[] bottomTubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight() / 2f - birds[flopState].getHeight() / 2f;
		gap = Gdx.graphics.getHeight() / 8;
		maxTubeOffset = gap;
		random = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() / 2f;
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffsets[i] = random.nextInt(maxTubeOffset);
			tubeX[i] = Gdx.graphics.getWidth() / 2f - topTube.getWidth() / 2f + + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState != 0) {
			if (Gdx.input.justTouched()) {
				velocity = -30;
			}
			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffsets[i] = random.nextInt(maxTubeOffset);
				}
				else {
					tubeX[i] -= tubeVelocity;
				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2f + gap + tubeOffsets[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2f - bottomTube.getHeight() - gap + tubeOffsets[i]);
				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2f + gap + tubeOffsets[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2f - bottomTube.getHeight() - gap + tubeOffsets[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (birdY > 0 || velocity < 0) {
				velocity += 2;
				birdY -= velocity;
			}
			else {
				birdY = 0;
			}

		}
		else {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}
		if (pause < 8)
			pause++;
		else {
			pause = 0;
			if (flopState == 0)
				flopState = 1;
			else
				flopState = 0;
		}
		batch.draw(birds[flopState],Gdx.graphics.getWidth() / 2f - birds[flopState].getWidth() / 2f, birdY);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2f, birdY + birds[flopState].getHeight() / 2f, birds[flopState].getWidth() / 2f);
		for (int i = 0; i < numberOfTubes; i++) {
			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				Gdx.app.log("Info", "Collided");
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
