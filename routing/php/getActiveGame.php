<?php
error_reporting(E_ALL);
ini_set("display_errors", 1);

include('getDB.php');

// Create a random 15 character string for the session
function get_id($len) {
    $charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    $retStr = "";
    $charSetSize = strlen( $charSet );
    for( $i = 0; $i < $len; $i++ ) {
        $retStr .= $charSet[ rand( 0, $charSetSize - 1 ) ];
    }

    return $retStr;
}


  try {
      $dbh = getDatabaseHandle();
  } catch( PDOException $e ) {
      echo $e->getMessage();
  }


if( $dbh ) {
    // Find out if there is already a waiting session, and if so, connect to it
    $cqry = $dbh->prepare("SELECT COUNT(*) as numRows FROM chatSessions WHERE numUsers=0 AND done=0");
    $cqry->execute(array());

    $cary = $cqry->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);
    $count = intval($cary['numRows']);

    $sqry = $dbh->prepare("SELECT session FROM chatSessions WHERE numUsers=0 AND done=0");
    $sqry->execute(array());

    $sary = $sqry->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);

    $session = "";
    $role = null;


    if( $count > 0 ) {
        $session = $sary['session'];
        $query = $dbh->prepare("UPDATE chatSessions SET numUsers=1 WHERE session=:session");
        $query->execute(array(":session"=>$session));
        $role = "user";
    }
    else {
        $session = get_id(10);
        $query = $dbh->prepare("INSERT INTO chatSessions (session, numWorkers, numUsers, done) VALUES(:session, 1, 0, 0)");
        $query->execute(array(":session"=>$session));
        $role = "worker";
    }
    //$ary = $query->fetch(PDO::FETCH_ASSOC, PDO::FETCH_ORI_NEXT);

    $id = get_id(15);
    $query = $dbh->prepare("INSERT INTO chatWorkers (id, assignedSession, assignedRole) VALUES(:id, :session, :role)");
    $query->execute(array(":id"=>$id, ":session"=>$session, ":role"=>$role));

    // Otherwise, create a new session


    print($id . "|" . $session . "|" . $role);
}

?>
