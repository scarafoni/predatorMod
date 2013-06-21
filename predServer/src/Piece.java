//for creating pieces and the board
public class Piece {
	
	//x, y location, id
	private int x;
	private int y;
	private Integer team;
	private Integer id;
	public boolean removed;
	
	//type of piece
	public PieceType type;
	
	//constructors, use default values for variables if none provided
	public Piece(int x, int y, PieceType type,int team,int id)
	{
		this.x = x;
		this.y = y;
		this.type = type;
		this.team = team;
		this.id = id;
		this.removed = false;
	}
	
	//type is either predator or prey
	public enum PieceType { predator, prey };
	
	//x value
	public void setX(int newX)
		{this.x = newX;}
	
	//y value
	public void setY(int newY)
		{this.y = newY;}
	
	//return x
	public int x()
		{return this.x;}
	
	//return y
	public int y()
		{return this.y;}
	
	//return team
	public Integer team()
		{return this.team;}
	
	//return id
	public Integer id()
		{return this.id;}
	
	public boolean removed()
	{return this.removed;}
}