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

public class SettMenu implements Menu {
	private SceneController sc;
	private Menu parent;
	
	private Scene scene;
	private VBox root;

	private Label titleLbl;

	private MenuButton changeUsernameBtn;
	private MenuButton deleteUserBtn;
	private MenuButton clearBtn;
	private MenuButton backBtn;

	// creates and sets up SettMenu scene
	public SettMenu(SceneController sceneController) {
		sc = sceneController;
		parent = sc.getCurrentMenu();

		// sets up root
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up titleLbl
		titleLbl = new Label("Settings");
		titleLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 36));

		// sets up changeUsernameBtn
		changeUsernameBtn = new MenuButton();
		changeUsernameBtn.setText("Change Username");
		changeUsernameBtn.addActionEventHandler(sc);

		// sets up deleteUserBtn
		deleteUserBtn = new MenuButton();
		deleteUserBtn.setText("Delete User");
		deleteUserBtn.addActionEventHandler(sc);

		// sets up clearBtn
		clearBtn = new MenuButton();
		clearBtn.setText("Clear All Progress");
		clearBtn.addActionEventHandler(sc);

		// sets up backBtn
		backBtn = new MenuButton();
		backBtn.setText("Back");
		backBtn.addActionEventHandler(sc);

		root.getChildren().addAll(titleLbl, changeUsernameBtn, deleteUserBtn, clearBtn, backBtn);
	}

	public Scene getScene() {
		return scene;
	}

	public Menu getParent() {
		return parent;
	}
}
