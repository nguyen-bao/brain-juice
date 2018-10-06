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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class CalcGame1 implements Minigame {
	private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00.00");
	private final long TOTAL_TIME_MILLIS = 60000;
	private final int NUMBER_OF_ELEMENTS = 4;

	// sets game information
	private final String GAME_CODE = "Calc1";
	private final String GAME_NAME = "1 Minute Patterns";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "A sequence of numbers will appear on the sceen. \n"
			+ "Enter the number that follows the pattern to \n"
			+ "increase your score. The game ends in 1 minute.\n"
			+ "\n"
			+ "Easy mode will include a small range of numbers\n"
			+ "Normal mode will include a larger range of numbers\n"
			+ "Hard mode will include a very large range of numbers\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {20, 20, 20};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private HBox row0;
	private Label timerLbl;
	private Label scoreLbl;

	private HBox gamePane;
	private Label problemLbl;
	private TextField inputTextField;

	private MenuButton quitBtn;

	private int difficulty;
	private int score;
	private int minA;
	private int maxA;
	private int minB;
	private int maxB;
	private int solution;
	private long timeAtStartMillis;

	private Timeline timer;

	// creates empty instance
	public CalcGame1() {

	}

	public CalcGame1(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets minA, maxA, minB, and maxB according to difficulty
		if (difficulty == 0) {
			minA = -3;
			maxA = 3;
			minB = -10;
			maxB = 10;
		}
		else if (difficulty == 1) {
			minA = -5;
			maxA = 5;
			minB = -15;
			maxB = 15;
		}
		else if (difficulty == 2) {
			minA = -10;
			maxA = 10;
			minB = -20;
			maxB = 20;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		row0 = new HBox(15);

		// sets up timerLbl
		timerLbl = new Label();
		timerLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		row0.getChildren().addAll(timerLbl, scoreLbl);

		// sets up gamePane
		gamePane = new HBox(15);
		gamePane.setAlignment(Pos.CENTER);
		gamePane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(1))));

		problemLbl = new Label();
		problemLbl.setFont(Font.font("Arial", FontWeight.BOLD, 36));

		inputTextField = new TextField();

		gamePane.getChildren().addAll(problemLbl, inputTextField);

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timer.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(row0, gamePane, quitBtn);

		// timer counts down from total time
		timer = new Timeline(
				new KeyFrame(Duration.millis(10), event -> {
					timerLbl.setText("Time Left: " + DECIMAL_FORMAT.format(((double)TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis))/1000));

					if ((double)TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis) < 0) {
						timer.stop();
						endGame();
					}
				}));
		timer.setCycleCount(Timeline.INDEFINITE);

		// handles onKeyReleased
		scene.setOnKeyReleased(event -> {
			// gets input from text field
			Integer input;
			try {
				input = Integer.parseInt(inputTextField.getText());
			}
			catch (NumberFormatException ex) {
				input = null;
			}

			// if input is solution, increase score and nextProblem
			if (input == (Integer)solution) {
				score++;
				scoreLbl.setText("Score: " + score);

				inputTextField.setText("");

				nextProblem();
			}
		});

		// binds gamePane dimensions to scene dimensions
		gamePane.prefWidthProperty().bind(scene.widthProperty());
		gamePane.prefHeightProperty().bind(scene.heightProperty());

		reset();
	}

	// creates next problem (An = A*n + B, A != 0)
	private void nextProblem() {
		// chooses a and B
		int A = 0;
		while (A == 0) {
			A = (int)(Math.random()*(maxA-minA+1)+minA);
		}
		int B = (int)(Math.random()*(maxB-minB+1)+minB);

		// sets problem to problemLbl
		String problem = "";
		for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
			if (i != 0) {
				problem = problem.concat(", ");
			}
			problem = problem.concat("" + (A*i+B));
		}
		problemLbl.setText(problem);

		solution = A*NUMBER_OF_ELEMENTS+B;
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
		timeAtStartMillis = System.currentTimeMillis();
		timer.playFromStart();

		nextProblem();
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