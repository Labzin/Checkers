import javax.swing.ImageIcon;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Hello_world {
	public static void main(String args[])
	{
		//try{
	
			StateRepresent Repr = new StateRepresent();
			
			
			byte[][] states = {
					{0, 7, 0, 7, 2, 7, 2, 7},
					{7, 0, 7, 2, 7, 2, 7, 2},
					{0, 7, 0, 7, 1, 7, 0, 7},
					{7, 1, 7, 1, 7, 1, 7, 0},
				    {0, 7, 0, 7, 0, 7, 0, 7},
				    {7, 0, 7, 2, 7, 1, 7, 3},
					{0, 7, 0, 7, 1, 7, 0, 7},
					{7, 4, 7, 0, 7, 0, 7, 1}};
			
			
			Repr.states = states;
			ArrayList<StateRepresent> Moves = new ArrayList<StateRepresent>();
			
			CheckersBoard Board = new CheckersBoard(Repr, 2);
			Board.Start();
			
			//Moves =	Repr.MoveFunction(5, 3);
			
			//System.out.println(Moves.size());
			//System.in.read();
			
			for (int i=0; i < Moves.size(); i++) 
			{ 
								
				System.out.println(i);
				System.out.println(i);
				System.out.println(i);
				System.out.println(i);
				
				Board = new CheckersBoard(Moves.get(i),1);
				Board.Start();
				
				Moves.get(i).PositionPrint();
				
				//Board.DrawState(Moves.get(i));		
			}
			
			
			//} catch (IOException ex) {
							//     ex.printStackTrace();
		//}
        

	}

}
