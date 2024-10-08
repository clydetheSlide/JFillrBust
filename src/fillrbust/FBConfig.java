package fillrbust;

import javax.swing.*;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.BufferedReader;
import java.io.InputStreamReader;

/** Defines a configuration for FillRBust <br>
 * and provides defaults values
 * and IO methods for storing and retrieving it. */
public class FBConfig {
	/** the score at which to declare a winner */
	int goal;
	/** player name(s) including preceding ai and risk */
	String[] players;
	/** true to use GUI; otherwise text-driven */
	boolean gui;
	/** Speak the instructions (unimplemented) */
	boolean speak;
	/** draw animated dice (unimplemented) */
	boolean pov;
	/** directory to find the images depicting the cards */
	String cardDir;
	/** directory to find the images depicting the dice */
	String diceDir;
	/** font size for the user instructions */
	int fontSize;
	/** orientation of the DicePanel seperator */
	DicePanel_V.LayoutOrientation layout;
	/** name of the file to which to write this data */
	String configFile;
	/** whether to write debugging info to STDOUT */
	boolean debug;

	/** name the default file to store this config info */
	public static String defaultFile() {
		return ".fillrbustrc";
	}

	/** sets the default config data */
	public void defaults() {
		configFile = defaultFile();
		goal = 5000;
		players = new String[]{"Myself", "aiFoe5"};
		gui = true;
		cardDir = "images/Cards/Orig/";  // Orig, Big, Huge
		diceDir = "images/Dice/Medium/";    // Medium, Big, Orig
		fontSize = 14;
		speak = false;
		pov = false;
		layout = DicePanel_V.LayoutOrientation.VERTICAL;
		debug = false;
	}

	/** Initializes the FillRBust configuration
	 *
	 * @param filename file to look for config data
	 */
	public FBConfig(String filename) {
	    //String lineA="Nothing read yet";
	    String whereAmI="I don't know where I am";
		//ArrayList<String> temp = new ArrayList<>(3);
		try {
			Process process = Runtime.getRuntime().exec("pwd ");
			String s;
			int exitCode1 = process.waitFor();
			//System.out.println("Exit Code: " + exitCode1);
			BufferedReader br = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			while ((s = br.readLine()) != null){
				//System.out.println("line: " + s);
				whereAmI=s;
			}
			process.waitFor();
			//System.out.println("exit: " + process.exitValue());
			process.destroy();
		} catch (IOException | InterruptedException e) {
			int fred = 4;
		}
		defaults();
		readFile(filename);
	}

	/** Looks in file for any FBConfig info */
	public void readFile(String filename){
	    String lineA="Nothing read yet";
	    String whereAmI="I don't know where I am";
		ArrayList<String> temp = new ArrayList<>(3);
		try {
			File myObj = new File(filename);
			//System.out.println("Openned RC file:" + filename);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				lineA = myReader.nextLine();
				String[] data = lineA.split(" ",2); // keyword, name -> name can contain spaces
				//System.out.println(lineA);
				switch (data[0]) {
					case "WINNING_SCORE":
						goal = Integer.parseInt(data[1]);
						break;
					case "PLAYER":
						temp.add(data[1]);
						break;
					case "CARDS_DIR":
						cardDir = data[1];
						break;
					case "DICE_DIR":
						diceDir = data[1];
						break;
					case "GUI":
						gui = data[1].equalsIgnoreCase("true");
						break;
					case "FONT_SIZE":
						fontSize = Integer.parseInt(data[1]);
						break;
					case "LAYOUT":
						layout = (data[1].equalsIgnoreCase("vertical"))?
						    DicePanel_V.LayoutOrientation.HORIZONTAL:
						    DicePanel_V.LayoutOrientation.VERTICAL;
						break;
					case "POV":
						pov = data[1].equalsIgnoreCase("true");
						break;
					case "SPEAK":
						speak = data[1].equalsIgnoreCase("true");
						break;
					case "DEBUG":
						debug = data[1].equalsIgnoreCase("true");
						break;
				}
			}
			myReader.close();
			configFile = filename;
			//players = (String[]) temp.toArray();
			players = new String[temp.size()];
			for (int ii = 0; ii< temp.size(); ii++) {
				players[ii]=temp.get(ii);
			}
		} catch (NullPointerException e) {
			System.out.println(String.format("A NullPointerError occurred reading config in %s; used defaults.",whereAmI));
			System.out.println(whereAmI);
			System.out.println(lineA);
			System.out.println("A NullPointerError occurred reading config; used defaults.");
		} catch (FileNotFoundException | ClassCastException e) {
			System.out.println(String.format("An error occurred reading config in %s; used defaults.",whereAmI));
			//System.out.println("An error occurred reading config; used defaults.");
			//e.printStackTrace();
		}
	}

	public FBConfig() {
		this(FBConfig.defaultFile());
	}

	/** opens file chooser to determine where to store config info
	 *
	 * @param master - the root window for the GUI
	 * @param max - score at which to declare a winner
	 * @param players - list of players' names
	 */
	public void writeConfig(JFrame master, int max, String[] players) {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(master);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				FileWriter myWriter = new FileWriter(file);
				myWriter.write(String.format("WINNING_SCORE %d\n", max));
				for (String each : players) myWriter.write(String.format("PLAYER %s\n", each));
				myWriter.write(String.format("CARDS_DIR %s\n", cardDir));
				myWriter.write(String.format("DICE_DIR %s\n", diceDir));
				myWriter.write(String.format("GUI %s\n", gui ? "true" : "false"));
				myWriter.write(String.format("LAYOUT %s\n", 
					    (layout==DicePanel_V.LayoutOrientation.VERTICAL) ?
					      "HORIZONTAL" : "VERTICAL"));
				myWriter.write(String.format("FONT_SIZE %d\n", fontSize));
				myWriter.write(String.format("POV %s\n", pov ? "true" : "false"));
				myWriter.write(String.format("SPEAK %s\n", speak ? "true" : "false"));
				myWriter.close();
			} catch (IOException x) {
				System.err.format("IOException: %s%n", x);
			}
		} else {
			System.out.println("Open command cancelled by user.");
		}
	}
}
