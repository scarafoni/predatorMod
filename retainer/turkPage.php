<?php
	$id = getNextId();
	
	function getNextId() 
	{
		$file = fopen("ids.php","r");
		$idi = fgets($file);
		fclose($file);
		
		$file = fopen("ids.php","w");
		fwrite($file,++$idi);
		fclose($file);
		return $idi;
	
	}
?>
<!DOCTYPE HTML>
<head>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script type="text/javascript" src="scripts/gup.js"></script>
    <script type="text/javascript" src="scripts/updateEndTime.js"></script>
    <script type="text/javascript" src="scripts/getMoneyOwed.js"></script>
    <script type="text/javascript" src="scripts/triggerCheck.js"></script>
    <script type="text/javascript" src="scripts/setOnline.js"></script>
	<title>Predator vs Prey</title>
</head>
<body>
	<script>
		$(document).ready(function() {
			document.getElementById('assignmentId').value = gup('assignmentId');
		});

	</script>
	<h2> please read the instructions below then click the button to be transferred to the waiting room when you are ready</h2>

    <h3>Task Instructions:</h3>
   <p> you will be placed on a grid like game, you can identify your chacater by your id</p>
		<strong>this is your id: <?php echo $id ?> don't forget it! </strong>
		use your arrow keys to move your character. Work with the other players to 
		make the shape specified by the blue squares, good luck! 
	</p>
	
	<form action="index.php" id="toIndex" name="toIndex" />
		<input type="hidden" name="assignmentId" id="assignmentId" value="filler" />
		<input type="hidden" name="workerId" value="<?php echo $id ?>" />
		<input type="hidden" name="task" value="shapeTester" />
		<input type="submit" value="click to begin!"
</body>