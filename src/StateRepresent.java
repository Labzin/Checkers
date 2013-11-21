
import java.util.*;

public class StateRepresent {
	
	public byte[][] states ={
			{2, 7, 2, 7, 2, 7, 2, 7},
			{7, 2, 7, 2, 7, 2, 7, 2},
			{2, 7, 2, 7, 2, 7, 2, 7},
			{7, 0, 7, 0, 7, 0, 7, 0},
		    {0, 7, 0, 7, 0, 7, 0, 7},
		    {7, 1, 7, 1, 7, 1, 7, 1},
			{1, 7, 1, 7, 1, 7, 1, 7},
			{7, 1, 7, 1, 7, 1, 7, 1}};
	int depth;
	StateRepresent ansestor;
	
	public StateRepresent(){
		this.depth = 1;
	}
	
	
	public StateRepresent(byte[][] state_, int depth_, StateRepresent ansestor)
	{
		for (int i =0;i<8;i++ )
			   for (int j = 0; j<8;j++) this.states[i][j] = state_[i][j];
		
		this.ansestor = ansestor;
		this.depth = depth_ + 1;
	}
	
	public void PositionPrint()
	{ for (int i =0;i<8;i++ )
		{
		   for (int j = 0; j<8;j++) {System.out.print(this.states[i][j] + "   ");} 
		   System.out.println("");
           System.out.println(""); 
	    }
	}
	
	//white = 1, black = 2 
	public ArrayList<StateRepresent> SuccessorsFunc(int colour_of_turn)
	{
		//list of potential moves
		ArrayList<StateRepresent> MoveList = new ArrayList<StateRepresent>();
		ArrayList<StateRepresent> MoveList_ = new ArrayList<StateRepresent>();
		//list of potential takes
		ArrayList<StateRepresent> TakeList = new ArrayList<StateRepresent>();
		ArrayList<StateRepresent> TakeList_ = new ArrayList<StateRepresent>();
		
		for (int i =0;i<8;i++ )
		   {
		    for (int j = 0; j<8;j++) {
		    	   //white turn
				   if (this.states[i][j] == colour_of_turn)
				   {
					   //check for possible takes
					   TakeList_ = EatCheck(i,j);
					   if (!TakeList_.isEmpty())
					   		TakeList.addAll(TakeList_);
					   
					   //if there is no possible takes, then check moves
					   if (TakeList.isEmpty())
						   MoveList_ = MoveFunction(i,j);
					   	   if (!MoveList_.isEmpty())
					   	   	  MoveList.addAll(MoveList_);
				   }
				   //black turn
				   else if (this.states[i][j] == colour_of_turn)
				   {
					   //check for possible takes
					   TakeList_ = EatCheck(i,j);
					   if (!TakeList_.isEmpty())
					   		TakeList.addAll(TakeList_);
					   
					   //if there is no possible takes, then check moves
					   if (TakeList.isEmpty())
						   MoveList_ = MoveFunction(i,j);
					   	   if (!MoveList_.isEmpty())
					   	   	  MoveList.addAll(MoveList_);
				   }
				   else{}			   				
		    }
		}
		
		//if there is possible takes, return takes
		if (!TakeList.isEmpty())
			return TakeList;
		//if there is no possible takes, return moves
		else
			return MoveList;
	    
		
	}
	
	//create of possible moves for position
	public ArrayList<StateRepresent> MoveFunction(int i, int j)
	{
		ArrayList<StateRepresent> MoveList = new ArrayList<StateRepresent>();
		StateRepresent newStateRepresent;
		
		if (this.states[i][j] == 2) 
			{		
			newStateRepresent = this.MoveCheck(i, j, (i+1), (j+1));
			  if (newStateRepresent != null)
				  MoveList.add(newStateRepresent);
			  
			newStateRepresent = this.MoveCheck(i, j, (i+1), (j-1));
		      if (newStateRepresent != null)
			      MoveList.add(newStateRepresent);
			}
			
		 else if ((this.states[i][j] == 1))
		 	{
			 
			  newStateRepresent = this.MoveCheck(i, j, (i-1), (j+1));
			  if (newStateRepresent != null)
				  MoveList.add(newStateRepresent);
			  
			  newStateRepresent = this.MoveCheck(i, j, (i-1), (j-1));
		      if (newStateRepresent != null)
			      MoveList.add(newStateRepresent);
		 	}
		
		return MoveList;
	}
	
	//method returns move if the move is possible
	private StateRepresent MoveCheck(int i_, int j_, int i_new, int j_new)
	{	 
		if ((this.BorderCheck(i_new, j_new))&&(states[i_new][j_new] == 0))
	 	{
			
			StateRepresent newStateRepresent  = new StateRepresent(this.states, this.depth, this);
			
		    newStateRepresent.states[i_new][j_new] = this.states[i_][j_];
		    newStateRepresent.states[i_][j_] = 0;
		    
		    return newStateRepresent;
	 	 }
		else {
			return null;}
	}
	
	//
	public ArrayList<StateRepresent> EatCheck(int i_, int j_)
	{
		ArrayList<StateRepresent> MoveList = new ArrayList<StateRepresent>();
		ArrayList<StateRepresent> MoveList_ = new ArrayList<StateRepresent>();
		
		/*i_eat
		  i_new 
		  j_eat
		  j_new   */		
		int[][] options_of_move  = {{1,  1, -1, -1},
									{2,  2, -2, -2},
									{1, -1,  1, -1},
									{2, -2,  2, -2}};
		
		StateRepresent newStateRepresent;
		
		
		byte opposite_colour;
		int i_eat, j_eat, i_new, j_new;
		
		if (this.states[i_][j_] == 1) 
			opposite_colour = 2;
		else
		    opposite_colour = 1;
		
		for (int k = 0; k < 4; k++)
		{	
				i_eat = i_ + options_of_move[0][k];
				i_new = i_ + options_of_move[1][k];
				j_eat = j_ + options_of_move[2][k];
				j_new = j_ + options_of_move[3][k];
		
		    //eat if possible
			if ((this.BorderCheck(i_new, j_new))&&(states[i_eat][j_eat] == opposite_colour)&&(states[i_new][j_new]==0))
			{
				newStateRepresent  = new StateRepresent(this.states, this.depth, this);
				
				newStateRepresent.states[i_][j_] = 0;
				newStateRepresent.states[i_eat][j_eat] = 0;
				newStateRepresent.states[i_new][j_new] = this.states[i_][j_];
				    
				    
				MoveList_ = newStateRepresent.EatCheck(i_new, j_new);
				
				if (MoveList_.isEmpty())
				    MoveList.add(newStateRepresent);
				else 
				    MoveList.addAll(MoveList_);
			 }
		}
		return MoveList;
	}
	
	//check borders
	private boolean BorderCheck(int i_, int j_)
	{
		if ((i_ <= 7 )&&(i_ >= 0)&&(j_ <= 7 )&&(j_ >= 0)){
			
			return true;}
		else
			return false;
	}
}
