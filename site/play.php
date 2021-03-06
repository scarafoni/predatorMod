<?php
	$id = $_REQUEST["id"];
	
	$isObserver = false; 
	if($id == "obs")
		$isObserver = true;
	
	if(!$isObserver)
		$id = getNextId();	

	function getNextId() 
	{
		$file = fopen("ids.php","r");
		$id = fgets($file);
		fclose($file);
		
		$file = fopen("ids.php","w");
		fwrite($file,$id+1);
		fclose($file);
		return $id;
	
	}
	//$spotFile = fopen("../hotSpots.txt","r");
	$text = file("../hotSpots.txt",FILE_IGNORE_NEW_LINES);
	//fclose($spotFile);
	$hs = $text;//$hs = explode("\n",$text);
	$hotSpots = array();
	
	for($i = 0; $i < sizeof($hs); $i++) {
		array_push($hotSpots,explode(",",$hs[$i]));
	}

	
?>
<!DOCTYPE HTML>
	<head>
		<meta charset="UTF-8">
		<script src="jquery-1.4.4.min.js" type="text/javascript"></script>
		<script src="game.js" type="text/javascript"></script>
		<script src="gup.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="style.css" />
		<meta name="keywords" content="HTML, CSS, Javascript, XHTML, PHP">
		<meta name="description" content="online shape tester game">
		<meta name="author" content="Dan Scarafoni">
		
		<title>Shape Tester</title>
	</head>
	
	<body>
		<script>
			//get info
			var id = '<?php echo $id; ?>';
			var isObs= '<?php echo $isObserver; ?>';
			
			var d;
			var start;
			//alert(start);
			var time = 0;
			var occupied = false;
			//hot spots
			var spaces = <?php echo json_encode($hotSpots); ?>;
			var index = spaces.indexOf("");
			//alert(spaces);
			if(index > -1){	
				spaces.splice(index,1);
			}
			//var spaces = [[0,0],[0,1],[0,2],[1,2],[2,2],[2,1],[2,0],[1,0]];
			//draw board on startup, set up interval
			$(document).ready(function()
			{
				d = new Date();
				start = d.getTime();
				
				//add key listeners
				try {
					if(document.addEventListener) {
						document.addEventListener("keydown",handleKey,true);
						document.addEventListener("keydown",stopScroll,true);
					}
					else {
						document.attach("onkeydown",handleKey);
						document.attach("onkeydown",stopScroll);
					}
				}catch(err){alert(err);}
				
				if(isObs)
					id = 0;	
				drawBoardM(10);
				try {
					Initialize("predator",'0',id,'w');
					}catch(err){alert("error");}
					
				document.getElementById('assignmentId').value = gup('assignmentId');
				setInterval(function(){drawBoard(id)},1000);
			});
			
			
			//when a key is pressed
			function handleKey(e)
			{
				if(!isObs)
				{
					d2 = new Date();
					time2 = d2.getTime();
					var diff = time2 - start;
					//alert("checkin");
					if(diff > 500) {
						//alert("working");
						start = time2;
						var keyNum = e.keyCode;
						
						 //e.preventDefault();
						if(keyNum == 37)
							Initialize("predator","0",id,'a');
						else if(keyNum == 38)
							Initialize("predator","0",id,'w');
						else if(keyNum == 39)
							Initialize("predator","0",id,'d');
						else if(keyNum = 40)
							Initialize("predator","0",id,'s');
					}
				}
			}
			
			//stop the screen from scrolling when arrow keys are pressed
			function stopScroll(e) {
				//arrow keys
				if([37, 38, 39, 40].indexOf(e.keyCode) > -1) {
					e.preventDefault();
				}
			}
			
		</script>
		<div id="topBanner"><h1>Shape Tester RG Edition</h1></div>
		<div id="leftBanner"></div>
		<div id="boardDiv">
			<div id="bpts">
				<ol>
					<li>CLICK the GRID to begin</li>
					<li>press the ARROW KEYS to move the GREEN piece onto a BLUE square</li>
					<li>after you do this, click the BUTTON at the BOTTOM of the screen RECIEVE PAYMENT </li>
					<li>REMEMBER- if you travel off of one side of the map, YOU WILL COME BACK ON THE OPPOSITE SIDE</li>
				</ol>
			</div>
			<p id="alertPlace"></p>
			<table border="1" id="board"></table>
			<form id="mturk_form" method="POST" action="https://www.mturk.com/mturk/externalSubmit">
				<input type="hidden" id="assignmentId" name="assignmentId" value="">
				<input type="hidden" id="mapSubmit" name="mapSubmit" value="holder">
				<input id="submitButton" type="submit" name="Submit" value="Complete HIT">
			</form>
		</div>
	</body>
