<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);

include('../_db.php');

  try {
      $dbh = getDatabaseHandle();
  } catch( PDOException $e ) {
      echo $e->getMessage();
  }


if( $dbh ) {
    $session = $_REQUEST['session'];
    $status = $_REQUEST['status'];

    $cqry = $dbh->prepare("SELECT COUNT(*) as numRows FROM liveStatus WHERE session=:session");
    $cqry->execute(array(":session"=>$session));

    $cary = $cqry->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);
    $count = intval($cary['numRows']);

    if( $count > 0 ) {
        $query = $dbh->prepare("UPDATE liveStatus SET status=:status WHERE session=:session");
        $query->execute(array(":status"=>$status, ":session"=>$session));
        echo("UPDATE liveStatus SET status=$status WHERE session=$session");
    }
    else {
        $query = $dbh->prepare("INSERT INTO liveStatus (session, status) VALUES(:session, :status)");
        $query->execute(array(":status"=>$status, ":session"=>$session));
        echo("INSERT INTO liveStatus (session, status) VALUES($session, $status)");
    }
    //$ary = $query->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);

}

?>
