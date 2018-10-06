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
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class ReactGame0 implements Minigame {
	private final long DELAY_TIME_MILLIS = 1000;

	// sets game information
	private final String GAME_CODE = "React0";
	private final String GAME_NAME = "Flashin' Colors";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "The screen will flash red, blue, or yellow. Press A if \n"
			+ "it is red, S if it is blue, and D if it is yellow to \n"
			+ "increase your score. There is a maximum of 10 points.\n"
			+ "\n"
			+ "Easy mode will flash for 1 second\n"
			+ "Normal mode will flash for .75 seconds\n"
			+ "Hard mode will flash for .5 seconds\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final int MASTER_SCORES[] = {10, 10, 10};

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
	private int expireTimeMillis;

	private Timeline timeline;

	// creates empty instance
	public ReactGame0() {

	}

	public ReactGame0(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		if (difficulty == 0) {
			expireTimeMillis = 1000;
		}
		else if (difficulty == 1){
			expireTimeMillis = 750;
		}
		else {
			expireTimeMillis = 500;
		}

		// sets up root gamePane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up score Label
		scoreLbl = new Label("Score: ");
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up gamePane
		gamePane = new Pane();
		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();
		gamePane.getChildren().add(canvas);

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up timeline
		timeline = new Timeline(
				// randomly changes background to red, blue, or yellow
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS), event -> {
					int rand = (int)(Math.random()*3);
					if (rand == 0) {
						gc.setFill(Color.RED);
					}
					else if (rand == 1) {
						gc.setFill(Color.BLUE);
					}
					else if (rand == 2) {
						gc.setFill(Color.YELLOW);
					}
					draw();
				}),
				// changes background to black
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS+expireTimeMillis), event -> {
					gc.setFill(Color.BLACK);
					draw();

					// increments turn. if past turn = 9, ends game
					turn++;
					if (turn > 9) {
						timeline.stop();
						endGame();
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// handles onKeyPressed
		scene.setOnKeyPressed(event -> {
			// increments score if background is not black and correct code is pressed for color
			if (!gc.getFill().equals(Color.BLACK) && ((event.getCode() == KeyCode.A && gc.getFill().equals(Color.RED))
					|| (event.getCode() == KeyCode.S && gc.getFill().equals(Color.BLUE))
					|| (event.getCode() == KeyCode.D && gc.getFill().equals(Color.YELLOW)))) {
				score++;
				scoreLbl.setText("Score: " + score);
			}

			timeline.playFrom(Duration.millis(DELAY_TIME_MILLIS+expireTimeMillis-1));
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
		// sets size of canvas according to gamePane
		canvas.setWidth(gamePane.getWidth());
		canvas.setHeight(gamePane.getHeight());

		// fills background
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

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

	// resets game
	private void reset() {
		score = 0;
		scoreLbl.setText("Score: " + score);
		turn = 0;
		gc.setFill(Color.BLACK);
		draw();

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
