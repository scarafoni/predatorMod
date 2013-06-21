import java.util.*;
import java.lang.Math;

public class AI {
	int[][] hotSpots = {{0,0},{1,0},{2,0},
						{0,1},      {2,1},
						{0,2},{1,2},{2,2}} ;
	
	
	int target = 0;
	String[][][] board;
	String[] moves = {"w","a","s","d"};
	Stack<int[]> path;
	int[] prev = new int[]{10,10};
	

	public AI() {
		Random random = new Random();
		Collections.shuffle(Arrays.asList(hotSpots));
		path = new Stack<int[]>();
	}
	
	//finds a piece, then finds the nearest prey and lunges towards it
	 public String soloMove(String[][][] board,String team, String id)
	    {
		 	this.board = board;
			//find our piece
		 	int x = 0;
		 	int y = 0;
	    	for(int i = 0; i < board.length; i++)
	    		for(int j = 0; j < board.length; j++){
	    			if(board[i][j][2].equals(id))
	    			{
	    				x = i;
	    				//System.out.println("found piece");
	    				y = j;
	    				//System.out.println("locatoin: "+x+","+y);
	    				break;
	    			}
	    		}
	    	int[] position = new int[]{x,y};
	    	if(!(position[0] == hotSpots[target][0] && hotSpots[target][1] == position[1])) {
	    		if(isOccupied(hotSpots[target]));
	    			getTarget();
	    		if(path.size() == 0)
	    			aStarMove(position,hotSpots[target]);
	    		int[] next = path.pop();
	    		if(isOccupied(next)) {
	    			path = new Stack<int[]>();
	    			aStarMove(position,hotSpots[target]);
	    			next = path.pop();
	    		}
	    		return makeMove(position[0],position[1],next[0],next[1]);
	    		//return aStarMove(position,hotSpots[target]);// makeMove(position[0],position[1],hotSpots[target][0],hotSpots[target][1]);
	    	}
	    	return "stop";
	    	/*
	    	//System.out.println("target- "+target);
	    	//make sure were not in a goal state
	    	if(hotSpots[target][0] == x && hotSpots[target][1] == y)
	    		return "stop";

	    	
	    	//find new target if first occupied
	    	if(isOccupied(hotSpots[target])) 
	    		for(int i = 0; i < hotSpots.length; i++)
	    			if(!isOccupied(hotSpots[i])) 
	    				target = i;
	    				*/
	    	/*
	    	//make new path if current blocked
	    	if(path.size() == 0 || isOccupied(path.get(0))) {
	    		path = new Stack<int[]>();
	    		aStarMove(position,hotSpots[target]);
	    	}
	    	
	    	
	    	int[] next = path.pop();
	    	return makeMove(position[0],position[1],next[0],next[1]);
	    	*/
	    }

	 private void getTarget() {
		 for(int i = 0; i < hotSpots.length; i++)
			 if(!isOccupied(hotSpots[i])) {
				 target = i;
				 return;
			 }
	 }
	 //wrapper for move making functions
	 private String makeMove(int predX,int predY,int preyX,int preyY)
	 {
		 int[] vec = makeVector(predX,predY,preyX,preyY);
		 return vec2Move(vec);
	 }

	 //makes a vector
	 private int[] makeVector(int predX,int predY,int preyX,int preyY)
	 {
		 int x = preyX-predX;
		 if(Math.abs(x) > 5)
			 x *= -1;
		 int y = preyY-predY;
		 if(Math.abs(y) > 5)
			 y *= -1;
		 int[] coord = {x,y};
		 return coord;
	 }
	 
	 //makes a move from a vector
	 private String vec2Move(int[] coord)
	 {
		 if(Math.abs(coord[0]) > Math.abs(coord[1])) {
			 if(coord[0] > 0)
				 return "d";
			 else if(coord[0] < 0)
				 return "a";
		 }
		 
		 else {
			 if(coord[1] > 0)
				 return  "s";
			 else if(coord[1] <  0)
				 return "w";
		 }
		 return "w";
	 }
	 
	 //get the best move from a viable option
	 private String aStarMove(int[] start, int[] goal) {
		 
		 //adjascent pieces
		 //System.out.println(start[0]+","+start[1]);
		 ArrayList<int[]> adj = getAdjascent(start,this.prev);
		
		 adj = sortAdj(adj,goal);
		 
		 
		 if(adj.size() < 1)
			 return "w";
		 /*
		 int[] next = adj.get(0);
		 this.prev = start;
		 return makeMove(start[0],start[1],next[0],next[1]);
		*/
		 
		 for(int[] a : adj) {
			 boolean works = getPathRec(1, a,goal);
			 if(works) {
				 this.path.push(a);
				 //System.out.println(makeMove(start[0],start[1],a[0],a[1]));
				 return makeMove(start[0],start[1],a[0],a[1]);
			 }
		 }
		 return "w";
	 }
	 
