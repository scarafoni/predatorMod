import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import Piece.PieceType;

//make board, put pieces on it, update their positions
public class Board {
	//board is a 2 dimensional piece
	private Piece[][] board;
	public List<ArrayList<Piece>> prey;
	public List<ArrayList<Piece>> predators;
	public String[] teams;
	
	public int boardWidth, boardHeight;
	
	private boolean done;
	
	public enum Direction {w,s,a,d};
	
	public Board(int width,int prey,int preds)
	{
		this.boardWidth = width;
		this.boardHeight = width;
		this.board = new Piece[boardWidth][boardHeight];
		
		this.prey = new ArrayList<ArrayList<Piece>>();
		for(int i = 0; i < prey; i++)
			this.prey.add(new ArrayList<Piece>());
		this.predators = new ArrayList<ArrayList<Piece>>();
		for(int i = 0; i < preds; i++)
			this.predators.add(new ArrayList<Piece>());
		this.done = false;
	}
	public Piece getPiece(int x,int y)
	{return board[x][y];}
	
	public Piece[][] board()
	{return this.board;}
	
	public void removePrey(Piece p)
	{
		p.removed = true;
		board[p.x()][p.y()] = null;
	}	
	
	public boolean isGameOver()
	{
		if(done)
			return true;
		if(squareFound()) 
			{
				writeAll("y");
				return true;
			}
//		for(int i = 0; i < prey.size(); i++)
//		{
//			for(int j = 0; j < prey.get(i).size(); j++)
//				if(prey.get(i).get(j).removed() == false)
//					return false;
//		}
		return false;
	}
	
	public boolean squareFound()
	{
		boolean isPiece = false;
		int[][] spaces = {{0,0},{0,1},{0,2},{1,2},{2,2},{2,1},{2,0},{1,0}};
		for( int i = 0; i < spaces.length; i++)
		{
			if(getPiece(spaces[i][0], spaces[i][1]) != null)
			{
				isPiece = true;
			}
			else return false;
		}
		return isPiece;
		
//		if(getPiece(0,0) != null && getPiece(1,0) != null && getPiece(2,0) != null && getPiece(2,1) != null && getPiece(2,2) != null && getPiece(1,2) != null && getPiece(0,2) != null && getPiece(0,1) != null)
//			return true;
//		return false;
	}
	
	public void endGame()
	{this.done = true;}
	
	public void addTeam(boolean isPrey)
	{
		if(isPrey)
			prey.add(new ArrayList<Piece>());
		else
			predators.add(new ArrayList<Piece>());
	}
	
	//add a piece randomly to the board
	public void addPiece(Piece.PieceType type,int team,int id)
	{
		//System.out.println("hi");
		Random rand = new Random();
		int x = rand.nextInt(this.boardWidth);
		int y = rand.nextInt(this.boardHeight);
		
		while (occupied(x,y))
		{
			x = rand.nextInt(this.boardWidth);
			y = rand.nextInt(this.boardHeight);
		}
		Piece p;
		if (type == Piece.PieceType.prey)
		{
			p = new Piece(x, y, Piece.PieceType.prey, team,id);
			this.prey.get(team).add(p);
		}
		else
		{
			p = new Piece(x, y, Piece.PieceType.predator,team,id);
			this.predators.get(team).add(p);
		}
		board[x][y] = p;
	}
	
	public boolean occupied(int x, int y)
	{return(board[x][y] != null);}
	
	public Piece occupied(int x, int y, boolean filler)
	{return board[x][y];}
	
	public Piece findPiece(int id)
	{
		for(int i = 0; i < prey.size(); i++)
			for(int j = 0; j < prey.get(i).size(); j++)
				if(id == prey.get(i).get(j).id())
					return prey.get(i).get(j);

		for(int i = 0; i < predators.size(); i++)
			for(int j = 0; j < predators.get(i).size(); j++)
				if(id == predators.get(i).get(j).id())
					return predators.get(i).get(j);
		return null;
	}
	
