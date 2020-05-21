import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.Timer;


public  class MinMaxAI {
	
	StateRepresent Repr = new StateRepresent();
	
	
	byte[][] states = {
			{0, 7, 0, 7, 0, 7, 0, 7},
			{7, 0, 7, 0, 7, 0, 7, 0},
			{0, 7, 0, 7, 0, 7, 2, 7},
			{7, 0, 7, 0, 7, 0, 7, 0},
		    {0, 7, 0, 7, 2, 7, 0, 7},
		    {7, 0, 7, 1, 7, 0, 7, 0},
			{0, 7, 0, 7, 0, 7, 0, 7},
			{7, 0, 7, 0, 7, 0, 7, 0}};
	
	
		
	public StateRepresent startMinMax(StateRepresent startState, int colour_of_turn, int depthOfSearch)
	{
		ArrayList<StateRepresent> successors = startState.SuccessorsFunc(colour_of_turn);
		
		ArrayList<StateRepresent> goodMoves = new ArrayList<StateRepresent>();
		
		int maxForWhite  = -Integer.MAX_VALUE;
		int maxForWhite_current  =  -Integer.MAX_VALUE;
		int a = -Integer.MAX_VALUE;
		
		int minForBlack  =  Integer.MAX_VALUE;
		int minForBlack_current  =  Integer.MAX_VALUE;
		int b = Integer.MAX_VALUE;
		
		for (int i=0; i < successors.size(); i++) 
		{   
			if (colour_of_turn == 1)
			{
				
				maxForWhite_current = this.recursivMinMax((StateRepresent)successors.get(i), 2, (depthOfSearch - 1), a, b);
				
//				System.out.println("Heuristic white"+ maxForWhite_current);
				
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
				minForBlack_current = this.recursivMinMax((StateRepresent)successors.get(i), 1, (depthOfSearch - 1), a, b);
				
//				System.out.println("Heuristic black"+ minForBlack_current);
				
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
		
//		System.out.println("Best moves "+ goodMoves.size());
		//return random move from list of best moves with equal quality
		return 	(goodMoves.get((int)(goodMoves.size()*Math.random())));

	}
	
	private int recursivMinMax(StateRepresent startState, int colour_of_turn, int depthOfSearch, int a, int b)
	{
		ArrayList<StateRepresent> successors = startState.SuccessorsFunc(colour_of_turn);
		
		//int maxForWhite  = -Integer.MAX_VALUE;
		int maxForWhite_current = -Integer.MAX_VALUE;

		
		//int minForBlack  =  Integer.MAX_VALUE;
		int minForBlack_current =  Integer.MAX_VALUE;
		
  
			//for white colour
			if (colour_of_turn == 1)
			{
				//if there are no successors or limit of depth, return current state heuristic
				if ((successors.isEmpty())||((depthOfSearch - 1) <= 0))
				{
					a = startState.heuristicDifference();
				}
				else
				{   //search over successors
					for (int i=0; i < successors.size(); i++) 
					{   
						//next step of recursion
						maxForWhite_current = this.recursivMinMax((StateRepresent)successors.get(i), 2, (depthOfSearch - 1), a, b);	
				
						
						//if (maxForWhite_current > maxForWhite)
						//	maxForWhite = maxForWhite_current;
						
						if (maxForWhite_current > a)
							a = maxForWhite_current;
						
						if (a >= b)
							return b;
					}	
				}
				return a;
			}
			//for black colour
			else
			{
				//if there are no successors, return current state heuristic
				if ((successors.isEmpty())||((depthOfSearch - 1) <= 0))
				{
					b = startState.heuristicDifference();
				}
				else
				{	//search over successors
					for (int i=0; i < successors.size(); i++) 
					{  
						//next step of recursion
						minForBlack_current = this.recursivMinMax((StateRepresent)successors.get(i), 1, (depthOfSearch - 1), a, b);
				
						//if (minForBlack_current < minForBlack)		
						//	minForBlack = minForBlack_current;
						
						if (minForBlack_current < b)
							b = minForBlack_current;
							
						if (a >= b)
							return a;
					}
				 }
				return b;
			}
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


	public void SimpleTimeDelay(long timeDelay)
	{
		long currentTime = System.currentTimeMillis();
		while ((System.currentTimeMillis() - currentTime) < timeDelay)
		{
//			Thread.sleep(1000);
		}
	}
}
