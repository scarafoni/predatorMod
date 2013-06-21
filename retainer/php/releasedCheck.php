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

	//get max and sent
	$sql = "SELECT max, sent FROM released WHERE url=:url ORDER BY ID DESC LIMIT 1";
	$sth = $dbh->prepare($sql); 
	$sth->execute(array(":url"=>$url));
	$result = $sth->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);
	$max = $result['max'];
	$sent = $result['sent'];

	if($sent < $max)
	{
		$sent = $sent + 1;
		$sql = "UPDATE released SET sent=:sent WHERE url=:url ORDER BY ID DESC LIMIT 1";
		$sth = $dbh->prepare($sql); 
		$sth->execute(array(":url"=>$url, ":sent"=>$sent));
		echo("true");
		
	}
	else {
		echo("false");
	}
}

?>
