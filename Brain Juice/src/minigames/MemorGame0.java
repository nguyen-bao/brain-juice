/* Bao Nguyen
 * Brain Juice
 */

package minigames;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.*;

public class MemorGame0 implements Minigame {
	private final long FLASH_DURATION_MILLIS = 500;
	private final Color[] COLORS = {
			Color.RED, Color.AQUAMARINE, Color.LIGHTBLUE, Color.GREEN,
			Color.LIGHTGREEN, Color.BLUE, Color.ORANGE, Color.HOTPINK,
			Color.VIOLET, Color.GRAY, Color.BROWN, Color.GREENYELLOW,
			Color.DARKBLUE, Color.TAN, Color.YELLOW, Color.DARKVIOLET
	};

	// sets game information
	private final String GAME_CODE = "Memor0";
	private final String GAME_NAME = "Memory Colors";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "The buttons will flash in a pattern. Remember the pattern \n"
			+ "and after it finishes, click the buttons in the same \n"
			+ "order to increase your score.\n"
			+ "\n"
			+ "Easy mode will have 4 buttons\n"
			+ "Normal mode will have 9 buttons\n"
			+ "Hard mode will have 16 buttons\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";

	// sets mastery scores
	private final double[] MASTER_SCORES = {10, 10, 10};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Pane gamePane;

	private Label scoreLbl;
	private MenuButton quitBtn;

	private int score;
	private int difficulty;
	private int numRows;
	private int currentIndex;
	private boolean inSequence;

	private Rectangle[] buttons;
	private ArrayList<Integer> sequence;
	private Timeline timeline;

	// creates empty instance
	public MemorGame0() {

	}

	public MemorGame0(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		if (difficulty == 0) {
			numRows = 2;
		}
		else if (difficulty == 1) {
			numRows = 3;
		}
		else {
			numRows = 4;
		}

		// sets up root gamePane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up grid
		gamePane = new Pane();

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up buttons
		buttons = new Rectangle[(int)Math.pow(numRows, 2)];		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Rectangle();
			buttons[i].setFill(COLORS[i]);
			gamePane.getChildren().add(buttons[i]);
		}

		sequence = new ArrayList<Integer>();

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.millis(0), event -> {
					buttons[sequence.get(currentIndex)].setFill(Color.WHITE);
					draw();
				}),
				new KeyFrame(Duration.millis(FLASH_DURATION_MILLIS), event -> {
					buttons[sequence.get(currentIndex)].setFill(COLORS[sequence.get(currentIndex)]);
					draw();

					// moves on to next button
					currentIndex++;
					// if at end of sequence, get out of timeline
					if (currentIndex > sequence.size()-1) {
						currentIndex = 0;
						inSequence = false;
					}
					else {
						timeline.playFromStart();
					}
				}));
		timeline.setCycleCount(1);

		// handles onMousePressed if not in sequence
		scene.setOnMousePressed(event -> {
			if (!inSequence) {
				// mouse pressed inside current button
				if (gamePane.localToScene(buttons[sequence.get(currentIndex)].getBoundsInLocal()).contains(event.getX(), event.getY())) {
					currentIndex++;

					// if pressed last button in sequence, increment score, add to sequence, and start animation
					if (currentIndex > sequence.size()-1) {
						score++;
						scoreLbl.setText("Score: " + score);

						sequence.add((int)(Math.random()*buttons.length));
						currentIndex = 0;
						inSequence = true;
						timeline.playFromStart();
					}
				}
				else {
					endGame();
				}
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

	// draws on grid
	private void draw() {
		double unitWidth = gamePane.getWidth()/numRows;
		double unitHeight = gamePane.getHeight()/numRows;

		// draws buttons according to gamePane
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numRows; x++) {
				buttons[y*numRows+x].setWidth(unitWidth);
				buttons[y*numRows+x].setHeight(unitHeight);
				buttons[y*numRows+x].setX(x*unitWidth);
				buttons[y*numRows+x].setY(y*unitHeight);
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

	// resets game
	private void reset() {
		score = 0;
		scoreLbl.setText("Score: " + score);

		sequence.clear();
		sequence.add((int)(Math.random()*buttons.length));
		currentIndex = 0;

		inSequence = true;
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
