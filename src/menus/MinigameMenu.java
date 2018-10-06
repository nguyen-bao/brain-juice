/* Bao Nguyen
 * Brain Juice
 */

package menus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.SceneController;
import util.Menu;
import util.MenuButton;

public class MinigameMenu implements Menu {
	private SceneController sc;
	private Menu parent;

	private Scene scene;
	private VBox root;

	private Label titleLbl;

	private MenuButton gameBtn1;
	private MenuButton gameBtn2;
	private MenuButton backBtn;

	// creates and sets up MinigameMenu scene
	public MinigameMenu(SceneController sceneController) {
		sc = sceneController;
		parent = sc.getCurrentMenu();

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up titleLbl
		titleLbl = new Label();
		titleLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 36));

		// sets up gameBtn1
		gameBtn1 = new MenuButton();
		gameBtn1.addActionEventHandler(sc);

		// sets up gameBtn2
		gameBtn2 = new MenuButton();
		gameBtn2.addActionEventHandler(sc);

		// sets up backBtn
		backBtn = new MenuButton();
		backBtn.setText("Back");
		backBtn.addActionEventHandler(sc);

		root.getChildren().addAll(titleLbl, gameBtn1, gameBtn2,	backBtn);
	}

	// sets values of titleLbl and game buttons
	public void setValues(String title, String gameBtn1Text, String gameBtn2Text) {
		titleLbl.setText(title);
		gameBtn1.setText(gameBtn1Text);
		gameBtn2.setText(gameBtn2Text);
	}

	public Scene getScene() {
		return scene;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}
}
