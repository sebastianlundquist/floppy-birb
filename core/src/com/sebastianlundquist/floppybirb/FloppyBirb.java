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

	@Override
	public void create () {
		batch = new SpriteBatch();

	}

	@Override
	public void render () {
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
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(birds[flopState],Gdx.graphics.getWidth() / 2f - birds[flopState].getWidth() / 2f, Gdx.graphics.getHeight() / 2f - birds[flopState].getHeight() / 2f);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
