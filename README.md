# Project Definition

Sketch.io is a desktop application that runs on top of a multithreaded client-server architecture. The main concept this game is dependent on two different user roles as ‘Drawer’ and ‘Guesser’. The client who assigned with the ‘drawer’ role shall try to demonstrate the given word to the other clients (guessers) using respective ui component which in this case is a canvas. In addition those words are mostly provided by default but also in login page any user can recommend additional question words to help out to us. Any other client besides the drawer is assigned with the ‘guesser’ role which means that particular client group shall try to guess the answer by looking at the incoming drawing that is being drawn by the drawer. These roles are not assigned indefinitely to any user, these roles turn among players respectively and hence there can be only one drawer at a specific time interval.

## Room System

Player shall pick a username to enter the room page. After clicking the login button user will be presented with the room page. This room page consists of eight different rooms.

Rooms has several functionalities and rules that are listed down below:

- Each room can have maximum of three users that can play concurrently.
- Each room has the respective label that shows the occupancy of itself.
- Any room may be fully occupied and this is demonstrated with door images, if door image is an open door this means room is available for more users and game has not started yet and vice versa.

## User Roles
### Drawer

There is only one situation which is going the trigger the change of drawer role among clients. That situation is the coming to the end of time interval that is given as 30 seconds by default.

Drawer has several functionalities and rules that are listed down below:

**Functionalities**

- Drawer can draw on the canvas to demonstrate the given word.
- Drawer can pass the given word by using ‘PASS’ button.
- Drawer can see the updated user list.

**Rules**

* Drawer will lose 2 points for each word that he or she passed.
* Drawer will earn 2 points for each guesser who guesses the word correct.
* Drawer can not use chat field.

### Guesser

Any player except the drawer is assigned with the ‘Guesser’ role.

Guesser has several functionalities and rules that are listed down below:

**Functionalities**
- Guesser can try to guess the drawing using chat.
- Guesser can use the chat freely if he or she hasn’t give the correct answer yet.

**Rules**
* Guesser will earn 5 points for each correct answer.
* Guesser can not use canvas.
* Guesser can not see ‘Pass’ button.
* Guesser can not use chat area after guessing the correct answer.

## Game Over

Game will be over under two circumstances:

  1. If any of the players in an individual room hits the necessary score (which is 20 points by default) for winning the game, all players in that respective room will be presented with the winners name and notified with a dialog box that indicates the game is over. After dialog box closed by any one player that player will be redirected to the room page.
  2. If any of the players disconnects from the room, all the users will be notified with a dialog box and they will all be kicked back to the room page after clicking the ‘OK’ on the dialog box.

## Structural Components

### Sqlite Database 

Database structure is responsible for providing the question words in a random fashion to the drawers.

### Java Swing Framework

Swing is used for designing the graphical user interface of Sketch.io.

### JUnit Testing Framework

JUnit tests are used for testing database functionalities in a unit testing manner.

### Java IO Library

Input and output operations are used for exchanging information between server instances and clients respectively.

### Java Collections Framework

Used for certain data structures and their respective functionalities provided by collections framework.

