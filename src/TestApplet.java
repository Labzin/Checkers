
 //Reference the required Java libraries
 import java.applet.*;
 import java.awt.*;
 import java.awt.event.*;
 
 //The applet code
 public class TestApplet extends Applet 
 {
	 Table_field WField; 
	 
     public void paint(Graphics g) {
 
       //Draw a rectangle width=250, height=100
       //g.drawRect(0,0,250,100); 
    	 
       WField = new Table_field();
       WField.setVisible(true);
       WField.setBackground(Color.BLACK);
       //Set the color to blue
       g.setColor(Color.black); 
       
       WField.paint(g);
       
       //Write the message to the web page
       g.drawString("Look at me, I'm a Java Applet!",10,50); 
    }
 } 