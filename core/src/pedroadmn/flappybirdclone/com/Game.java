package pedroadmn.flappybirdclone.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {

	private int xMoviment = 0;
	private int yMoviment = 0;
	private SpriteBatch batch;
	private Texture[] birds;
	private Texture background;

	// Config
	private float deviceWidth;
	private float deviceHeight;
	private float variation = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		birds = new Texture[3];
		birds[0] = new Texture("passaro1.png");
		birds[1] = new Texture("passaro2.png");
		birds[2] = new Texture("passaro3.png");

		background = new Texture("fundo.png");

		deviceWidth = Gdx.graphics.getWidth();
		deviceHeight = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {
		batch.begin();

		if (variation > 3) {
			variation = 0;
		}

		batch.draw(background, 0, 0, deviceWidth, deviceHeight);
		batch.draw(birds[(int)variation], 30, deviceHeight / 2);

		variation += Gdx.graphics.getDeltaTime() * 10;
		xMoviment++;
		yMoviment++;
		batch.end();
	}
	
	@Override
	public void dispose () {
	}
}
