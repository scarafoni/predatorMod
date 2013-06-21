<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);

include('_db.php');

  try {
      $dbh = getDatabaseHandle();
  } catch( PDOException $e ) {
      echo $e->getMessage();
  }


if( $dbh ) {
	$url = $_REQUEST["url"];
	$max = $_REQUEST["max"];
	$zero = 0;

	//make new entry
	$sql = "INSERT INTO released (url, max, sent) VALUES (:url, :max, :sent)";
	$sth = $dbh->prepare($sql); 
	$sth->execute(array(':url'=>$url, ':max'=>$max, ':sent'=>$zero));
	}


?>
