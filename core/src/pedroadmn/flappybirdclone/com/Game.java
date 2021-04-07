package pedroadmn.flappybirdclone.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture[] birds;
	private Texture bottomTube;
	private Texture topTube;
	private Texture background;

	// Config
	private float deviceWidth;
	private float deviceHeight;
	private float variation = 0;
	private float gravity = 0;
	private float initBirtVerticalPosition = 0;
	private float horizontalTubePosition;
	private float verticalTubePosition;
	private float spaceBetweenTubes;
	private Random random;
	private int scores = 0;
	private boolean passTube = false;

	// Texts
	BitmapFont scoreText;

	@Override
	public void create () {
		initTextures();
		initObjects();
	}

	@Override
	public void render () {
		verifyGameState();
		validateScore();
		drawTextures();
	}

	private void validateScore() {
		if (horizontalTubePosition < 50 - birds[0].getWidth()) {
			if (!passTube) {
				scores++;
				passTube = true;
			}
		}
	}

	private void verifyGameState() {
		horizontalTubePosition -= Gdx.graphics.getDeltaTime() * 200;

		if (horizontalTubePosition < -bottomTube.getWidth()) {
			horizontalTubePosition = deviceWidth;
			verticalTubePosition = random.nextInt(400) - 200;
			passTube = false;
		}

		boolean touchScreen = Gdx.input.justTouched();

		if (touchScreen) {
			gravity = -20;
		}

		if (initBirtVerticalPosition > 0 || touchScreen) {
			initBirtVerticalPosition = initBirtVerticalPosition - gravity;
		}


		variation += Gdx.graphics.getDeltaTime() * 10;

		if (variation > 3) {
			variation = 0;
		}

		gravity++;
	}

	private void drawTextures() {
		batch.begin();

		batch.draw(background, 0, 0, deviceWidth, deviceHeight);
		batch.draw(birds[(int)variation], 50, initBirtVerticalPosition);
		batch.draw(bottomTube, horizontalTubePosition, deviceHeight / 2 - bottomTube.getHeight() - spaceBetweenTubes / 2 + verticalTubePosition);
		batch.draw(topTube, horizontalTubePosition, (deviceHeight / 2) + spaceBetweenTubes / 2 + verticalTubePosition);

		scoreText.draw(batch, String.valueOf(scores), deviceWidth / 2, deviceHeight - 100);

		batch.end();
	}

	private void initTextures() {
		birds = new Texture[3];
		birds[0] = new Texture("passaro1.png");
		birds[1] = new Texture("passaro2.png");
		birds[2] = new Texture("passaro3.png");

		background = new Texture("fundo.png");

		bottomTube = new Texture("cano_baixo_maior.png");
		topTube = new Texture("cano_topo_maior.png");
	}

	private void initObjects() {
		batch = new SpriteBatch();
		random = new Random();

		// Texts
		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(10);

		deviceWidth = Gdx.graphics.getWidth();
		deviceHeight = Gdx.graphics.getHeight();
		initBirtVerticalPosition = deviceHeight / 2;

		horizontalTubePosition = deviceWidth;
		spaceBetweenTubes = 400;
	}
	
	@Override
	public void dispose () {
	}
}
