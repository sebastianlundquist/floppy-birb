package com.sebastianlundquist.floppybirb;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FloppyBirb extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flopState = 0;
	int pause = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight() / 2f - birds[flopState].getHeight() / 2f;
	}

	@Override
	public void render () {
		if (gameState != 0) {
			if (Gdx.input.justTouched()) {
				velocity = -30;
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
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(birds[flopState],Gdx.graphics.getWidth() / 2f - birds[flopState].getWidth() / 2f, birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
