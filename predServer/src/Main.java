import java.io.*;
import java.net.*;
import java.util.*;
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
        	final Board board = new Board(10,preyTeams,predTeams,readHotSpots());
			board.write("site/ids.php","0");
			board.write("moves.txt", "");
			//System.out.println("working?");
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
	
	public static int[][] readHotSpots() {
		ArrayList<Integer[]> hotSpots = new ArrayList<Integer[]>(); 
		try {
			BufferedReader br =new BufferedReader(new FileReader("hotSpots.txt"));
			String line = br.readLine();
			
			while(line != null) {
				String[] split = line.split(",");
				Integer[] toInsert = new Integer[2];
				toInsert[0] = Integer.parseInt(split[0]);
				toInsert[1] = Integer.parseInt(split[1]);
				hotSpots.add(toInsert);
				line = br.readLine();
			}
		}catch(IOException e){}
		int[][] toReturn = new int[hotSpots.size()][2];
		for(int i = 0; i < hotSpots.size(); i++) {
			toReturn[i][0] = hotSpots.get(i)[0];
			toReturn[i][1] = hotSpots.get(i)[1];
		}
		return toReturn;
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
			//System.out.println("test");
			if (board.findPiece(this.id) == null) {
				//System.out.println("test");
				board.addPiece(type,team,id);
				piece = board.findPiece(this.id);
				String x = ((Integer)piece.x()).toString();
				String y = ((Integer)piece.y()).toString();
				String iPos = "start " +x +" "+ y;
				board.appendMove(System.currentTimeMillis()-start,initial[2],iPos);
			}
			
			piece = board.findPiece(this.id);
			
			if(board.isGameOver())
			{
				board.writeAll("y");
				for(int i = 0; i < board.predators.get(0).size(); i++) {
					Piece p = board.predators.get(0).get(i);
					String x = ((Integer)p.x()).toString();
					String y = ((Integer)p.y()).toString();
					String iPos = "end " +x +" "+ y;
					board.appendMove(System.currentTimeMillis()-start,p.id().toString(),iPos);
					
				}
				out.close();
				in.close();
				clientSocket.close();
				System.exit(1);
			}
			//else
				//System.out.println("not gae over");
	
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
	
				else if(inputLine.equals("start")) {
					Integer x = Integer.parseInt(initial[4]);
					Integer y = Integer.parseInt(initial[5]);
					piece.setX(x);
					piece.setY(y);
				}
				String map = board.printBoard(piece,filterSide,filterTeam,radius);
				board.printAll(filterSide,filterTeam,radius);
				String x = ((Integer)piece.x()).toString();
				String y = ((Integer)piece.y()).toString();
				board.appendMove(System.currentTimeMillis()-start,initial[2],initial[3]+" "+x+" "+ y);
				out.println(map);
			}
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {e.printStackTrace();}
	}
} 	