PREDATOR VS PREY

this game involves several teams of predators (cats) and several teams of prey (mice). The predators try and catch the prey. If they catch all the prey, they win.
There are two major components to the project- the server side program and the client side


Server Side:

The server side program is a java program that takes recieves input from players, updates each players vision of the playing map, and sends the respective maps to the players.
the .jar (gserver.jar) is run as follows: from the directory /predator, in a console (i prefer putty session) type 
"java -jar gserver.jar port preyTeams, predatorTeams, showSide, showTeam, radius".

where:
	port- the port to be used by the server for connection

	preyTeams- the number of teams of prey (an integer)

	predTeams- number of predator teams (an integer)

	showSide- allow the players to see members of the same type (predator/prey) through the fog of war (true or false)

	showTeam- similar to showSide, but with members of the same team only (true or false)

	radius- the sight radius when fog of war is enabled, set to 0 to disable fog of war, any other integer will enable fog of war. (an integer)

	example: enter "java -jar gserver.jar 4444 1 1 false false 2"
	this will start a game listening through port 4444 with 1 prey team, 1 predator team (each team 0 on their side) each player will not be able to see members on their side or team through the fog of war, 
	the fog of war will be enabled with players able to see 2 spaces in either direction


Client Side:

I have made a broswer version of this game using javascript and html tables, however, client side functions can theoretically be done by any program
index.php is the php file that is first accessed, it contains instructions for the player and forms
to fill out. simply mark the appropriate numbers (teams for each side start at 0, and both sides' teams can have the same ids (i.e., both can have a team 0) 

play.php contains the html for the actual game board.
use the arrow keys to move, your piece is the one with the corresponding id.

you should go to the index.php file first, it's url is here-
http://roc.cs.rochester.edu/predator/site/index.php


for individuals wishing to construct alternate clients:

when you want to move (or create a new player, the program creates a new player and then moves it if you try to move a non existing player), connect to the
socket that the server is listening on (the web client arbitrarily uses port 4444), and send it the following string: "type,team,id,direction"
where:
	type- is predator or prey :: <{predator,prey}>
	team- is the team number of a piece :: <#>
	id- is the piece id :: <string>
	direction- is the direction you want it to move (use w,a,s,d as analogies of the arrow keys for direction :: <{w,a,s,d}>
		w- up
		a- left
		s- down
		d- right

The server program will then send a String version of the game map to the client, which consist of the following symbols:
	,- an empty space on the board
	;- an end of line on the board
	(number)- the side (0 for prey , 1 for predator), team#, and id of a player on the board (in that order for every player)
	X- a piece of the board obscured by the fog of war

 ==> So, spacs are delimited by commas (,) lines are delimited by semi-colons (;) non-visible spaces are X's, and players are the corresponding ID number
																	
The connection is terminated after this move is made, to make another move, then rinse and repeat the above steps.


