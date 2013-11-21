import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class CheckersBoard extends JFrame {

	private JPanel mainPanel = new JPanel();
	private JPanel downPanel = new JPanel();
    private JPanel leftPanel = new JPanel();
	
	private PanelIm[][] spots= new PanelIm[8][8];
	private JLabel[][]  spotsLabels = new JLabel[8][8];
	private Image wField;
	private Image bField;
	
	private ImageIcon bPiece;
	private ImageIcon wPiece;
	
	StateRepresent state;
	
    public CheckersBoard(StateRepresent state) {
        try {
        	this.wField = ImageIO.read(new File("n:/WField.jpg"));
			this.bField = ImageIO.read(new File("n:/BField.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.state = state;

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
	            	 if ((col + row) % 2 == 0) {
	            		 spots[row][col] = new PanelIm(wField);
	                 } 
	            	 else {
	                	 spots[row][col] = new PanelIm(bField);
	                 }
	            	 
	            	 spotsLabels[row][col] = new JLabel();
	            	 spots[row][col].add(spotsLabels[row][col]);
	            	 
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
	
	private void Draw()
	{
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
	}
