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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class AccurGame0 implements Minigame {
	// sets game information
	private final String GAME_CODE = "Accur0";
	private final String GAME_NAME = "Bullseye";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "Bullseyes will randomly appear on the screen. Hit them \n"
			+ "with your mouse. The closer you are to the center of the \n"
			+ "circle, the more your score increases. There is a maximum \n"
			+ "of 50 points.\n"
			+ "\n"
			+ "Easy mode bullseyes will expire in 1.25 seconds\n"
			+ "Normal mode bullseys will expire in 1 second\n"
			+ "Hard mode bullseyes will expire in .75 seconds\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {45, 45, 45};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Pane gamePane;

	private Label scoreLbl;
	private MenuButton quitBtn;

	private int difficulty;
	private int turn;
	private int score;
	private int expireTimeMillis;

	private Circle[] circles;
	private int centerX;
	private int centerY;

	private Rectangle background;
	private Timeline timeline;

	// creates empty instance
	public AccurGame0() {

	}

	// creates and sets up AccurGame1 scene
	public AccurGame0(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets expireTimeMillis according to difficulty
		if (difficulty == 0) {
			expireTimeMillis = 1250;
		}
		else if (difficulty == 1){
			expireTimeMillis = 1000;
		}
		else {
			expireTimeMillis = 750;
		}

		// sets up root gamePane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		gamePane = new Pane();

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up background
		background = new Rectangle(gamePane.getWidth(), gamePane.getHeight());
		background.setFill(Color.SANDYBROWN);
		gamePane.getChildren().add(background);

		// sets up circles
		circles = new Circle[5];
		for (int i = 0; i < circles.length; i++) {
			circles[i] = new Circle();

			// adds event handler that increases score according to circle index
			final int j = i;
			circles[i].setOnMousePressed(event -> {
				score+= j+1;
				scoreLbl.setText("Score: " + score);

				timeline.playFrom(Duration.millis(expireTimeMillis-1));
			});

			gamePane.getChildren().add(circles[i]);
		}

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.ZERO, event -> {
					// creates random coordinates 1 - 9
					centerX = (int)(Math.random()*9)+1;
					centerY= (int)(Math.random()*9)+1;

					draw();
				}),
				new KeyFrame(Duration.millis(expireTimeMillis), event -> {
					turn++;

					// if past turn 10, end the game
					if (turn > 9) {
						timeline.stop();

						endGame();
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// binds gamePane dimensions to scene dimensions
		gamePane.prefWidthProperty().bind(scene.widthProperty());
		gamePane.prefHeightProperty().bind(scene.heightProperty());

		// redraw when gamePane dimensions change
		gamePane.widthProperty().addListener(observable -> Platform.runLater(() -> draw()));
		gamePane.heightProperty().addListener(observable -> Platform.runLater(() -> draw()));

		reset();
	}

	// resizes and draws on gamePane
	private void draw() {
		// sizes background to gamePane size
		background.setWidth(gamePane.getWidth());
		background.setHeight(gamePane.getHeight());

		// sets the unit to 1/10 of gamePane width and height
		double unitWidth = gamePane.getWidth()/10;
		double unitHeight = gamePane.getHeight()/10;

		// draws the circles like a bullseye
		circles[0].setRadius(unitWidth*.5);
		circles[0].setFill(Color.WHITE);
		circles[0].setCenterX(centerX*unitWidth);
		circles[0].setCenterY(centerY*unitHeight);

		circles[1].setRadius(unitWidth*.4);
		circles[1].setFill(Color.BLACK);
		circles[1].setCenterX(centerX*unitWidth);
		circles[1].setCenterY(centerY*unitHeight);

		circles[2].setRadius(unitWidth*.3);
		circles[2].setFill(Color.BLUE);
		circles[2].setCenterX(centerX*unitWidth);
		circles[2].setCenterY(centerY*unitHeight);

		circles[3].setRadius(unitWidth*.2);
		circles[3].setFill(Color.RED);
		circles[3].setCenterX(centerX*unitWidth);
		circles[3].setCenterY(centerY*unitHeight);

		circles[4].setRadius(unitWidth*.1);
		circles[4].setFill(Color.YELLOW);
		circles[4].setCenterX(centerX*unitWidth);
		circles[4].setCenterY(centerY*unitHeight);
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
		turn = 0;

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
