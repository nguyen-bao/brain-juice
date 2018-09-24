/* Bao Nguyen
 * Brain Juice
 */

package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class UserController {
	private ArrayList<User> users = new ArrayList<>();

	// populates users ArrayList
	public void createUsers() {
		// creates "users" directory and fills array with its contents
		File dir = new File("users");
		File[] files = dir.listFiles();

		// array is not empty
		if (files !=null) {
			// creates new users from files and adds them to users ArrayList
			for (int i = 0; i < files.length; i ++) {
				if (files[i].getName().substring(files[i].getName().length()-4).equalsIgnoreCase(".sav")) {
					users.add(new User(files[i].getName().substring(0, files[i].getName().length() - 4), files[i]));
				}
			}
		}
	}

	public void clearUsers() {
		users.clear();
	}

	// returns list of usernames
	public ArrayList<String> getList() {
		// creates and populates usernames ArrayList with names from users
		ArrayList<String> usernames = new ArrayList<>();
		for (int i = 0; i < users.size(); i ++) {
			usernames.add(users.get(i).getName());
		}
		return usernames;
	}

	// searches users ArrayList and returns the user with the given name
	// if no user has that name, returns 0
	public User getUser(String name) {
		if (users.size() > 0) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getName().equals(name)) {
					return users.get(i);
				}
			}
		}
		return null;
	}

	// adds new user to users ArrayList with given name
	public void addUser(String name) throws IOException {
		users.add(new User(name));
	}

	// changes the name of given file to newName
	public void changeUsername(String newName, File file) throws IOException {
		// creates "users" directory
		File dir = new File("users");
		dir.mkdirs();

		// creates a new file in directory with newName and renames file to it
		File newFile = new File(dir, newName + ".sav");
		if (newFile.exists()) {
			throw new IOException();
		}
		if (file.renameTo(newFile) == false) {
			throw new IOException();
		}
	}

	// searches users ArrayList for user with given name and removes it
	public void removeUser(String name) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getName().equals(name)) {
				users.get(i).deleteFile();
				users.remove(i);
			}
		}
	}

	// removes user with given name and creates a new one with the same name
	public void clearUser(String name) throws IOException {
		removeUser(name);
		users.add(new User(name));
	}
}