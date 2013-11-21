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
import java.io.File;
import java.io.IOException;
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
	
	private ImageIcon bPiece;
	private ImageIcon wPiece;
	
	AtomicReference<CheckersBoard> linkBoard = new AtomicReference<CheckersBoard>();
	
	StateRepresent state;
	
	//drag and drop
	MouseListener listenerMouse;
	DragSource dragSource =  new DragSource();
	
    public CheckersBoard(StateRepresent state) {
        try {
        	this.wField = ImageIO.read(new File("n:/WField.jpg"));
			this.bField = ImageIO.read(new File("n:/BField.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.state = state;
        
        linkBoard.set(this);
        
     
        wPiece = new ImageIcon("n:/WPieceS.jpg");
        bPiece = new ImageIcon("n:/BPieceS.jpg");

    }
	
	public void Start() {
			
        setTitle("Board");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.CreatePanels(getContentPane());
        
        setSize(800, 600);
        setLocationRelativeTo(null);
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
            	switch (state.states[row][col]){
    			//white piece
    			case 1: spotsLabels[row][col].setIcon(wPiece);
    					break;
    			//black piece
    			case 2: spotsLabels[row][col].setIcon(bPiece);
    					break;
    			default: spotsLabels[row][col].setIcon(null);
    					break;
            	}
            }
		}
	}
	
	public void  DrawState(StateRepresent state)
	{
		this.state = state;
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
             	

    //catch drag event!
	@Override
	public void dragGestureRecognized(DragGestureEvent event) {
		
		JLabel label = (JLabel) event.getComponent();
		System.out.println(label.getName());
		
		
		Cursor cursor = null;
        if (event.getDragAction() == DnDConstants.ACTION_COPY) {
            cursor = DragSource.DefaultCopyDrop;
        }
        
        event.startDrag(cursor, this);
	}

	//catch drop event!!
	class MyDropTargetListener extends DropTargetAdapter {

	        private DropTarget dropTarget;
	        private JPanel panel;
	        public AtomicReference<CheckersBoard> board;
	        
	     public MyDropTargetListener(JPanel panel, AtomicReference<CheckersBoard>  board) {
	        this.panel = panel;
	        this.board = board; 
	        
	        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, 
	            this, true, null);
	      }


	      public void drop(DropTargetDropEvent event) {
	        try {
	        	//System.out.println(label.getName());
	        	//name.set(label.getName()); 
	        	System.out.println(panel.getName());
	        	board.get().Draw();
	        	//this.label.setIcon(null);
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