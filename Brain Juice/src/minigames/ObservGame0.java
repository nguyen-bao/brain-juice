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

public class ObservGame0 implements Minigame {
	// sets game information
	private final String GAME_CODE = "Observ0";
	private final String GAME_NAME = "Flashin' Tiles";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "Tiles with numbers will flash on the screen. Remember \n"
			+ "the order of the tiles and click them in that order \n"
			+ "to increase your score. There is a maximum of 7 points.\n"
			+ "\n"
			+ "Easy mode will flash for 1.5 seconds\n"
			+ "Normal mode will flash for 1 second\n"
			+ "Hard mode will flash for .5 seconds\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {5, 5, 5};

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
	private int currentTileIndex;
	private int numTiles;
	private long flashTimeMillis;
	private boolean inFlash;

	private Sprite[] tiles;
	private Timeline timeline;

	// creates empty instance
	public ObservGame0() {

	}

	public ObservGame0(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// stes flashTimeMillis according to difficulty
		if (difficulty == 0) {
			flashTimeMillis = 1500;
		}
		else if (difficulty == 1) {
			flashTimeMillis = 1000;
		}
		else if (difficulty == 2) {
			flashTimeMillis = 500;
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up gamePane
		gamePane = new Pane();
		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();
		gamePane.getChildren().add(canvas);

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up tiles
		tiles = new Sprite[9];
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new Sprite(gc);
			tiles[i].addFrame("sprites/observGame1/tileBack.png");
			tiles[i].addFrame("sprites/observGame1/tile_" + i + ".png");
			tiles[i].setExists(false);
		}

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.millis(0), event -> {
					inFlash = true;

					// draws tiles
					for (int i = 0; i < numTiles; i++) {
						tiles[i].setExists(true);
						tiles[i].setFrame(1);

						// sets tile positions and makes sure tiles don't intersect
						boolean intersects = true;
						while (intersects == true) {
							intersects = false;
							tiles[i].setPosition((int)(Math.random()*10), (int)(Math.random()*10));
							for (int j = 0; j < i; j++) {
								if (tiles[i].getX() == tiles[j].getX() && tiles[i].getY() == tiles[j].getY()) {
									intersects = true;
								}
							}
						}
					}
					draw();
				}),
				new KeyFrame(Duration.millis(flashTimeMillis), event -> {
					// flips tiles to the back
					for (int i = 0; i < numTiles; i++) {
						tiles[i].setFrame(0);
					}
					draw();

					inFlash = false;
				}));
		timeline.setCycleCount(1);

		// handles onMousePressed in not in flash
		scene.setOnMousePressed(event -> {
			if (!inFlash) {
				// flips current tile over
				tiles[currentTileIndex].setFrame(1);
				draw();

				// mouse is in current tile
				if(gamePane.localToScene(tiles[currentTileIndex].getBounds()).contains(event.getX(), event.getY())) {
					// increments currentTileIndex. if past last tile, resets currentTileIndex and increments turn
					currentTileIndex++;
					if (currentTileIndex > numTiles-1) {
						currentTileIndex = 0;

						// increments turn. if past turn = 6, ends game
						turn++;
						scoreLbl.setText("Score: " + turn);
						numTiles = turn+3;
						if (turn > 6) {
							timeline.stop();
							endGame();
						}
						else {
							timeline.playFromStart();
						}
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

	// draws on canvas
	private void draw() {
		// resizes canvas according to gamePane
		canvas.setWidth(gamePane.getWidth());
		canvas.setHeight(gamePane.getHeight());

		// sets background
		gc.setFill(Color.GOLD);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

		double unitWidth = canvas.getWidth()/10;
		double unitHeight = canvas.getHeight()/10;

		// displays tiles that exist
		for (int i = 0; i < tiles.length; i++) {
			if (tiles[i].exists()) {
				tiles[i].display(tiles[i].getX()*unitWidth, tiles[i].getY()*unitHeight, unitWidth, unitHeight);
			}
		}
	}

	// procedure for ending the game
	private void endGame() {
		Platform.runLater(() -> {
			// sets up gameOverDialog
			Alert gameOverDialog = new Alert(AlertType.CONFIRMATION);
			gameOverDialog.setTitle("Game Over");

			gameOverDialog.setHeaderText("Score: " + turn + "\n"
					+ "Mastery : " + (int)MASTER_SCORES[difficulty] + " points\n"
					+ "Try Again?");

			Optional<ButtonType> result = gameOverDialog.showAndWait();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// updates the file with current date, score, and difficulty
			try {
				sc.getCurrentUser().updateFile(GAME_CODE, dateFormat.format(new Date()), turn, difficulty);
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
		turn = 0;
		scoreLbl.setText("Score: " + turn);
		numTiles = turn+3;
		currentTileIndex = 0;

		for (int i = 0; i < tiles.length; i++) {
			tiles[i].setExists(false);
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