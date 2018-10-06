/* Bao Nguyen
 * Brain Juice
 */

package util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Score {
	private final SimpleIntegerProperty index;
	private final SimpleStringProperty date;
	private final SimpleStringProperty time;
	private final SimpleStringProperty score;

	// creates and properties
	public Score(Integer index, String date, String time, String score) {
		this.index = new SimpleIntegerProperty(index);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.score = new SimpleStringProperty(score);
	}

	public Integer getIndex() {
		return index.get();
	}
	
	public void setIndex(Integer index) {
		this.index.set(index);
	}
	
	public String getDate() {
		return date.get();
	}
	
	public void setDate(String date) {
		this.date.set(date);
	}

	public String getTime() {
		return time.get();
	}
	
	public void setTime(String time) {
		this.time.set(time);
	}

	public String getScore() {
		return score.get();
	}
	
	public void setScore(String score) {
		this.score.set(score);
	}
}
