/* Bao Nguyen
 * Brain Juice
 */

package util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class MenuButton  extends Button {
	private final String BUTTON_CSS = "-fx-font-size: 22px;"
			+ "-fx-font-family: \"Arial\";"
			+ "-fx-background-color: #6496c8;"
			+ "-fx-text-fill: white";

	// creates and sets up button
	public MenuButton() {
		super();
		setStyle(BUTTON_CSS);
		setPrefSize(260, 70);
	}

	// adds handler for action events
	public void addActionEventHandler(EventHandler<ActionEvent> handler) {
		addEventHandler(ActionEvent.ACTION, handler);
	}
}
