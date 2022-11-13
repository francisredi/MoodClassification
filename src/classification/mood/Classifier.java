package src.classification.mood;

import java.awt.*;
import java.awt.event.*;

/**
 * The Classifier class is a class that starts the mood classification program. It creates a dialog window for input.
 * 
 * @author Francisco Rojas
 * @author Enkhbold Nyamsuren
 */
public class Classifier {

	/**
	 * This is where the program gets booted up.
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		/**
		 * contains the object for application window
		 */
		MainWindow myUI = new MainWindow();
		/**
		 * window event listener for application window
		 */
		WindowListener wL = new WindowAdapter() { 
			public void windowClosing(WindowEvent e){
				((Window) e.getSource()).dispose();
				System.exit(0);
			}
		};
		
		myUI.addWindowListener(wL);
	}
}