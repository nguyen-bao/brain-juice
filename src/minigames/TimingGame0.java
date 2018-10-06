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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class TimingGame0 implements Minigame {
	private final long DELAY_TIME_MILLIS = 500;
	private final double TRIANGLE_WIDTH = 150;
	private final double TRIANGLE_HEIGHT = 150;
	private final Color[] COLORS = {
			Color.RED, Color.AQUAMARINE, Color.LIGHTBLUE, Color.GREEN,
			Color.LIGHTGREEN, Color.BLUE, Color.ORANGE, Color.HOTPINK,
			Color.VIOLET, Color.GRAY, Color.BROWN, Color.GREENYELLOW,
			Color.DARKBLUE, Color.TAN, Color.YELLOW, Color.DARKVIOLET
	};

	// sets game information
	private final String GAME_CODE = "Timing0";
	private final String GAME_NAME = "Crashin' Triangles";
	private final String GAME_INSTRUCTIONS = "Instructions:\n"
			+ "Two triangles will come from left and right. You won't \n"
			+ "be able to see them when they approach the center. Click \n"
			+ "when you think they collide to increase your score. There \n"
			+ "is a maximum of 20 points.\n"
			+ "\n"
			+ "Easy mode triangles are slow\n"
			+ "Normal mode triangles are faster\n"
			+ "Hard mode are very fast\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "points";
	private final double[] MASTER_SCORES = {20, 20, 20};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Pane gamePane;

	private Label scoreLbl;
	private MenuButton quitBtn;

	private int difficulty;
	private int turn;
	private int score;
	private double minSpeed;
	private double maxSpeed;
	private double triSpeed;
	private boolean trianglesMoving;

	private double tri0rotate;
	private double tri0X;

	private double tri1rotate;
	private double tri1X;

	private Polygon tri0;
	private Polygon tri1;
	private Rectangle blind;
	private Rectangle background;
	private Timeline timeline;
	private Timeline animationTimeline;

	// creates empty instance
	public TimingGame0() {

	}

	public TimingGame0(SceneController sceneController, int difficulty) {	
		sc = sceneController;
		this.difficulty = difficulty;
		
		// sets triSpeed according to difficulty
		if (difficulty == 0) {
			minSpeed = 4;
			maxSpeed = 6;
		}
		else if (difficulty == 1){
			minSpeed = 6;
			maxSpeed = 8;
		}
		else {
			minSpeed = 8;
			maxSpeed = 10;
		}

		// sets up root pane
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
		quitBtn.addActionEventHandler(event -> animationTimeline.stop());
		quitBtn.addActionEventHandler(event -> timeline.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(scoreLbl, gamePane, quitBtn);

		// sets up background
		background = new Rectangle();
		background.setFill(Color.BLACK);

		// sets up blind
		blind = new Rectangle();
		blind.setFill(Color.WHITE);

		// creates triangles
		tri0 = new Polygon();
		tri1 = new Polygon();

		gamePane.getChildren().addAll(background, tri0, tri1, blind);

		// sets up timeline
		timeline = new Timeline(
				new KeyFrame(Duration.millis(DELAY_TIME_MILLIS), event -> {
					triSpeed = Math.random()*(maxSpeed-minSpeed+1)+minSpeed;
					
					// sets random color
					tri0.setFill(COLORS[(int)(Math.random()*COLORS.length)]);
					// sets random rotation [0,180)
					tri0rotate = Math.random()*180;
					// positions tri0 at left edge
					tri0X = 0;

					// sets random color
					tri1.setFill(COLORS[(int)(Math.random()*COLORS.length)]);
					// sets random rotation [0,180)
					tri1rotate = Math.random()*180;
					// positions tri1 at right edge
					tri1X = 999;
					
					tri0.setVisible(true);
					tri1.setVisible(true);
					trianglesMoving = true;
					animationTimeline.playFromStart();
				}));
		timeline.setCycleCount(1);

		animationTimeline = new Timeline(
				new KeyFrame(Duration.millis(10), event -> {
					tri0X += triSpeed;
					tri1X -= triSpeed;

					draw();
				}));
		animationTimeline.setCycleCount(Timeline.INDEFINITE);

		// handles onMousePressed
		scene.setOnMousePressed(event -> {
			if (trianglesMoving) {
				if (tri0.getBoundsInParent().intersects(tri1.getBoundsInParent())) {
					score++;
					scoreLbl.setText("Score: " + score);
				}

				// increments turn. if past turn = 19, ends game
				turn++;
				if (turn > 19) {
					timeline.stop();
					animationTimeline.stop();
					endGame();
				}
				else {
					timeline.playFromStart();
				}
				
				tri0.setVisible(false);
				tri1.setVisible(false);
				trianglesMoving = false;
				animationTimeline.stop();
				timeline.playFromStart();
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
		background.setWidth(gamePane.getWidth());
		background.setHeight(gamePane.getHeight());

		blind.setWidth(gamePane.getWidth()*.5);
		blind.setHeight(gamePane.getHeight());
		blind.setX(gamePane.getWidth()/2-gamePane.getWidth()*.25);
		blind.setY(0);
		
		double unitWidth = gamePane.getWidth()/1000;
		double unitHeight = gamePane.getHeight()/1000;

		tri0.getPoints().setAll(new Double[]{
				0.0, 0.0,
				2.0*TRIANGLE_WIDTH*unitWidth, 1.0*TRIANGLE_HEIGHT*unitHeight,
				1.0*TRIANGLE_WIDTH*unitWidth, 2.0*TRIANGLE_HEIGHT*unitHeight
		});
		tri0.setRotate(tri0rotate);
		tri0.relocate(tri0X*unitWidth-(tri0.getBoundsInLocal().getWidth()*unitWidth), 500*unitHeight);

		tri1.getPoints().setAll(new Double[]{
				0.0, 0.0,
				2.0*TRIANGLE_WIDTH*unitWidth, 1.0*TRIANGLE_HEIGHT*unitHeight,
				1.0*TRIANGLE_WIDTH*unitWidth, 2.0*TRIANGLE_HEIGHT*unitHeight
		});
		tri1.setRotate(tri1rotate);
		tri1.relocate(tri1X*unitWidth, 500*unitHeight);
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
		turn = 0;
		
		tri0.setVisible(false);
		tri1.setVisible(false);
		trianglesMoving = false;
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
