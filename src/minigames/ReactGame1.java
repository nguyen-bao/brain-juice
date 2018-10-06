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
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import util.Sprite;


public class ReactGame1 implements Minigame {
	private final long DELAY_TIME_MILLIS = 500;
	private final int NUMBER_OF_ROWS = 3;

	// sets game information
	private final String GAME_CODE = "React1";
	private final String GAME_NAME = "Whack-a-Troll";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "Trolls will appear in the holes. Whack them with your \n"
			+ "mouse to increase your score. They will retreat after \n"
			+ "some time. There is a maximum of 20 points.\n"
			+ "\n"
			+ "Easy mode trolls will retreat after 1 second\n"
			+ "Normal mode trolls will retreat after .75 seconds\n"
			+ "Hard mode trolls will retreat after .5 seconds\n"
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
	private int expireTimeMillis;
	private int currentHole;

	private ArrayList<Sprite> holes;
	private Sprite stick;
	private Timeline timeline;

	// creates empty instance
	public ReactGame1() {

	}

	public ReactGame1(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets expireTimeMillis according to difficulty
		if (difficulty == 0) {
			expireTimeMillis = 1000;
		}
		else if (difficulty == 1){
			expireTimeMillis = 750;
		}
		else {
			expireTimeMillis = 500;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
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
		quitBtn.addActionEventHandler(event -> {
			gamePane.setCursor(Cursor.DEFAULT);
			timeline.stop();
		});
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up holes
		holes = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_ROWS*3; i ++) {
			Sprite newHole = new Sprite(gc);
			newHole.addFrame("sprites/reactGame2/hole.png");
			newHole.addFrame("sprites/reactGame2/hole_troll.png");

			holes.add(newHole);
		}

		// sets up stick
		stick = new Sprite(gc);
		stick.addFrame("sprites/reactGame2/stick.png");
		stick.setFrame(0);
		gamePane.setCursor(Cursor.NONE);

		timeline = new Timeline(
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS), event -> {
					// choose nextHole that isn't currentHole
					int nextHole;
					do {
						nextHole = (int)(Math.random()*9);
					} while (nextHole == currentHole);
					currentHole = nextHole;
					
					holes.get(currentHole).setFrame(1);
					draw();
				}),
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS+expireTimeMillis), event -> {
					holes.get(currentHole).setFrame(0);
					draw();
					
					// increments turn. if past turn = 19, ends game
					turn++;
					if(turn > 19) {
						gamePane.setCursor(Cursor.DEFAULT);
						timeline.stop();						
						endGame();
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// positions stick where mouse is
		gamePane.setOnMouseMoved(event -> {
			stick.setPosition(event.getX(), event.getY());
			draw();
		});

		// handles onMousePressed
		scene.setOnMousePressed(event -> {
			// if mouse is in hole that is on frame 1, increment score and change frame
			for (int i = 0; i < holes.size(); i ++) {
				if (gamePane.localToScene(holes.get(i).getBounds()).contains(event.getX(), event.getY()) && holes.get(i).getFrameIndex() == 1) {
					score++;
					scoreLbl.setText("Score: " + score);
				}
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

	public void draw() {
		// resizes canvas according to scene
		canvas.setWidth(gamePane.getWidth());
		canvas.setHeight(gamePane.getHeight());

		// sets background
		gc.setFill(Color.PURPLE);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		double unitWidth = canvas.getWidth()/3;
		double unitHeight = canvas.getHeight()/3;
		
		// draws holes
		for (int x = 0; x < NUMBER_OF_ROWS; x++) {
			for (int y = 0; y < NUMBER_OF_ROWS; y++) {
				holes.get(x*NUMBER_OF_ROWS+y).display(x*unitWidth, y*unitHeight, unitWidth, unitHeight);
			}
		}
		
		stick.setWidth(canvas.getWidth()/10);
		stick.setHeight(canvas.getHeight()/2);
		stick.display();
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
	public void reset() {
		score = 0;
		scoreLbl.setText("Score: " + score);
		turn = 0;
		currentHole = -1;

		for(int i = 0; i < holes.size(); i ++) {
			holes.get(i).setFrame(0);
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
