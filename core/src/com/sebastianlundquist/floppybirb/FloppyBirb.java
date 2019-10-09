package com.sebastianlundquist.floppybirb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private Texture gameOver;

	private Circle birdCircle;
	private Rectangle[] topTubeRectangles;
	private Rectangle[] bottomTubeRectangles;
	private BitmapFont font;
	private Random random;

	private int gameState = 0;
	private int flopState = 0;
	private int pause = 0;
	private int gap = 400;
	private int numberOfTubes = 4;
	private int maxTubeOffset;
	private int score = 0;
	private int scoringTube;
	private int[] tubeOffsets = new int[numberOfTubes];

	private float birdY = 0;
	private float velocity = 0;
	private float[] tubeX = new float[numberOfTubes];
	private float distanceBetweenTubes;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("gameover.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		gap = Gdx.graphics.getHeight() / 8;
		maxTubeOffset = gap;
		random = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() / 2f;
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(12);
		startGame();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2f) {
				score++;
				Gdx.app.log("Score", String.valueOf(score));
				if (scoringTube < numberOfTubes - 1)
					scoringTube++;
				else
					scoringTube = 0;
			}
			if (Gdx.input.justTouched())
				velocity = -30;
			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffsets[i] = random.nextInt(maxTubeOffset);
				}
				else {
					tubeX[i] -= 12;
				}
				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2f + gap + tubeOffsets[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2f - bottomTube.getHeight() - gap + tubeOffsets[i]);
				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2f + gap + tubeOffsets[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2f - bottomTube.getHeight() - gap + tubeOffsets[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (birdY > 0) {
				velocity += 2;
				birdY -= velocity;
			}
			else {
				gameState = 2;
			}

		}
		else if (gameState == 0) {
			if (Gdx.input.justTouched())
				gameState = 1;
		}
		else if (gameState == 2) {
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2f - gameOver.getWidth() / 2f, 3 * Gdx.graphics.getHeight() / 4f - gameOver.getHeight() / 2f);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				score = 0;
				scoringTube = 0;
				velocity = 0;
				startGame();
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
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2f, birdY + birds[flopState].getHeight() / 2f, birds[flopState].getWidth() / 2f);
		for (int i = 0; i < numberOfTubes; i++) {
			if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i]))
				gameState = 2;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}

	private void startGame() {
		birdY = Gdx.graphics.getHeight() / 2f - birds[flopState].getHeight() / 2f;
		for (int i = 0; i < numberOfTubes; i++) {
			tubeOffsets[i] = random.nextInt(maxTubeOffset);
			tubeX[i] = Gdx.graphics.getWidth() / 2f - topTube.getWidth() / 2f + + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}
}
