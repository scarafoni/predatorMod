<?php

	//$id = $_REQUEST["workerId"];
	//if($id == "")
	$id = getNextId();
	$type= "predator";
	$team = 0;
	function getNextId() 
	{
		$file = fopen("ids.php","r");
		$id = fgets($file);
		fclose($file);
		
		$file = fopen("ids.php","w");
		fwrite($file,++$id);
		fclose($file);
		return $id;
	
	}
?>
<!DOCTYPE HTML>
	<head>
		<script src="jquery-1.4.4.min.js" type="text/javascript"></script>
		<script src="game.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="style.css" />
		<meta name="keywords" content="HTML, CSS, Javascript, XHTML, PHP">
		<meta name="description" content="online shape tester game">
		<meta name="author" content="Dan Scarafoni">
		
		<title>Predator vs Prey</title>
	</head>
	
	<body onkeydown="handleKey(event)">
		<script>
			//get info
			var type = '<?php echo $type; ?>';
			var team = '<?php echo $team; ?>';
			var id = '<?php echo $id; ?>';

			//draw board on startup, set up interval
			$(document).ready(function()
			{
				drawBoardM(10);
				try {
				Initialize(type,team,id,'w');
				}catch(err){alert("error");}
				document.getElementById('assignmentId').value = gup('assignmentId');
				setInterval(function(){drawBoard(id)},100);
			});
			
			//when a key is pressed
			function handleKey(e)
			{
				var keyNum;
				if(window.event)
					keyNum = e.keyCode;
				else
					keyNum = e.which;
				
				 e.preventDefault();
				if(keyNum == 38)
					Initialize(type,team,id,'w');
				else if(keyNum == 40)
					Initialize(type,team,id,'s');
				else if(keyNum == 37)
					Initialize(type,team,id,'a');
				else if(keyNum = 39)
					Initialize(type,team,id,'d');
			}
			//draw the board
			function drawBoardM(size)
			{
				var ta = document.getElementById("board");
				for(var i = 0; i < size; i++)
				{
					var row = ta.insertRow(i);
					for(var j = 0; j < size; j++)
						row.insertCell(j);
				}
			}
			
			function gup( name )
				{
					var regexS = "[\\?&]"+name+"=([^&#]*)";
					var regex = new RegExp( regexS );
					var tmpURL = window.location.href;
					var results = regex.exec( tmpURL );
					if( results == null ) {
						//alert("gub found nothing");
						return "";
					}
					else {
						//alert("grub result- "+results[1]);
						return results[1];
					}
				}
		</script>
		<div id="topBanner"><h1>Predator vs Prey RG Edition</h1></div>
		<div id="leftBanner"></div>
		<div id="boardDiv">
			<table border="1" id="board"></table>
			<p id="alertPlace"></p>
		<form id="mturk_form" method="POST" action="http://www.mturk.com/mturk/externalSubmit">
			<input type="hidden" id="assignmentId" name="assignmentId" value="">
			<input type="hidden" id="mapSubmit" name="mapSubmit" value="holder">
			<input id="submitButton" type="submit" name="Submit" value="Complete HIT">
		</form>
		<script>
				//alert("in the right script");
				//document.getElementById('assignmentId').value = gup('assignmentId');
				//alert(document.getElementById('assignmentId').value);
				if (gup('assignmentId') == "ASSIGNMENT_ID_NOT_AVAILABLE")
				{
					// If we're previewing, disable the button and give it a helpful message
					document.getElementById('submitButton').disabled = true;
					document.getElementById('submitButton').value = "You must ACCEPT the HIT before you can submit the results.";
				} else {
					var form = document.getElementById('mturk_form');
					if (document.referrer && ( document.referrer.indexOf('workersandbox') != -1) ) {
						form.action = "http://workersandbox.mturk.com/mturk/externalSubmit";
					}
				}
			</script>
		</div>
	</body>
