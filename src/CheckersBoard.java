import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;

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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.TransferHandler;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
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

public class CheckersBoard extends JFrame implements DragGestureListener, Transferable, ChangeListener   {

	private JPanel mainPanel = new JPanel();
	private JPanel downPanel = new JPanel();
    private JPanel leftPanel = new JPanel();
    private JPanel sliderPanel = new JPanel();
    private JButton buttonStart = new JButton("Start");
    private JButton buttonInformation = new JButton("?");
    
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
	
	//reference for drag and drop, important!!
	AtomicReference<CheckersBoard> linkBoard = new AtomicReference<CheckersBoard>();
	
	//actual state of the board
	StateRepresent currentState;
	
	//coordinates of the piece for drug
	int iStart, jStart;
	
	//drag and drop
	MouseListener listenerMouse;
	DragSource dragSource =  new DragSource();
	
	//1 = white, 2 = black
	int current_colour_of_turn = 1;
	
	ArrayList<StateRepresent> successors;
	
	//class for AI
	MinMaxAI sudoAI;
	
	//depth of search
	JSlider depthOfSearchSlider = new JSlider(3,15,3);
	int  depthOfSearch = 3;
	
	//link to current component
	private Component currentComponent = this;
	
	String tutorial_guidance = "Black moves first and play proceeds alternately. From their initial positions, checkers may only move forward.\n"
    		+ "There are two types of moves that can be made, capturing moves and non-capturing moves.\n"
    		+ "Non-capturing moves are simply a diagonal move forward from one square to an adjacent square.\n"
    		+ "Capturing moves occur when a player 'jumps' an opposing piece. This is also done on the diagonal\n"
    		+ " and can only happen when the square behind (on the same diagonal) is also open. This means that you may not jump an opposing piece around a corner.\n"
    		+ "The victory will be due to complete elimination.\n\n"
    		+ "Capturing Move:\n"
    		+ "On a capturing move, a piece may make multiple jumps. If after a jump a player is in a position to make another jump then he may do so.\n"
    		+ "This means that a player may make several jumps in succession, capturing several pieces on a single turn.\n\n"
    		+ "Forced Captures:\n"
    		+ "When a player is in a position to make a capturing move, he must make a capturing move.\n"
    		+ "When he has more than one capturing move to choose from he may take whichever move suits him.\n\n"
    		+ "Kings:\n"
    		+ "When a checker achieves the opponent's edge of the board (called the 'king's row') it is crowned with another checker.\n" 
    		+ "This signifies that the checker has been made a king. The king now gains an added ability to move backward.\n" 
    		+ "The king may now also jump in either direction or even in both directions in one turn (if he makes multiple jumps).";
	
