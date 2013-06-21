import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException 
    {
    	//info about predator id and such
    	String team = args[0];
    	String id = args[1]; 
    	int difficulty = 1000-(Integer.parseInt(args[2])*100);
    	
    	int port = 4444;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String host = "roc.cs.rochester.edu";

        String move = "w";
        int boardSize = 0;
        
        long start = System.currentTimeMillis();
        while (true) {
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
				
				//
				//end of error checking
				//
				
				//get move
				out.println("prey"+","+team+","+id+","+move);
				String fromServer = in.readLine();
        		/*String fromServer = "XXXXXXXXXX;" +
        							"XXXXXXXXXX;" +
        							"XXXXXXXXXX;" +
        							"XXXXXXXXXX;" +
        							"XXXXXXXXXX;" +
        							"XXXXXXXXXX;" +
        							"XXXXX,,,,,;" +
        							"XXXXX101|,,,,;" +
        							"XXXX,,,XXX;" +
        							"XXXXXX,,,000|;";
        		*/
				if(boardSize == 0)
					boardSize = getBoardSize(fromServer);
				String[][][] board = makeBoard(fromServer,boardSize,team,id);
				
				AI ai = new AI();
				//move = ai.groupMove(board,team);
				//move = ai.soloMove(board, team, id);
				move = ai.preyMove(board, team, id);
				//printBoard(board);
				String toPrint = "prey"+","+team+","+id+","+move;
				out.println(toPrint);
				
				
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
    	int xIndex = 0;
    	while(xIndex < size)
    	{
			//System.out.println("yindex -"+yIndex);
    		int yIndex = 0;
    		while(yIndex < size)
    		{
    			char currentChar = fromServer.charAt(textIndex);
    			//blank square
    			if(currentChar == ',' || currentChar == 'X')
    			{
    				board[xIndex][yIndex][0] = ",";
    				board[xIndex][yIndex][1] = ",";
    				board[xIndex][yIndex][2] = ",";
    				textIndex++; yIndex++;
    			}
    			//newline
    			else if(currentChar == ';')
    				{textIndex++; xIndex++; yIndex = 0; break;}
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
    				yIndex++;
    				
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