	 private boolean getPathRec(int depth, int[] start, int[] goal) {
		 //System.out.println("getPath recur");
		 //if we're at our goal, stop
		 //System.out.println("goal- "+goal[0]+","+goal[1]);
		 //System.out.println("start- "+start[0]+","+start[1]);
		 if(start[0] == goal[0] && start[1] == goal[1])
			 return true;
		 
		 //get all adjascent spots
		 ArrayList<int[]> adj = getAdjascent(start,prev);
		 if(adj.size() == 0 || depth > 20)
			 return false;
		 
		 adj = sortAdj(adj,goal);
		 
		//try and find a viable path
		for(int[] a : adj) {
			boolean works = getPathRec(depth+1,a,goal);
			 if(works) {
				 this.path.push(a);
				 return true;
			 }
		}
		 return false;
	 }
	 
	 
	 
	 //get the open 
	 private ArrayList<int[]> getAdjascent(int[] coords,int[] prev) {
		 //System.out.println("prev- "+prev[0]+","+prev[1]);
		//System.out.println("coords- "+coords[0]+","+coords[1]);
		 //get all adjascent coordinates
		 ArrayList<int[]> adj = new ArrayList<int[]>();
		 int x = coords[0];
		 int y = coords[1];
		 for(int i = -1; i < 2; i++) {
			 int xp = (x+i) % 10;
			 if(xp == -1)
				 xp = 9;
			 
			 int yp = (y+i) % 10;
			 if(yp == -1)
				 yp = 9;
			 
			 if(i != 0) {
				 int[] newSpot = new int[] {xp,y};
				 if(!isOccupied(newSpot) && !(newSpot[0] == prev[0] && newSpot[1] == prev[1]))  
					adj.add((newSpot));
				 
				 newSpot = new int[] {x,yp};
				 if(!isOccupied(newSpot) && !(newSpot[0] == prev[0] && newSpot[1] == prev[1]))  
					adj.add((newSpot));
			 }
		 }
		 return adj;
	 }
	 
	 private boolean isOccupied(int[] spot) {
		 if(!board[spot[0]][spot[1]][0].equals(","))
			 return true;
		 return false;
	 }
	 
	 private double hyp(int[] a, int[] b) {
		 double x1 = (double)a[0]; 
		 double y1 = (double)a[1];
		 double x2 = (double)b[0]; 
		 double y2 = (double)b[1]; 
		 double delx = x2-x1;
		 if(delx > 5)
			 delx -= delx % 5;
		 double dely = y2-y1;
		 if(dely > 5)
			 dely -= dely % 5;
		 
		 //System.out.println("a- "+a[0]+","+a[1]+" \nb- "+b[0]+","+b[1]);
		 //System.out.println(Math.pow((double)(b[0] - a[0]),2));
		 double hyp = Math.sqrt(Math.pow(delx,2) + Math.pow(dely,2));
		 //System.out.println(hyp);
		 return hyp;
	 }
	 
	 private ArrayList<int[]> sortAdj(ArrayList<int[]> adj,int[] goal) {
		 double[][] distances = new double[adj.size()][2];
		 for(int i = 0; i < adj.size(); i++) {
			 distances[i][0] = hyp(adj.get(i),goal);
			 distances[i][1] = i;
		 }
		 distances = sortDist(distances);
		 //for(int i = 0; i < distances.length; i++)
			 //System.out.println(distances[i][0]+","+distances[i][1]);
		 ArrayList<int[]> adjSorted = new ArrayList<int[]>(); 
		 for(int i = 0; i < adj.size(); i++) {
			 adjSorted.add(adj.get((int)distances[i][1]));
		 }
		 return adjSorted;
	 }
	 
	 
	 private double[][] sortDist(double[][] distance) {
		 double[] hold;
		 for(int i = 0; i < distance.length-1; i++)
			 for(int j = i+1; j < distance.length; j++) {
				 if(distance[j][0] < distance[i][0]) {
					 hold = distance[i];
					 distance[i] = distance[j];
					 distance[j] = hold;
				 }
			 }
		 return distance;
	 }
}
