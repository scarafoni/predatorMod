import java.io.*;
import java.net.*;
public class Main 
{	
	public static void main(String[] args) throws IOException 
	{
        	final int port = Integer.parseInt(args[0]);
        	final int preyTeams = 1;//Integer.parseInt(args[1]);
        	final int predTeams = 1;//Integer.parseInt(args[2]);
        	final boolean filterSide = false;// Boolean.parseBoolean(args[3]);
        	final boolean filterTeam = false;//Boolean.parseBoolean(args[4]);
        	final int radius = 0;//Integer.parseInt(args[5]);
        	final Board board = new Board(10,preyTeams,predTeams);
			board.write("site/ids.php","0");
	        try
	        {
	        	ServerSocket serverSocket = new ServerSocket(port);
	        	Socket clientSocket = null;
	        	long start = System.currentTimeMillis();
	        	while(true)
	        	{
	        		clientSocket = serverSocket.accept();
	        		Thread t = new Thread(new Server(serverSocket,clientSocket,board,filterSide,filterTeam,radius,start));
	        		t.start();
	        	}
	        }
	        catch(IOException e)
	        {
	        	System.out.println("IOException on socket listen" + e);
	        	e.printStackTrace();
	        }
	}
}
	
class Server implements Runnable
{
	private Socket clientSocket;
	private Board board;
	public ServerSocket serverSocket;
	private Piece.PieceType type;
	private int team;
	private int id;
	private Piece piece;
	private boolean filterTeam;
	private boolean filterSide;
	private int radius;
	private long start;
	
	Server(ServerSocket serverSocket,Socket clientSocket, Board board, boolean filterSide, boolean filterTeam, int radius,long start)
	{
		this.serverSocket = serverSocket;
		this.clientSocket = clientSocket;
		this.board = board;
		this.filterSide = filterSide;
		this.filterTeam = filterTeam;
		this.radius = radius;
		this.start = start;
	}
	
	public void run()
	{
		try 
		{
			PrintWriter out;
			BufferedReader in;
			String inputLine;
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String[] initial = in.readLine().split(",");
			this.type = Piece.PieceType.valueOf(initial[0]);
			this.team = Integer.parseInt(initial[1]);
			this.id = Integer.parseInt(initial[2]);
			if (board.findPiece(this.id) == null)
				board.addPiece(type,team,id);
			
			piece = board.findPiece(this.id);
			
			if(board.isGameOver())
			{
				board.writeAll("y");
				System.exit(1);
			}
	
			if(piece.removed == true)
			{
				board.write(piece.id().toString()+"_map.txt","you got eated");
				out.println("you got eated");
			}
			else
			{
			
				inputLine = initial[3];
				if (inputLine.equals("w") 
						|| inputLine.equals("s")
						|| inputLine.equals("d") 
						|| inputLine.equals("a")) 
					board.move(piece,Board.Direction.valueOf(inputLine));
	
				String map = board.printBoard(piece,filterSide,filterTeam,radius);
				board.printAll(filterSide,filterTeam,radius);
				board.appendMove(System.currentTimeMillis()-start,initial[2],initial[3]);
				out.println(map);
			}
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}	    			  