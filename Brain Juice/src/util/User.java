/* Bao Nguyen
 * Brain Juice
 */

package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class User {	
	private String name;
	private File file;

	private ArrayList<Score> data;

	// creates user with name and file
	public User (String name, File file) {		
		this.name = name;
		this.file = file;
	}

	// creates user with name and new file
	public User (String name) throws IOException {
		this.name = name;

		// creates "users" directory
		File dir = new File("users");
		dir.mkdirs();

		// instantiates file in directory with name
		this.file = new File(dir, name + ".sav");

		// throws exception if file exits, name is empty or name equals "new user"
		if (file.exists() || name.length() == 0 || name.equals("new user")) {
			throw new IOException();
		}
		file.createNewFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		// writes blank memory game data
		writer.write("Memor0 0 Scores = \r\n");
		writer.write("Memor0 0 Dates = \r\n");
		writer.write("Memor0 1 Scores = \r\n");
		writer.write("Memor0 1 Dates = \r\n");
		writer.write("Memor0 2 Scores = \r\n");
		writer.write("Memor0 2 Dates = \r\n");
		writer.write("Memor1 0 Scores = \r\n");
		writer.write("Memor1 0 Dates = \r\n");
		writer.write("Memor1 1 Scores = \r\n");
		writer.write("Memor1 1 Dates = \r\n");
		writer.write("Memor1 2 Scores = \r\n");
		writer.write("Memor1 2 Dates = \r\n");

		// writes blank reaction game data
		writer.write("React0 0 Scores = \r\n");
		writer.write("React0 0 Dates = \r\n");
		writer.write("React0 1 Scores = \r\n");
		writer.write("React0 1 Dates = \r\n");
		writer.write("React0 2 Scores = \r\n");
		writer.write("React0 2 Dates = \r\n");
		writer.write("React1 0 Scores = \r\n");
		writer.write("React1 0 Dates = \r\n");
		writer.write("React1 1 Scores = \r\n");
		writer.write("React1 1 Dates = \r\n");
		writer.write("React1 2 Scores = \r\n");
		writer.write("React1 2 Dates = \r\n");

		// writes blank accuracy game data
		writer.write("Accur0 0 Scores = \r\n");
		writer.write("Accur0 0 Dates = \r\n");
		writer.write("Accur0 1 Scores = \r\n");
		writer.write("Accur0 1 Dates = \r\n");
		writer.write("Accur0 2 Scores = \r\n");
		writer.write("Accur0 2 Dates = \r\n");
		writer.write("Accur1 0 Scores = \r\n");
		writer.write("Accur1 0 Dates = \r\n");
		writer.write("Accur1 1 Scores = \r\n");
		writer.write("Accur1 1 Dates = \r\n");
		writer.write("Accur1 2 Scores = \r\n");
		writer.write("Accur1 2 Dates = \r\n");

		// writes blank observation game data
		writer.write("Observ0 0 Scores = \r\n");
		writer.write("Observ0 0 Dates = \r\n");
		writer.write("Observ0 1 Scores = \r\n");
		writer.write("Observ0 1 Dates = \r\n");
		writer.write("Observ0 2 Scores = \r\n");
		writer.write("Observ0 2 Dates = \r\n");
		writer.write("Observ1 0 Scores = \r\n");
		writer.write("Observ1 0 Dates = \r\n");
		writer.write("Observ1 1 Scores = \r\n");
		writer.write("Observ1 1 Dates = \r\n");
		writer.write("Observ1 2 Scores = \r\n");
		writer.write("Observ1 2 Dates = \r\n");

		// writes blank flexibility game data
		writer.write("Flex0 0 Scores = \r\n");
		writer.write("Flex0 0 Dates = \r\n");
		writer.write("Flex0 1 Scores = \r\n");
		writer.write("Flex0 1 Dates = \r\n");
		writer.write("Flex0 2 Scores = \r\n");
		writer.write("Flex0 2 Dates = \r\n");
		writer.write("Flex1 0 Scores = \r\n");
		writer.write("Flex1 0 Dates = \r\n");
		writer.write("Flex1 1 Scores = \r\n");
		writer.write("Flex1 1 Dates = \r\n");
		writer.write("Flex1 2 Scores = \r\n");
		writer.write("Flex1 2 Dates = \r\n");

		// writes blank  game data
		writer.write("Timing0 0 Scores = \r\n");
		writer.write("Timing0 0 Dates = \r\n");
		writer.write("Timing0 1 Scores = \r\n");
		writer.write("Timing0 1 Dates = \r\n");
		writer.write("Timing0 2 Scores = \r\n");
		writer.write("Timing0 2 Dates = \r\n");
		writer.write("Timing1 0 Scores = \r\n");
		writer.write("Timing1 0 Dates = \r\n");
		writer.write("Timing1 1 Scores = \r\n");
		writer.write("Timing1 1 Dates = \r\n");
		writer.write("Timing1 2 Scores = \r\n");
		writer.write("Timing1 2 Dates = \r\n");

		// writes blank calculation game data
		writer.write("Calc0 0 Scores = \r\n");
		writer.write("Calc0 0 Dates = \r\n");
		writer.write("Calc0 1 Scores = \r\n");
		writer.write("Calc0 1 Dates = \r\n");
		writer.write("Calc0 2 Scores = \r\n");
		writer.write("Calc0 2 Dates = \r\n");
		writer.write("Calc1 0 Scores = \r\n");
		writer.write("Calc1 0 Dates = \r\n");
		writer.write("Calc1 1 Scores = \r\n");
		writer.write("Calc1 1 Dates = \r\n");
		writer.write("Calc1 2 Scores = \r\n");
		writer.write("Calc1 2 Dates = \r\n");

		writer.close();
	}

	public void deleteFile() {
		file.delete();
	}

	// updates the file with the given date, score, and difficulty
	public void updateFile(String gameCode, String date, double score, int difficulty) throws IOException {
		File tempFile = File.createTempFile("BrainJuice", "tempSave");

		// sets up reader for file and writer for tempFile
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		// writes each line from file to tempFile and adds score and date to correct lines
		String line = reader.readLine();
		while (line != null) {
			if (line.startsWith(gameCode + " " + difficulty + " Scores")) {
				if (!line.endsWith("= ")) {
					line = line.concat(", ");
				}
				line = line.concat("" + score);
			}
			if (line.startsWith(gameCode + " " + difficulty + " Dates")) {
				if (!line.endsWith("= ")) {
					line = line.concat(", ");
				}
				line = line.concat("" + date);
			}

			writer.write(line + "\r\n");
			line = reader.readLine();
		}

		reader.close();
		writer.close();

		// replaces file with tempFile
		file.delete();
		if (tempFile.renameTo(file) == false) {
			throw new IOException();
		}
		tempFile.delete();
	}

	// creates list of scores for given gameCode and difficulty
	public void createList(String gameCode, String scoreUnits, int difficulty) throws IOException{
		data = new ArrayList<>();

		// sets up reader
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = reader.readLine();

		String[] scores = new String[0];
		String[] dates = new String[0];

		// searches file and fills scores and dates with the correct data
		while (line != null) {
			if (line.startsWith(gameCode + " " + difficulty + " Scores = ") && !line.endsWith("= ")) {
				scores = line.split(", ");
			}
			if (line.startsWith(gameCode + " " + difficulty + " Dates = ") && !line.endsWith("= ")) {
				dates = line.split(", ");
			}
			line = reader.readLine();
		}

		reader.close();

		// if array is not empty, cuts first part off of first element
		if (scores.length > 0) {
			scores[0] = scores[0].substring((gameCode + " " + difficulty + " Scores = ").length());
			dates[0] = dates[0].substring((gameCode + " " + difficulty + " Dates = ").length());
		}

		// populates data ArrayList with formatted scores and dates
		for (int i = 0; i < scores.length; i ++) {
			if (scoreUnits.equals("points")) {
				DecimalFormat decimalFormat = new DecimalFormat("000");
				data.add(new Score(i+1, dates[i].substring(0, 10), dates[i].substring(11), decimalFormat.format(Double.parseDouble(scores[i])) + " " + scoreUnits));
			}
			else if (scoreUnits.equals("seconds")) {
				DecimalFormat decimalFormat = new DecimalFormat("000.00");
				data.add(new Score(i+1, dates[i].substring(0, 10), dates[i].substring(11), decimalFormat.format(Double.parseDouble(scores[i])) + " " + scoreUnits));
			}
		}
	}

	/* returns average score of all games played
	 * if more than 30 were played, returns the average of the past 30 games
	 * if no games were played, returns -1
	 */
	public double getAverage() {
		double sum = 0;

		if (data.size() == 0) {
			return -1;
		}
		else if (data.size() > 30) {
			for (int i = data.size()-1; i >= data.size()-30; i --) {
				sum += Double.parseDouble(data.get(i).getScore().replaceAll("[^\\d.]", ""));
			}
			return sum/30;
		}
		else {
			for (int i = 0; i < data.size(); i ++) {
				sum += Double.parseDouble(data.get(i).getScore().replaceAll("[^\\d.]", ""));
			}
			return sum/(double)data.size();
		}
	}

	public ArrayList<Score> getData() {
		return data;
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}
}
