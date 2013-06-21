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

	$url = $_REQUEST['url'];
	$task = $_REQUEST['task'];
	
	$sql = "SELECT link FROM triggerFlag ORDER BY id DESC LIMIT 1";
	$sth = $dbh->prepare($sql); 
	$sth->execute();
	$result = $sth->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);
	$link = $result['link'];

	// If there are no empty tasks it means there are no users in the pool, tasks currently only made by user entering pool when no open task exists
	if( $link != null && $link != "") {
		// $query = $dbh->prepare("INSERT INTO triggerFlag(task) VALUES(:task)");
// 		$sth1 = $dbh->prepare($query); 
// 		$sth1->execute(array(':task' => $task));

	}
	else {
		$sql1 ="UPDATE triggerFlag SET link=:url, fireTime=now() WHERE task=:task";
		$sth1 = $dbh->prepare($sql1); 
		$sth1->execute(array(':url' => $url, ':task' => $task));

		//$sql1 ="UPDATE triggerFlag SET fireTime = now() WHERE task = :task";
		//$sth1 = $dbh->prepare($sql1); 
		//$sth1->execute();
	}
	
}

?>
