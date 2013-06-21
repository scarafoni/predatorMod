<?php
$type = $_REQUEST["type"];
//echo $type;
$team = $_REQUEST["team"];
//echo $team;
$id = $_REQUEST["id"];
//echo $id;
$move = $_REQUEST["move"];

$loginInfo = $type.",".$team.",".$id.",".$move."\n";
echo $loginInfo;
$socket = socket_create(AF_INET, SOCK_STREAM, 0)
	or die("couldnt create socket");

$succ = socket_connect($socket,"localhost",4444)
	or die("couldn't connect to host");

socket_write($socket,$loginInfo,strlen($loginInfo))
	or die("failed to write to socket");
socket_read($socket,1)

?>