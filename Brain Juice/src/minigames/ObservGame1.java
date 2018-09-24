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
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class ObservGame1 implements Minigame {
	private final int NUM_TILES_AT_START = 3;
	private final long FLASH_TIME_MILLIS = 1000;
	private final long MAX_NUM_TURNS = 100;

	private final String GAME_CODE = "Observ1";
	private final String GAME_NAME = "How Many?";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "Different colored tiles will flash on the screen. Remember \n"
			+ "how many of each there were and enter in those amounts\n"
			+ "to increase your score.\n"
			+ "\n"
			+ "Easy mode will have 2 colors\n"
			+ "Normal mode will have 3 colors\n"
			+ "Hard mode will have 4 colors\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {5, 5, 5};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Pane canvasPane;
	private VBox inputPane;

	private Label scoreLbl;
	private MenuButton quitBtn;

	private int difficulty;
	private int turn;
	private int numBlueTiles;
	private int numRedTiles;
	private int numGreenTiles;
	private int numYellowTiles;

	private ArrayList<Rectangle> tiles;
	private ArrayList<Point2D> tileCoords;
	private ArrayList<Color> colors;
	private Timeline timeline;

	// creates empty instance
	public ObservGame1() {

	}

	public ObservGame1(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets up colors
		colors = new ArrayList<>();
		colors.add(Color.BLUE);
		colors.add(Color.RED);

		// adds colors according to difficulty
		if (difficulty > 0) {
			colors.add(Color.GREEN);
		}
		if (difficulty > 1) {
			colors.add(Color.YELLOW);
		}

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		canvasPane = new Pane();

		// sets up scoreLbl
		scoreLbl = new Label();
		scoreLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, canvasPane, quitBtn);

		// sets up inputPane
		inputPane = new VBox(15);
		inputPane.setPadding(new Insets(15, 15, 15, 15));
		inputPane.setAlignment(Pos.CENTER);
		inputPane.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(1))));

		Label promptLabel = new Label("How many tiles of each color did you see?");
		inputPane.getChildren().add(promptLabel);

		// sets up red row
		HBox row0 = new HBox(15);
		row0.setAlignment(Pos.CENTER);
		Label redLabel = new Label("Number of red tiles: ");
		TextField redTextField = new TextField();
		row0.getChildren().addAll(redLabel, redTextField);
		inputPane.getChildren().add(row0);

		// sets up blue row
		HBox row1 = new HBox(15);
		row1.setAlignment(Pos.CENTER);
		Label blueLabel = new Label("Number of blue tiles: ");
		TextField blueTextField = new TextField();
		row1.getChildren().addAll(blueLabel, blueTextField);
		inputPane.getChildren().add(row1);

		// sets up green row if difficulty is higher than easy
		TextField greenTextField = new TextField();
		if (difficulty > 0) {
			HBox row2 = new HBox(15);
			row2.setAlignment(Pos.CENTER);
			Label greenLabel = new Label("Number of green tiles: ");
			row2.getChildren().addAll(greenLabel, greenTextField);
			inputPane.getChildren().add(row2);
		}

		// sets up yellow row if difficulty is higher than normal
		TextField yellowTextField = new TextField();
		if (difficulty > 1) {
			HBox row3 = new HBox(15);
			row3.setAlignment(Pos.CENTER);
			Label yellowLabel = new Label("Number of yellow tiles: ");
			row3.getChildren().addAll(yellowLabel, yellowTextField);
			inputPane.getChildren().add(row3);
		}

		// sets up submitBtn
		Button submitBtn = new Button("Submit");
		submitBtn.setOnAction(event -> {
			// gets inputs from each of the textfields
			Integer redInput;
			try {
				redInput = Integer.parseInt(redTextField.getText());
			}
			catch (NumberFormatException ex) {
				redInput = 0;
			}
			redTextField.setText("");

			Integer blueInput;
			try {
				blueInput = Integer.parseInt(blueTextField.getText());
			}
			catch (NumberFormatException ex) {
				blueInput = 0;
			}
			blueTextField.setText("");

			Integer greenInput = 0;
			if (difficulty > 0) {
				try {
					greenInput = Integer.parseInt(greenTextField.getText());
				}
				catch (NumberFormatException ex) {
					greenInput = 0;
				}
			}
			greenTextField.setText("");

			Integer yellowInput = 0;
			if (difficulty > 1) {
				try {
					yellowInput = Integer.parseInt(yellowTextField.getText());
				}
				catch (NumberFormatException ex) {
					yellowInput = 0;
				}
			}
			yellowTextField.setText("");

			// if all inputs are correct, next turn
			if (redInput == (Integer)numRedTiles && blueInput == (Integer)numBlueTiles 
					&& greenInput == (Integer)numGreenTiles && yellowInput == (Integer)numYellowTiles) {
				turn++;
				scoreLbl.setText("Score: " + turn);
				if (turn > MAX_NUM_TURNS-1) {
					timeline.stop();
					endGame();
				}
				else {
					timeline.playFromStart();
				}
			}
			else {
				timeline.stop();
				endGame();
			}
		});
		inputPane.getChildren().add(submitBtn);

		// creates tiles and tileCoords
		tiles = new ArrayList<>();
		tileCoords = new ArrayList<>();

		timeline = new Timeline(
				new KeyFrame(Duration.millis(0), event -> {					
					tiles.clear();
					tileCoords.clear();
					canvasPane.getChildren().clear();
					numBlueTiles = 0;
					numRedTiles = 0;
					numGreenTiles = 0;
					numYellowTiles = 0;

					// sets up tiles and tileCoords
					for (int i = 0; i < NUM_TILES_AT_START+turn; i++) {
						// creates a new tile with random fill and adds it
						Rectangle newTile = new Rectangle();
						int colorIndex = (int)(Math.random()*colors.size());
						newTile.setFill(colors.get(colorIndex));
						tiles.add(newTile);
						canvasPane.getChildren().add(newTile);

						// updates number of tiles for each color
						if (colorIndex == 0) {
							numBlueTiles++;
						}
						else if (colorIndex == 1) {
							numRedTiles++;
						}
						else if (colorIndex == 2) {
							numGreenTiles++;
						}
						else if (colorIndex == 3) {
							numYellowTiles++;
						}

						// adds random tileCoord that doesn't intersect with others
						tileCoords.add(new Point2D((int)(Math.random()*10), (int)(Math.random()*10)));
						boolean intersects = true;
						while (intersects) {
							tileCoords.set(i, new Point2D((int)(Math.random()*10), (int)(Math.random()*10)));
							intersects = false;
							for (int j = 0; j < i; j++) {
								if (tileCoords.get(i).equals(tileCoords.get(j))) {
									intersects = true;
								}
							}
						}
					}

					// sets canvasPane
					root.getChildren().setAll(scoreLbl, canvasPane, quitBtn);
					draw();
				}),
				new KeyFrame(Duration.millis(FLASH_TIME_MILLIS), event -> {
					// sets inputPane
					root.getChildren().setAll(scoreLbl, inputPane, quitBtn);
				}));
		timeline.setCycleCount(1);

		// binds gamePane dimensions to scene dimensions
		inputPane.prefWidthProperty().bind(scene.widthProperty());
		inputPane.prefHeightProperty().bind(scene.heightProperty());

		// binds gamePane dimensions to scene dimensions
		canvasPane.prefWidthProperty().bind(scene.widthProperty());
		canvasPane.prefHeightProperty().bind(scene.heightProperty());

		// redraw when gamePane dimensions change
		canvasPane.widthProperty().addListener(observable -> Platform.runLater(() -> draw()));
		canvasPane.heightProperty().addListener(observable -> Platform.runLater(() -> draw()));

		reset();
	}

	// draws tiles
	private void draw() {
		double unitWidth = canvasPane.getWidth()/10;
		double unitHeight = canvasPane.getHeight()/10;

		// draws tiles according to canvasPane dimensions
		for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).setX(tileCoords.get(i).getX()*unitWidth);
			tiles.get(i).setY(tileCoords.get(i).getY()*unitHeight);

			tiles.get(i).setWidth(unitWidth);
			tiles.get(i).setHeight(unitHeight);
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
		root.getChildren().setAll(scoreLbl, canvasPane, quitBtn);
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