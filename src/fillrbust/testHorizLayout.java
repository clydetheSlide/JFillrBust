package fillrbust;
import javax.swing.*;					 //                GridBagLayout
import java.awt.event.ActionListener;                    // +----------+----------+---------+-----------+
import java.awt.event.ActionEvent;                       // |          |          |         |           |
import java.awt.Toolkit;                                 // | card     |  rolled  | reserv  |           |
import java.awt.Image;                                   // |          |          |         |           |
import java.awt.Color;                                   // |          |          |         |  users    |
import java.awt.Dimension;                               // +----------+----------+---------+           +
import java.awt.FlowLayout;                              // |          |                    |           |
import java.awt.Font;                                    // | options  |    instruction     |           |
import java.awt.GridBagLayout;                           // |          |                    |           |
import java.awt.GridBagConstraints;                      // |          |                    |           |
import java.util.*;                                      // +----------+--------------------+-----------+


/** Build mockup of GUI with two dice panels on the left
 * and other controls on the right.
 */
class testHorizLayout extends JFrame{
	private JPanel choices;
	private JPanel rightBottom;	
	private JScrollPane detailsP;
	private int nlines = 1;
	private JTextArea details;
	private DicePanel_V diceP;
	private ReservedDice_V diceR;
	private ArrayList<UserPanel> players = new ArrayList<UserPanel>(4);
	private JLabel maxScore;
	private JButton card;
	private JButton optionA;
	private JButton optionB;
	private DicePanel_V.LayoutOrientation vert_layout;

