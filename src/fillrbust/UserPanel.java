/*
 * display the user name, the history of scores, the total score.
 * Provide methods to change:
 *    the color of the name - to indicate it is this player's turn
 *    the total score
 *    add a score change
 * Provide method to interact with the parent panel
 */
package fillrbust;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Color;

class UserPanel extends JPanel {
    JButton name;
    JLabel scoreToDate;
    JScrollPane textScroller;
    JTextArea scores;
    Player player;
	String uname;
    boolean turn = false;

    UserPanel(Player player){
	this.player = player;
    }
    /* for initial development of layout dont bind to a player */
    UserPanel(String uname) {
	setPreferredSize(new Dimension(80,300));
	if (uname.indexOf("ai")==0){
		//System.out.println(uname+" is ai");
		int es = uname.length();
		char aa =uname.charAt(es-1);
		System.out.println(aa);
		 if (Character.isDigit(aa))
		 {
			 es -=1;
			 //System.out.println(uname + " has risk " + aa);
		 }
		this.uname = uname.substring(0, es);
	} else {
		this.uname = uname;
		//System.out.println(uname+" is not ai");
	}
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
	name.setText(newn);
    }

	/**
	 * update the total score for the player
	 * @param news
	 */
    public void newScore(String news){
	scoreToDate.setText(news);
    }
	/**
	 * update the total score for the player
	 * @param news
	 */
    public void newScore(int news){
	scoreToDate.setText(String.format("%d",news));
    }

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
}
