/* Bao Nguyen
 * Brain Juice
 */

package menus;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Window;
import main.SceneController;
import util.Menu;
import util.MenuButton;
import util.Minigame;
import util.Score;

public class ProgSubmenu implements Menu {
	private final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

	private SceneController sc;
	private Menu parent;

	private Scene scene;
	private VBox root;
	private HBox row0;
	private HBox row1;

	private Label titleLbl;
	private Text infoText;

	private MenuButton easyBtn;
	private MenuButton normBtn;
	private MenuButton hardBtn;

	private TableView<Score> table;

	private MenuButton graphBtn;
	private MenuButton backBtn;

	private Minigame game;
	private int currentDifficulty;

	// creates and sets up ProgSubmenu scene
	@SuppressWarnings("unchecked")
	public ProgSubmenu(SceneController sceneController, Minigame miniGame) {
		sc = sceneController;
		parent = sc.getCurrentMenu();

		game = miniGame;

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up titleLbl
		titleLbl = new Label(game.getGameName() + " Progress");
		titleLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 36));

		infoText = new Text();

		// sets up row0
		row0 = new HBox(15);
		row0.setPadding(new Insets(15, 15, 15, 15));
		row0.setAlignment(Pos.CENTER);

		// sets up buttons
		easyBtn = new MenuButton();
		easyBtn.setText("Easy");
		normBtn = new MenuButton();
		normBtn.setText("Normal");
		hardBtn = new MenuButton();
		hardBtn.setText("Hard");

		row0.getChildren().addAll(easyBtn, normBtn, hardBtn);

		// sets up table and columbs
		table = new TableView<>();

		TableColumn<Score, String> indexCol = new TableColumn<>("#");
		indexCol.setCellValueFactory(new PropertyValueFactory<>("index"));

		TableColumn<Score, String> dateCol = new TableColumn<>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<Score, String> timeCol = new TableColumn<>("Time");
		timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

		TableColumn<Score, String> scoreCol = new TableColumn<>("Score");
		scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

		table.getColumns().addAll(indexCol, dateCol, timeCol, scoreCol);

		easyBtn.setOnAction(event -> {
			currentDifficulty = 0;
			update(currentDifficulty);
		});

		normBtn.setOnAction(event -> {
			currentDifficulty = 1;
			update(currentDifficulty);
		});

		hardBtn.setOnAction(event -> {
			currentDifficulty = 2;
			update(currentDifficulty);
		});

		// sets up row1
		row1 = new HBox(15);
		row1.setPadding(new Insets(15, 15, 15, 15));
		row1.setAlignment(Pos.CENTER);

		// sets up graphBtn
		graphBtn = new MenuButton();
		graphBtn.setText("Graph");
		graphBtn.addActionEventHandler(event -> {
			ArrayList<Score> data = sc.getCurrentUser().getData();

			// creates and labels axes
			NumberAxis xAxis = new NumberAxis();
			NumberAxis yAxis = new NumberAxis();
			if (data.size() <= 30) {
				xAxis.setLabel("Past " + data.size() + " games");
			}
			else {
				xAxis.setLabel("Past 30 games");
			}
			yAxis.setLabel("Score (" + game.getScoreUnit() + ")");

			LineChart<Number, Number> graph = new LineChart<>(xAxis, yAxis);

			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName("Progress");

			// fills the graph with data
			int dataSize  = data.size();
			if (dataSize > 30) {
				dataSize = 30;
			}
			for (int i = 0; i < dataSize; i++) {
				XYChart.Data<Number, Number> point;
				if (game.getScoreUnit().equals("points")) {
					point = new XYChart.Data<Number, Number>(i+1, Integer.parseInt(data.get(i).getScore().substring(0, 3)));
				}
				else {
					point = new XYChart.Data<Number, Number>(i+1, Double.parseDouble(data.get(i).getScore().substring(0, 6)));
				}
				series.getData().add(point);
			}

			graph.getData().add(series);

			// sets up dialog
			Dialog<?> dialog = new Dialog<>();
			dialog.setTitle(game.getGameName());
			dialog.setResizable(true);
			dialog.initModality(Modality.NONE);
			
			Window window = dialog.getDialogPane().getScene().getWindow();
			window.setOnCloseRequest(closeEvent -> window.hide());

			// sets header text
			if (currentDifficulty == 0) {
				dialog.setHeaderText("Easy Mode");
			}
			else if (currentDifficulty == 1) {
				dialog.setHeaderText("Normal Mode");
			}
			else {
				dialog.setHeaderText("Hard Mode");
			}

			dialog.getDialogPane().setContent(graph);
			dialog.show();
		});

		// sets up backBtn
		backBtn = new MenuButton();
		backBtn.setText("Back");
		backBtn.addActionEventHandler(sc);

		row1.getChildren().addAll(graphBtn, backBtn);

		root.getChildren().addAll(titleLbl, row0, infoText, table, row1);

		easyBtn.fire();
	}

	private void update(int currentDifficulty) {
		// creates list at currentDifficulty
		try {
			sc.getCurrentUser().createList(game.getGameCode(), game.getScoreUnit(), currentDifficulty);
		}
		// catches IOException and informs user
		catch (IOException ex) {
			Alert errorDialog = new Alert(AlertType.ERROR);
			errorDialog.setTitle("Error");
			errorDialog.setHeaderText("Error");
			errorDialog.setContentText("Progress could not be calculated. File corrupted.");
			errorDialog.showAndWait();
		}

		String info = "";

		// adds difficulty to info string according to currentDifficulty
		if (currentDifficulty == 0) {
			info = info.concat("Easy\n");
		}
		else if (currentDifficulty == 1) {
			info = info.concat("Normal\n");
		}
		else {
			info = info.concat("Hard\n");
		}

		// average is -1 (no games were played)
		if (sc.getCurrentUser().getAverage() == -1) {
			info = info.concat("Average (Past 30 games): No games played yet\n"
					+ "NOT MASTERED. Mastery: " + DECIMAL_FORMAT.format(game.getMasterScore(currentDifficulty)) + " " + game.getScoreUnit());
		}
		// average is less than score for mastery
		else if ((sc.getCurrentUser().getAverage() < game.getMasterScore(currentDifficulty) && game.getScoreUnit().equals("points")) ||
				(sc.getCurrentUser().getAverage() > game.getMasterScore(currentDifficulty) && game.getScoreUnit().equals("seconds"))) {
			info = info.concat("Average (Past 30 games): " + DECIMAL_FORMAT.format(sc.getCurrentUser().getAverage()) + " " + game.getScoreUnit() + "\n"
					+ "NOT MASTERED. Mastery: " + DECIMAL_FORMAT.format(game.getMasterScore(currentDifficulty)) + " " + game.getScoreUnit());
		}
		// average is greater than or equal to score for mastery
		else {
			info = info.concat("Average (Past 30 games): " + DECIMAL_FORMAT.format(sc.getCurrentUser().getAverage()) + " " + game.getScoreUnit() + "\n"
					+ "MASTERED. Mastery: " + DECIMAL_FORMAT.format(game.getMasterScore(currentDifficulty)) + " " + game.getScoreUnit());
		}

		infoText.setText(info);

		table.setItems(FXCollections.observableArrayList(sc.getCurrentUser().getData()));
	}

	public Scene getScene() {
		return scene;
	}

	public Menu getParent() {
		return parent;
	}
}
