package src.classification.mood;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import src.classification.mood.technique.Technique;
import src.classification.mood.technique.TermCooccurrencesImpl;
import src.classification.mood.technique.VADClusteringImpl;
import src.classification.mood.technique.WordFrequencyImpl;

/**
 * This class provides UI interface for the whole application,
 * The user can trigger all three types of method just using single window.
 * Also the class acts as action listener for all controls that it creates.
 * 
 * @author Enkhbold Nyamsuren
 * @see JFrame
 * @see ActionListener
 */

public class MainWindow extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	/**
	 * this is the container object of main window
	 */
	Container windowContainer;
	/**
	 * contains reference for the blog file to be classified 
	 */
	File blogFile;
	
	/**
	 * shows the path to blog file or directory chosen by user
	 */
	JTextField filePath;
	
	/**
	 * Open File button	 
	 */
	JButton openFileButton;
	
	/**
	 * Activates select file mode for File chooser dialog 
	 */
	JRadioButton selectFile;
	
	/**
	 * Activates select directory mode for File chooser dialog 
	 */
	JRadioButton selectDirectory;
	
	/**
	 * Sets VADClustering method as a preffered classification method 
	 */
	JRadioButton vad;
	
	/**
	 * Sets VADClustering method as a preffered classification method 
	 */
	JRadioButton frequency;
	
	/**
	 * Sets VADClustering method as a preffered classification method 
	 */
	JRadioButton cooccurence;
	
	/**
	 * Start Classification button
	 */
	JButton startButton;
	/**
	 * Shows the result of the classification
	 */
	JTextArea resultArea;
	
	/**
	 * File chooser section of the main window
	 */
	JPanel fileSection;
	
	/**
	 * Classification method choosing section of main window
	 */
	JPanel methodSection;
	
	/**
	 * Result showing section of main window
	 */
	JPanel resultSection;
	
	/**
	 * Constructor of the class automatically creates the sections.
	 * Whole window is divided int 3 sections: section to select file
	 * section to select classification method, and section to see
	 * result of the classification.
	 */
	public MainWindow(){
		super("Mood Classifier");
		windowContainer = this.getContentPane();
	
		createFileSection();
		createMethodSection();
		createResultSection();
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(3,1));
			
		mainPanel.add(fileSection);
		mainPanel.add(methodSection);
		mainPanel.add(resultSection);
		
		windowContainer.add(mainPanel);
		
		this.setSize(500, 500);
		this.setVisible(true);
	}
	
	/**
	 * creates the section for choosing a file from file browsing dialog that
	 * consists of button for calling file browser dialog and textfield showing
	 * path of the file
	 * 
	 */
	public void createFileSection(){
		Dimension fieldDim = new Dimension(100, 25);

		fileSection = new JPanel();
		fileSection.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(1, 1, 1, 1);
		c.gridx = 0;
		c.gridy = 0;
		JLabel label = new JLabel("Select file to classify:");
		fileSection.add(label, c);
		
		c.gridx = 0;
		c.insets = new Insets(1, 1, 1, 1);
		c.gridy = 1;
		c.ipadx = 250;
		filePath = new JTextField();
		filePath.setPreferredSize(fieldDim);
		filePath.setEditable(false);
		fileSection.add(filePath, c);
		
		c.ipadx = 0;
		c.gridx = 1;
		c.gridy = 1;
		openFileButton = new JButton("Open File");
		openFileButton.addActionListener(this);
		fileSection.add(openFileButton, c);
		
		c.gridx = 0;
		c.gridy = 3;
		selectFile = new JRadioButton("Select File");
		selectFile.addActionListener(this);
		selectFile.setSelected(true);
		fileSection.add(selectFile, c);
		
		c.gridx = 1;
		c.gridy = 3;
		selectDirectory = new JRadioButton("Select Folder");
		selectDirectory.addActionListener(this);
		fileSection.add(selectDirectory, c);
	}
	
	/**
	 * creates the the method selection section of the window
	 * includes the 3 method radio buttons and Start Classsification button
	 */
	public void createMethodSection(){
		methodSection = new JPanel();
		methodSection.setLayout(new GridBagLayout());

		vad = new JRadioButton("VAD");
		vad.setSelected(true);
		vad.addActionListener(this);
		frequency = new JRadioButton("Frequency");
		frequency.addActionListener(this);
		cooccurence = new JRadioButton("Cooccurence");
		cooccurence.addActionListener(this);
		
		startButton = new JButton("Start Classification");
		startButton.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(1, 1, 1, 1);
		
		c.gridx = 2;
		c.gridy = 0;
		JLabel label = new JLabel("Select classification method:");
		methodSection.add(label, c);

		c.gridx = 0;
		c.gridy = 1;
		methodSection.add(vad, c);
		
		c.gridx = 2;
		methodSection.add(frequency, c);
		
		c.gridx = 3;
		methodSection.add(cooccurence, c);	
		
		c.anchor = GridBagConstraints.CENTER&GridBagConstraints.SOUTH;
		c.gridy = 4;
		methodSection.add(startButton, c);
		startButton.setEnabled(false);
	}
	
	/**
	 * Creates the Panel for showing results of classification
	 * The result can be outputed in the panel thorugh "printOutput" method
	 * @see printOutput
	 */
	public void createResultSection(){	
		resultSection = new JPanel();
		resultSection.setLayout(new BorderLayout());

		resultArea = new JTextArea();
		resultArea.setEditable(false);
		resultArea.setColumns(50);
		resultArea.setRows(10);
		resultArea.setText("");
		
		resultSection.add(new JScrollPane(resultArea));
	}
	
	/**
	 * This action callback method is used for all controls usen in current class. 
	 * This callback method is called on event on: Open File button, 
	 * radio buttons for selecting file or folder, radio buttons for selecting classification method, 
	 * and Start Classification button
	 *  
	 * @param ActionEvent	event that was done on the controll, such as pressing
	 * @return void
	 * @see ActionEvent
	 */
	public void actionPerformed(ActionEvent myEvent){	//
		String action = myEvent.getActionCommand();

		if(action.equals("Select File")){	//for Select File radio button
			selectFile.setSelected(true);
			selectDirectory.setSelected(false);
		}
		
		if(action.equals("Select Folder")){	//for Select Folder radio button
			selectFile.setSelected(false);
			selectDirectory.setSelected(true);
		}
		
		if(action.equals("Open File")){	//Open File button action is handled here
			blogFile = openFile();
			if(blogFile != null){
				filePath.setText(blogFile.getPath());
			} else{
				JOptionPane.showMessageDialog(this, "Error selecting file!");
			}
		}
		
		if(action.equals("VAD")){ //VAD radio button
			vad.setSelected(true);
			frequency.setSelected(false);
			cooccurence.setSelected(false);
		} 

		if(action.equals("Frequency")){	//Word Frequency radio button
			vad.setSelected(false);
			frequency.setSelected(true);
			cooccurence.setSelected(false);
		}

		if(action.equals("Cooccurence")){	//Term cooccurence Frequency radio button
			vad.setSelected(false);
			frequency.setSelected(false);
			cooccurence.setSelected(true);
		}
		
		//Action handler for "Start Classification" starts the process of classification
		if(action.equals("Start Classification")){
			startClassification();
		}
	}
	
	/**
	 * This method is called when Open File button is pressed.
	 * The function of this method is to create and call file choosing dialog 
	 * which is object of JFileChooser;
	 *  
	 * @see JFileChooser
	 */
	private File openFile(){		
		//opens file or directory which user selected using file browser dialog
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open Blog");
		if(selectFile.isSelected())	//cheking wheter to open file or directory, depends on selection of radio buttons
			fc.setFileSelectionMode( JFileChooser.FILES_ONLY);	
		else 
			fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		fc.setCurrentDirectory (new File ("."));

		int result = fc.showOpenDialog(this);
		
		if(result == JFileChooser.CANCEL_OPTION){ 
			return null;
		} else if(result == JFileChooser.APPROVE_OPTION){
			startButton.setEnabled(true);	//enabling Start Classification button
			return fc.getSelectedFile();			
		}
		return null;
	}
	
	/**
	 * This method is called when Start Classification button is pressed.
	 * Method is used to start classification process. Depending on user 
	 * priority the method creates the object of class for classification
	 * method, and calls its runTechnique method to start classification.
	 *  
	 * @see TechniqueImpls
	 * @see VADClusteringImpl
	 * @see WordFrequencyImpl
	 * @see TermCooccurrencesImpl
	 */
	public void startClassification(){
		Technique technique = null;
		
		// depending on radio button selection checking whihc method to call
		if(vad.isSelected()){      // if VAD Clustering is desired
			technique = new VADClusteringImpl(this);
		}
		else if(frequency.isSelected()){ // if Word Frequency is desired
			technique = new WordFrequencyImpl(this);
		}
		else if(cooccurence.isSelected()){ // default is Term Co-occurrences
			technique = new TermCooccurrencesImpl(this);
		}
		
		if(technique != null){
			technique.runTechnique(blogFile);	
		}
	}
	
	/**
	 * This method is used to print out the result of the classification
	 * on the result textarea.
	 *  
	 * @param output	The string output to print out.
	 */
	public void printOutput(String output){
		resultArea.setText(resultArea.getText() + "\n" + output);
	}
}