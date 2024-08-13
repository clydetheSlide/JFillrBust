package fillrbust;

import javax.swing.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/** DicePanel_V
 * Shows the state of the dice graphically.
 *
 * Show the numbered dice<br>
 *	whether it scores,<br>
 *	whether it is chosen to score.<br>
 *	Seperate the reserved ones from those that were just rolled.<br>
 *	Separater is vertical on the right side
 *	or
 *	separater is horizontal on the bottom.
 *  
 */
class DicePanel_V extends JPanel{
    private String IMG_DIR;

    private ArrayList<JToggleButton> dice;
    private int iconW=0;
    private int iconH=0;
    private int gapW = 0; //15;	    // gap between dice
    private int gapH = 0; //25;
	private int fontH = 21;
    // dice gap set to zero until I understand why the gap between is not BLACk as I thought I set
    private int fHit = 20;  // fence thickness
    private String score = "0";
    private JLabel scoreLabel;
    private JLabel spaceLabel;
    private boolean debug=false;

	/**
	 * orientation of the seperator graphic
	 */
    public enum LayoutOrientation{
	VERTICAL, HORIZONTAL;
    }
    private LayoutOrientation vert_layout = LayoutOrientation.VERTICAL;

    /** Display a set of six dice
     *  and a number
     *  (presumably the score of the displayed dice)
     */
    public DicePanel_V() {
		this("images/");
    }
    public DicePanel_V(String diceDir) {
	this(diceDir,false);
    }
    public DicePanel_V(String diceDir, boolean debug) {
	this(diceDir, LayoutOrientation.VERTICAL, false);
    }
    public DicePanel_V(LayoutOrientation layOr) {
		this("images/", layOr);
    }
    public DicePanel_V(String diceDir, LayoutOrientation layOr) {
	this(diceDir, layOr, false);
    }
    public DicePanel_V(String diceDir, LayoutOrientation layOr, boolean debug) {
	this.vert_layout = layOr;
	  //setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	  //setLayout(new GridLayout(2,3,gapW,gapH));
	  setLayout(new GridBagLayout());
	  GridBagConstraints gbC = new GridBagConstraints();
	  setBackground(Color.BLACK);
	  if(debug)setBorder(BorderFactory.createLineBorder(Color.GREEN));
	IMG_DIR = diceDir;
	//System.out.println(IMG_DIR);
	dice =  new ArrayList<JToggleButton>();
	ImageIcon dicImg = new ImageIcon(getClass().getResource(IMG_DIR+"undieb.gif"));
	ImageIcon dicPImg = new ImageIcon(getClass().getResource(IMG_DIR+"undie.gif"));
	iconW=dicPImg.getIconWidth();
	iconH=dicPImg.getIconHeight();
	if(debug)System.out.println("building dice panel");
	//gbC.fill = GridBagConstraints.HORIZONTAL;
	gbC.ipadx=gapW;
	gbC.ipady=gapH;
	gbC.weightx = .45;
	for (int j=0;j<2;j++){	    // two row
	for (int i=0;i<3;i++){	    // three dice per row
	    JToggleButton each =  new JToggleButton(dicImg);
	    each.setSelectedIcon(dicPImg);
	    each.setMaximumSize(new Dimension(dicImg.getIconWidth(),dicImg.getIconHeight()));
	    each.setPreferredSize(new Dimension(dicImg.getIconWidth(),dicImg.getIconHeight()));
	    each.setBackground(Color.BLACK);
	    each.setForeground(Color.BLACK);
	    each.setOpaque(true);
	    each.setBorderPainted(false);
	    dice.add(each);
	    gbC.gridx=i;
	    gbC.gridy=j*2;
	    add(each, gbC);
	    //each.addActionListener(new ActionListener() {
            //    public void actionPerformed(ActionEvent fred) {
	//	    myGame.update("DICE");
            //    }
           // });
	}
	}
	// display the score below the dice
	scoreLabel = new JLabel(score);
	scoreLabel.setForeground(Color.YELLOW);
	scoreLabel.setBackground(Color.BLUE);
	scoreLabel.setFont(new Font("SansSerif",Font.BOLD,fontH));
	scoreLabel.setHorizontalAlignment(JLabel.CENTER);
	gbC.fill=GridBagConstraints.HORIZONTAL;
	gbC.gridwidth=3;
	gbC.gridy=3;
	gbC.gridx=0;
	add(scoreLabel, gbC);
	Fence fence;
        if(vert_layout== LayoutOrientation.VERTICAL){
	// make a "fence" right of the dice
	fence = new Fence(fHit,(iconH*2/*+gapW*2*/)+fontH);
	gbC.fill=GridBagConstraints.VERTICAL;
	gbC.anchor=GridBagConstraints.EAST;
	gbC.weightx=1.;
	gbC.gridwidth=1;
	gbC.gridx=3;
	gbC.gridy=0;
	gbC.gridheight = 4;
        }else{
	// make a "fence" below the dice
	fence = new Fence((iconW*3/*+gapW*2*/)-20, fHit);
	gbC.fill=GridBagConstraints.BOTH;
	gbC.fill=GridBagConstraints.HORIZONTAL;
	gbC.weightx=0.;
	gbC.gridwidth=3;
	gbC.gridx=0;
	gbC.gridy=4;
        }
	add(fence, gbC);
	// make a space between the rows
	spaceLabel =   new JLabel("Clyde");
	spaceLabel.setForeground(Color.BLACK);
	gbC.fill=GridBagConstraints.BOTH;
        if(vert_layout== LayoutOrientation.VERTICAL)
	      gbC.anchor=GridBagConstraints.CENTER;
	gbC.gridwidth=3;
	gbC.gridy=1;
        if(vert_layout== LayoutOrientation.VERTICAL)
	     gbC.gridheight = 1;
	add(spaceLabel, gbC);
    }

