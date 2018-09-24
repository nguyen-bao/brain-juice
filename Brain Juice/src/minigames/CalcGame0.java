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

public class CalcGame0 implements Minigame {
	private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00.00");
	private final long TOTAL_TIME_MILLIS = 60000;

	// sets game information
	private final String GAME_CODE = "Calc0";
	private final String GAME_NAME = "1 Minute Calculations";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "A mathematical expression will appear at the center \n"
			+ "of the screen. Type in the equivalent integer to \n"
			+ "increase your score. The game ends in 1 minute.\n"
			+ "\n"
			+ "Easy mode will include a small range of numbers\n"
			+ "Normal mode will include a larger range of numbers\n"
			+ "Hard mode will include a very large range of numbers\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {30, 30, 30};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private HBox row0;
	private Label timerLbl;
	private Label scoreLbl;

	private VBox gamePane;
	private Label problemLbl;
	private TextField inputTextField;

	private MenuButton quitBtn;

	private int difficulty;
	private int score;
	private int solution;
	private int minInt;
	private int maxInt;
	private long timeAtStartMillis;

	private Timeline timer;

	// creates empty instance
	public CalcGame0() {

	}

	public CalcGame0(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets minInt and maxInt according to difficulty
		if (difficulty == 0) {
			minInt = -10;
			maxInt = 10;
		}
		else if (difficulty == 1) {
			minInt = -15;
			maxInt = 15;
		}
		else if (difficulty == 2) {
			minInt = -30;
			maxInt = 30;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		row0 = new HBox(15);

		// set sup timerLbl
		timerLbl = new Label();
		timerLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		row0.getChildren().addAll(timerLbl, scoreLbl);

		// sets up gamePane
		gamePane = new VBox(15);
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
					timerLbl.setText("Time Left: " + DECIMAL_FORMAT.format((TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis))/1000.0));

					if (TOTAL_TIME_MILLIS-(System.currentTimeMillis()-timeAtStartMillis) < 0) {
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

	// creates next problem
	private void nextProblem() {
		// chooses between + (0), - (1), * (2), or / (3) operator
		int operator = (int)(Math.random()*4);

		// chooses first integer within range
		int firstInt = (int)(Math.random()*(maxInt-minInt+1) + minInt);

		//chooses second integer within range	
		int secondInt = (int)(Math.random()*(maxInt-minInt) + minInt);

		// creates solution
		if (operator == 0) {
			solution = firstInt + secondInt;
			problemLbl.setText(firstInt + " + " + secondInt);
		}
		else if (operator == 1) {
			solution = firstInt - secondInt;
			problemLbl.setText(firstInt + " - " + secondInt);
		}
		else if (operator == 2) {
			solution = firstInt * secondInt;
			problemLbl.setText(firstInt + " * " + secondInt);
		}
		// in the case of divide, secondInt must be reassigned until it is nonzero and there is a nonzero remainder
		else if (operator == 3) {
			while (secondInt == 0 || firstInt%secondInt != 0) {
				secondInt = (int)(Math.random()*(maxInt-minInt) + minInt);
			}
			solution = firstInt / secondInt;
			problemLbl.setText(firstInt + " / " + secondInt);
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