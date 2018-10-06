# Brain Juice
A compilation of minigames that train and track your brain on abilities like timing, reaction, and memory.

### Features
- 7 categories of minigames (Memory, Reaction, Accuracy, Timing, Observation, Calculation, and Flexibility)
- 2 minigames in every category, each with 3 difficulties
- charts and graphs scores for each game
- user management (new user, delete user, clear scores, change name)

### Details
- developed in `Java 8`
- used `JavaFX` for GUI
- used `File I/O` for user save files

### Screenshots
![Crashin' Triangles](https://i.imgur.com/D8PuFlf.png)
  - "Crashin' Trangles" is a timing minigame in which the user scores points by clicking when the two triangles are touching

![Progress](https://i.imgur.com/KYtPjs0.png)
  - The progress page of every game stores all past scores and graphs them

### Setup
Clone this repository. You will need `Java` installed.
Compile all source files (`javac main\*.java menus\*.java minigames\*.java util\*.java` in src folder).
Execute main.SceneController (`java main.SceneController` in src folder).

### Status
January 2017 - May 2017 (Completed)

### Reflection
Brain Juice was my first real project in Java. I used the OOP knowledge I had acquired throughout my Computer Science program in high school to create something unique. The project was a great learning experience for me.

I had to learn how to:
- create a GUI using `JavaFX`
- develop an algorithm for every minigame
- use `EventHandler` and `Timer` to manage the internal state of the minigame
- use `File I/O` to store save data