	/** set the value to be displayed as
	 *  the score of the selected dice
	 * @param score
	 */
    public void setScore(int score) {
	setScore(String.format("%d",score));
    }
    public void setScore(String score) {
	scoreLabel.setText(score);
    }

    /**
     * Set the dice display with two 6-character Strings:
     *   @param set  string represents the dice values (1-6),
     *   @param picked string indicates whether the die is selected
     *                 (1 selected, 0 not)
     */
    public void setDice(String set, String picked){
	boolean reserv = false;
	if(debug)System.out.println("in setDice, dir = "+IMG_DIR);
	for (int i=0;i<set.length();i++) {
	    char c = set.charAt(i);
	    JToggleButton bb = dice.get(i);
	    //String iname=IMG_DIR+"undie";
	    String iname=IMG_DIR+"blackdie";
	    if (c == '1') iname=IMG_DIR+"one";
	    else if (c == '2') iname=IMG_DIR+"two";
	    else if (c == '3') iname=IMG_DIR+"three";
	    else if (c == '4') iname=IMG_DIR+"four";
	    else if (c == '5') iname=IMG_DIR+"five";
	    else if (c == '6') iname=IMG_DIR+"six";
		try {
			//InputStream is;
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
	    if ( picked.charAt(i)=='1') bb.setSelected(true);
	}
	for (int i=set.length();i<6;i++){
	    String iname=IMG_DIR+"blackdie";
	    JToggleButton bb = dice.get(i);
		try {
			//InputStream is;
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
	}
    }

	/** report the estimated dimensions of the panel based on the dice image set
	 *
	 * @return width and height in a Dimension object
	 */
    public Dimension getPSize(){
	//return new Dimension(3*(iconW+gapW)-4, 2*(iconH+gapH)-4+10);
        if(vert_layout== LayoutOrientation.VERTICAL)
	       return new Dimension(3*iconW+15+fHit, 2*iconH+fontH+65);
        else
	       return new Dimension(3*iconW+2, 2*iconH+fHit+48+34+2);
    }

    /** graphics to embed in GUI
     */
    public class Fence extends JLabel {
	private BufferedImage image0;
	Fence(int wid, int hit) {
	    super();
	    image0 = new BufferedImage(wid,hit, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = image0.createGraphics();
	    GradientPaint shader;
           if(vert_layout== LayoutOrientation.VERTICAL)
	    shader = new GradientPaint(  0.f,0.f,new Color(174,174,184),
		                         wid,0.f,Color.WHITE);
	   else
            shader = new GradientPaint(  0.f,0.f,new Color(174,174,184),
		                         0.f,hit,Color.WHITE);
	    g2d.setPaint(shader);
	    g2d.fillRect(0, 0, wid,hit);
            setPreferredSize(new Dimension(wid, hit));
	}

	@Override
	public void paintComponent(Graphics g) {
	    /*
	    super.paintComponent(g);
	    GradientPaint primary = new GradientPaint(
                        0f, 0f, Color.WHITE, 200f, 0f, Color.ORANGE);
	    g.setColor(Color.BLUE);
	    //g.setPaint(primary);
            g.fillRect(200, 10, 30, 80);
	    */
           if(vert_layout== LayoutOrientation.VERTICAL)
	    g.drawImage(image0,0,10,null);
	   else
	    g.drawImage(image0,10,0,null);
        }
    }

    /** Set which dice are selected
     *  @param set  a 6-character String<br>
     *  1 means selected; anything else deselects it
     */
    public void setSelected(String set) {
	int limit = (set.length()<=6)?set.length():6;
		for (int i=0;i<limit;i++) {
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
	if (args.length > 0) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
		JFrame frame = new JFrame("Show Dice Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DicePanel_V dp = new DicePanel_V();
		//frame.setSize(700,300);
		Dimension dim = dp.getPSize();
		frame.setSize((int)dim.getWidth(), (int)dim.getHeight());
		frame.getContentPane().add(dp);
                frame.setVisible(true);
            }
        });
    }else {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
		JFrame frame = new JFrame("Show Dice Panel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DicePanel_V dp = new DicePanel_V(LayoutOrientation.HORIZONTAL);
		//frame.setSize(700,300);
		Dimension dim = dp.getPSize();
		frame.setSize((int)dim.getWidth(), (int)dim.getHeight());
		frame.getContentPane().add(dp);
                frame.setVisible(true);
            }
        });
    }

    }

}
