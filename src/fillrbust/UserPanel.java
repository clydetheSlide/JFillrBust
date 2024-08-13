package fillrbust;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;

/** Display the user name, the history of scores, the total score.
 * Provide methods to change:
 *    the color of the name - to indicate it is this player's turn
 *    the total score
 *    add a score change
 * Provide method to interact with the parent panel
 */
class UserPanel extends JPanel {
    JButton name;
    JLabel scoreToDate;
    JScrollPane textScroller;
    JTextArea scores;
    Player player;
	String uname;
    boolean turn = false;
	int index;

    UserPanel(Player player, int index){
	  this.player = player;
	  this.index = index;
    }
    /* for initial development of layout dont bind to a player */
    UserPanel(String uname, int index) {
	//setPreferredSize(new Dimension(110,300));
	setPreferredSize(new Dimension(110,240));
	if (uname.indexOf("ai")==0){
		//System.out.println(uname+" is ai");
		int es = uname.length();
		char aa =uname.charAt(es-1);
		//System.out.println(aa);
		 if (Character.isDigit(aa))
		 {
			 es -=1;
			 //System.out.println(uname + " has risk " + aa);
		 }
		this.uname = uname.substring(0, es);  // name does not include risk value
	} else {
		this.uname = uname;
		//System.out.println(uname+" is not ai");
	}
	this.index = index;
	name = new JButton(this.uname);
	scoreToDate = new JLabel("0");
	scores = new JTextArea(25,6);
	scores.setEditable(false);
	textScroller = new JScrollPane(scores);

	//textScroller.add(scores);
	setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	add(name);
	add(textScroller);
	add(scoreToDate);
    }

	public void changeName(String newn){
		changeName(newn,false);
	}
    public void changeName(String newn, boolean debug){
	    if (newn.indexOf("ai")==0){
		    if(debug)System.out.println(newn+" is ai");
		    int es = newn.length();
		    char aa =newn.charAt(es-1);
		    //System.out.println(aa);
		    if (Character.isDigit(aa))
		    {
			    es -=1;
			    if(debug)System.out.println(newn + " has risk " + aa);
		    }
		    this.uname = newn.substring(0, es);
		    if(debug)System.out.println("Rename to "+this.uname);
	    } else {
		    this.uname = newn;
		    if(debug)System.out.println(newn+" is not ai");
	    }
	name.setText(newn);
    }

	/**
	 * update the total score for the player
	 * @param news is the new score for the user
	 */
    public void newScore(String news){
	scoreToDate.setText(news);
    }
	/**
	 * update the total score for the player
	 * @param news is the new score for the user
	 */
    public void newScore(int news){
	scoreToDate.setText(String.format("%d",news));
    }

	public String getScore() { return scoreToDate.getText();}

	/**
	 * add a score to the player's scoresheet
	 * @param news is the score to add
	 */
    public void addScore(String news) {
	scores.append(news+"\n");
	scores.setCaretPosition(scores.getDocument().getLength());
    }
	/**
	 * add a score to the player's scoresheet
	 * @param news is the score to add
	 */
    public void addScore(int news) {
	scores.append(String.format("%d\n",news));
	scores.setCaretPosition(scores.getDocument().getLength());
    }

	/**
	 * toggle the indicator of whether the player is playing
	 */
    public void toggleTurn() {
	turn = ! turn;
	updateTurn();
    }

	void updateTurn() {
		if (turn) name.setBackground(Color.GREEN);
		else name.setBackground(Color.WHITE);
	}

	/**
	 *
	 * @return player's name
	 */
	public String getUName() { return uname;}

	public int getIndex() {return index;}

	public JButton myButton() {return name;
	}

	public void reset(){
		scores.setText("");
		newScore(0);
		// TODO
	}
}