	public testHorizLayout() {
	    this(DicePanel_V.LayoutOrientation.VERTICAL);
	}
	public testHorizLayout(DicePanel_V.LayoutOrientation layOr) {
	    this.vert_layout = layOr;
	    setTitle("testHorizLayout");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBackground(Color.BLACK);
	    setForeground(Color.BLACK);
	    // dice panels
	    diceP = new DicePanel_V("images/", vert_layout);
	    diceR = new ReservedDice_V("images/", vert_layout);
	    // opts
	    choices = new JPanel();
	    choices.setLayout(new BoxLayout(choices,BoxLayout.Y_AXIS));
	    choices.setBackground(Color.BLACK);
	    choices.setBorder(BorderFactory.createLineBorder(Color.BLUE));
	    optionA = new JButton("optionA");
	    optionB = new JButton("optionB");
	    choices.add(optionA);
	    choices.add(optionB);
	    // card
	    String cardFolder = "images/";
	    ImageIcon imi =new ImageIcon(getClass().getResource(cardFolder+"title.gif"));
	    JPanel cPan = new JPanel();
	    cPan.setBackground(Color.BLACK);
            card = new JButton(imi);
	    card.setPreferredSize(new Dimension(imi.getIconWidth(),imi.getIconHeight()));
	    cPan.add(card);
	    // user date to the side
	    JPanel users = new JPanel();
	    users.setLayout(new GridBagLayout());
	       GridBagConstraints uGB = new GridBagConstraints();
	    maxScore = new JLabel("5000");
	    uGB.gridx     =0;
	    uGB.gridy     =0;
	    uGB.gridwidth =2;
	    users.add(maxScore, uGB);
	    UserPanel clyde = new UserPanel("Clyde",players.size());
	    uGB.gridx     =0;
	    uGB.gridy     =1;
	    uGB.gridwidth =1;
	    users.add(clyde, uGB);
	    players.add(clyde);
	    UserPanel nancy = new UserPanel("Nancy",players.size());
	    uGB.gridx     =1;
	    users.add(nancy,uGB);
	    users.setBorder(BorderFactory.createLineBorder(Color.RED));
	    players.add(nancy);
	    // instructions
	    details = new JTextArea(8,30);
	    details.setFont(new Font("Ænigma Scrawl 4 BRK",Font.PLAIN, /*config.fontSize)*/15));
	    details.setEditable(false);
	    detailsP = new JScrollPane(details);
	    //detailsP.setBounds(0,frameDims[1].height,
	    //		      frameDims[0].width,frameDims[0].height-frameDims[1].height);
	    //		    123456789012345678901234567890
	    details.append("Follow these instructions:\n");
	    //details.append("Follow");
	    detailsP.setBorder(BorderFactory.createLineBorder(Color.RED));
      
	    // layout components
	    setLayout(new GridBagLayout());
	    GridBagConstraints gbC = new GridBagConstraints();
	    if (vert_layout == DicePanel_V.LayoutOrientation.VERTICAL){
	    // opts
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.gridx =0;
	    gbC.gridy =0;
	    gbC.gridheight=1;
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.anchor = GridBagConstraints.PAGE_START;
	    add(choices, gbC);
	    // card
	    gbC.gridx =0;
	    gbC.gridy =1;
	    gbC.fill = GridBagConstraints.NONE;
	    //gbC.anchor = GridBagConstraints.CENTER;
	    gbC.anchor = GridBagConstraints.NORTH;
	    gbC.gridheight=1;
	    add(cPan, gbC);

	    // dice panels
	    gbC.gridx =1;
	    gbC.gridy =0;
	    gbC.gridheight=1;
	    gbC.anchor = GridBagConstraints.EAST;
	    gbC.fill = GridBagConstraints.VERTICAL;
	    add(diceP, gbC);
	    gbC.gridx =2;
	    gbC.gridy =0;
	    gbC.anchor = GridBagConstraints.WEST;
	    add(diceR, gbC);

	    // user data to the side
	    gbC.gridx =3;
	    gbC.gridy =0;
	    gbC.gridheight=2;
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.weighty = 0.;
	    gbC.weightx = 1.;
	    add(users, gbC);

	    // instructions
	    gbC.weightx = 0.;
	    gbC.weighty = 1.;
	    gbC.gridx =1;
	    gbC.gridy =1;
	    gbC.gridheight=1;
	    gbC.gridwidth=2;
	    //gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.anchor = GridBagConstraints.NORTH;
	    add(detailsP,gbC);
	    }else{
	    gbC.gridx =0;
	    gbC.gridy =0;
	    gbC.gridheight=3;
	    gbC.anchor = GridBagConstraints.SOUTH;
	    gbC.fill = GridBagConstraints.HORIZONTAL;
	    add(diceP, gbC);
	    gbC.gridx =0;
	    gbC.gridy =3;
	    //gbC.gridheight=3;
	    gbC.gridheight=2;
	    gbC.anchor = GridBagConstraints.NORTH;
	    add(diceR, gbC);

	    // opts
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.gridx =1;
	    gbC.gridy =0;
	    gbC.gridheight=1;
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.anchor = GridBagConstraints.PAGE_START;
	    add(choices, gbC);

	    gbC.gridx =1;
	    gbC.gridy =1;
	    gbC.fill = GridBagConstraints.NONE;
	    //gbC.anchor = GridBagConstraints.CENTER;
	    gbC.anchor = GridBagConstraints.SOUTH;
	    gbC.gridheight=3;
	    add(cPan, gbC);

	    // user date to the side
	    gbC.gridx =2;
	    gbC.gridy =0;
	    gbC.gridheight=4;
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.weighty = 0.;
	    gbC.weightx = 1.;
	    add(users, gbC);

	    // instructions
	    gbC.weightx = 1.;
	    gbC.weighty = 1.;
	    gbC.gridx =1;
	    gbC.gridy =4;
	    gbC.gridheight=1;
	    gbC.gridwidth=2;
	    //gbC.fill = GridBagConstraints.HORIZONTAL;
	    gbC.fill = GridBagConstraints.BOTH;
	    gbC.anchor = GridBagConstraints.NORTH;
	    add(detailsP,gbC);
	    }
	    
	    Dimension dimP = diceP.getPSize();
	    Dimension dimR = diceR.getPSize();

	    if (vert_layout == DicePanel_V.LayoutOrientation.VERTICAL){
	    setSize((int)(dimR.getWidth()*1.5+dimP.getWidth()*1.5) + imi.getIconWidth() +2*50,  // nusers
		    (int)dimP.getHeight()+21*10);   // fontsize*nlines
	    }else{
	    setSize((int)(dimR.getWidth()*1.7) + 250,
		    (int)dimP.getHeight()+(int)dimR.getHeight()+100);
	    }

	    // bind actions
	    optionA.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    System.exit(1);
                }
            });
	    optionB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    details.append("Another line "+nlines+"\n");
		    nlines+=1;
                }
            });
	}

	private void initState(){
	    diceP.setDice("12355","10001");
	    diceP.setScore(200);
	    diceR.setDice("1");
	    diceR.setScore(100);
	}
	public testHorizLayout(int dummy, DicePanel_V.LayoutOrientation layOr) {
	    this(layOr);
	    initState();
	}
	public testHorizLayout(int dummy) {
	    this();
	    initState();
	}

	public static void main(String args[]){
	    if(args.length > 0){
	    java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
		    new testHorizLayout(1).setVisible(true);
		}
	    });
	    }else {
	    java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
		    new testHorizLayout(1, DicePanel_V.LayoutOrientation.HORIZONTAL).setVisible(true);
		}
	    });
	    }
	}
}
