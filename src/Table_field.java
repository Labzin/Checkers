
import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Table_field extends JPanel
{
	Object[][] cellData = {
		    {"row1-col1", "row1-col2"},
		    {"row2-col1", "row2-col2"}};
		String[] columnNames = {"col1", "col2"};

		JTable table = new JTable(cellData, columnNames);
		
		public void paint(Graphics g)
	    {
			super.paint(g);
	    }
		
}
