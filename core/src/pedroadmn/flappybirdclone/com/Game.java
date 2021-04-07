package pedroadmn.flappybirdclone.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {

	private int xMoviment = 0;
	private int yMoviment = 0;
	private SpriteBatch batch;
	private Texture bird;
	private Texture background;

	// Config
	private float deviceWidth;
	private float deviceHeight;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bird = new Texture("passaro1.png");
		background = new Texture("fundo.png");

		deviceWidth = Gdx.graphics.getWidth();
		deviceHeight = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, deviceWidth, deviceHeight);
		xMoviment++;
		yMoviment++;
		batch.draw(bird, xMoviment, 500);
		batch.end();
	}
	
	@Override
	public void dispose () {
	}
}
