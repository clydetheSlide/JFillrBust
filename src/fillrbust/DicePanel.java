package fillrbust;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.InputStream;
import java.util.*;

/* DicePanel
 * Shows the state of the dice graphically.
 *
 * Show the numbered dice
 *	whether it scores
 *	whether it is chosen to score
 *	seperate the reserved ones from those that were just rolled
 *  
 */

class DicePanel extends JPanel{
    String IMG_DIR;

    ArrayList<JToggleButton> dice;

    /** display a set of six dice with a seperator to indicate some are inactive */
    public DicePanel() {
		this("images/");
    }
    public DicePanel(String diceDir) {
	  //setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	  setLayout(new FlowLayout(FlowLayout.CENTER,0,2));
	  setBackground(Color.BLACK);
	  setBorder(BorderFactory.createLineBorder(Color.GREEN));
	IMG_DIR = diceDir;
	dice =  new ArrayList<JToggleButton>();
	ImageIcon dicImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(IMG_DIR+"undieb.gif"));
	ImageIcon dicPImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(IMG_DIR+"undie.gif"));
	//System.out.println("building dice panel");
	for (int i=0;i<7;i++){
	    JToggleButton each =  new JToggleButton(dicImg);
	    each.setSelectedIcon(dicPImg);
	    each.setMaximumSize(new Dimension(dicImg.getIconWidth(),dicImg.getIconHeight()));
	    each.setPreferredSize(new Dimension(dicImg.getIconWidth(),dicImg.getIconHeight()));
	    each.setBackground(Color.BLACK);
	    dice.add(each);
	    add(each);
	    //each.addActionListener(new ActionListener() {
            //    public void actionPerformed(ActionEvent fred) {
	//	    myGame.update("DICE");
            //    }
           // });
	}
	ImageIcon sepImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(IMG_DIR+"fence.gif"));
	dice.get(6).setIcon(sepImg);
	dice.get(6).setMaximumSize(new Dimension(sepImg.getIconWidth(),sepImg.getIconHeight()));
    }

    /**
     * Set the dice display with two 7-character Strings:
     *   @param set  the first string represents the values (1-6),
     *   a space indicates all dice after are 'reserved'
     *   @param picked the second string indicates whether the die is selected
     */
    public void setDice(String set, String picked){
	boolean reserv = false;
	for (int i=0;i<set.length();i++) {
	    char c = set.charAt(i);
	    JToggleButton bb = dice.get(i);
	    String iname=IMG_DIR+"undie";
	    if (c == '1') iname=IMG_DIR+"one";
	    else if (c == '2') iname=IMG_DIR+"two";
	    else if (c == '3') iname=IMG_DIR+"three";
	    else if (c == '4') iname=IMG_DIR+"four";
	    else if (c == '5') iname=IMG_DIR+"five";
	    else if (c == '6') iname=IMG_DIR+"six";
	    else if (c == ' ') {
		iname=IMG_DIR+"fence";
		reserv = true;
	    }
		try {
			InputStream is;
			bb.setIcon(new ImageIcon(getClass().getResource(
					String.format("%sb.gif",iname))));
			bb.setSelectedIcon(new ImageIcon(getClass().getResource(
					String.format("%s.gif",iname))));
		} catch (NullPointerException e) {
			Image dicImg = Toolkit.getDefaultToolkit().getImage(String.format("%sb.gif", iname));
			Image dicPImg = Toolkit.getDefaultToolkit().getImage(String.format("%s.gif", iname));
			bb.setIcon(new ImageIcon(dicImg));
			bb.setSelectedIcon(new ImageIcon(dicPImg));
		}
	    if (reserv || picked.charAt(i)=='1') bb.setSelected(true);
	}
    }

    /** Set which dice are selected
     *  @param set  a 7-character String<br>
     *  1 means selected; anything else deselects it
     */
    public void setSelected(String set) {
		for (int i=0;i<set.length();i++) {
			dice.get(i).setSelected(set.charAt(i)=='1');
		}
    }

    /** get the set of JToggleButtons that depict the dice */
    public ArrayList<JToggleButton> getDice() {
		return dice;
    }

    /** Report which dice are selected
     *   as a String
     * @return String where '1' indicates it has been selected; '0' otherwise
     */
    public String getSelected() {
		StringBuilder out = new StringBuilder();
		for (JToggleButton eac: dice) {
			if (eac.isSelected()) out.append("1");
			else out.append("0");
		}
		return out.toString();
    }

    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
				JFrame frame = new JFrame("Show Dice Panel");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(700,300);
				DicePanel dp = new DicePanel();
				frame.getContentPane().add(dp);

                frame.setVisible(true);
            }
        });

    }

}
