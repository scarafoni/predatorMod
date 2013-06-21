import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException 
    {
    	//info about predator id and such
    	String team = "0";//args[0];
    	String id = args[0]; 
    	int difficulty = 1000-(Integer.parseInt(args[1])*100);
    	
    	int port = 4444;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String host = "roc.cs.rochester.edu";

        String move = "w";
        int boardSize = 0;
        
        long start = System.currentTimeMillis();
        AI ai = new AI();
        while (true) {
        	//System.out.println("startin while loop");
        	long current = System.currentTimeMillis();
        	if(current % difficulty == start % difficulty)
        	{
				try 
				{
					socket = new Socket(host, port);
					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
				} 
				catch (UnknownHostException e) 
				{
					System.err.println("Don't know about host: " + host);
					System.exit(1);
				} 
				catch (IOException e) 
				{
					System.err
							.println("Couldn't get I/O for the connection to the host.");
					System.exit(1);
				}
				
				//System.out.println("connected");
				//
				//end of error checking
				//
				
				//get move
				out.println("predator"+","+team+","+id+","+move);
				String fromServer = in.readLine();
				//System.out.println(fromServer);
        		/*String fromServer = ",,,,,,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,101|,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,,,,,,,;" +
        							",,,,,,,,,,;";*/
				if(boardSize == 0)
					boardSize = getBoardSize(fromServer);
				//System.out.println("about to make board");
				String[][][] board = makeBoard(fromServer,boardSize,team,id);
				
				//System.out.println("before ai");
				move = ai.soloMove(board, team, id);
				//System.out.println(move);
				if(!move.equals("stop")) {
					String toPrint = "predator"+","+team+","+id+","+move;
				out.println(toPrint);
				}
				
				
				out.close();
				in.close();
				socket.close();
        	}
        }
    }
    
    static String[][][] makeBoard(String fromServer,int size, String team, String id)
    {
    	String[][][] board = new String[size][size][3];
    	int textIndex = 0;
    	int yIndex = 0;
    	while(yIndex < size)
    	{
			//System.out.println("yindex -"+yIndex);
    		int xIndex = 0;
    		while(xIndex < size)
    		{
    			char currentChar = fromServer.charAt(textIndex);
    			//blank square
    			if(currentChar == ',' || currentChar == 'X')
    			{
    				board[xIndex][yIndex][0] = ",";
    				board[xIndex][yIndex][1] = ",";
    				board[xIndex][yIndex][2] = ",";
    				textIndex++; xIndex++;
    			}
    			//newline
    			else if(currentChar == ';')
    				{textIndex++; yIndex++; xIndex = 0; break;}
    			//player
    			else if(currentChar == '1' || currentChar == '0')
    			{
    				//this ai's piece
    					//same team
    				if(fromServer.charAt(textIndex+1) == team.charAt(0) &&
    						//same id
    						fromServer.substring(textIndex+2,fromServer.indexOf('|',textIndex+2)).equals(id))
    					board[xIndex][yIndex][0] = "M";
    				//any other piece
    				else
    					board[xIndex][yIndex][0] = Character.toString(currentChar);
    				
    				textIndex++;
    				board[xIndex][yIndex][1] = Character.toString(fromServer.charAt(textIndex));
    				String idHold = "";
    				textIndex++;
    				while(currentChar != '|')
    				{
    					currentChar = fromServer.charAt(textIndex);
    					idHold += Character.toString(currentChar);
    					textIndex++;
    					currentChar = fromServer.charAt(textIndex);
    				}
    				textIndex++;
    				board[xIndex][yIndex][2] = idHold;
    				xIndex++;
    				
    			}
    		}
    	}
    	//printBoard(board);
    	return board;
    }
    
    static void printBoard(String[][][] board)
    {
    	for(int i = 0; i < board.length; i++)
    	{
    		for(int j = 0; j < board.length; j++)
    		{
    			if(board[i][j][0].equals(",")|| board[i][j][0].equals("X"))
    				System.out.print(",");
    			else
	    			for(int k = 0; k < 3; k++)
	    					System.out.print(board[i][j][k]);
    		}
    		System.out.println();
    	}
    	System.out.println();
    }
    //find the size of the first line to derive board size
    static int getBoardSize(String fromServer)
    {
    	return 10;
    	/*
    	int sizeSoFar = 0;
    	int index = 0;
    	while(true)
    	{
    		char currentChar = fromServer.charAt(index);
    		if(currentChar == ';')
    			break;
    		else if(currentChar == ',' || currentChar == '|' || currentChar == 'X')
    		{
    			index++;
    			sizeSoFar ++;
    		}
    	}
    	return sizeSoFar;
    	*/
    }
    
    static String readFile(String fileName)
	{
		File file = new File(fileName);
		//read the file
		try
		{
			Scanner scanner = new Scanner(file);
			String returned = "";
			
			while (scanner.hasNext()) 
			{returned += scanner.next();}
			//System.out.println(returned);
			return returned;
		}
		//if not found throw an exception
		catch (FileNotFoundException e)
		{e.printStackTrace();}
		return "";
	}
}