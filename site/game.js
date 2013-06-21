var teamCol;
var tex;
//update the board
function drawBoard(id)
{
	//get the needed file
	tex = pollFile("../"+id+"_map.txt");
	document.getElementById('mapSubmit').value = tex;
	
	var ta = document.getElementById("board");
	var texCount = 0;
	//go through each row
	var rowL =ta.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
	if(tex[0] == "y")
	{
		document.getElementById("alertPlace").innerHTML = "You made a shape, congrats!";
	}
	else
	{
		for(var i = 0; i < rowL.length; i++)
		{
			//for each cell in row r
			var colL = rowL[i].getElementsByTagName("td"); 
			var j = 0;
			while(j < colL.length)
			{	
				var cell = colL[j];
				//; == newline, don't draw
				if(tex[texCount] == ";")
					texCount++;
				//, == empty
				else if(tex[texCount] == ",")
				{
					//empty out cell
					cell.style.outline = "";
					cell.style.borderWidth = "1px";
					if(cell.style.backgroundColor != "white")
						cell.style.backgroundColor = "white";
					if(cell.innerHTML != "")
						cell.innerHTML = "";

					texCount++;
					j++;
				}
				//X == hidden
				else if(tex[texCount] == "X")
				{
					//make background black
					if(cell.style.backgroundColor != "black")
						cell.style.backgroundColor = "black";
					if(cell.innerHTML != "")
						cell.innerHTML = "";

					texCount++;
					j++;
				}
				//if its non of the above it's a piece
				else
				{
					//optimized begin
					var sidel = tex[texCount]; texCount++;
					var teaml = tex[texCount]; texCount++;
					var inner = teaml+":";
					cell.style.backgroundColor= "rgb("+teamCol[sidel][teaml]+")";
					cell.style.outline = "purple solid 3px";
					//optimized end

					//write id
					var idl = "";
					while(tex[texCount] != "|")
					{	
						idl += tex[texCount];		
						inner += tex[texCount];
						texCount++;
					}
					cell.innerHTML = inner;
					if(idl == id || idl == 0+id)
						cell.style.backgroundColor="green";
					texCount++;
					j++;
				}
			}
		}
		var spaces = [[0,0],[0,1],[0,2],[1,2],[2,2],[2,1],[2,0],[1,0]];
		for(var i = 0; i < spaces.length; i++)
		{
			var colL = rowL[spaces[i][0]].getElementsByTagName("td"); 
			var cell = colL[spaces[i][1]];
			cell.style.backgroundColor = "blue";
		}
	}
}

//set up connection, fork over credentials, receive initial board
function Initialize(type,team,id,move) 
{
	teamCol = teamColors();
	var toWrite = "type="+type+"&&team="+team+"&&id="+id+"&&move="+move;
	try
	{
		passInfo(toWrite);
		drawBoard(id);
	}
	catch(err)
	{
		alert("error");
	}   
}

function teamColors()
{
	var teamCol = new Array();
	teamCol[0] = new Array();
	teamCol[1] = new Array();

	for(var i = 0; i < 10; i++)
	{
		var green = i * 20;
		var pred = "255,"+green+",0";
		var prey = "0,"+green+",255";
		teamCol[0][i] = prey;
		teamCol[1][i] = pred
	}
	return teamCol;
}
//get map
function pollFile(toRead) 
{
	var datafile = toRead;
	var line = "";
  	objXml = new XMLHttpRequest();
    	objXml.open("GET",datafile,false);
    	objXml.send(null);
    	return objXml.responseText;
}

//send move to server
function passInfo(toWrite) 
{
    objXml = new XMLHttpRequest();
    objXml.open("POST","write.php",false);
    objXml.setRequestHeader("Content-Type", "application/x-www-form-urlencoded"); 
    objXml.send(toWrite);
}