/* Bao Nguyen
 * Brain Juice
 */

package main;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import menus.*;
import minigames.*;
import util.*;

public class SceneController extends Application implements EventHandler<ActionEvent> {
	private Stage window;
	private UserController uc;

	private LoginMenu loginMenu;
	private MainMenu mainMenu;
	private GameMenu gameMenu;
	private ProgMenu progMenu;
	private SettMenu settMenu;

	private MinigameMenu memorGameMenu;
	private MinigameMenu reactGameMenu;
	private MinigameMenu accurGameMenu;
	private MinigameMenu calcGameMenu;
	private MinigameMenu timingGameMenu;
	private MinigameMenu observGameMenu;
	private MinigameMenu flexGameMenu;

	private MinigameMenu memorProgMenu;
	private MinigameMenu reactProgMenu;
	private MinigameMenu accurProgMenu;
	private MinigameMenu calcProgMenu;
	private MinigameMenu timingProgMenu;
	private MinigameMenu observProgMenu;
	private MinigameMenu flexProgMenu;

	private TextInputDialog inputDialog;
	private Alert informDialog;
	private Alert confirmDialog;
	private Alert errorDialog;
	private Optional<ButtonType> result;

	private User currentUser;
	private Menu currentMenu;

	// starts javafx
	public static void main(String[] args) {
		launch(args);
	}

	// sets up gui
	@Override
	public void start(Stage stage) {
		// sets up window
		window = stage;
		window.setTitle("Brain Juice");
		window.setWidth(800);
		window.setHeight(600);

		window.setMinWidth(400);
		window.setMinHeight(300);

		// sets up users
		uc = new UserController();
		uc.createUsers();

		// creates and sets scene to loginMenu
		loginMenu = new LoginMenu(this, uc.getList());
		window.setScene(loginMenu.getScene());
		currentMenu = loginMenu;

		window.show();
	}

