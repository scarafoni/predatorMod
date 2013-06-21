<?php
 $turker_id = $_REQUEST["turk"];
 $assignment_id = $_REQUEST["assignmentid"];
?>
<!DOCTYPE HTML>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css" />
		<title>Predator vs Prey</title>
	</head>

	<body>
		<script>
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
		<div id="instructions">
			<form action="play.php">
				<!-- 
Type:</br> <input type="radio" name="type" value="predator" />Predator <input type="radio" name="type" value="prey" />Prey				
				</br>
				Team: <input type="text" name="team" />
 -->
				Id: <input type="text" name="id" id="id" />
				<input type="hidden" name="turk" value="<?php echo $turker_id?>"/>
				<input type="hidden" name="assignmentId" id="assignmentId" value="" />
				<input type="submit" id="submitButton" value="begin" />
			</form>
			<p>
			Instructions: </br> 
			1. Enter your assigned id number </br>
			2. use arrow keys to move, on chessboard, you are "0:id#"</br>
			3. your job is to put your piece on one of the colored squares to help make a shape. </br>
			</p>
			
			<script>
				//alert("in the right script");
				document.getElementById('assignmentId').value = gup('assignmentId');
				if (gup('assignmentId') == "ASSIGNMENT_ID_NOT_AVAILABLE")
				{
					//alert("no id");
					document.getElementById('submitButton').disabled = true;
					document.getElementById('submitButton').value = "You must ACCEPT the HIT before you can submit the results.";
				} else {
					//alert("id found");
					var form = document.getElementById('id');
					form.action = "play.php";
				}
			</script>
		</div>
	</body>
</html>