/* Bao Nguyen
 * Brain Juice
 */

package menus;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.SceneController;
import util.*;

import java.util.ArrayList;

import javafx.collections.FXCollections;

public class LoginMenu implements Menu {
	private SceneController sc;

	private Scene scene;
	private VBox root;

	private Label titleLbl;
	private Label userLbl;

	private MenuButton loginBtn;
	private MenuButton exitBtn;

	private ComboBox<String> userCombo;

	// creates and sets up LoginMenu scene
	public LoginMenu(SceneController sceneController, ArrayList<String> userList) {
		sc = sceneController;

		// sets up root pane
		root = new VBox(15);
		root.setPadding(new Insets(15, 15, 15, 15));
		root.setAlignment(Pos.CENTER);
		scene = new Scene(root);

		// sets up titleLbl
		titleLbl = new Label("Brain Juice");
		titleLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 36));

		// sets up userLbl
		userLbl = new Label("Select the user");
		userLbl.setFont(Font.font("Arial", FontWeight.MEDIUM, 18));
		userLbl.setTextFill(Color.BLUE);

		// sets up userCombo
		userCombo = new ComboBox<>(FXCollections.observableArrayList(userList));
		userCombo.getItems().add("new user");
		userCombo.setOnAction(e -> userLbl.setText(userCombo.getValue()));

		// sets up loginBtn
		loginBtn = new MenuButton();
		loginBtn.setText("Login");
		loginBtn.addActionEventHandler(sc);

		// sets up exitBtn
		exitBtn = new MenuButton();
		exitBtn.setText("Exit");
		exitBtn.addActionEventHandler(sc);

		root.getChildren().addAll(titleLbl, userLbl, userCombo, loginBtn, exitBtn);
	}
	
	public String getUsername() {
		return userCombo.getValue();
	}
	
	public void setUserLabel(String value) {
		userLbl.setText(value);
	}

	public Scene getScene() {
		return scene;
	}

	public Menu getParent() {
		return null;
	}
}
