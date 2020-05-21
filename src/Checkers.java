public class Checkers {
	public static void main(String args[])
	{
			StateRepresent Repr = new StateRepresent();
			
			
			byte[][] initialState  = {
					{2, 7, 2, 7, 2, 0, 2, 0},
					{7, 2, 7, 0, 0, 2, 0, 2},
					{2, 7, 2, 7, 2, 7, 2, 0},
					{7, 0, 7, 0, 7, 0, 7, 0},
				    {0, 7, 0, 7, 2, 7, 0, 7},
				    {7, 1, 7, 1, 7, 1, 7, 1},
					{1, 7, 1, 7, 1, 7, 1, 7},
					{7, 1, 7, 1, 7, 1, 7, 1}};
			
			
			Repr.states = initialState;

			CheckersBoard Board = new CheckersBoard(Repr, 1);
			Board.Start();
//			Board.simulation(1, 3,  8);
	}
}