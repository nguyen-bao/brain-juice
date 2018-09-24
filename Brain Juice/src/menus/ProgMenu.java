/* Bao Nguyen
 * Brain Juice
 */

package menus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.SceneController;
import util.*;

public class ProgMenu implements Menu {
	private SceneController sc;
	private Menu parent;
	
	private Scene scene;
	private VBox root;
	private HBox row0;
	private HBox row1;
	private HBox row2;
	private HBox row3;

	private Label titleLbl;

	private MenuButton memorBtn;
	private MenuButton reactBtn;
	private MenuButton accurBtn;
	private MenuButton observBtn;
	private MenuButton flexBtn;
	private MenuButton timingBtn;
	private MenuButton calcBtn;
	private MenuButton backBtn;

	// creates and sets up ProgMenu scene
	public ProgMenu(SceneController sceneController) {
		sc = sceneController;
		parent = sc.getCurrentMenu();

		// sets up root
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up titleLbl
		titleLbl = new Label("Progress");
		titleLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 36));

		// sets up row0
		row0 = new HBox(15);
		row0.setAlignment(Pos.CENTER);

		// sets up memorBtn
		memorBtn = new MenuButton();
		memorBtn.setText("Memory Progress");
		memorBtn.addActionEventHandler(sc);

		// sets up reactBtn
		reactBtn = new MenuButton();
		reactBtn.setText("Reaction Progress");
		reactBtn.addActionEventHandler(sc);

		row0.getChildren().addAll(memorBtn, reactBtn);

		// sets up row1
		row1 = new HBox(15);
		row1.setAlignment(Pos.CENTER);

		// sets up accurBtn
		accurBtn = new MenuButton();
		accurBtn.setText("Accuracy Progress");
		accurBtn.addActionEventHandler(sc);

		// sets up timingBtn
		timingBtn = new MenuButton();
		timingBtn.setText("Timing Progress");
		timingBtn.addActionEventHandler(sc);

		row1.getChildren().addAll(accurBtn, timingBtn);

		// sets up row2
		row2 = new HBox(15);
		row2.setAlignment(Pos.CENTER);

		// sets up observBtn
		observBtn = new MenuButton();
		observBtn.setText("Observation Progress");
		observBtn.addActionEventHandler(sc);

		// sets up calcBtn
		calcBtn = new MenuButton();
		calcBtn.setText("Calculation Progress");
		calcBtn.addActionEventHandler(sc);

		row2.getChildren().addAll(observBtn, calcBtn);

		// sets up row3
		row3 = new HBox(15);
		row3.setAlignment(Pos.CENTER);

		// sets up flexBtn
		flexBtn = new MenuButton();
		flexBtn.setText("Flexibility Progress");
		flexBtn.addActionEventHandler(sc);

		// sets up backBtn
		backBtn = new MenuButton();
		backBtn.setText("Back");
		backBtn.addActionEventHandler(sc);

		row3.getChildren().addAll(flexBtn, backBtn);

		root.getChildren().addAll(titleLbl, row0, row1, row2, row3);
	}

	public Scene getScene() {
		return scene;
	}

	public Menu getParent() {
		return parent;
	}
}
