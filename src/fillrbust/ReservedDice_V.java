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

/** ReservedDice
 * Shows the state of the dice graphically.
 *
 * Show the numbered dice: <br>
 *	separator oriented vertically on left
 *	or
 *	separater is horizontal on the bottom
 */
class ReservedDice_V extends JPanel{
    private String IMG_DIR;

    private ArrayList<JLabel> dice;
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
    private DicePanel_V.LayoutOrientation vert_layout;

    /** Display a set of six dice
     *  and a number
     *  (presumable the score of the displayed dice)
     */
    public ReservedDice_V() {
		this("images/");
    }
    public ReservedDice_V(String diceDir) {
	this(diceDir, false);
    }
    public ReservedDice_V(String diceDir, boolean debug) {
	this(diceDir, DicePanel_V.LayoutOrientation.VERTICAL, false);
    }
    public ReservedDice_V(DicePanel_V.LayoutOrientation layOr) {
		this("images/", layOr);
    }
	/** Display a set of six dice
	 *  and a number
	 *  (presumably the score of the displayed dice)
	 *
	 * @param diceDir - directory to find the images to represent the dice
	 * @param layOr the orientation of the seperator graphic
	 */
    public ReservedDice_V(String diceDir, DicePanel_V.LayoutOrientation layOr) {
	this(diceDir, layOr, false);
    }
    public ReservedDice_V(String diceDir, DicePanel_V.LayoutOrientation layOr, boolean debug) {
	this.vert_layout = layOr;
	  //setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
	  //setLayout(new GridLayout(2,3,gapW,gapH));
	  setLayout(new GridBagLayout());
	  GridBagConstraints gbC = new GridBagConstraints();
	  setBackground(Color.BLACK);
	  if(debug)setBorder(BorderFactory.createLineBorder(Color.GREEN));
	IMG_DIR = diceDir;
	//System.out.println(IMG_DIR);
	dice =  new ArrayList<JLabel>();
	ImageIcon dicPImg = new ImageIcon(getClass().getResource(IMG_DIR+"blackdie.gif"));
	iconW=dicPImg.getIconWidth();
	iconH=dicPImg.getIconHeight();
	if(debug)System.out.println("building dice panel");
	//gbC.fill = GridBagConstraints.HORIZONTAL;
	gbC.ipadx=gapW;
	gbC.ipady=gapH;
	gbC.weightx = .45;
	for (int i=0;i<3;i++){	    // three dice per row
	for (int j=0;j<2;j++){	    // two row
	    JLabel each =  new JLabel(dicPImg);
	    each.setMaximumSize(new Dimension(dicPImg.getIconWidth(),dicPImg.getIconHeight()));
	    each.setPreferredSize(new Dimension(dicPImg.getIconWidth(),dicPImg.getIconHeight()));
	    each.setBackground(Color.BLACK);
	    each.setForeground(Color.BLACK);
	    each.setOpaque(true);
	    //each.setBorderPainted(false);
	    dice.add(each);
            if(vert_layout == DicePanel_V.LayoutOrientation.VERTICAL){
	    gbC.gridx=i+1;
	    gbC.gridy=j*2;
            }else {
	    gbC.gridx=i;
	    gbC.gridy=2*j+1;
            }
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
	if(vert_layout == DicePanel_V.LayoutOrientation.VERTICAL){
	    gbC.gridy=3;
	    gbC.gridx=1;
	}else {
	    gbC.gridy=4;
	    gbC.gridx=0;
	}
	add(scoreLabel, gbC);
	Fence fence;
	// make a "fence" above the dice
        if(vert_layout== DicePanel_V.LayoutOrientation.VERTICAL){
	    fence = new Fence(fHit,(iconH*2/*+gapW*2*/)+fontH);
	    gbC.fill=GridBagConstraints.NONE;
	    gbC.fill=GridBagConstraints.VERTICAL;
	    gbC.anchor=GridBagConstraints.WEST;
	    gbC.weightx=1.;
	    gbC.gridwidth=1;
	    gbC.gridheight=4;
	    gbC.gridx=0;
	    gbC.gridy=0;
        } else {
	    fence = new Fence((iconW*3/*+gapW*2*/)-20, fHit);
	    gbC.fill=GridBagConstraints.BOTH;
	    gbC.weightx=0.;
	    gbC.gridwidth=3;
	    gbC.gridx=0;
	    gbC.gridy=0;
        }
	add(fence, gbC);
	// make a space between the rows
	spaceLabel = new Spacer((iconW*3/*+gapW*2*/)-20, fHit);
	//spaceLabel.setBackground(Color.YELLOW);	Fence fence;
        if(vert_layout== DicePanel_V.LayoutOrientation.VERTICAL){
	gbC.gridx=1;
	    gbC.anchor=GridBagConstraints.CENTER;
	    gbC.weightx=0.;
        gbC.gridheight=1;
	gbC.gridy=1;
        } else {
         gbC.gridy=2;
        }
	gbC.fill=GridBagConstraints.BOTH;
	gbC.gridwidth=3;
	    
	//add(spaceLabel, gbC);
	JLabel scoreLabel2 =  new JLabel("Clyde");
	scoreLabel2.setForeground(Color.BLACK);
	add(scoreLabel2, gbC);
    }

	/** set the number that will be displayed to represent the score of the dice
	 *
	 * @param score
	 */
    public void setScore(int score) {
	setScore(String.format("%d",score));
    }
    public void setScore(String score) {
	scoreLabel.setText(score);
    }

    /**
     * Set the dice display with 6-character String:
     *   @param set  string represents the values of the dice (1-6),
     */
    public void setDice(String set){
	//System.out.println("in setDice, dir = "+IMG_DIR);
	for (int i=0;i<set.length();i++) {
	    char c = set.charAt(i);
	    JLabel bb = dice.get(i);
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
					String.format("%s.gif",iname))));
		} catch (NullPointerException e) {
			Image dicPImg = Toolkit.getDefaultToolkit().getImage(
					String.format("%s.gif", iname));
			bb.setIcon(new ImageIcon(dicPImg));
		}
	}
	for (int i=set.length();i<6;i++){
	    String iname=IMG_DIR+"blackdie";
	    JLabel bb = dice.get(i);
		try {
			bb.setIcon(new ImageIcon(getClass().getResource(
				String.format("%s.gif",iname))));
		} catch (NullPointerException e) {
			Image dicPImg = Toolkit.getDefaultToolkit().getImage(
				String.format("%s.gif", iname));
			bb.setIcon(new ImageIcon(dicPImg));
		}
	}
    }

	/** report the dimensions of the panel based on
	 *  the size of the images used to represent the dice.
	 * @return width and height as a Dimension object
	 */
	public Dimension getPSize(){
	//return new Dimension(3*(iconW+gapW)-4, 2*(iconH+gapH)-4+10);
        if(vert_layout== DicePanel_V.LayoutOrientation.VERTICAL)
	    return new Dimension(3*iconW+2+fHit, 2*iconH+34+18+2);
        else
	    return new Dimension(3*iconW+2, 2*iconH+fHit+34+18+2);
    }

    /** graphics to embed in GUI
     */
    public class Spacer extends JLabel {
	int wid=0;
	int hit=0;
	Spacer(int wid, int hit) {
	    super();
	    this.wid=wid;
	    this.hit=hit;
	    /*
	    image0 = new BufferedImage(wid,hit, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = image0.createGraphics();
	    g2d.setColor(Color.BLACK);
	    g2d.fillRect(0, 0, wid,hit);
	    */
            setPreferredSize(new Dimension(wid, hit));
	}

	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(Color.BLUE);
            g.fillRect(100, 50, wid, hit);
	    /*
	    GradientPaint primary = new GradientPaint(
                        0f, 0f, Color.WHITE, 200f, 0f, Color.ORANGE);
	    //g.setPaint(primary);
	    */
	    //g.drawImage(image0,10,0,null);
        }
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
           if(vert_layout== DicePanel_V.LayoutOrientation.VERTICAL)
	     shader = new GradientPaint(  0.f,0.f,new Color(164,164,164),
			                  wid,0.f,Color.BLACK);
           else
	     shader = new GradientPaint(  0.f,0.f,new Color(164,164,164),
			                  0.f,hit,Color.BLACK);
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
           if(vert_layout== DicePanel_V.LayoutOrientation.VERTICAL)
	    g.drawImage(image0,0,10,null);
           else
	    g.drawImage(image0,10,0,null);
        }
    }

    public static void main(String[] args){
	String set;
	if (args.length >0){
	    set = args[0];
	    java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
		    JFrame frame = new JFrame("Show Reserved Dice");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    ReservedDice_V dp = new ReservedDice_V();
		    //frame.setSize(700,300);
		    Dimension dim = dp.getPSize();
		    dp.setBounds(0,0,(int)dim.getWidth(), (int)dim.getHeight()+22);
		    frame.setSize((int)dim.getWidth(), (int)dim.getHeight()+29);
		    frame.getContentPane().add(dp);
		    frame.setVisible(true);
		    dp.setDice(set);
		    dp.setScore("550");
		}
	    });
	}else 
	    java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
		    JFrame frame = new JFrame("Show Reserved Dice");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    ReservedDice_V dp = new ReservedDice_V(DicePanel_V.LayoutOrientation.HORIZONTAL);
		    //frame.setSize(700,300);
		    Dimension dim = dp.getPSize();
		    dp.setBounds(0,0,(int)dim.getWidth(), (int)dim.getHeight()+21);
		    frame.setSize((int)dim.getWidth(), (int)dim.getHeight()+29);
		    frame.getContentPane().add(dp);
		    frame.setVisible(true);
		}
	    });

    }

}
