/* Bao Nguyen
 * Brain Juice
 */

package util;

import javafx.scene.Scene;

public interface Minigame {
	public String getGameCode();
	
	public String getGameName();
	
	public String getGameInstructions();
	
	public String getScoreUnit();
	
	public double getMasterScore(int difficulty);

	public Scene getScene();
}
