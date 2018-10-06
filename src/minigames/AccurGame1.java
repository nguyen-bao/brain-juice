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

import javafx.animation.Animation;
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
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.SceneController;
import util.MenuButton;
import util.Minigame;

public class AccurGame1 implements Minigame {
	private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

	// sets game information
	private final String GAME_CODE = "Accur1";
	private final String GAME_NAME = "Track";
	private final String GAME_INSTRUCTIONS = "Instructions\n"
			+ "A track will appear on the screen. Starting from \n"
			+ "red, use your mouse to guide your cursor to the \n"
			+ "green without touching the black. Try to be quick.\n"
			+ "\n"
			+ "Easy mode has a short and wide track\n"
			+ "Normal mode has a longer and thinner track\n"
			+ "Hard mode has a very long and thin track\n"
			+ "\n"
			+ "Choose your difficulty:";
	private final String SCORE_UNIT = "seconds";
	private final double[] MASTER_SCORES = {5, 15, 30};

	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Pane gamePane;
	private Canvas canvas;
	private GraphicsContext gc;

	private Label timerLbl;
	private MenuButton quitBtn;

	private int difficulty;
	private long timeAtStartMillis;
	private boolean onTrack;

	private Timeline timer;
	private Image track;
	private WritableImage resizedTrack;

	// creates empty instance
	public AccurGame1() {

	}

	public AccurGame1(SceneController sceneController, int difficulty) {
		sc = sceneController;
		this.difficulty = difficulty;

		// sets up root gamePane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up timeLbl
		timerLbl = new Label();
		timerLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up gamePane
		gamePane = new Pane();
		gamePane.setMinSize(100, 100);
		canvas = new Canvas();
		gc = canvas.getGraphicsContext2D();
		gamePane.getChildren().add(canvas);

		// creates track with given difficulty
		track = new Image("file:sprites/accurGame2/track_" + difficulty + ".png");

		// sets up quitBtn
		quitBtn = new MenuButton();
		quitBtn.setText("Quit");
		quitBtn.addActionEventHandler(event -> timer.stop());
		quitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(timerLbl, gamePane, quitBtn);

		// updates timerLbl
		timer = new Timeline(
				new KeyFrame(Duration.millis(10), event -> {
					timerLbl.setText("Time: " + DECIMAL_FORMAT.format((System.currentTimeMillis()-timeAtStartMillis)/1000.0) + " seconds");
				}));
		timer.setCycleCount(Animation.INDEFINITE);

		// checks if mouse is onTrack when moved
		gamePane.setOnMouseMoved(event -> {			
			if (resizedTrack != null) {
				// color under mouse is red
				if (resizedTrack.getPixelReader().getColor((int)event.getX(), (int)event.getY()).toString().equals("0xff0000ff")) {
					// if not onTrack before, resets timer
					if (!onTrack) {
						timeAtStartMillis = System.currentTimeMillis();
					}
					timer.playFromStart();
					onTrack = true;
				}

				// color under mouse is black
				if (resizedTrack.getPixelReader().getColor((int)event.getX(), (int)event.getY()).toString().equals("0x000000ff")) {
					// not on snapshot, stops timer
					timer.stop();
					timerLbl.setText("OFF TRACK. START AT RED.");
					onTrack = false;
				}

				// color under mouse is green and onTrack
				if (resizedTrack.getPixelReader().getColor((int)event.getX(), (int)event.getY()).toString().equals("0x008000ff") && onTrack) {
					timer.stop();		
					endGame();
				}
				draw();
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

	// resizes track
	private void draw() {
		// resizes canvas according to scene
		canvas.setWidth(gamePane.getWidth());
		canvas.setHeight(gamePane.getHeight());

		if (canvas.getWidth() > 0 && canvas.getHeight() > 0) {
			resizedTrack = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
		}
		else {
			resizedTrack = null;
		}

		if (resizedTrack != null) {

			// sets up pixelReader and pixelWriter
			PixelReader pixelReader = track.getPixelReader();
			PixelWriter pixelWriter = resizedTrack.getPixelWriter();

			double widthScale = resizedTrack.getWidth()/track.getWidth();
			double heightScale = resizedTrack.getHeight()/track.getHeight();

			// copies pixels from track into resizedTrack according to scale
			for (int y = 0; y < resizedTrack.getHeight(); y++) {
				for (int x = 0; x < resizedTrack.getWidth(); x++){
					pixelWriter.setColor(x, y, pixelReader.getColor((int)(x/widthScale), (int)(y/heightScale)));
				}
			}

			gc.drawImage(resizedTrack, 0, 0);
		}
	}

	// procedure for ending the game
	private void endGame() {
		Platform.runLater(() -> {			
			// sets up gameOverDialog
			Alert gameOverDialog = new Alert(AlertType.CONFIRMATION);
			gameOverDialog.setTitle("Game Over");

			gameOverDialog.setHeaderText("Time: " + DECIMAL_FORMAT.format((System.currentTimeMillis()-timeAtStartMillis)/1000.0) + "\n"
					+ "Mastery : " + DECIMAL_FORMAT.format(MASTER_SCORES[difficulty]) + " seconds\n"
					+ "Try Again?");

			Optional<ButtonType> result = gameOverDialog.showAndWait();

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			// updates the file with current date, score, and difficulty
			try {
				sc.getCurrentUser().updateFile(GAME_CODE, dateFormat.format(new Date()), (System.currentTimeMillis()-timeAtStartMillis)/1000.0, difficulty);
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
	void reset() {
		timerLbl.setText("OFF TRACK. START AT RED.");
		onTrack = false;

		draw();
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
