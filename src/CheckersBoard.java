import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.TransferHandler;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicReference;

public class CheckersBoard extends JFrame implements DragGestureListener, Transferable  {

	private JPanel mainPanel = new JPanel();
	private JPanel downPanel = new JPanel();
    private JPanel leftPanel = new JPanel();
	
	private PanelIm[][] spots= new PanelIm[8][8];
	private JLabel[][]  spotsLabels = new JLabel[8][8];
	private Image wField;
	private Image bField;
	
	//icons for jLabel
	private ImageIcon bPiece;
	private ImageIcon wPiece;
	private ImageIcon bPieceKing;
	private ImageIcon wPieceKing;
	private ImageIcon stepMark;
	
	//cursors for drug and drop
	Cursor cursorW;
	Cursor cursorB;
	
	AtomicReference<CheckersBoard> linkBoard = new AtomicReference<CheckersBoard>();
	
	//actual state of the board
	StateRepresent currentState;
	
	//coordinates of the drug piece
	int iStart, jStart;
	
	//drag and drop
	MouseListener listenerMouse;
	DragSource dragSource =  new DragSource();
	
	//1 = white, 2 = black
	int current_colour_of_turn = 1;
	//
	ArrayList<StateRepresent> successors;
	
    public CheckersBoard(StateRepresent state, int colour_of_turn) {
        try {
        	//images for fields 
        	this.wField = ImageIO.read(new File("n:/WField.jpg"));
			this.bField = ImageIO.read(new File("n:/BField.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.currentState = state;
        
        linkBoard.set(this);
        
        //cursor for drag and drop
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image imageW = toolkit.getImage("n:/WCursor.gif");
        cursorW = toolkit.createCustomCursor(imageW , new Point(0, 0), "img");
        
        Image imageB = toolkit.getImage("n:/BCursor.gif");
        cursorB = toolkit.createCustomCursor(imageB , new Point(0, 0), "img"); 
        
        //icons for jLabel
        wPiece = new ImageIcon("n:/WPieceS.jpg");
        bPiece = new ImageIcon("n:/BPieceS.jpg");
        bPieceKing = new ImageIcon("n:/BPieceSking.jpg");
        wPieceKing = new ImageIcon("n:/WPieceSking.jpg");
        
        stepMark = new ImageIcon("n:/stepMarkS.gif");
        
        //turn and successors
        current_colour_of_turn = colour_of_turn;
        successors = currentState.SuccessorsFunc(current_colour_of_turn);
    }
	
	public void Start() {
			
        setTitle("Board");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.CreatePanels(getContentPane());
        
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        
        this.Draw();
		}
	
	private void CreatePanels(Container Panel)
		{
			GridLayout gridLayout = new GridLayout(8, 8);
			mainPanel.setLayout(gridLayout);
			
			
			for (int row = 0; row < 8; row++) 
			{
	            for (int col = 0; col < 8; col++) 
	            {
	            	 if ((row + col) % 2 == 0) {
	            		 spots[row][col] = new PanelIm(wField);
	                 } 
	            	 else {
	                	 spots[row][col] = new PanelIm(bField);
	                 }
	            	 
	            	 spotsLabels[row][col] = new JLabel();
	            	 
	            	 	            	           	 
	            	 spotsLabels[row][col].setName(String.valueOf(row) +" "+ String.valueOf(col));
	            	 spots[row][col].setName(String.valueOf(row) +" "+ String.valueOf(col));
	            	 
	            	 spots[row][col].add(spotsLabels[row][col]);
        	 
	            	//drag and drop
	            	 dragSource.createDefaultDragGestureRecognizer(spotsLabels[row][col], DnDConstants.ACTION_MOVE, this);
	            	 new MyDropTargetListener(spots[row][col], linkBoard);
	            	 
	            	 mainPanel.add(spots[row][col]);	
	            }	                	
	         }
									
							
			
			//set numbers
			leftPanel.setLayout(new GridLayout(8, 0));
			downPanel.setLayout(new GridLayout(0, 8));
	        for (int i = 0; i < 8; i++) {
	            leftPanel.add(new JLabel(String.valueOf(i) + ""));
	            downPanel.add(new JLabel("               " +String.valueOf(i) + ""));     
	        }
	                
	        
	        Panel.add(leftPanel, BorderLayout.WEST);
			Panel.add(mainPanel, BorderLayout.CENTER);
			Panel.add(downPanel, BorderLayout.SOUTH);
		}
	
	public void Draw()
	{
		System.out.println("!!!!!!!!!!!!");
		for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
            	switch (currentState.states[row][col]){
    			//white piece
    			case 1: spotsLabels[row][col].setIcon(wPiece);
    					break;
    			//black piece
    			case 2: spotsLabels[row][col].setIcon(bPiece);
    					break;
    			//white piece king
    			case 3: spotsLabels[row][col].setIcon(wPieceKing);
    					break;
    			//black piece king
    			case 4: spotsLabels[row][col].setIcon(bPieceKing);
    					break;				
    			default: spotsLabels[row][col].setIcon(null);
    					break;
            	}
            }
		}
	}
	
	public void  DrawState(StateRepresent state)
	{
		this.currentState = state;
		this.Draw();
		
	}  
	
	//nested class used to set the background of frame contenPane
    class PanelIm extends JPanel {

        private Image image;

        public PanelIm(Image img) {
            image = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            //draws image to background to scale of frame
            g.drawImage(image, 0, 0, null);
        	}
    	} 
             	
    //check is possible the move started from given drag
    private boolean MoveCheck(int i, int j)
    {
    	//flag for possibility of the step
    	boolean stepFlag = false;
    	
    	//potential moves
    	if ((currentState.states[i][j]==current_colour_of_turn)||(currentState.states[i][j]==(current_colour_of_turn + 2)))
    	{
    		//ArrayList<StateRepresent> Moves = currentState.SuccessorsFunc(current_colour_of_turn);
    	
    	
    	
    		//check is the move is possible
    		for (int k=0; k < successors.size(); k++) 
    		{ 
    			if (successors.get(k).states[i][j] ==0)
    			{
    				//search for the place in which the piece can be moved and mark it
    				for (int row =0;row<8;row++ )
    						for (int col = 0; col<8; col++) 			//condition for regular step		   
    							if((currentState.states[row][col]==0)&&((successors.get(k).states[row][col]==currentState.states[i][j])||
    																	//condition for became white king
    																	((successors.get(k).states[row][col] == 3)&& (row == 0)&&(currentState.states[i][j] == 1))||
    																	//condition for became black king
    																	((successors.get(k).states[row][col] == 4)&& (row == 7)&&(currentState.states[i][j] == 2))))
    							{
    								spotsLabels[row][col].setIcon(stepMark); 
    							}
    										
    				stepFlag = true;
    			}
    		}
    	}
    
    	if (stepFlag)
    		return true;
    	else 
    		return false;
    }
    
    //catch drag event!
	@Override
	public void dragGestureRecognized(DragGestureEvent event) {
		
		JLabel label = (JLabel) event.getComponent();
		
		int i = Character.getNumericValue(label.getName().charAt(0));
		int j = Character.getNumericValue(label.getName().charAt(2));
		
		
		//check if the move is possible
		if (this.MoveCheck(i, j))
		{
			spotsLabels[i][j].setIcon(null);
			//put cursor depends from the colour of the piece
			if ((currentState.states[i][j] == 1)||(currentState.states[i][j] == 3))
				   event.startDrag(cursorW, this);
		    else 
		           event.startDrag(cursorB, this);
			
			//write coordinates for the drug piece
			iStart = i;
			jStart = j;
			System.out.println(iStart + " " + jStart);
		}
	}
	
	public void makeMove(int iDestination, int jDestination)
	{

		//check is the move is possible
		for (int k=0; k < successors.size(); k++) 
		{ 
			//based on coordinates from drug action(iStart, jStart) search the move to in a given point			   
				if((successors.get(k).states[iStart][jStart]==0)&
						//condition for regular step
						((successors.get(k).states[iDestination][jDestination]==currentState.states[iStart][jStart])||
						//condition for became white king
						(successors.get(k).states[iDestination][jDestination] == 3)&&(iDestination==0)&&(currentState.states[iStart][jStart]==1)||
						//condition for became black king
						(successors.get(k).states[iDestination][jDestination] == 4)&&(iDestination==7)&&(currentState.states[iStart][jStart]==2)))
						{
							//change the board state
							this.currentState = new StateRepresent(successors.get(k).states,this.currentState.depth + 1, this.currentState);
							this.currentState.PositionPrint();
							
							System.out.println();
							System.out.println();
										
			   			    //change the turn
							if (current_colour_of_turn ==1) current_colour_of_turn = 2;
							    else current_colour_of_turn = 1;
							
							successors =  currentState.SuccessorsFunc(current_colour_of_turn);
							
							break;
						}
		}
		
		this.Draw();
	}

	//catch drop event!!
	class MyDropTargetListener extends DropTargetAdapter {

	        private DropTarget dropTarget;
	        private JPanel panel;
	        public AtomicReference<CheckersBoard> board;
	        int i,j;
	        
	    //AtomicReference to a create link on the main board object
	    public MyDropTargetListener(JPanel panel, AtomicReference<CheckersBoard>  board) {
	        this.panel = panel;
	        this.board = board; 
	        
	        i = Character.getNumericValue(panel.getName().charAt(0));
			j = Character.getNumericValue(panel.getName().charAt(2));
	        
	        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, 
	            this, true, null);
	      }

	     //
	      public void drop(DropTargetDropEvent event) {
	        try {
	        	
	        	System.out.println(i + " " + j);
	        	
	        	//run in the main Board!!
	        	board.get().makeMove(i,j);

	          event.rejectDrop();
	        } catch (Exception e) {
	          e.printStackTrace();
	          event.rejectDrop();
	        }
	      }
	    }
	
	
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		return false;
	}
	
}