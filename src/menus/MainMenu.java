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

public class MainMenu implements Menu {
	private SceneController sc;
	private Menu parent;
	
	private Scene scene;
	private VBox root;

	private Label titleLbl;
	private Label userLbl;

	private MenuButton gameBtn;
	private MenuButton progBtn;
	private MenuButton settBtn;
	private MenuButton logoutBtn;
	private MenuButton exitBtn;

	// creates and sets up MainMenu scene
	public MainMenu(SceneController sceneController) {
		sc = sceneController;
		parent = sc.getCurrentMenu();

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up titleLbl
		titleLbl = new Label("Brain Juice");
		titleLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 36));

		// sets up userLbl
		userLbl = new Label("Signed in as " + sc.getCurrentUser().getName());
		userLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));

		// sets up gameBtn
		gameBtn = new MenuButton();
		gameBtn.setText("Games");
		gameBtn.addActionEventHandler(sc);

		// sets up progBtn
		progBtn = new MenuButton();
		progBtn.setText("Progress");
		progBtn.addActionEventHandler(sc);

		// sets up settBtn
		settBtn = new MenuButton();
		settBtn.setText("Settings");
		settBtn.addActionEventHandler(sc);

		// sets up logoutBtn
		logoutBtn = new MenuButton();
		logoutBtn.setText("Log Out");
		logoutBtn.addActionEventHandler(sc);

		// sets up exitBtn
		exitBtn = new MenuButton();
		exitBtn.setText("Exit");
		exitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(titleLbl, userLbl, gameBtn, progBtn, settBtn, logoutBtn, exitBtn);
	}
	
	public void setUserLabel(String value) {
		userLbl.setText(value);
	}

	public Scene getScene() {
		return scene;
	}

	public Menu getParent() {
		return parent;
	}
}