    public CheckersBoard(StateRepresent state, int colour_of_turn) {
    	
    	//slider for depth of search
    	depthOfSearchSlider.addChangeListener(this);
    	depthOfSearchSlider.setMajorTickSpacing(1);
    	depthOfSearchSlider.setPaintTicks(true);
    	depthOfSearchSlider.setPaintLabels(true);
    	depthOfSearchSlider.setPreferredSize(new Dimension(400,50));
    	
    	
    	
        try {
        	//images for fields 
        	this.wField = ImageIO.read(this.getClass().getResource("/images/WField.jpg"));
			this.bField = ImageIO.read(this.getClass().getResource("/images/BField.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.currentState = state;
        
        linkBoard.set(this);
        
        //cursor for drag and drop
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image imageW = toolkit.getImage(this.getClass().getResource("/images/WCursor.gif"));
        cursorW = toolkit.createCustomCursor(imageW , new Point(0, 0), "img");
        
        Image imageB = toolkit.getImage(this.getClass().getResource("/images/BCursor.gif"));
        cursorB = toolkit.createCustomCursor(imageB , new Point(0, 0), "img"); 
        
        //icons for jLabel
        wPiece = new ImageIcon(this.getClass().getResource("/images/WPieceS.jpg"));
        bPiece = new ImageIcon(this.getClass().getResource("/images/BPieceS.jpg"));
        bPieceKing = new ImageIcon(this.getClass().getResource("/images/BPieceSking.jpg"));
        wPieceKing = new ImageIcon(this.getClass().getResource("/images/WPieceSking.jpg"));
        
        stepMark = new ImageIcon(this.getClass().getResource("/images/stepMarkS.gif"));
        
        //turn and successors
        current_colour_of_turn = colour_of_turn;
        successors = currentState.SuccessorsFunc(current_colour_of_turn);
        
        //create AI
        sudoAI = new MinMaxAI();
        
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
	
	//draw move regular or step-by-step
	public void DrawMove(StateRepresent state)
	{
		
		//draw step-by-step
		for (int i=0; i < state.transSteps.size(); i++) 
		{ 
			this.currentState = new StateRepresent(state.transSteps.get(i).states, state.depth + 1, state);
			
			this.Draw();
			   
			this.currentState.PositionPrint();
			
			System.out.println();
			System.out.println("step");
			state.transSteps.get(i).PositionPrint();
			
			sudoAI.SimpleTimeDelay(1000);
		}
		
		//sudoAI.SimpleTimeDelay(300);
		
		System.out.println("step1");
		this.currentState = state;
		this.currentState.PositionPrint();
		successors =  currentState.SuccessorsFunc(current_colour_of_turn);
		
		this.Draw();
		
	}  
	
	//runs only with the constructor 
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
	            	 
	            	 //set names 	            	           	 
	            	 spotsLabels[row][col].setName(String.valueOf(row) +" "+ String.valueOf(col));
	            	 spots[row][col].setName(String.valueOf(row) +" "+ String.valueOf(col));
	            	 
	            	 //add label to a panel
	            	 spots[row][col].add(spotsLabels[row][col]);
        	 
	            	//drag and drop
	            	 dragSource.createDefaultDragGestureRecognizer(spotsLabels[row][col], DnDConstants.ACTION_MOVE, this);
	            	 new MyDropTargetListener(spots[row][col], linkBoard);
	            	 
	            	 //add panel on main board
	            	 mainPanel.add(spots[row][col]);	
	            }	                	
	         }
									
							
			
			//set numbers
			//leftPanel.setLayout(new GridLayout(8, 0));
			//downPanel.setLayout(new GridLayout(0, 8));
	        //for (int i = 0; i < 8; i++) {
	        //    leftPanel.add(new JLabel(String.valueOf(i) + ""));
	        //    downPanel.add(new JLabel("               " +String.valueOf(i) + ""));     
	        //}
	        
	        //set start button listener
	        buttonStart.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	            	//set start state
	                currentState = new StateRepresent();
	                successors =  currentState.SuccessorsFunc(current_colour_of_turn);
	                Draw();
	           }
	        });
	        
	        
	        sliderPanel.setLayout(new GridLayout(0, 2));
	        //add slider
	        sliderPanel.add(depthOfSearchSlider);
	        //add button
	        sliderPanel.add(buttonStart);
	        
	        //set information button listener
	        buttonInformation.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent event) {
	            	//show tutorial guidance
	                JOptionPane.showMessageDialog(currentComponent, tutorial_guidance, "Tutorial guidance", JOptionPane.INFORMATION_MESSAGE); 
	           }
	        });
	        leftPanel.setLayout(new GridLayout(8, 0));
	        leftPanel.add(buttonInformation);
	        
	        Panel.add(leftPanel, BorderLayout.WEST);
			Panel.add(mainPanel, BorderLayout.CENTER);
			//Panel.add(downPanel, BorderLayout.SOUTH);
			Panel.add(sliderPanel, BorderLayout.SOUTH);
		}
	
	//show message about winner
	private void showPopUpWindow(String text, int timeToShow)
	{
		JButton label = new JButton(text);
		label.setPreferredSize((new Dimension(200,50)));
		
        PopupFactory factory = PopupFactory.getSharedInstance();
        final Popup popup = factory.getPopup(this, label, 850, 600);
        popup.show();
        
        ActionListener hider = new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            popup.hide();
          }
        };
        // Hide popup in 3 seconds
        Timer timer = new Timer(timeToShow, hider);
        timer.start();
		
	}
	
	//draw current state representation
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
		this.repaint();
	}
	
	public void simulation(int colour_of_turn)
	{
		while (!((this.currentState.checkLose(1))||(this.currentState.checkLose(2))))
		{
			//try 
			//{
			   StateRepresent newStape;
			
			   this.currentState =  sudoAI.startMinMax(this.currentState, 1, 3);
				
				
				this.Draw();
				
				if (this.currentState.checkLose(2))
						{
							showPopUpWindow("The white player is won!",2000);
							break;
						}
				sudoAI.SimpleTimeDelay(1000);
				
				this.currentState =  sudoAI.startMinMax(this.currentState, 2, 15);
				//sudoAI.randomStep(this.currentState, 2);
				
				this.Draw();	
				
				if (this.currentState.checkLose(1))
				{
					showPopUpWindow("The black player is won!",2000);
					break;
				}
				
				sudoAI.SimpleTimeDelay(1000);
			
		}
		
	}
	
	public void  DrawState(StateRepresent state)
	{
		this.currentState = state;
		this.Draw();
		
	}  
	
	//class to draw image in bachground for panel
    class PanelIm extends JPanel {

        private Image image;

        public PanelIm(Image img) {
            image = img;
        }

        @Override
        protected void paintComponent(Graphics g) {
            //draws image to background
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
	
	//!!!make a move!!!
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
							//this.currentState = new StateRepresent(successors.get(k).states, this.currentState.depth + 1, this.currentState);
							
							
						    this.DrawMove(successors.get(k));
							
							this.currentState.PositionPrint();
							//this.Draw();
							
							System.out.println();
							System.out.println();
							/*if (current_colour_of_turn ==1)	
								current_colour_of_turn = 2;
							else
								current_colour_of_turn = 1;*/
							
			   			    //move of the opponent
							if (current_colour_of_turn ==1) 
							{
								//check for end of the game
								if (this.currentState.checkLose(2))
									{
									    showPopUpWindow("The white player is won!",2000); 
									    break;
									}
										
								// get move from AI and change the board state
								this.currentState = sudoAI.startMinMax(this.currentState, 2, this.depthOfSearch);
								
								//check for end of the game
								if (this.currentState.checkLose(1))
									{
										showPopUpWindow("The black player is won!",2000);     
										break;
									}
								
							}
							else
							{ 
								//check for end of the game
								if (this.currentState.checkLose(1))
									{
										showPopUpWindow("The black player is won!",2000); 
										break;
									}
								
								// get move from AI and change the board state
								this.currentState = sudoAI.randomStep(this.currentState, 1);
								
								//check for end of the game
								if (this.currentState.checkLose(2))
									{
									 	showPopUpWindow("The white player is won!",2000); 
										break;
									}
							}
							
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
	
	//system method for drag and drop, is not used
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	//system method for drag and drop, is not used
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return null;
	}

	//system method for drag and drop, is not used
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//change listener for the slider
	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) 
	    {
	    	this.depthOfSearch = (int)source.getValue();
	    }
	}
	
}