import javax.swing.ImageIcon;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Hello_world {
	public static void main(String args[])
	{
		try{
	
			StateRepresent Repr = new StateRepresent();
			
			
			byte[][] states = {
					{2, 7, 2, 7, 2, 7, 2, 7},
					{7, 2, 7, 2, 7, 2, 7, 2},
					{2, 7, 2, 7, 2, 7, 2, 7},
					{7, 1, 7, 0, 7, 0, 7, 0},
				    {0, 7, 0, 7, 0, 7, 0, 7},
				    {0, 1, 7, 1, 7, 1, 7, 1},
					{1, 7, 0, 7, 1, 7, 1, 7},
					{7, 1, 7, 1, 7, 1, 7, 1}};
			
			
			Repr.states = states;
			ArrayList<StateRepresent> Moves = new ArrayList<StateRepresent>();
			Moves =	Repr.EatCheck(2 ,2);
			
			for (int i=0; i < Moves.size(); i++) 
			{ 
				System.in.read();
				Moves.get(i).PositionPrint();
				CheckersBoard Board = new CheckersBoard(Moves.get(i));
				Board.Start();		
			}
			
					
			} catch (IOException ex) {
							     ex.printStackTrace();
		}
        

	}

}
