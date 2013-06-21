<?php
function getDatabaseHandle() {
  $dbh = new PDO("mysql:host=localhost;dbname=retainer", "retainer", "welcometothecrowd");
  return $dbh;
}
?>
