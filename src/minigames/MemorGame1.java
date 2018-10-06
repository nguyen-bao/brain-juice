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
import javafx.scene.control.TextField;
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

public class MemorGame1 implements Minigame {
	private final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private final long EXPIRE_TIME_MILLIS = 5000;

	// sets game information
	private final String GAME_CODE = "Memor1";
	private final String GAME_NAME = "String Memory";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "A string of characters will appear on the screen. \n"
			+ "After 5 seconds, it will be hidden. Enter the \n"
			+ "same characters to increase your score. There \n"
			+ "are 5 turns.\n"
			+ "\n"
			+ "Easy mode will have 5 characters each string\n"
			+ "Normal mode will have 7 characters each string\n"
			+ "Hard mode will have 9 characters each string\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {20, 30, 40};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Label scoreLbl;

	private VBox gamePane;
	private Label stringLbl;
	private TextField inputTextField;
	private MenuButton submitBtn;

	private MenuButton quitBtn;

	private int difficulty;
	private int score;
	private int turn;
	private int numOfCharacters;

	private String string;
	private Timeline timeline;

	// creates empty instance
	public MemorGame1() {

	}

	public MemorGame1(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		if (difficulty == 0) {
			numOfCharacters = 5;
		}
		else if (difficulty == 1) {
			numOfCharacters = 7;
		}
		else if (difficulty == 2) {
			numOfCharacters = 9;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up inputPane
		gamePane = new VBox(15);
		gamePane.setAlignment(Pos.CENTER);
		gamePane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(1))));

		// sets up stringLbl
		stringLbl = new Label();
		stringLbl.setFont(Font.font("Arial", FontWeight.BOLD, 36));

		inputTextField = new TextField();
		inputTextField.setMaxWidth(400);

		// sets up submitBtn
		submitBtn = new MenuButton();
		submitBtn.setText("Submit");
		submitBtn.setPrefSize(130, 40);
		submitBtn.addActionEventHandler(event -> {
			// updates score by comparing input and string
			String input = inputTextField.getText();
			for (int i = 0; i < string.length() && i < input.length(); i++) {
				if (string.charAt(i) == inputTextField.getText().charAt(i)) {
					score++;
				}
			}
			scoreLbl.setText("Score: " + score);

			// increments turn and endGame if past turn = 4
			turn++;
			if (turn > 4) {
				endGame();
			}
			else {
				timeline.playFromStart();
			}
		});

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.millis(0), event -> {
					string = "";
					for (int i = 0; i < numOfCharacters; i++) {
						int charIndex = (int)(Math.random()*CHARACTERS.length());
						string = string.concat("" + CHARACTERS.charAt(charIndex));
					}
					stringLbl.setText(string);

					gamePane.getChildren().setAll(stringLbl);
				}),
				new KeyFrame(Duration.millis(EXPIRE_TIME_MILLIS), event -> {
					stringLbl.setText("What was the string?");
					inputTextField.setText("");

					gamePane.getChildren().setAll(stringLbl, inputTextField, submitBtn);
				}));
		timeline.setCycleCount(1);

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