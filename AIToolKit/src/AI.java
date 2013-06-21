import java.util.*;
public class AI 
{
	public String preyMove(String[][][] board,String team, String id)
	{
		int x = 0;
    	int y = 0;
    	//find our piece
    	for(int i = 0; i < board.length; i++)
    		for(int j = 0; j < board.length; j++)
    			if(board[i][j][0].equals("M"))
    			{x = i;y = j;break;}
    	//start a radial search
		 for(int rad = 1; rad < board.length; rad++)
		 {
			 int[] startPoint = {x-rad,y-rad};
			 //check top
			 for(int i = 0; i < rad+2; i++)
			 {
				 if(foundPred(board,startPoint[0],startPoint[1]+i))
					 return makeMove(x,y,startPoint[0],startPoint[1]+i);
			 }
			 //right
			 int compensator = (rad-1)*2 + 3;
			 for(int i = 0; i < rad+compensator; i++)
			 {
				 if(foundPred(board,startPoint[0]+i,startPoint[1]+compensator))
					 return makePreyMove(x,y,startPoint[0]+i,startPoint[1]+compensator);

			 }
			 //bottom
			 for(int i = 0; i < rad+compensator; i++)
			 {
				 if(foundPred(board,compensator+startPoint[0],startPoint[1]+i))
					 return makePreyMove(x,y,compensator+startPoint[0],startPoint[1]+i);
			 }
			 //left
			 for(int i = 0; i < rad+compensator; i++)
			 {
				 if(foundPred(board,startPoint[0]+i,startPoint[1]))
					 return makePreyMove(x,y,startPoint[0]+i,startPoint[1]);
			 }
		 }
		 return "w";
	}
	//finds a piece, then finds the nearest prey and lunges towards it
	 public String soloMove(String[][][] board,String team, String id)
	    {
	    	int x = 0;
	    	int y = 0;
	    	//find our piece
	    	for(int i = 0; i < board.length; i++)
	    		for(int j = 0; j < board.length; j++)
	    			if(board[i][j][0].equals("M"))
	    			{x = i;y = j;break;}
	    	//start a radial search
			 for(int rad = 1; rad < board.length; rad++)
			 {
				 int[] startPoint = {x-rad,y-rad};
				 //check top
				 for(int i = 0; i < rad+2; i++)
				 {
					 if(foundPrey(board,startPoint[0],startPoint[1]+i))
						 return makeMove(x,y,startPoint[0],startPoint[1]+i);
				 }
				 //right
				 int compensator = (rad-1)*2 + 3;
				 for(int i = 0; i < rad+compensator; i++)
				 {
					 if(foundPrey(board,startPoint[0]+i,startPoint[1]+compensator))
						 return makeMove(x,y,startPoint[0]+i,startPoint[1]+compensator);

				 }
				 //bottom
				 for(int i = 0; i < rad+compensator; i++)
				 {
					 if(foundPrey(board,compensator+startPoint[0],startPoint[1]+i))
						 return makeMove(x,y,compensator+startPoint[0],startPoint[1]+i);
				 }
				 //left
				 for(int i = 0; i < rad+compensator; i++)
				 {
					 if(foundPrey(board,startPoint[0]+i,startPoint[1]))
						 return makeMove(x,y,startPoint[0]+i,startPoint[1]);
				 }
			 }
			 return "w";
	 }
	 
	 //makes a move based on concensus of all teamMates
	 public String groupMove(String[][][] board,String team)
	 {
		 //find my piece
		 String side = "1";
		 int[] myPiece = findMyPiece(board);
		 int x = myPiece[0];
		 int y = myPiece[1];
		 
		 //find teamMates
		 ArrayList<int[]> coords = findTeamMates(board,side,team);
		 //get the center of us all
		 int[] center = findCenter(coords);
		 //find the nearest prey to the center
		 int[] nearEnemy = pointFind(board,center[0],center[1]);
		 //System.out.println("up "+nearEnemy[0]);
		 //move towards it
		 return makeMove(x,y,nearEnemy[0],nearEnemy[1]);
	 }
	 
	 //finds my piece on the map
	 private int[] findMyPiece(String[][][] board)
	 {
		 int x = 0;
		 int y = 0;
		 //find our piece
		 for(int i = 0; i < board.length; i++)
			 for(int j = 0; j < board.length; j++)
				 if(board[i][j][0].equals("M"))
				 {x = i;y = j;break;}
		 return new int[]{x,y};
	 }
	 //finds average (middle) point of various pieces from their locations
	 private int[] findCenter(ArrayList<int[]> coords)
	 {
		 int xtot = 0;
		 int ytot = 0;
		 for(int i = 0; i < coords.size(); i++)
		 {
			 xtot += coords.get(i)[0];
			 ytot += coords.get(i)[1];
		 }
		 //System.out.println("center- "+xtot/coords.size()+","+ytot/coords.size());
		 return new int[]{xtot/coords.size(),ytot/coords.size()};
	 }
	 
