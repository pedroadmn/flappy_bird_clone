package pedroadmn.flappybirdclone.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture bird;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		bird = new Texture("passaro1.png");
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(bird, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
	}
}