	// handles inter-menu buttons
	@Override
	public void handle(ActionEvent event) {
		Button sender = (Button) event.getSource();

		switch (sender.getText()) {
		case "Login":
			boolean nameIsValid = true;

			// user is selected
			if (loginMenu.getUsername() == null) {
				loginMenu.setUserLabel("No user selected");
				nameIsValid = false;
			}

			// "new user" is selected
			else if (loginMenu.getUsername().equals("new user")) {
				// sets up "new user" login
				inputDialog = new TextInputDialog();
				inputDialog.setTitle("New User");
				inputDialog.setHeaderText("Enter a username");

				Optional<String> name = inputDialog.showAndWait();

				// name is entered
				if (name.isPresent()) {
					// adds and sets the user and sets userLbl in mainMenu to user
					try {
						uc.addUser(name.get());
						currentUser = uc.getUser(name.get());
						if(mainMenu == null) {
							mainMenu = new MainMenu(this);
						}
						mainMenu.setUserLabel(name.get());
					}

					// catches IOException and informs the user
					catch (IOException ex) {
						errorDialog = new Alert(AlertType.INFORMATION);
						errorDialog.setTitle("Error");
						errorDialog.setHeaderText("Error");
						errorDialog.setContentText("Invalid username entered");
						errorDialog.showAndWait();

						nameIsValid = false;
					}
				}

				// name is not entered
				else {
					nameIsValid = false;
				}
			}

			// user is selected
			else {
				// sets user and sets userLbl in mainMenu to user
				currentUser = uc.getUser(loginMenu.getUsername());
				if(mainMenu == null) {
					mainMenu = new MainMenu(this);
				}
				mainMenu.setUserLabel(loginMenu.getUsername());
			}

			// selected user or name entered is valid
			if (nameIsValid) {
				if(mainMenu == null) {
					mainMenu = new MainMenu(this);
				}
				window.setScene(mainMenu.getScene());
				currentMenu = mainMenu;
			}

			break;

		case "Games":
			// creates gameMenu if not already created
			if (gameMenu == null) {
				gameMenu = new GameMenu(this);
			}

			// sets scene to gameMenu
			window.setScene(gameMenu.getScene());
			currentMenu = gameMenu;

			break;
		case "Progress":
			// creates progMenu if not already created
			if (progMenu == null) {
				progMenu = new ProgMenu(this);
			}

			// sets scene to progMenu
			window.setScene(progMenu.getScene());
			currentMenu = progMenu;

			break;
		case "Settings":
			// creates settMenu if not already created
			if (settMenu == null) {
				settMenu = new SettMenu(this);
			}

			// sets scene to settMenu
			window.setScene(settMenu.getScene());
			currentMenu = settMenu;

			break;

		case "Memory Games":
			// creates memorGameMenu if not already created
			if (memorGameMenu == null) {
				memorGameMenu = new MinigameMenu(this);
				memorGameMenu.setValues("Memory Games", "Memory Colors", "String Memory");
			}

			// sets scene to memorGameMenu
			memorGameMenu.setParent(currentMenu);
			window.setScene(memorGameMenu.getScene());
			currentMenu = memorGameMenu;

			break;
		case "Reaction Games":
			// creates gameMenu if not already created
			if (reactGameMenu == null) {
				reactGameMenu = new MinigameMenu(this);
				reactGameMenu.setValues("Reaction Games", "Flashin' Colors", "Whack-a-Troll");
			}

			// sets scene to reactGameMenu
			reactGameMenu.setParent(currentMenu);
			window.setScene(reactGameMenu.getScene());
			currentMenu = reactGameMenu;

			break;
		case "Accuracy Games":
			// creates gameMenu if not already created
			if (accurGameMenu == null) {
				accurGameMenu = new MinigameMenu(this);
				accurGameMenu.setValues("Accuracy Games", "Bullseye", "Track");
			}

			// sets scene to accurGameMenu
			accurGameMenu.setParent(currentMenu);
			window.setScene(accurGameMenu.getScene());
			currentMenu = accurGameMenu;

			break;
		case "Timing Games":
			// creates gameMenu if not already created
			if (timingGameMenu == null) {
				timingGameMenu = new MinigameMenu(this);
				timingGameMenu.setValues("Timing Games", "Crashin' Triangles", "Countdown");
			}
			timingGameMenu.setParent(currentMenu);
			window.setScene(timingGameMenu.getScene());
			currentMenu = timingGameMenu;

			break;
		case "Observation Games":
			// creates gameMenu if not already created
			if (observGameMenu == null) {
				observGameMenu = new MinigameMenu(this);
				observGameMenu.setValues("Observation Games", "Flashin' Tiles", "How Many?");
			}

			// sets scene to observGameMenu
			observGameMenu.setParent(currentMenu);
			window.setScene(observGameMenu.getScene());
			currentMenu = observGameMenu;

			break;
		case "Calculation Games":
			// creates calcGameMenu if not already created
			if (calcGameMenu == null) {
				calcGameMenu = new MinigameMenu(this);
				calcGameMenu.setValues("Calculation Games", "1 Minute Calculations", "1 Minute Patterns");
			}

			// sets scene to observGameMenu
			calcGameMenu.setParent(currentMenu);
			window.setScene(calcGameMenu.getScene());
			currentMenu = calcGameMenu;

			break;
		case "Flexibility Games":
			// creates flexGameMenu if not already created
			if (flexGameMenu == null) {
				flexGameMenu = new MinigameMenu(this);
				flexGameMenu.setValues("Flexibility Games", "Get Stuff", "Which Way?");
			}

			// sets scene to flexGameMenu
			flexGameMenu.setParent(currentMenu);
			window.setScene(flexGameMenu.getScene());
			currentMenu = flexGameMenu;

			break;

		case "Memory Progress":
			// creates gameMenu if not already created
			if (memorProgMenu == null) {
				memorProgMenu = new MinigameMenu(this);
				memorProgMenu.setValues("Memory Progress", "Memory Colors", "String Memory");
			}

			// sets scene to memorProgMenu
			memorProgMenu.setParent(currentMenu);
			window.setScene(memorProgMenu.getScene());
			currentMenu = memorProgMenu;

			break;
		case "Reaction Progress":
			// creates gameMenu if not already created
			if (reactProgMenu == null) {
				reactProgMenu = new MinigameMenu(this);
				reactProgMenu.setValues("Reaction Progress", "Flashin' Colors", "Whack-a-Troll");
			}

			// sets scene to reactProgMenu
			reactProgMenu.setParent(currentMenu);
			window.setScene(reactProgMenu.getScene());
			currentMenu = reactProgMenu;

			break;
		case "Accuracy Progress":
			// creates gameMenu if not already created
			if (accurProgMenu == null) {
				accurProgMenu = new MinigameMenu(this);
				accurProgMenu.setValues("Accuracy Progress", "Bullseye", "Track");
			}

			// sets scene to accurProgMenu
			accurProgMenu.setParent(currentMenu);
			window.setScene(accurProgMenu.getScene());
			currentMenu = accurProgMenu;

			break;
		case "Timing Progress":
			// creates gameMenu if not already created
			if (timingProgMenu == null) {
				timingProgMenu = new MinigameMenu(this);
				timingProgMenu.setValues("Timing Progress", "Crashin' Triangles", "Countdown");
			}

			// sets scene to timingProgMenu
			timingProgMenu.setParent(currentMenu);
			window.setScene(timingProgMenu.getScene());
			currentMenu = timingProgMenu;

			break;
		case "Observation Progress":
			// creates gameMenu if not already created
			if (observProgMenu == null) {
				observProgMenu = new MinigameMenu(this);
				observProgMenu.setValues("Observation Progress", "Flashin' Tiles", "How Many?");
			}

			// sets scene to observProgMenu
			observProgMenu.setParent(currentMenu);
			window.setScene(observProgMenu.getScene());
			currentMenu = observProgMenu;

			break;
		case "Calculation Progress":
			// creates gameMenu if not already created
			if (calcProgMenu == null) {
				calcProgMenu = new MinigameMenu(this);
				calcProgMenu.setValues("Calculation Progress", "1 Minute Calculations", "1 Minute Patterns");
			}

			// sets scene to calcProgMenu
			calcProgMenu.setParent(currentMenu);
			window.setScene(calcProgMenu.getScene());
			currentMenu = calcProgMenu;

			break;
		case "Flexibility Progress":
			// creates gameMenu if not already created
			if (flexProgMenu == null) {
				flexProgMenu = new MinigameMenu(this);
				flexProgMenu.setValues("Flexibility Progress", "Get Stuff", "Which Way?");
			}

			// sets scene to flexProgMenu
			flexProgMenu.setParent(currentMenu);
			window.setScene(flexProgMenu.getScene());
			currentMenu = flexProgMenu;

			break;

		case "Memory Colors":
			MemorGame0 memorGame0 = new MemorGame0();
			
			// currentMenu is memorGameMenu
			if (currentMenu == memorGameMenu) {
				result = showGameDialog(memorGame0.getGameName(), memorGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of MemorGame1 at difficulty 0
					memorGame0 = new MemorGame0(this, 0);
					window.setScene(memorGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of MemorGame1 at difficulty 1
					memorGame0 = new MemorGame0(this, 1);
					window.setScene(memorGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of MemorGame1 at difficulty 2
					memorGame0 = new MemorGame0(this, 2);
					window.setScene(memorGame0.getScene());
					break;//up dog
				}
			}

			// currentMenu is memorProgMenu
			else {
				// creates new ProgSubmenu with Memor1 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, memorGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "String Memory":
			MemorGame1 memorGame1 = new MemorGame1();
			
			// currentMenu is memorGameMenu
			if (currentMenu == memorGameMenu) {
				result = showGameDialog(memorGame1.getGameName(), memorGame1.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of MemorGame2 at difficulty 0
					memorGame1 = new MemorGame1(this, 0);
					window.setScene(memorGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of MemorGame2 at difficulty 0
					memorGame1 = new MemorGame1(this, 1);
					window.setScene(memorGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of MemorGame2 at difficulty 0
					memorGame1 = new MemorGame1(this, 2);
					window.setScene(memorGame1.getScene());
					break;
				}
			}

			// current menu is memorProgMenu
			else {
				// creates new ProgSubmenu with Memor2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, memorGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Flashin' Colors":
			ReactGame0 reactGame0 = new ReactGame0();
			
			// currentMenu is reactGameMenu
			if (currentMenu == reactGameMenu) {
				result = showGameDialog(reactGame0.getGameName(), reactGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of ReactGame1 at difficulty 0
					reactGame0 = new ReactGame0(this, 0);
					window.setScene(reactGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of ReactGame1 at difficulty 1
					reactGame0 = new ReactGame0(this, 1);
					window.setScene(reactGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of ReactGame1 at difficulty 2
					reactGame0 = new ReactGame0(this, 2);
					window.setScene(reactGame0.getScene());
					break;
				}
			}

			// currentMenu is reactProgMenu
			else {
				// creates new ProgSubMenu with React1 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, reactGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Whack-a-Troll":
			ReactGame1 reactGame1 = new ReactGame1();
			
			// currentMenu is reactGameMenu
			if (currentMenu == reactGameMenu) {
				result = showGameDialog(reactGame1.getGameName(), reactGame1.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of ReactGame2 at difficulty 0
					reactGame1 = new ReactGame1(this, 0);
					window.setScene(reactGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of ReactGame2 at difficulty 1
					reactGame1 = new ReactGame1(this, 1);
					window.setScene(reactGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of ReactGame2 at difficulty 2
					reactGame1 = new ReactGame1(this, 2);
					window.setScene(reactGame1.getScene());
					break;
				}
			}

			// currentMenu is reactProgMenu
			else {
				// creates new ProgSubMenu with React2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, reactGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Bullseye":
			AccurGame0 accurGame0 = new AccurGame0();
			
			// currentMenu is accurGameMenu
			if (currentMenu == accurGameMenu) {
				result = showGameDialog(accurGame0.getGameName(), accurGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of AccurGame1 at difficulty 0
					accurGame0 = new AccurGame0(this, 0);
					window.setScene(accurGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of AccurGame1 at difficulty 1
					accurGame0 = new AccurGame0(this, 1);
					window.setScene(accurGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of AccurGame1 at difficulty 2
					accurGame0 = new AccurGame0(this, 2);
					window.setScene(accurGame0.getScene());
					break;
				}
			}

			// currentMenu is accurProgMenu
			else {
				// creates new ProgSubMenu and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, accurGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Track":
			AccurGame1 accurGame1 = new AccurGame1();
			
			// currentMenu is accurGameMenu
			if (currentMenu == accurGameMenu) {
				result = showGameDialog(accurGame1.getGameName(), accurGame1.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of AccurGame2 at difficulty 0
					accurGame1 = new AccurGame1(this, 0);
					window.setScene(accurGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of AccurGame2 at difficulty 1
					accurGame1 = new AccurGame1(this, 1);
					window.setScene(accurGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of AccurGame2 at difficulty 2
					accurGame1 = new AccurGame1(this, 2);
					window.setScene(accurGame1.getScene());
					break;
				}
			}

			// currentMenu is accurProgMenu
			else {
				// creates new ProgSubmenu with Accur2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, accurGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Crashin' Triangles":
			TimingGame0 timingGame0 = new TimingGame0();
			
			// currentMenu is timingGameMenu
			if (currentMenu == timingGameMenu) {
				result = showGameDialog(timingGame0.getGameName(), timingGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of TimingGame1 at difficulty 0
					timingGame0 = new TimingGame0(this, 0);
					window.setScene(timingGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of TimingGame1 at difficulty 1
					timingGame0 = new TimingGame0(this, 1);
					window.setScene(timingGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of TimingGame1 at difficulty 2
					timingGame0 = new TimingGame0(this, 2);
					window.setScene(timingGame0.getScene());
					break;
				}
			}

			// currentMenu is timingProgMenu
			else {
				// creates new ProgSubmenu with Timing1 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, timingGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Countdown":
			TimingGame1 timingGame1 = new TimingGame1();
			
			// currentMenu is timingGameMenu
			if (currentMenu == timingGameMenu) {
				result = showGameDialog(timingGame1.getGameName(), timingGame1.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of TimingGame2 at difficulty 0
					timingGame1 = new TimingGame1(this, 0);
					window.setScene(timingGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of TimingGame2 at difficulty 1
					timingGame1 = new TimingGame1(this, 1);
					window.setScene(timingGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of TimingGame2 at difficulty 2
					timingGame1 = new TimingGame1(this, 2);
					window.setScene(timingGame1.getScene());
					break;
				}
			}

			// currentMenu is timingProgMenu
			else {
				// creates new ProgSubMenu with Timing2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, timingGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Flashin' Tiles":
			ObservGame0 observGame0 = new ObservGame0();
			
			// currentMenu is observGameMenu
			if (currentMenu == observGameMenu) {
				result = showGameDialog(observGame0.getGameName(), observGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of ObservGame1 at difficulty 0
					observGame0 = new ObservGame0(this, 0);
					window.setScene(observGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of ObservGame1 at difficulty 1
					observGame0 = new ObservGame0(this, 1);
					window.setScene(observGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of ObservGame1 at difficulty 2
					observGame0 = new ObservGame0(this, 2);
					window.setScene(observGame0.getScene());
					break;
				}
			}

			// currentMenu is observProgMenu
			else {
				// creates new ProgSubmenu with Observ1 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, observGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "How Many?":
			ObservGame1 observGame1 = new ObservGame1();
			
			// currentMenu is observGameMenu
			if (currentMenu == observGameMenu) {
				result = showGameDialog(observGame1.getGameName(), observGame1.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of ObservGame2 at difficulty 0
					observGame1 = new ObservGame1(this, 0);
					window.setScene(observGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of ObservGame2 at difficulty 1
					observGame1 = new ObservGame1(this, 1);
					window.setScene(observGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of ObservGame2 at difficulty 2
					observGame1 = new ObservGame1(this, 2);
					window.setScene(observGame1.getScene());
					break;
				}
			}

			// currentMenu is observProgMenu
			else {
				// creates new ProgSubmenu with Observ2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, observGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "1 Minute Calculations":
			CalcGame0 calcGame0 = new CalcGame0();
			
			// currentMenu is calcGameMenu
			if (currentMenu == calcGameMenu) {
				result = showGameDialog(calcGame0.getGameName(), calcGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of CalcGame1 at difficulty 0
					calcGame0 = new CalcGame0(this, 0);
					window.setScene(calcGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of CalcGame1 at difficulty 1
					calcGame0 = new CalcGame0(this, 1);
					window.setScene(calcGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of CalcGame1 at difficulty 2
					calcGame0 = new CalcGame0(this, 2);
					window.setScene(calcGame0.getScene());
					break;
				}
			}

			// currentMenu is calcProgMenu
			else {
				// creates new ProgSubMenu with Calc1 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, calcGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "1 Minute Patterns":
			CalcGame1 calcGame1 = new CalcGame1();
			
			// currentMenu is calcGameMenu
			if (currentMenu == calcGameMenu) {
				result = showGameDialog(calcGame1.getGameName(), calcGame1.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of CalcGame2 at difficulty 0
					calcGame1 = new CalcGame1(this, 0);
					window.setScene(calcGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of CalcGame2 at difficulty 1
					calcGame1 = new CalcGame1(this, 1);
					window.setScene(calcGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of CalcGame2 at difficulty 2
					calcGame1 = new CalcGame1(this, 2);
					window.setScene(calcGame1.getScene());
					break;
				}
			}

			// currentMenu is calcProgMenu
			else {
				// creates new ProgSubMenu with Calc2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, calcGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Get Stuff":
			FlexGame0 flexGame0 = new FlexGame0();
			
			// currentMenu is flexGameMenu
			if (currentMenu == flexGameMenu) {
				result = showGameDialog(flexGame0.getGameName(), flexGame0.getGameInstructions());

				//handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of FlexGame1 at difficulty 0
					flexGame0 = new FlexGame0(this, 0);
					window.setScene(flexGame0.getScene());
					break;
				case "Normal":
					// sets scene to new instance of FlexGame1 at difficulty 1
					flexGame0 = new FlexGame0(this, 1);
					window.setScene(flexGame0.getScene());
					break;
				case "Hard":
					// sets scene to new instance of FlexGame1 at difficulty 2
					flexGame0 = new FlexGame0(this, 2);
					window.setScene(flexGame0.getScene());
					break;
				}
			}

			// currentMenu is flexProgMenu
			else {
				// creates new ProgSubmenu with Flex1 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, flexGame0);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Which Way?":
			FlexGame1 flexGame1 = new FlexGame1();
			
			// currentMenu is flexGameMenu
			if (currentMenu == flexGameMenu) {
				result = showGameDialog(flexGame1.getGameName(), flexGame1.getGameInstructions());

				// handles the result
				switch (result.get().getText()) {
				case "Easy":
					// sets scene to new instance of FlexGame2 at difficulty 0
					flexGame1 = new FlexGame1(this, 0);
					window.setScene(flexGame1.getScene());
					break;
				case "Normal":
					// sets scene to new instance of FlexGame2 at difficulty 1
					flexGame1 = new FlexGame1(this, 1);
					window.setScene(flexGame1.getScene());
					break;
				case "Hard":
					// sets scene to new instance of FlexGame2 at difficulty 2
					flexGame1 = new FlexGame1(this, 2);
					window.setScene(flexGame1.getScene());
					break;
				}
			}

			// currentMenu is flexProgMenu
			else {
				// creates new ProgSubmenu with Flex2 values and sets scene to it
				ProgSubmenu newMenu = new ProgSubmenu(this, flexGame1);
				window.setScene(newMenu.getScene());
				currentMenu = newMenu;
			}

			break;

		case "Change Username":
			// sets up inputDialog
			inputDialog = new TextInputDialog();
			inputDialog.setTitle("Change Username");
			inputDialog.setHeaderText("Enter a new username");

			Optional<String> name = inputDialog.showAndWait();

			// name is entered
			if (name.isPresent()) {
				try {
					// renames file and removes user from list
					uc.changeUsername(name.get(), currentUser.getFile());
					uc.removeUser(currentUser.getName());

					// recreates list of users
					uc.clearUsers();
					uc.createUsers();

					// changes currentUser and userLbl in mainMenu
					currentUser = uc.getUser(name.get());
					mainMenu.setUserLabel(name.get());

					// sets up and displays informDialog
					informDialog = new Alert(AlertType.INFORMATION);
					informDialog.setTitle("Change Username");
					informDialog.setHeaderText("Success!");
					informDialog.setContentText("Username changed successfully.");
					informDialog.showAndWait();
				}

				// catches IOException and informs the user
				catch (IOException ex) {
					errorDialog = new Alert(AlertType.ERROR);
					errorDialog.setTitle("Error");
					errorDialog.setHeaderText("Error");
					errorDialog.setContentText("Invalid username entered");
					errorDialog.showAndWait();
				}
			}

			break;
		case "Delete User":
			// sets up confirmDialog
			confirmDialog = new Alert(AlertType.CONFIRMATION);
			confirmDialog.setTitle("Delete User");
			confirmDialog.setHeaderText("Are you sure you want to delete the user \"" + currentUser.getName() + "\"?");

			result = confirmDialog.showAndWait();

			// OK button selected
			if (result.get() == ButtonType.OK) {
				currentUser.deleteFile();

				// recreates list of users
				uc.clearUsers();
				uc.createUsers();

				// creates new instance of LoginMenu with new list and sets scene to it
				loginMenu = new LoginMenu(this, uc.getList());
				window.setScene(loginMenu.getScene());
				currentMenu = loginMenu;

				currentUser = null;

				// sets up and displays informDialog
				informDialog = new Alert(AlertType.INFORMATION);
				informDialog.setTitle("Delete User");
				informDialog.setHeaderText("Success!");
				informDialog.setContentText("User deleted successfully.");
				informDialog.showAndWait();
			}

			break;
		case "Clear All Progress":
			try {
				// sets up confirmDialog
				confirmDialog = new Alert(AlertType.CONFIRMATION);
				confirmDialog.setTitle("Clear Progress");
				confirmDialog.setHeaderText("Are you sure you want to clear the progress of \"" + currentUser.getName() + "\"?");

				result = confirmDialog.showAndWait();

				// OK button selected
				if (result.get() == ButtonType.OK) {
					uc.clearUser(currentUser.getName());
				}

				// sets up and displays informDialog
				informDialog = new Alert(AlertType.INFORMATION);
				informDialog.setTitle("Clear Progress");
				informDialog.setHeaderText("Success!");
				informDialog.setContentText("Progress cleared successfully.");
				informDialog.showAndWait();
			}

			// catches IOException and informs user
			catch (IOException ex) {
				errorDialog = new Alert(AlertType.ERROR);
				errorDialog.setTitle("Error");
				errorDialog.setHeaderText("Error");
				errorDialog.setContentText("Progress could not be cleared. File corrupted.");
				errorDialog.showAndWait();
			}

			break;

		case "Quit":
			window.setScene(currentMenu.getScene());

			break;

		case "Log Out":
			// recreates list of users
			uc.clearUsers();
			uc.createUsers();

			// creates new instance of LoginMenu with new list and sets scene to it
			loginMenu = new LoginMenu(this, uc.getList());
			window.setScene(loginMenu.getScene());
			currentMenu = loginMenu;

			currentUser = null;

			break;
		case "Back":
			// sets scene to currentMenu's parent menu
			window.setScene(currentMenu.getParent().getScene());
			currentMenu = currentMenu.getParent();

			break;
		case "Exit":
			window.close();

			break;
		}
	}

	private Optional<ButtonType> showGameDialog(String gameName, String instructions) {
		// sets up gameDialog
		Alert gameDialog = new Alert(AlertType.CONFIRMATION);
		gameDialog.setTitle(gameName);
		gameDialog.setHeaderText(instructions);

		// sets up difficulty buttons and cancel button
		ButtonType resultOne = new ButtonType("Easy");
		ButtonType resultTwo = new ButtonType("Normal");
		ButtonType resultThree = new ButtonType("Hard");
		ButtonType resultCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		gameDialog.getButtonTypes().setAll(resultOne, resultTwo, resultThree, resultCancel);

		return gameDialog.showAndWait();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public Menu getCurrentMenu() {
		return currentMenu;
	}

	public Stage getStage() {
		return window;
	}
}