	 //finds all team mates on the board
	 ArrayList<int[]> findTeamMates(String[][][] board,String side, String team)
	 {
		 ArrayList<int[]> coords = new ArrayList<int[]>(); 
		 
		 for(int i = 0; i < board.length; i++)
			 for(int j = 0; j < board.length; j++)
			 {
				 //System.out.println("team- "+team+"\nside- "+side);
				 //System.out.println(board[i][j][1]);
				 //System.out.println(board[i][j][1]);
				 if(board[i][j][1].equals(team) && (board[i][j][0].equals(side) || board[i][j][0].equals("M")))
				 {
					 int toAdd[] = {i,j};
					 coords.add(toAdd);
				 }
			 }
		 return coords;
	 }
	 
	 //finds the nearest prey with respect to a given point and returns its coordinates
	 public int[] pointFind(String[][][] board,int x, int y)
	 {
		 //start a radial search
		 for(int rad = 1; rad < board.length; rad++)
		 {
			 int[] startPoint = {x-rad,y-rad};
			 //check top
			 for(int i = 0; i < rad+2; i++)
			 {
				 if(foundPrey(board,startPoint[0],startPoint[1]+i))
				 {
					 //System.out.println("w");
					 return new int[]{startPoint[0],startPoint[1]+i};
				 }
			 }
			 //right
			 int compensator = (rad-1)*2 + 3;
			 for(int i = 0; i < rad+compensator; i++)
			 {
				 if(foundPrey(board,startPoint[0]+i,startPoint[1]+compensator))
				 {
					 //System.out.println("d");
					 return new int[]{startPoint[0]+i,startPoint[1]+compensator};
				 }

			 }
			 //bottom
			 for(int i = 0; i < rad+compensator; i++)
			 {
				 if(foundPrey(board,compensator+startPoint[0],startPoint[1]+i))
				 {
					 //System.out.println("s");
					 //System.out.print(compensator+startPoint[0]+","); System.out.println(startPoint[1]+i);
					 return new int[]{compensator+startPoint[0],startPoint[1]+i};
				 }
			 }
			 //left
			 for(int i = 0; i < rad+compensator; i++)
			 {
				 if(foundPrey(board,startPoint[0]+i,startPoint[1]))
				 {
					 //System.out.println("sa");
					 return new int[]{startPoint[0]+i,startPoint[1]};
				 }
			 }
		 }
		 //System.out.println("none");
		 return new int[]{0,0};
	 }
	 //check for prey in a given spot
	 private boolean foundPrey(String[][][] board,int i,int j)
	 {
		 if(i >= 0 && i < board.length &&
					j >= 0 && j < board.length)
				{
					if(board[i][j][0].equals("0"))
					{
						//System.out.println(i+","+j);
						return true;
					}
				}
		 return false;
	 }
	 
	 private boolean foundPred(String[][][] board,int i,int j)
	 {
		 if(i >= 0 && i < board.length &&
					j >= 0 && j < board.length)
				{
					if(board[i][j][0].equals("1"))
					{
						//System.out.println(i+","+j);
						return true;
					}
				}
		 return false;
	 }
	 
	 //wrapper for move making functions
	 private String makeMove(int predX,int predY,int preyX,int preyY)
	 {
		 int[] vec = makeVector(predX,predY,preyX,preyY);
		 return vec2Move(vec);
	 }
	 
	 private String makePreyMove(int preyX,int preyY,int predX,int predY)
	 {
		 int[] vec = makeVectorPrey(preyX,preyY,predX,predY);
		 return vec2MovePrey(vec);
	 }
	 
	 //makes a vector
	 private int[] makeVector(int predX,int predY,int preyX,int preyY)
	 {
		 int x = preyX-predX;
		 int y = preyY-predY;
		 int[] coord = {x,y};
		 return coord;
	 }
	 
	 private int[] makeVectorPrey(int preyX,int preyY,int predX,int predY)
	 {
		 int x = predX-preyX;
		 int y = predY-preyY;
		 int[] coord = {x,y};
		 return coord;
	 }
	 
	 //makes a move from a vector
	 private String vec2Move(int[] coord)
	 {
		 if(coord[0] > 0)
			 return "s";
		 else if(coord[0] < 0)
			 return "w";
		 
		 if(coord[1] > 0)
			 return  "d";
		 else if(coord[1] <  0)
			 return "a";
		 
		 return "w";
	 }
	 
	 private String vec2MovePrey(int[] coord)
	 {
		 if(coord[0] > 0)
			 return "w";
		 else if(coord[0] < 0)
			 return "s";
		 
		 if(coord[1] > 0)
			 return  "a";
		 else if(coord[1] <  0)
			 return "d";
		 
		 return "w";
	 }
}
