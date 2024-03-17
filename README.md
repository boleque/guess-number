# Guess number game
![](https://github.com/boleque/guess_number_game/blob/session-as-nickname/game.gif)
## Requirements
Write backend for a guess the number game with using WebSocket for communication.  
Game process:  
1) The server starts a round of the game and gives 10 seconds to place a bet for the players on
   numbers from 1 to 10 with the amount of the bet;  
2) After the time expires, the server generates a random number from 1 to 10;  
3) If the player guesses the number, a message is sent to him that he won with a winnings of
   9.9 times the stake;  
4) If the player loses receives a message about the loss;  
5) All players receive a message with a list of winning players in which there is a nickname and
   the amount of winnings;  
6) The process is repeated.  

Technical requirements:  
Java 11+, Gradle, Spring Boot 2+  
Unit and integration test coverage  
Communication between players and the server via WebSocket  
STOMP is not used  
Validation of submitted data  

## Architecture, Implementation
For simplicity, I've implemented a simple user registration, namely when a client connects, a record of the form {Player_SesionId} is created in the database.  
In essence, the lifetime of an account is the lifetime of a session, but having an entry in the database allows to accumulate statistics.  

##  Game
The service can be run locally, no additional dependencies are required. H2 is used as a database.  
After the connection is established, you can take part in the game by sending a message in the following form roundId:stake:guess, for example **3:200:7**  