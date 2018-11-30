# README
## URL TO CLONE:
    https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0652

## ANDROID STUDIO SETUP INSTRUCTIONS:
On Android Studio press check out project from version control, select Git.
When prompted use the given URL and the directory below the URL should
end with \StudioProjects\group_0652. Clone the project.
Click yes when asked if you want to create an android studio project
Have Import project from external model selected, with Gradle selected.
Set the Gradle Project to be: StudioProjects\group_0652\Phase2\GameCentre
Without changing any other configurations, press Finish.
Press Ok if you are prompted To sync android SDKs.
Unregistered VSC root detected message will appear. Select add root.
Now the app may be successfully run. As per the assignment requirements,
we emulated our software using a Pixel 2 when writing this program,
running API 27, and we encourage our users to run the app with the same
conditions.
​    
## DESCRIPTION OF VARIOUS IMPLEMENTED FUNCTIONALITIES:
	Once the app has been configured by above steps, the app may run. When the app runs, the 
	Game Launch Centre page will appear. Here the user has several features, listed below: 
### Game Centre
- **HIGH SCORE** - This button will handles the per-user score. Pressing this button will show 
     ​    the best score for each game. For example, in our game center we implemented 3 games,
     ​    Draughts, 2048 and Sliding Tiles, pressing this button will show the best scores achieved
         for the three games. It will not show how you perform against other users.
- **CREATE ACCOUNT** - There are 2 text fields which allow user to type their username and password.
     ​    When the "CREATE ACCOUNT" button is pressed, and the username already exist,
     ​    a pop up text "User Already Exists" will appear. If new account is
     ​    successfully created, a pop up text "User Added".
- **LOGIN** - This button will validate the username and password entered in the text fields. If the
     ​    text fields are both empty, then the a pop up text "Incorrect Username". If the user
     ​    enters a correct credentials, then a pop up text "Welcome (username)" will appear. 
     ​    Subsequently, a "LOG OUT" button will appear.In cases where the user decides not to 
     ​    login and play the game, then the user will be playing as "Guest".
     
- **Game Launcher** - There are three buttons on the top of the screen, each game having their own
         respective button. The buttons are labeled Sliding Tiles, Draughts and 2048 and when tapped
         will launch the game on the label.
     
    
    
### Sliding Tiles Game
- **NEW GAME** - Pressing this button will prompt a cyan coloured pop up to appear on screen.
         If the user mistakenly pressed the new game button, the user may revert this mistake by 
         either pressing back on their phone, or pressing anywhere on screen off of the pop up;
         this will return you to the Game Launch Centre page. On the Pop up, the text "Undo Limit" 
         will appear. Underneath this text, the  user may choose how many undos they wish to play 
         with (From 0 to 7 undos) by selecting a value from the spinner directly below the text.
         By default this spinner is configured to 3 undos. Directly underneath this spinner the 
         text "Select the Puzzle Board Size:" will appear. Underneath this text, the user may 
         choose the puzzle game complexity they wish to play with (options are 3x3, 4x4, 5x5) 
         on the spinner below. By default, this is configured to play with 4x4 complexity. 
         Directly underneath this spinner the text "Choose From Picture or Number Tiles:" will 
         appear. Underneath this text, the user may choose to either play with Numbered Tiles 
         or Picture Tiles on a spinner. Once the user has made their selections, below all the
         spinners is a button that says "START NEW GAME". Pressing this button will send the 
         user to a tile game with the configurations they've chosen on the pop up. This puzzle 
         game is immediately saved after "START NEW GAME" is pressed.
- **SAVE GAME** - Save the state of the board, total steps taken, the size of the board, the user
     ​    playing the puzzle.
- **LOAD SAVE FILE** - This button will allow user to continue playing the last saved state of the 
     ​    puzzle. When it was pressed, it will read the last saved BoardManager from
     ​    the file and present it to the user. If no games have been played prior to this button
         being pressed, a puzzle game with the default parameters listed in "NEW GAME" will
         appear for the user to play.               
- **HIGH SCORE** - Pressing this button will show the user the per-game score. It will show the top 
     ​    5 scores for each complexity, where the score is measured based on the total 
     ​    number of steps to solve the puzzle. The fewer the total number of steps taken, 
     ​    the higher the ranking. If the player never played the game before, then it will
     ​    show "No record found for puzzle complexity)". Each user only can view his/her 
     ​    score and the user's score will not be compared against other users. Assuming 
     ​    the user never login and plays the game, then all the scores will be save under 
     ​    the username "Guest". 

- **Auto Save** - In addition to the save Button, we've implemented the feature that the game 
     ​    will auto save the game every 3 moves (not counting undos). The type of data saved
     ​    is the same as the implementation of "SAVE GAME" button.

  **Undo** - Also, in game there is an UNDO button that undos your last move one by one. 
            There is a limit on undos allowed set by the spinner above start new game labeled 
            "undo limit".

### Draughts






### 2048