import java.util.ArrayList;


public class Hello_world {
	public static void main(String args[])
	{
		StateRepresent Repr = new StateRepresent();
		//Repr.PositionPrint();
		ArrayList<StateRepresent> Moves = new ArrayList<StateRepresent>();
		Moves =		Repr.MoveFunction(5 ,2);
		//System.out.println(Repr.states[5-1][2-1]);
		//System.out.println("");
		
		for (int i=0; i < Moves.size(); i++) 
		{ 
			Moves.get(i).PositionPrint(); 
			System.out.println("");
			System.out.println("");
		}
	}

}
