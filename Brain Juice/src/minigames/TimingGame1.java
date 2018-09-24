/* Bao Nguyen
 * Brain Juice
 */

package minigames;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class TimingGame1 implements Minigame {
	private final long TOTAL_TIME_MILLIS = 5000;
	private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00.00");

	// sets game information
	private final String GAME_CODE = "Timing1";
	private final String GAME_NAME = "Countdown";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "The number in the center will decrease to 0. You won't be \n"
			+ "able to see it after a while. Click the screen when you \n"
			+ "think the number is at 0. Try to be as close as you can. \n"
			+ "There will be 10 countdowns.\n"
			+ "\n"
			+ "Easy mode will blind you after 3 seconds\n"
			+ "Normal mode will blind you after 2 seconds\n"
			+ "Hard mode will blind you after 1 second\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "seconds";
	private final double[] MASTER_SCORES = {5, 5, 5};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Label scoreLbl;
	private MenuButton quitBtn;

	private VBox gamePane;

	private int difficulty;
	private int turn;	
	private long totalTimeOffMillis;
	private long timeAtStartMillis;
	private long blindTimeMillis;

	private Timeline timer;
	private Label timerLbl;

	// creates empty instance
	public TimingGame1() {

	}

	public TimingGame1 (SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets blindTimeMillis according to difficulty
		if (difficulty == 0) {
			blindTimeMillis = 3000;
		}
		else if (difficulty == 1){
			blindTimeMillis = 2000;
		}
		else {
			blindTimeMillis = 1000;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		gamePane = new VBox();

		// sets up timerLbl
		timerLbl = new Label();
		timerLbl.setFont(Font.font("Arial", FontWeight.BOLD, 72));

		// sets up gamePane
		gamePane.getChildren().add(timerLbl);
		gamePane.setAlignment(Pos.CENTER);
		gamePane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(1))));

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timer.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up timer
		timer = new Timeline(
				new KeyFrame(Duration.millis(10), event -> {
					if (System.currentTimeMillis()-timeAtStartMillis < blindTimeMillis) {
						timerLbl.setText(DECIMAL_FORMAT.format((TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis))/1000.0));
					}
					else {
						timerLbl.setText("?");
					}
				}));
		timer.setCycleCount(Timeline.INDEFINITE);

		scene.setOnMousePressed(event -> {
			// updates scoreLbl and totalTimeOffMillis
			scoreLbl.setText("Time Off: " + DECIMAL_FORMAT.format(totalTimeOffMillis/1000.0) 
			+ " + |" + DECIMAL_FORMAT.format((TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis))/1000.0) + "| seconds");
			totalTimeOffMillis += Math.abs(TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis));

			// increments turn. if past turn = 9, ends game
			turn ++;
			if (turn > 9) {
				timer.stop();
				endGame();
			}
			else {
				timeAtStartMillis = System.currentTimeMillis();
				timer.playFromStart();
			}
		});

		// binds gamePane dimensions to scene dimensions
		gamePane.prefWidthProperty().bind(scene.widthProperty());
		gamePane.prefHeightProperty().bind(scene.heightProperty());

		reset();
	}

	// procedure for ending the game
	private void endGame() {
		Platform.runLater(() -> {			
			// sets up gameOverDialog
			Alert gameOverDialog = new Alert(AlertType.CONFIRMATION);
			gameOverDialog.setTitle("Game Over");

			gameOverDialog.setHeaderText("Time: " + DECIMAL_FORMAT.format(totalTimeOffMillis/1000.0) + "\n"
					+ "Mastery : " + DECIMAL_FORMAT.format(MASTER_SCORES[difficulty]) + " seconds\n"
					+ "Try Again?");

			Optional<ButtonType> result = gameOverDialog.showAndWait();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// updates the file with current date, score, and difficulty
			try {
				sc.getCurrentUser().updateFile(GAME_CODE, dateFormat.format(new Date()), totalTimeOffMillis/1000.0, difficulty);
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
		turn = 0;
		timeAtStartMillis = System.currentTimeMillis();
		totalTimeOffMillis = 0;
		scoreLbl.setText("Time Off: " + DECIMAL_FORMAT.format(totalTimeOffMillis/1000.0) + " seconds");

		timer.playFromStart();
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
