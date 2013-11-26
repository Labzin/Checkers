import java.util.ArrayList;


public  class MinMaxAI {
	
	public StateRepresent startMinMax(StateRepresent startState, int colour_of_turn, int depthOfSearch)
	{
		ArrayList<StateRepresent> successors = startState.SuccessorsFunc(colour_of_turn);
		
		ArrayList<StateRepresent> goodMoves = new ArrayList<StateRepresent>();
		
		int maxForWhite  = -Integer.MAX_VALUE;
		int maxForWhite_current  =  -Integer.MAX_VALUE;
		
		int minForBlack  =  Integer.MAX_VALUE;
		int minForBlack_current  =  Integer.MAX_VALUE;
		
		for (int i=0; i < successors.size(); i++) 
		{   
			if (colour_of_turn == 1)
			{
				
				maxForWhite_current = this.recursivMinMax((StateRepresent)successors.get(i), 2, (depthOfSearch - 1));
				
				System.out.println("Heuristic white"+ maxForWhite_current);
				
				if (maxForWhite_current > maxForWhite)
				{
					maxForWhite = maxForWhite_current;					
					
					//add to list of moves with equal quality
					goodMoves = new ArrayList<StateRepresent>();
					goodMoves.add((StateRepresent)successors.get(i));
				}
				else if (maxForWhite_current == maxForWhite)
				{
					//add to list of moves with equal quality
					goodMoves.add((StateRepresent)successors.get(i));
				}
				
			}
			else 
			{			
				minForBlack_current = this.recursivMinMax((StateRepresent)successors.get(i), 1, (depthOfSearch - 1));
				
				System.out.println("Heuristic white"+ minForBlack_current);
				
				if (minForBlack_current < minForBlack)
				{
					minForBlack = minForBlack_current;
					
					//add to list of moves with equal quality
					goodMoves = new ArrayList<StateRepresent>();
					goodMoves.add((StateRepresent)successors.get(i));
				}
				else if (minForBlack_current == minForBlack)
				{
					//add to list of moves with equal quality
					goodMoves.add((StateRepresent)successors.get(i));
				}
			}
		}
		
		System.out.println("Best moves "+ goodMoves.size());
		//return random move from list of best moves with equal quality
		return 	(goodMoves.get((int)(goodMoves.size()*Math.random())));

	}
	
	private int recursivMinMax(StateRepresent startState, int colour_of_turn, int depthOfSearch)
	{
		ArrayList<StateRepresent> successors = startState.SuccessorsFunc(colour_of_turn);
		
		int maxForWhite  = -Integer.MAX_VALUE;
		int maxForWhite_current = -Integer.MAX_VALUE;

		
		int minForBlack  =  Integer.MAX_VALUE;
		int minForBlack_current =  Integer.MAX_VALUE;
		
  
			//for white colour
			if (colour_of_turn == 1)
			{
				//if there are no successors or limit of depth, return current state heuristic
				if ((successors.isEmpty())||((depthOfSearch - 1) <= 0))
				{
					maxForWhite = startState.heuristicDifference();
				}
				else
				{   //search over successors
					for (int i=0; i < successors.size(); i++) 
					{   
						//next step of recursion
						maxForWhite_current = this.recursivMinMax((StateRepresent)successors.get(i), 2, (depthOfSearch - 1));	
				
						
						if (maxForWhite_current > maxForWhite)
							maxForWhite = maxForWhite_current;
					}
				}
			}
			//for black colour
			else
			{
				//if there are no successors, return current state heuristic
				if ((successors.isEmpty())||((depthOfSearch - 1) <= 0))
				{
					minForBlack = startState.heuristicDifference();
				}
				else
				{	//search over successors
					for (int i=0; i < successors.size(); i++) 
					{  
						//next step of recursion
						minForBlack_current = this.recursivMinMax((StateRepresent)successors.get(i), 1, (depthOfSearch - 1));
				
						if (minForBlack_current < minForBlack)		
							minForBlack = minForBlack_current;
						
					}
				}
					
		}
		
		if 	(colour_of_turn == 1)
			return maxForWhite;
		else
			return minForBlack;
	}

	//return random move
	public StateRepresent randomStep(StateRepresent startState,  int colour_of_turn)
	{
		ArrayList<StateRepresent> successors = startState.SuccessorsFunc(colour_of_turn);
		
		if (successors.isEmpty())
			return null;
		else 
			return (successors.get((int)(successors.size()*Math.random())));
	}
}