	public boolean move(Piece piece, Direction dir)
	{
		//calc position based on piece, direction, wrap
		int x = piece.x();
		int y = piece.y();
		switch(dir)
		{
			case s: ++x;
					    x %= boardWidth;
					    break;
			case w:  --x;
					    x = (x + boardWidth) % boardWidth;
					    break;
			case d:    ++y;
						y %= boardHeight;
						break;
			case a:  --y;
						y = (y + boardHeight) % boardHeight;
						break;
			default: 	return false;
		}
		
		Piece p;
		if (occupied(x,y)) 
		{
			p = occupied(x,y,true);
			if (p.type == Piece.PieceType.prey
					&& piece.type == Piece.PieceType.predator)
				this.removePrey(p);
			else
				return false;
		}
		//move piece
		board[piece.x()][piece.y()] = null;
		piece.setX(x);
		piece.setY(y);
		board[x][y] = piece;
		
		return true;
	}
	
	public String printBoard(Piece piece, boolean filterTeam, boolean filterSide, int filterPos)
	{
		//filterTeam- can see members of the same team
		//filterSide- can see members of the same side
		Piece p;
		String finalString = "";
		for (int col = 0; col < boardWidth; ++col)
		{
			for (int row = 0; row < boardHeight; ++row)
			{
				p = board[col][row];
				//if sight restrictions are imposed
				if(filterPos > 0)
				{
					//if it is outside a certain distance of the piece
					if(!((col >= (piece.x() - filterPos) && col <= (piece.x() + filterPos))
						&& (row >= (piece.y() - filterPos) && row <= (piece.y() + filterPos))))
					{
						//if the space isn't null
						if (p != null) 
						{
							//if they're the same type and filterside is on, they're visible
							if (p.type == piece.type && filterSide)
							{
								if(p.type == Piece.PieceType.prey)
									finalString += "0";
								else
									finalString += "1";
								finalString += p.team().toString();
								finalString += p.id().toString();
								finalString += "|";
							}
							//if they're the same team and filterTeam is on, they're visible
							else if (p.team() == piece.team() && filterTeam)
							{
								if(p.type == Piece.PieceType.prey)
									finalString += "0";
								else
									finalString += "1";
								finalString += p.team().toString();
								finalString += p.id().toString();
								finalString += "|";
							}
							//otherwise its not visible
							else
								finalString += "X";
						}
						//if it's null it's an X
						else
							finalString += "X";
					}
					//if it's within the FOV
					else if (p == null)
						finalString += ",";
					else
					{
						if(p.type == Piece.PieceType.prey)
							finalString += "0";
						else
							finalString += "1";
						finalString += p.team().toString();
						finalString += p.id().toString();
						finalString += "|";
					}
				}
				//if there are no filters
				else
				{
					if (p == null)
						finalString += ",";
					else
					{
						if(p.type == Piece.PieceType.prey)
							finalString += "0";
						else
							finalString += "1";
						finalString += p.team().toString();
						finalString += p.id().toString();
						finalString += "|";
					}
				}
			}
			finalString += ";";
		}
		String name = piece.id().toString()+"_map.txt";
		write(name,finalString);
		return finalString;
	}
	
	//print board for all players
	public void printAll(boolean filterSide,boolean filterTeam,int radius)
	{
		for(int i = 0; i < this.prey.size(); i++)
			for(int j = 0; j < this.prey.get(i).size(); j++)
				printBoard(this.prey.get(i).get(j), filterTeam, filterSide, radius);
		
		for(int i = 0; i < this.predators.size(); i++)
			for(int j = 0; j < this.predators.get(i).size(); j++)
				printBoard(this.predators.get(i).get(j), filterTeam, filterSide, radius);
	}
	
	//write something to all player's files (other than board)
	public void writeAll(String toWrite)
	{
		for(int i = 0; i < this.prey.size(); i++)
			for(int j = 0; j < this.prey.get(i).size(); j++)
				write(prey.get(i).get(j).id().toString()+ "_map.txt",toWrite);
		
		for(int i = 0; i < this.predators.size(); i++)
			for(int j = 0; j < this.predators.get(i).size(); j++)
				write(predators.get(i).get(j).id().toString()+ "_map.txt",toWrite);
	}
	
	public void write(String fileName,String text)
	{
		try 
		{
			Writer out2 = new OutputStreamWriter(new FileOutputStream(fileName));
			out2.write(text);
			out2.close();
	    }
	    catch(IOException e){};
	  }
	
	public void appendMove(long time,String id, String move)
	{
		try 
		{
			Writer out2 = new OutputStreamWriter(new FileOutputStream("moves.txt",true));
			Integer timeI = (int)(long) time;
			String toWrite = timeI.toString()+" "+id+" "+move+"\n";
			out2.write(toWrite);
			out2.close();
	    }
	    catch(IOException e){};
	}
}