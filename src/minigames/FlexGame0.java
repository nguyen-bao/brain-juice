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
import util.Sprite;

// Get Stuff
// clean up and add comments

public class FlexGame0 implements Minigame {
	private final double STUFF_SPEED = .03;

	// sets game information
	private final String GAME_CODE = "Flex0";
	private final String GAME_NAME = "Get Stuff";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "There are 4 minigames. Control the top-left one with \n"
			+ "the up and down arrows, the top-right one with the \n"
			+ "left and right arrows, the bottom-left one with the \n"
			+ "A and D keys, and the bottom-right one with the W and \n"
			+ "S keys. Move the white circles to get the black circles, \n"
			+ "called stuff, to increase your score. There is a maximum \n"
			+ "score of 20.\n"
			+ "\n"
			+ "Easy mode stuff will spawn slowly\n"
			+ "Normal mode stuff will spawn faster\n"
			+ "Hard mode stuff will spawn very quickly\n"
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
	private long spawnRateMillis;

	private Timeline spawnTimeline;
	private Timeline timeline;
	private Sprite[] getters;
	private Sprite[] stuffs;

	// creates empty instance
	public FlexGame0() {

	}

	public FlexGame0(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets spawnRateMillis according to difficulty
		if (difficulty == 0) {
			spawnRateMillis = 1500;
		}
		else if (difficulty == 1) {
			spawnRateMillis = 1000;
		}
		else if (difficulty == 2) {
			spawnRateMillis = 750;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up game
		gamePane = new Pane();
		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();
		gamePane.getChildren().add(canvas);

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> {
			spawnTimeline.stop();
			timeline.stop();
		});
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up getters
		getters = new Sprite[4];
		for (int i = 0; i < getters.length; i++) {
			getters[i] = new Sprite(gc);
			getters[i].addFrame("sprites/flexGame1/getter.png");
			getters[i].setFrame(0);
		}
		getters[0].setPosition(0, 2);
		getters[1].setPosition(2, 4);
		getters[2].setPosition(2, 4);
		getters[3].setPosition(0, 2);

		// sets up stuffs
		stuffs = new Sprite[4];
		for (int i = 0; i < stuffs.length; i++) {
			stuffs[i] = new Sprite(gc);
			stuffs[i].addFrame("sprites/flexGame1/stuff.png");
			stuffs[i].setFrame(0);
		}

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.millis(10), event -> {
					// moves stuffs in the correct direction if they exist
					for (int i = 0; i < stuffs.length; i++) {
						if (stuffs[i].exists()) {
							if (i == 0 || i == 3) {
								stuffs[i].setPosition(stuffs[i].getX()-STUFF_SPEED, stuffs[i].getY());
								if (stuffs[i].getX() < 0) {
									stuffs[i].setExists(false);
									stuffs[i].setPosition(4, (int)(Math.random()*5));
									turn++;
								}
								else if (stuffs[i].getBounds().intersects((getters[i].getBounds()))
										&& stuffs[i].getY() == getters[i].getY()) {
									stuffs[i].setExists(false);
									stuffs[i].setPosition(4, (int)(Math.random()*5));
									turn++;
									score++;
									scoreLbl.setText("Score: " + score);
								}
							}
							else if (i == 1 || i == 2) {
								stuffs[i].setPosition(stuffs[i].getX(), stuffs[i].getY()+STUFF_SPEED);
								if (stuffs[i].getY() > 4) {
									stuffs[i].setExists(false);
									stuffs[i].setPosition((int)(Math.random()*5), 0);
									turn++;
								}
								else if (stuffs[i].getBounds().intersects((getters[i].getBounds()))
										&& stuffs[i].getX() == getters[i].getX()) {
									stuffs[i].setExists(false);
									stuffs[i].setPosition((int)(Math.random()*5), 0);
									turn++;
									score++;
									scoreLbl.setText("Score: " + score);
								}
							}
						}
					}

					draw();

					// if past turn = 19, ends game
					if (turn > 19) {
						spawnTimeline.stop();
						timeline.stop();

						endGame();
					}
				}));
		timeline.setCycleCount(Timeline.INDEFINITE);

		// sets up spawnTimeline
		spawnTimeline = new Timeline(
				new KeyFrame(Duration.millis(spawnRateMillis), event -> {
					boolean tooMany = true;
					int stuffIndex = 0;

					// calculates whether there are too many stuffs to spawn more
					while (tooMany && stuffIndex < stuffs.length) {						
						if (!stuffs[stuffIndex].exists()) {
							tooMany = false;
						}
						stuffIndex++;
					}

					// chooses random stuff that doesn't exist and makes it exist
					if (!tooMany) {
						stuffIndex = (int)(Math.random()*4);
						while (stuffs[stuffIndex].exists()) {
							stuffIndex = (int)(Math.random()*4);
						}

						stuffs[stuffIndex].setExists(true);

						draw();
					}
				}));
		spawnTimeline.setCycleCount(Timeline.INDEFINITE);

		// handles onKeyPressed
		scene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.UP && getters[0].getY() > 0) {
				getters[0].setPosition(getters[0].getX(), getters[0].getY()-1);
			}
			if (event.getCode() == KeyCode.DOWN && getters[0].getY() < 4) {
				getters[0].setPosition(getters[0].getX(), getters[0].getY()+1);
			}
			if (event.getCode() == KeyCode.LEFT && getters[1].getX() > 0) {
				getters[1].setPosition(getters[1].getX()-1, getters[1].getY());
			}
			if (event.getCode() == KeyCode.RIGHT && getters[1].getX() < 4) {
				getters[1].setPosition(getters[1].getX()+1, getters[1].getY());
			}
			if (event.getCode() == KeyCode.W && getters[3].getY() > 0) {
				getters[3].setPosition(getters[3].getX(), getters[3].getY()-1);
			}
			if (event.getCode() == KeyCode.S && getters[3].getY() < 4) {
				getters[3].setPosition(getters[3].getX(), getters[3].getY()+1);
			}
			if (event.getCode() == KeyCode.A && getters[2].getX() > 0) {
				getters[2].setPosition(getters[2].getX()-1, getters[2].getY());
			}
			if (event.getCode() == KeyCode.D && getters[2].getX() < 4) {
				getters[2].setPosition(getters[2].getX()+1, getters[2].getY());
			}

			draw();
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
		// resizes canvas according to scene
		canvas.setWidth(gamePane.getWidth());
		canvas.setHeight(gamePane.getHeight());

		// draws 4 backgrounds
		gc.setFill(Color.BLUE);
		gc.fillRect(0, 0, canvas.getWidth()/2, canvas.getHeight()/2);

		gc.setFill(Color.RED);
		gc.fillRect(canvas.getWidth()/2, 0, canvas.getWidth()/2, canvas.getHeight()/2);

		gc.setFill(Color.ORANGE);
		gc.fillRect(0, canvas.getHeight()/2, canvas.getWidth()/2, canvas.getHeight()/2);

		gc.setFill(Color.PURPLE);
		gc.fillRect(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/2, canvas.getHeight()/2);

		double unitWidth = canvas.getWidth()/2*.2;
		double unitHeight = canvas.getHeight()/2*.2;

		// draws 4 getters
		getters[0].display(getters[0].getX()*unitWidth, getters[0].getY()*unitHeight, unitWidth, unitHeight);
		getters[1].display(getters[1].getX()*unitWidth+canvas.getWidth()/2, getters[1].getY()*unitHeight, unitWidth, unitHeight);
		getters[2].display(getters[2].getX()*unitWidth, getters[2].getY()*unitHeight+canvas.getHeight()/2, unitWidth, unitHeight);
		getters[3].display(getters[3].getX()*unitWidth+canvas.getWidth()/2, getters[3].getY()*unitHeight+canvas.getHeight()/2, unitWidth, unitHeight);

		// draws 4 stuffs
		if (stuffs[0].exists()) {
			stuffs[0].display(stuffs[0].getX()*unitWidth, stuffs[0].getY()*unitHeight, unitWidth, unitHeight);
		}
		if (stuffs[1].exists()) {
			stuffs[1].display(stuffs[1].getX()*unitWidth+canvas.getWidth()/2, stuffs[1].getY()*unitHeight, unitWidth, unitHeight);
		}
		if (stuffs[2].exists()) {
			stuffs[2].display(stuffs[2].getX()*unitWidth, stuffs[2].getY()*unitHeight+canvas.getHeight()/2, unitWidth, unitHeight);
		}
		if (stuffs[3].exists()){
			stuffs[3].display(stuffs[3].getX()*unitWidth+canvas.getWidth()/2, stuffs[3].getY()*unitHeight+canvas.getHeight()/2, unitWidth, unitHeight);
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

		// resets stuffs
		for (int i = 0; i < stuffs.length; i++) {
			if (i == 0 || i == 3) {
				stuffs[i].setPosition(4, (int)(Math.random()*5));
			}
			else if (i == 1 || i == 2) {
				stuffs[i].setPosition((int)(Math.random()*5), 0);
			}
			stuffs[i].setExists(false);
		}

		spawnTimeline.playFromStart();
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
