package pedroadmn.flappybirdclone.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

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

	// Forms
	private ShapeRenderer shapeRenderer;
	private Circle birdCircle;
	private Rectangle topTubeRectangle;
	private Rectangle bottomTubeRectangle;

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
		detectCollisions();
	}

	private void detectCollisions() {

		birdCircle.set(50 + birds[0].getWidth() /2 , initBirtVerticalPosition + birds[0].getHeight() / 2, birds[0].getWidth() / 2);

		bottomTubeRectangle.set(horizontalTubePosition, deviceHeight / 2 - bottomTube.getHeight() - spaceBetweenTubes / 2 + verticalTubePosition,
				bottomTube.getWidth(),
				bottomTube.getHeight());

		topTubeRectangle.set(
				horizontalTubePosition,
				(deviceHeight / 2) + spaceBetweenTubes / 2 + verticalTubePosition,
				topTube.getWidth(),
				topTube.getHeight()
		);

		if (Intersector.overlaps(birdCircle, bottomTubeRectangle) || Intersector.overlaps(birdCircle, topTubeRectangle)) {

		}


		 /*shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		 shapeRenderer.circle(50 + birds[0].getWidth() /2 , initBirtVerticalPosition + birds[0].getHeight() / 2, birds[0].getWidth() / 2);

		 // Top
		 shapeRenderer.rect(
				 horizontalTubePosition,
				 (deviceHeight / 2) + spaceBetweenTubes / 2 + verticalTubePosition,
				 topTube.getWidth(),
				 topTube.getHeight()

		 );

		 // Bottom
		shapeRenderer.rect(
				horizontalTubePosition, deviceHeight / 2 - bottomTube.getHeight() - spaceBetweenTubes / 2 + verticalTubePosition,
				bottomTube.getWidth(),
				bottomTube.getHeight()
		);


		 shapeRenderer.setColor(Color.RED);
		 shapeRenderer.end();*/
	}

	private void validateScore() {
		if (horizontalTubePosition < 50 - birds[0].getWidth() ) {
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
			gravity = -15;
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
		spaceBetweenTubes = 300;

		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangle = new Rectangle();
		bottomTubeRectangle = new Rectangle();
	}
	
	@Override
	public void dispose () {
	}
}
