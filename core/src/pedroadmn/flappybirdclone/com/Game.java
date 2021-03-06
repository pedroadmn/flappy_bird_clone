package pedroadmn.flappybirdclone.com;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class Game extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture[] birds;
	private Texture bottomTube;
	private Texture topTube;
	private Texture background;
	private Texture gameOver;

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
	private int maxScore = 0;
	private boolean passTube = false;
	private float birdHorizontalPosition = 0;

	// Texts
	BitmapFont scoreText;
	BitmapFont restartText;
	BitmapFont bestScoreText;

	// Sons
	Sound flyingSound;
	Sound colissionSound;
	Sound scoreSound;

	// Forms
	private ShapeRenderer shapeRenderer;
	private Circle birdCircle;
	private Rectangle topTubeRectangle;
	private Rectangle bottomTubeRectangle;

	// Game Status
	private int gameStatus = 0;

	Preferences preferences;

	// Camera
	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH = 720;
	private final float VIRTUAL_HEIGHT = 1280;

	@Override
	public void create () {
		initTextures();
		initObjects();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		verifyGameState();
		validateScore();
		drawTextures();
		detectCollisions();
	}

	private void detectCollisions() {

		birdCircle.set(50 + birdHorizontalPosition + birds[0].getWidth() /2 , initBirtVerticalPosition + birds[0].getHeight() / 2, birds[0].getWidth() / 2);

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
			if (gameStatus == 1) {
				colissionSound.play();
				gameStatus = 2;
			}
		}
	}

	private void validateScore() {
		if (horizontalTubePosition < 50 - birds[0].getWidth() ) {
			if (!passTube) {
				scores++;
				passTube = true;
				scoreSound.play();
			}
		}

		variation += Gdx.graphics.getDeltaTime() * 10;

		if (variation > 3) {
			variation = 0;
		}
	}

	private void verifyGameState() {
		boolean touchScreen = Gdx.input.justTouched();

		if (gameStatus == 0) {
			if (touchScreen) {
				gravity = -15;
				gameStatus = 1;
				flyingSound.play();
			}
		} else if (gameStatus == 1) {
			if (touchScreen) {
				gravity = -15;
				flyingSound.play();
			}

			horizontalTubePosition -= Gdx.graphics.getDeltaTime() * 200;

			if (horizontalTubePosition < -bottomTube.getWidth()) {
				horizontalTubePosition = deviceWidth;
				verticalTubePosition = random.nextInt(400) - 200;
				passTube = false;
			}

			if (initBirtVerticalPosition > 0 || touchScreen) {
				initBirtVerticalPosition = initBirtVerticalPosition - gravity;
			}

			gravity++;
		} else if (gameStatus == 2) {
			birdHorizontalPosition -= Gdx.graphics.getDeltaTime() * 500;

			int currentBestScore = preferences.getInteger("score");

			if (currentBestScore < scores) {
				preferences.putInteger("score", scores);
			}

			if (touchScreen) {
				gameStatus = 0;
				scores = 0;
				gravity = 0;
				birdHorizontalPosition = 0;
				initBirtVerticalPosition = deviceHeight / 2;
				horizontalTubePosition = deviceWidth;
			}
		}
	}

	private void drawTextures() {
		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(background, 0, 0, deviceWidth, deviceHeight);
		batch.draw(birds[(int)variation], 50 + birdHorizontalPosition, initBirtVerticalPosition);
		batch.draw(bottomTube, horizontalTubePosition, deviceHeight / 2 - bottomTube.getHeight() - spaceBetweenTubes / 2 + verticalTubePosition);
		batch.draw(topTube, horizontalTubePosition, (deviceHeight / 2) + spaceBetweenTubes / 2 + verticalTubePosition);

		scoreText.draw(batch, String.valueOf(scores), deviceWidth / 2, deviceHeight - 100);

		if (gameStatus == 2) {

			if (scores > maxScore) {
				maxScore = scores;
				preferences.putInteger("score", maxScore);
			}

			batch.draw(gameOver, deviceWidth / 2 - gameOver.getWidth() / 2, deviceHeight /2);
			restartText.draw(batch, "Touch on the screen to restart", deviceWidth / 2 - 200, deviceHeight / 2 - gameOver.getHeight() / 2);
			bestScoreText.draw(batch, "Your record is: " + maxScore, deviceWidth / 2 - 150, deviceHeight / 2 - gameOver.getHeight());
		}

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

		gameOver = new Texture("game_over.png");
	}

	private void initObjects() {
		batch = new SpriteBatch();
		random = new Random();

		// Texts
		scoreText = new BitmapFont();
		scoreText.setColor(Color.WHITE);
		scoreText.getData().setScale(10);

		restartText = new BitmapFont();
		restartText.setColor(Color.GREEN);
		restartText.getData().setScale(2);

		bestScoreText = new BitmapFont();
		bestScoreText.setColor(Color.RED);
		bestScoreText.getData().setScale(2);

		deviceWidth = VIRTUAL_WIDTH;
		deviceHeight = VIRTUAL_HEIGHT;
		initBirtVerticalPosition = deviceHeight / 2;

		horizontalTubePosition = deviceWidth;
		spaceBetweenTubes = 300;

		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		topTubeRectangle = new Rectangle();
		bottomTubeRectangle = new Rectangle();

		flyingSound = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		colissionSound = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));

		preferences = Gdx.app.getPreferences("flappyBird");
		maxScore = preferences.getInteger("score", 0);

		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose () {
	}
}
