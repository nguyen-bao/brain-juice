/* Bao Nguyen
 * Brain Juice
 */

package minigames;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;
import util.Sprite;

public class FlexGame1 implements Minigame {
	private static final long DELAY_TIME_MILLIS = 250;

	// sets game information
	private final String GAME_CODE = "Flex1";
	private final String GAME_NAME = "Which Way?";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "Blue and red arrows will appear on the screen. If \n"
			+ "the arrow is blue, press the arrow key in the same \n"
			+ "direction as it to increase your score. If it is \n"
			+ "red, press the arrow key in the opposite direction \n"
			+ "of it to increase your score. There is a maximum of \n"
			+ "20 points.\n"
			+ "\n"
			+ "Easy mode arrows will expire in 1.25 seconds\n"
			+ "Normal mode arrows will expire in 1 second\n"
			+ "Hard mode arrows will expire in .75 seconds\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {20, 20, 20};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Pane gamePane;
	private Canvas canvas;
	private GraphicsContext gc;

	private Label scoreLbl;
	private MenuButton quitBtn;

	private int difficulty;
	private int turn;
	private int score;
	private long expireTimeMillis;
	private int arrowIndex;
	private boolean currentArrowIsBlue;
	private boolean arrowIsShowing;

	private Timeline timeline;
	private Sprite[] blueArrows;
	private Sprite[] redArrows;

	// creates empty instance
	public FlexGame1() {

	}

	public FlexGame1(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets expireTimeMillis according to difficulty
		if (difficulty == 0) {
			expireTimeMillis = 1250;
		}
		else if (difficulty == 1) {
			expireTimeMillis = 1000;
		}
		else if (difficulty == 2) {
			expireTimeMillis = 750;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up gamePane
		gamePane = new Pane();
		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();
		gamePane.getChildren().add(canvas);

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> {
			timeline.stop();
		});
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// creates blue arrows
		blueArrows = new Sprite[4];
		for (int i = 0; i < blueArrows.length; i++) {
			blueArrows[i] = new Sprite(gc);
			blueArrows[i].addFrame("sprites/flexGame2/blue_" + i + ".png");
			blueArrows[i].setFrame(0);
			blueArrows[i].setPosition(0, 0);
		}

		// creates red arrows
		redArrows = new Sprite[4];
		for (int i = 0; i < redArrows.length; i++) {
			redArrows[i] = new Sprite(gc);
			redArrows[i].addFrame("sprites/flexGame2/red_" + i + ".png");
			redArrows[i].setFrame(0);
			redArrows[i].setPosition(0, 0);
		}

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS), event -> {
					// shows either blue or red arrow in random direction
					arrowIndex = (int)(Math.random()*4);
					if ((int)(Math.random()*2) == 0) {
						currentArrowIsBlue = true;
						blueArrows[arrowIndex].setExists(true);
					}
					else {
						currentArrowIsBlue = false;
						redArrows[arrowIndex].setExists(true);
					}

					arrowIsShowing = true;
					draw();
				}),
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS+expireTimeMillis), event -> {
					// destroys previous arrow
					if (currentArrowIsBlue) {
						blueArrows[arrowIndex].setExists(false);
					}
					else {
						redArrows[arrowIndex].setExists(false);
					}

					// increments turn. if past turn = 19, ends game
					turn++;
					if (turn > 19) {
						timeline.stop();
						endGame();
					}

					arrowIsShowing = false;
					draw();
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// handles onKeyPressed
		scene.setOnKeyPressed(event -> {
			if (arrowIsShowing) {
				// increments score if key pressed follows the rules
				if (event.getCode() == KeyCode.UP && ((currentArrowIsBlue && arrowIndex == 0) || (!currentArrowIsBlue && arrowIndex == 2))
						|| (event.getCode() == KeyCode.RIGHT && ((currentArrowIsBlue && arrowIndex == 1) || (!currentArrowIsBlue && arrowIndex == 3)))
						|| (event.getCode() == KeyCode.DOWN && ((currentArrowIsBlue && arrowIndex == 2) || (!currentArrowIsBlue && arrowIndex == 0)))
						|| (event.getCode() == KeyCode.LEFT && ((currentArrowIsBlue && arrowIndex == 3) || (!currentArrowIsBlue && arrowIndex == 1)))) {
					score++;
					scoreLbl.setText("Score: " + score);
				}
				timeline.playFrom(Duration.millis(DELAY_TIME_MILLIS+expireTimeMillis-1));
			}
		});

		// binds gamePane dimensions to scene dimensions
		gamePane.prefWidthProperty().bind(scene.widthProperty());
		gamePane.prefHeightProperty().bind(scene.heightProperty());

		// redraw when gamePane dimensions change
		gamePane.widthProperty().addListener(observable -> Platform.runLater(() -> draw()));
		gamePane.heightProperty().addListener(observable -> Platform.runLater(() -> draw()));

		reset();
	}

	// draws on canvas
	private void draw() {
		canvas.setWidth(scene.getWidth());
		if (scene.getHeight()-150 >= 0) {
			canvas.setHeight(scene.getHeight()-150);
		}

		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		for (int i = 0; i < blueArrows.length; i++) {
			if (blueArrows[i].exists()) {
				blueArrows[i].setWidth(canvas.getWidth());
				blueArrows[i].setHeight(canvas.getHeight());
				blueArrows[i].display();
			}
			if (redArrows[i].exists()) {
				redArrows[i].setWidth(canvas.getWidth());
				redArrows[i].setHeight(canvas.getHeight());
				redArrows[i].display();
			}
		}
	}

	// procedure for ending the game
	private void endGame() {
		Platform.runLater(() -> {
			// sets up gameOverDialog
			Alert gameOverDialog = new Alert(AlertType.CONFIRMATION);
			gameOverDialog.setTitle("Game Over");

			gameOverDialog.setHeaderText("Score: " + score + "\n"
					+ "Mastery : " + (int)MASTER_SCORES[difficulty] + " points\n"
					+ "Try Again?");

			Optional<ButtonType> result = gameOverDialog.showAndWait();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// updates the file with current date, score, and difficulty
			try {
				sc.getCurrentUser().updateFile(GAME_CODE, dateFormat.format(new Date()), score, difficulty);
			}
			// catches IOException and informs the user
			catch (IOException ex) {
				Alert errorDialog = new Alert(AlertType.ERROR);
				errorDialog.setTitle("Error");
				errorDialog.setHeaderText("Error");
				errorDialog.setContentText("File could not be saved");
				errorDialog.showAndWait();
			}

			// resets if OK button is selected
			if (result.get() == ButtonType.OK) {
				reset();
			}

			// quits in CANCEL button is selected
			if(result.get() == ButtonType.CANCEL) {
				quitBtn.fire();
			}
		});
	}

	// resets the game
	private void reset() {
		score = 0;
		scoreLbl.setText("Score: " + score);
		turn = 0;

		// resets arrows
		for (int i = 0; i < blueArrows.length; i++) {
			blueArrows[i].setExists(false);
			redArrows[i].setExists(false);
		}

		timeline.playFromStart();
	}

	public String getGameCode() {
		return GAME_CODE;
	}

	public String getGameName() {
		return GAME_NAME;
	}

	public String getGameInstructions() {
		return GAME_INSTRUCTIONS;
	}

	public String getScoreUnit() {
		return SCORE_UNIT;
	}

	public double getMasterScore(int difficulty) {
		return MASTER_SCORES[difficulty];
	}

	public Scene getScene() {
		return scene;
	}
}