package fillrbust;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
//import java.io.IOException;
//import java.io.InputStream;
import java.util.*;


/** FillrBustGui
 * shows the state of the game by displaying graphical depictions of
 * <ul>
 * <li>    the players' scores,
 * <li>    the card,
 *  <li>   the dice,
 *  <li>   the choices,
 * <li>    the instructions to the user
 * </ul>
 *  The state of the game is maintained by the game itself.
 *  The action buttons, ie the card, the dice and the choice buttons
 *  just query the game for its state and update the graphical depictions.
 *  
 */

class FillrBustGui extends JFrame{

    private static String cmdHelp="\033[1mUsage\033[0m: java -jar <path_to_jar> \033[32m[-options]\033[0m\n"+
"   where options are:\n"+
"      \033[32m-p\033[0m playerName	    contestant name\n"+
"                           There are expected to be two or more contestants.\n"+
"                           Just repeat the argument for each. Prepend 'ai' to\n"+
"                           make the player computer controlled.\n"+
"      \033[32m-m\033[0m winningScore	    the score at which someone is delared the winner\n"+
"      \033[32m-F\033[0m fontSize	    size of the font for the instruction box\n"+
"      \033[32m-d\033[0m diceDir	    directory for dice images: Medium, Big, Orig\n"+
"      \033[32m-c\033[0m cardDir	    directory for card images: Orig, Big, Huge, GG\n"+
"      \033[32m-V\033[0m vertical orientation of diceGUI (default is horizontal)\n"+
"      \033[32m-C\033[0m configFile	    path to configuration file\n"+
"\n";

    private static String therules="see official rules from Bowman Games Inc: instr.pdf\n"+
"Fill'RBust uses six standard cubic dice and 54 cards (as described below)\n"+
"It is turn based.\n\n"+
"A 'fill' is when all six dice are scoreable (see dice scoring below).\n"+
"A 'bust' is when the dice are rolled and none are scoreable.\n"+
"RULE 1. The first step in any turn is to draw a card. "+
"The card you turn over will determine your course of play. "+
"How you play each card is explained later under PLAYING THE DRAW CARDS.\n\n"+
"RULE 2: After turning over a card, you begin your turn by tossing all six dice. "+
"Rolling a \"Bust\" ends your turn.\n"+
"You score by tossing 1s, 5s, any triples or a straight (1,2,3,4,5 and 6 thrown in a single toss of all six dice).\n\n"+
"Dice scoring:\n"+
"	1 = 100\n"+
"	5 = 50\n"+
"	three of a kind = 100x N, except three 1's = 1000\n"+
"	straight (in a single roll)= 1500\n\n"+
"RULE 3: \n"+
" After each toss you MUST remove some or all of the scoring dice. "+
" Set aside all the scoring dice you have chosen and add up their points. "+
" You can decide to take these points and add them to the scoresheet ending your turn. "+
" Or, you can risk those points and try to score more points with the remaining dice.\n\n"+
" HOW TO SCORE A FILL\n"+
"If you continue to score on every toss of the dice, until you eventually set aside all six dice, YOU HAVE SCORED A  \"FILL\". "+
"After scoring a \"FILL\" you can decide to take the points scored during that turn and add them to the scoresheet, ending your turn. "+
"Or, you can risk those points and try to score more points by turning over another DRAW CARD and start by tossing all six dice again. "+
"There is no limit to the number of \"FILLS\" that may be scored in a turn.\n\n"+
"WHAT IS A \"BUST\"?\n"+
" If on any toss you DO NOT roll any scoring dice, that toss is a \"BUST\". "+
" When you \"BUST\" during a turn, you lose ALL the points you scored in that turn. "+
" (Exception is during MUST BUST, see below)"+
" You do not lose any points that were already added to the scoresheet. "+
" After a \"BUST\", your turn ends and you pass the dice to the next player.\n\n"+
"Cards:\n"+
"    Bonus 300, 400, 500\n"+
"    	if filled, get additional bonus points\n"+
"    Fill 1000\n"+
"    	roll until you bust, you keep no score unless filled;\n"+
"    	but if you fill it you get 1000 bonus points\n"+
"    DoubleTrouble\n"+
"    	no score unless filled twice. Score is doubled and added to scoresheet.\n"+
"    	Then you immediately get another turn.\n"+
"    Must Bust\n"+
"    	no risk; a bust just ends the turn; you still get the points\n"+
"	continue rolling until no scoring dice, ie BUST.\n"+
"	all scorable dice must be accepted as they are rolled.\n"+
"    No Dice\n"+
"    	forfeit turn immediately\n"+
"    Vengeance 2500\n"+
"    	you must fill to score\n"+
"	when/if filled, the score is added to the scoresheet and leader loses 2500 points.\n "+
"	After successful Vengeance, player immediately gets another turn without risk of points just made.\n\n"+
" In the deck, there are\n"+
" 12- 300 pt. \"BONUS\" cards\n"+
" 10- 400 pt. \"BONUS\" cards\n"+
"  8- 500 pt. \"BONUS\" cards\n"+
"  4- MUST BUST cards\n"+
"  6- FILL 1000 cards\n"+
"  4- VENGEANCE 2500 cards\n"+
"  8- NO DICE cards\n"+
"  2- DOUBLE TROUBLE cards";

    private static String theGUI="The Graphical User Interface (GUI) has been developed to lead you through the game. "+
"On the left are the buttons that you use to play the game: the cards, the dice, "+
"and two buttons to select an appropriate action when there is a decision to make. "+
"On the right is the information to show you who is winning (and who is/are not).\n\n"+
"The first part of every turn is to draw a card.  Press the deck of cards and you get a new card "+
"if that is the appropriate next step. Look in the instruction window at the bottom; it will guide you. "+
"The rules dictate what you can do for each card. The option buttons below the dice help you actually do it. "+
"Typically, after drawing a card, you want to roll the dice, so there is an option button that says \"Roll Dice\". "+
"Click it. The dice will indicate what was rolled. Any dice that might score are highlighted. "+
"Clicking on the dice will highlight/de-highlight them. The potential score will change based on your selection. "+
"Highlighted dice will be reserved; the rest will be rolled (if that is an allowed choice). "+
"Again, see the instruction window below; it will tell you. "+
"When you roll again, dice you highlighted will be moved to the right and shown in a fence, "+
"and the result of rolling the remaining unhighlighted dice will be shown on the left.\n\n"+
"When you decide to take a score, it will be posted on the scoresheet to the right "+
"and the next player gets a turn. "+
"(Only scores are posted; nobody likes to be reminded of their failures.)\n"+
"The new player's name will turn green.\n\n"+
"The \"Game\" menu at the top allows you to add players and change the ultimate score that wins the game. "+
"Any player name that starts with \"ai\" will be computer controlled. "+
"So, Aiden and Aisha, don't forget to capitalize the first letter of your name "+
"or the computer will make your decisions for you. "+
"A number (1-9) appended to the end of an aiName will indicate how much risk that ai player is willing to accept "+
"when deciding to roll again, take vengeance, continue after a Fill, etc. A lower number assigns less risk.";

    private static String diceHelp="             Dice:\n"+
"The dice are displayed and modeled as typical cubic dice.\n"+
"Clicking any unrolled die will generate the first roll using all six dice. "+
"Subsequent to the first roll, clicking will select and de-select the individual dice to be reserved for scoring. "+
"Selected dice are shown red and will not be rolled. "+
"Of the rolled dice, at least one must be reserved for scoring (it's a rule). "+
"Dice that were reserved and not rolled are displayed on the right side of the fence.";

    private static String cardHelp="             Card:\n"+
"The cards are displayed and modeled as a shuffled Fill'RBust deck. "+
"When it is appropriate to draw a card, clicking on the card will show the next card in the shuffled deck. "+
"When all 54 cards have been exhausted, the deck is reshuffled.";

    private static String optionHelp="           Option Buttons:\n"+
"There are two option buttons because there are usually two options from which the player must choose. "+
"The effect of clicking the option depends on the state of the game. "+
"The button text describes the effect consisely. "+
"The instruction box below them describe the decision a bit more. "+
"Sometimes there is only one option, so one button is disabled.";

    private static String goalHelp="You can change the score at which someone is called the winner\n"+
"(and the rest are not).\n"+
"At the end of the game, ie when someone's score exceeds the goal and the 'Winner' screen appears,\n"+
"right clicking in it will allow you to change the goal. This allows for sore losers!";

	private static final String newHelp="Start a new game with the same players.";

    private static final String playerHelp="Any time during a game, another player can jump in - with zero score of course.\n\n"+
"Prepending 'ai' to a name makes that player computer controlled. "+
"A number at the end of an ai player name indicates the risk that player will accept for decisions "+
"such as whether to roll again, take vengeance, or continue after a FILL. Default value is 5. "+
"Right click allows name change. ERROR ALERT: can't change player from real to AI.";

    private static final String quitHelp=" Duh! It quits; goes away; exits; beats a hasty retreat, makes like a tree and leaves, makes like a buffalo turd and hits the dusty trail.\n\n Which part of quit don't you understand?";

    private static final String helpHelp="Obviously you found that 'specific' help describes individual buttons.\n "+
"'Rules' describes the rules of the game.\n"+
"'Synopsis' describes how the game is implemented with this Graphical User Interface";

    private static final String aboutHelp="FillRBust\n\n"
		    +"javax.swing, java.awt\n"
		    +"Version 2.0  July 24, 2024\n"
		    +"Clyde Gumbert, mizugana@gmail.com\n"
		    +"I had fun and learned some stuff building it.\n"
		    +"If you recognize areas of improvement,\n"
		    +"please send me useful suggestions how to do it.\n"
		    +"I doubt any of this can hurt your computer, "
		    +"but if it does it's on you for trusting unskilled "
		    +"labor like me. "
		    ;

	private FillRBustGame.STATES gameSTATE = FillRBustGame.STATES.INIT;
	private FillRBustGame myGame;
	private static int UPDATE_PERIOD = 100; //milliseconds
	private static String cardFolder = "images/";

	private JMenuBar mbar;
	private JMenu game;
	private JMenu help;
	private JMenuItem quit,rc,addPlayer,target,newG;  //save,load;
	private JMenuItem rules, synopsis, specific, about;
	private Dimension[] frameDims;
	private JPanel choices;
	private JPanel rightBottom;	
	private JPanel users;
	private ArrayList<JToggleButton> dice;
	private JScrollPane detailsP;
	private int nlines = 1;
	private JTextArea details;
	private DicePanel_V diceP;
	private ReservedDice_V diceR;
	private ArrayList<UserPanel> players = new ArrayList<UserPanel>(4);
	private int currentPlayer;
	private JLabel winningScore;
	private JButton card;
	private JButton optionA;
	private JButton optionB;
	private FBConfig config;
	private boolean vertOrient = true;

	/** Provide a Graphical User Interface to play FillRBust.<br>
	 *
	 * @param config describes the players' names,<br>
	 *               the score at which to declare a winner,<br>
	 *               the directories to find the images for the dice and cards,<br>
	 *               the orientation of the dice display,<br>
	 *               the font-size of the user instructions
	 */
	public FillrBustGui(FBConfig config) {
	   this.config = config;
	   myGame = new FillRBustGame(config.players, config.goal);
	   frameDims = getDims(config.players.length, config.cardDir, config.diceDir);
	   cardFolder = config.cardDir;
	    setTitle("Play FillRBust");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setBackground(Color.BLACK);
	    setForeground(Color.BLACK);
	   mbar = new JMenuBar();
	   game = new JMenu("Game");
	   target = new JMenuItem("Change Winning Score");
	   addPlayer = new JMenuItem("add player");
	   rc = new JMenuItem("write rc file");
	   newG = new JMenuItem("new game");
	   quit = new JMenuItem("quit");
	     game.add(target); game.add(addPlayer);
	     game.add(target); game.add(addPlayer);
		 game.add(newG);
	    target.setToolTipText("<html><p width=\"180\">"+goalHelp+"</html>");
	    newG.setToolTipText("<html><p width=\"180\">"+newHelp+"</html>");
		addPlayer.setToolTipText("<html><p width=\"180\">"+playerHelp+"</html>");
		quit.setToolTipText("<html><p width=\"180\">"+quitHelp+"</html>");
		 game.add(rc); game.add(quit);
	   help = new JMenu("Help");
	   help.setToolTipText("<html><p width=\"180\">"+helpHelp+"</html>");
	   rules = new JMenuItem("Rules");
	   synopsis = new JMenuItem("Synopsis");
	   specific = new JMenuItem("specific");
	   about = new JMenuItem("About");
	     help.add(rules); help.add(synopsis); help.add(specific); help.add(about);
	   mbar.add(game);
	   mbar.add(help);
	   setJMenuBar(mbar);

	    diceP = new DicePanel_V(config.diceDir, config.layout);
		diceP.setToolTipText("<html><p width=\"180\">"+diceHelp+"</html>");
	    diceR = new ReservedDice_V(config.diceDir, config.layout);
	    // opts
	    choices = new JPanel();
	    //choices.setLayout(new FlowLayout(FlowLayout.CENTER));
	    choices.setLayout(new BoxLayout(choices,BoxLayout.Y_AXIS));
	    choices.setBackground(Color.BLACK);
	    if(config.debug)choices.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		choices.setToolTipText("<html><p width=\"180\">"+optionHelp+"</html>");
	    optionA = new JButton("optionA");
	    optionB = new JButton("optionB");
	    choices.add(optionA);
	    choices.add(optionB);
	    ImageIcon imi =new ImageIcon(getClass().getResource(cardFolder+"title.gif"));
	    JPanel cPan = new JPanel();
	    cPan.setBackground(Color.BLACK);
            card = new JButton(imi);
	    card.setPreferredSize(new Dimension(imi.getIconWidth(),imi.getIconHeight()));
	    cPan.add(card);
	    // user date to the side
	    users = new JPanel();
	    users.setLayout(new GridBagLayout());
	       GridBagConstraints uGB = new GridBagConstraints();
	    winningScore = new JLabel(String.format("%d",config.goal));
	    uGB.gridx     =0;
	    uGB.gridy     =0;
	    uGB.gridwidth =config.players.length;
	    users.add(winningScore, uGB);
	    uGB.gridy     =1;
	    uGB.gridwidth =1;
	    uGB.gridx     =0;
		for (String each: config.players){
			UserPanel entry = new UserPanel(each, players.size());
			//UserPanel entry = new UserPanel(each);
			players.add(entry);
			users.add(entry, uGB);
			uGB.gridx+=1;
		}
	    if(config.debug)users.setBorder(BorderFactory.createLineBorder(Color.RED));
	    // instructions
	    details = new JTextArea(8,30);
	    details.setFont(new Font("Ænigma Scrawl 4 BRK",Font.PLAIN, config.fontSize/*15*/));
	    details.setEditable(false);
	    detailsP = new JScrollPane(details);
	    //detailsP.setBounds(0,frameDims[1].height,
	    //		      frameDims[0].width,frameDims[0].height-frameDims[1].height);
	    //		    123456789012345678901234567890
	    details.append("Follow these instructions:\n");
	    //details.append("Follow");
	    if(config.debug)detailsP.setBorder(BorderFactory.createLineBorder(Color.RED));
                                                
	    // layout dice vertically
	    setLayout(new GridBagLayout());
	    GridBagConstraints gbC = new GridBagConstraints();
	    if (config.layout == DicePanel_V.LayoutOrientation.VERTICAL){
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

	    Dimension dimP = diceP.getPSize();
	    Dimension dimR = diceR.getPSize();

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

	    if (config.layout == DicePanel_V.LayoutOrientation.VERTICAL){
	    setSize((int)(dimR.getWidth()*1.5+dimP.getWidth()*1.5) + imi.getIconWidth() +2*50,  // nusers
		    (int)dimP.getHeight()+21*10);   // fontsize*nlines
	    }else{
	    setSize((int)(dimR.getWidth()*1.7) + 250,
		    (int)dimP.getHeight()+(int)dimR.getHeight()+100);
	    }

	    // bind actions
	    // game menu
	    quit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    System.exit(1);
                }
	    });
	    // help menu
	    JFrame frame = this;
	    rules.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    MyDialog showHelp = new MyDialog(frame, therules);
		    showHelp.pack();
		    showHelp.setVisible(true);
                }
	    });
	    synopsis.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    MyDialog showHelp = new MyDialog(frame, theGUI);
		    showHelp.pack();
		    showHelp.setVisible(true);
                }
	    });
	    about.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    MyDialog showHelp = new MyDialog(frame, aboutHelp);
		    showHelp.pack();
		    showHelp.setVisible(true);
                }
	    });
	    // options
	    optionA.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
		    myGame.update("OPTION_A");
		    //testOptA();
                }
	    });
	    optionB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
		    myGame.update("OPTION_B");
		    //testOptB();
                }
	    });
	    card.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent fred) {
		    if (gameSTATE == FillRBustGame.STATES.INIT){
				init_game();
		    }else if (gameSTATE == FillRBustGame.STATES.DRAWCARD){
			    myGame.update("CARD");
		    }
		  }
	    });

	    dice = diceP.getDice();
	    for (JToggleButton die : dice) {
		    die.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent fred) {
				    myGame.update("DICE");
			    }
		    });

	    }
	    addPlayer.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent fred) {
			    addNewPlayer();
		    }
	    });
	    rc.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent fred) {
			    makeRCfile();
		    }
	    });
	    newG.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent fred) {
			    newGame();
		    }
	    });
	    target.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent fred) {
			    getNewTarget();
		    }
	    });
	    rules.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    MyDialog showHelp = new MyDialog(frame, therules);
		    showHelp.pack();
		    showHelp.setVisible(true);
                }
	    });
	    synopsis.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    MyDialog showHelp = new MyDialog(frame, theGUI);
		    showHelp.pack();
		    showHelp.setVisible(true);
                }
	    });
	    about.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    MyDialog showHelp = new MyDialog(frame, aboutHelp);
		    showHelp.pack();
		    showHelp.setVisible(true);
                }
	    });

	    ActionListener updateTask = new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent evt) {
		     URrepaint();  // check to see if the Computer player is playing
		  }
	    };
	    Action winUp = new AbstractAction() {
		  public void actionPerformed(ActionEvent e) {
		     frameDims[0].height+=20;
		     frame.setSize(frameDims[0].width, frameDims[0].height);
		  }
	    };
	    card.getInputMap().put(KeyStroke.getKeyStroke('e'),
                            "winUp");
	    card.getActionMap().put("winUp",
                             winUp);
	    Action fontDn = new AbstractAction() {
		  public void actionPerformed(ActionEvent e) {
		    config.fontSize-=4;
		    details.setFont(new Font("Ænigma Scrawl 4 BRK",Font.PLAIN, config.fontSize));
		  }
	    };
	    details.getInputMap().put(KeyStroke.getKeyStroke('f'),
                            "fontDn");
	    details.getActionMap().put("fontDn",
                             fontDn);
	    Action fontUp = new AbstractAction() {
		  public void actionPerformed(ActionEvent e) {
		    config.fontSize+=4;
		    details.setFont(new Font("Ænigma Scrawl 4 BRK",Font.PLAIN, config.fontSize));
		  }
	    };
	    details.getInputMap().put(KeyStroke.getKeyStroke('F'),
                            "fontUp");
	    details.getActionMap().put("fontUp",
                             fontUp);
		Action togDebug = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				config.debug = !config.debug;
				myGame.setDebug();
				System.out.println("toggled DEBUG "+(config.debug?"ON":"OFF"));
			}
		};
		details.getInputMap().put(KeyStroke.getKeyStroke('D'),
				"togDebug");
		details.getActionMap().put("togDebug",
				togDebug);

		for (UserPanel each:players) {
			each.myButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					updateUserName(each);
				}
			});
		}

	    init_game();
		new javax.swing.Timer(UPDATE_PERIOD, updateTask).start();
	}

	void URrepaint() {
		//System.out.println("repaint                     repaint");
		myGame.aiInterface();
		//detailsP.repaint();
	}

     //  game action methods: {
    void init_game() {
	 currentPlayer = myGame.setGui(this);
	 currentPlayer = 0;
	 gameSTATE=myGame.getState();
	 players.get(currentPlayer).toggleTurn();
	 //System.out.println("init_game was here; currentPlayer "+currentPlayer);
	 addDetails(String.format("%s, it is your turn.\n Draw a card.\n",
			 players.get(currentPlayer).getUName()));
    }

	/**
	 * Start new game with current parameters
	 */
	public void newGame() {
		for (UserPanel each: players) {
			each.reset();
		}
		myGame.resetGame();
		card.setIcon(new ImageIcon(getClass().getResource(cardFolder+"title.gif")));
		setDice();
		details.setText("");
		// TODO
	}

    void setOptionA(String option) {
	    optionA.setText(option);
     }

	 /** Display empty dice panels */
    void deRoll(){
	    //System.out.println("deRoll");
		//diceP.setDice("000000", "000000");
		//diceR.setDice("000000");
		setDice("000000 ","000000");
    }

	/** display Card
	 *
	 * @param name FillRBust card name
	 */
    void setCard(Cards.Name name) {
	    String imgN = mapCardImage(name);
	    //System.out.println("new card image is " + imgN);
	    try {
		    //InputStream is;
		    //is = getClass().getResourceAsStream(imgN);
		    //Image titleImg = Toolkit.getDefaultToolkit().getImage( imgN );
		    //card.setIcon(new ImageIcon(titleImg));
		    card.setIcon(new ImageIcon(getClass().getResource(imgN)));
			//System.out.println("found it in the jar");
	    } catch ( NullPointerException e) {
		    Image titleImg = Toolkit.getDefaultToolkit().getImage( imgN );
		    card.setIcon(new ImageIcon(titleImg));
	    }
    }

	/** Add a line to the user instructions */
    void addDetails(String line){
	    details.append(line);
    }

    void setOptionB(String option) {
	    optionB.setText(option);
		//System.out.println("tried to set OptionB");
     }
    //  game action methods: }

	/** switch the Player that is currently playing
	 *
	 * @param name the next player
	 */
	public void setCurrentPlayer(String name){
		players.get(currentPlayer).toggleTurn();
		currentPlayer = (currentPlayer+1)%players.size();
		if (players.get(currentPlayer).getUName().equals(name))
			players.get(currentPlayer).toggleTurn();
		else {
			//System.out.println("in Gui setCurrentPlayer:");
			//for (UserPanel each : players) System.out.println(each.getUName());
			//System.out.println(currentPlayer);
			System.out.println(name+" not equal "+players.get(currentPlayer).getUName());
			ComplainLoudly("Player name mismatch");
		}
	}

	/**
	 * change the size of the frame
	 * to accomodate changes to:
	 *    # of players
	 *    size of dice
	 *    size of card
	 */
	void reSizeFrame() {
	   frameDims = getDims(config.players.length, config.cardDir, config.diceDir);
         this.setSize(frameDims[0].width, frameDims[0].height);//  |                      |               |
	        /*
		top.setBounds(0,0,frameDims[0].width,frameDims[1].height);
		detailsP.setBounds(0,frameDims[1].height,
					frameDims[0].width,frameDims[0].height-frameDims[1].height);
		data.setBounds(frameDims[1].width,0,
				frameDims[0].width-frameDims[1].width, frameDims[1].height);
		actions.setBounds(0,0,frameDims[1].width,frameDims[1].height);
		int ypos = 0;
	      ImageIcon imi =new ImageIcon(getClass().getResource(cardFolder+"title.gif"));
	    card.setBounds(200,ypos, imi.getIconWidth(), imi.getIconHeight());  // TODO geom based on image size
		ypos+=imi.getIconHeight();
	    imi =new ImageIcon(getClass().getResource(config.diceDir+"one.gif"));
	    diceP.setBounds(0,ypos,frameDims[1].width,imi.getIconHeight());
	    ypos+=imi.getIconHeight();
	    scores.setBounds(80,ypos,400,25);
		ypos+=30;
	    choices.setBounds(200,ypos,100,95);
	    */
	}

	/** Open a Frame to display a message
	 *
	 * @param complaint  - the test of the message
	 * @param topic      - the title of the message frame
	 * @param large      - the font size
	 */
	public void ComplainLoudly(String complaint, String topic, int large) {
		MyDialog showHelp = new MyDialog(this, complaint,topic);
		showHelp.setFont(large);
		showHelp.pack();
		showHelp.setVisible(true);
	}
	public void ComplainLoudly(String complaint, String topic) {
		MyDialog showHelp = new MyDialog(this, complaint,topic);
		showHelp.pack();
		showHelp.setVisible(true);
	}
	public void ComplainLoudly(String complaint) {
		MyDialog showHelp = new MyDialog(this, complaint);
		showHelp.pack();
		showHelp.setVisible(true);
	}

	public void addScore(int round) {
		players.get(currentPlayer).addScore(round);
	}

	/** add a score to the record of the player's scores
	 *
	 * @param name player name (current player if not specified)
	 * @param score score to add
	 */
	public void addScore(String name, int score) {
		for( UserPanel each : players) {
			if (each.getUName().equals(name)){
				each.addScore(score);
				break;
			}
		}
	}

	/** update player total score
	 *
	 * @param name
	 * @param score
	 */
	public void updateScore(String name, int score) {
		for( UserPanel each : players) {
			if (each.getUName().equals(name)){
				each.newScore(score);
				break;
			}
		}
	}
	public void updateScore(int score) {
		players.get(currentPlayer).newScore(score);
	}

	/** change the name of a player in the display and the game.
	 *
	 * @param up the UserPanel for the player that needs a name change
	 */
	void updateUserName(UserPanel up){
		System.out.println(up.getUName());
		System.out.println(up.getIndex());
		// get a new name
		String s = JOptionPane.showInputDialog(
				this,"Enter a new name (unimplemented): "
				,up.getUName()
		);
		// replace the user name in the game and the display
		if (s != null && !s.isEmpty()) {
			if (myGame.changeName(up.getIndex(), s) == 0) {
				up.changeName(s, config.debug);
			} else {
				ComplainLoudly("Conversion between AI and human is unimplemented.");
			}
		}
	}

	/**
	 * set running and potential scores;
	 * running score is the score of reserved dice and any previous cards' score;
	 * potential is the chosen of the most recently rolled dice.
	 */
	public void setRunning(int running, int potential) {
		diceP.setScore(potential);
		diceR.setScore(running);
	}

	/** report which dice are selected in the DicePanel. */
	public String getDiceSelection() {
		return diceP.getSelected();
	}

	public void setSelected(String mask) {
	        //System.out.println("setselected mask:"+mask+"|");
		String[] parts = mask.split(" ");
		//diceP.setSelected(mask);
		diceP.setSelected(parts[0]);
	}

	/**
	 * Given a string representing the dice,
	 * set the graphical depiction.<br>
	 * The first part of the seven character string is the recently rolled dice;<br>
	 * any reserved dice are listed after a space-character.
	 *
	 * @param set the 7-character string of dice values
	 * @param mask the selection mask: 1 - selected; 0-  not<br>
	 *
	 *  Without arguments indicates no dice displayed
	 */
	public void setDice(String set, String mask){
		String[] parts=set.split(" ");
		diceP.setDice(parts[0],mask.substring(0,parts[0].length()));
		if (parts.length > 1)diceR.setDice(parts[1]);
		else diceR.setDice("");
	}
	public void setDice(){
		diceP.setDice("","");
		diceR.setDice("");
	}

	public void declareWinner(String name) {
		ComplainLoudly(String.format("  !!! %s is the big winner !!!",name),"WINNER", 44);
	}

	/**
	 * Dialog panel to set a new value for the winning score
	 */
	public void getNewTarget (){
		//System.out.println("'Change the score to win' is unimplemented.\n");
		String s = JOptionPane.showInputDialog(
				this,"Enter a new max score: "
				,myGame.getMaxScore()
		);
		if (s!=null){
			try {
				int maxScore = Integer.parseInt(s);
				winningScore.setText(s);
				myGame.setMaxScore(maxScore);
			}catch (NumberFormatException e)  {
				ComplainLoudly("Score is a NUMBER", "ERROR");
			}
		}
	}

	/*
	public void saveGame() {
		//notTODO
	}

	public void readGame() {
		//notTODO
	}
	 */

	public void makeRCfile() {
		config.writeConfig(this, myGame.getMaxScore(), myGame.getPlist());
	}

	public void toggleHoverHelp(String help) {
		//TODO make this work
		MyDialog showHelp = new MyDialog(this, help);
		showHelp.pack();
		showHelp.setVisible(true);
	}

	public void addNewPlayer() {
		String s = JOptionPane.showInputDialog(
				this,"Enter the name of the new player: \n"
				+" prepend 'ai' if player is computer controlled.\n"
				+" if last character is digit, it sets the riskiness."
		);
		UserPanel entry = new UserPanel(s, players.size());
		//UserPanel entry = new UserPanel(s);
	       GridBagConstraints uGB = new GridBagConstraints();
		uGB.gridx     =0;
		uGB.gridy     =1;
		uGB.gridwidth =1;
		uGB.gridx     =players.size();
		players.add(entry);
		users.add(entry,uGB);
		myGame.addPlayer(s);
		System.out.println("'add new player' is unimplemented.\n");
		System.out.println("User Panel size:"+users.getSize());
		for (UserPanel each: players) System.out.println(each.getSize());
		config.players = Arrays.copyOf(config.players, config.players.length + 1);
		//config.players[config.players.length - 1] = entry.getName();
		config.players[config.players.length - 1] = entry.getUName();
		entry.myButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				updateUserName(entry);
			}
		});
		reSizeFrame();
	}

	/** maps the card name to the image that depicts it */
    String mapCardImage(Cards.Name type){
	    if (type==Cards.Name.BONUS_300) return cardFolder+"bonus300.gif";
	    if (type==Cards.Name.BONUS_400) return cardFolder+"bonus400.gif";
	    if (type==Cards.Name.BONUS_500) return cardFolder+"bonus500.gif";
	    if (type==Cards.Name.MUST_BUST) return cardFolder+"mustbust.gif";
	    if (type==Cards.Name.FILL_1000) return cardFolder+"fill1000.gif";
	    if (type==Cards.Name.VENGEANCE) return cardFolder+"vengeance.gif";
	    if (type==Cards.Name.NO_DICE) return cardFolder+"nodice.gif";
	    if (type==Cards.Name.DOUBLE_TROUBLE) return cardFolder+"doubletrouble.gif";
	    return cardFolder+"deck.gif";
    }

	/**
	 * Uses components to determine size of the GUI areas
	 * @param numPlayers
	 * @param cardFolder
	 * @param diceFolder
	 * @return array of dimensions:<br>
	 *                           0-> total<br>
	 *                           1-> actions
	 */
	Dimension[] getDims(int numPlayers, String cardFolder, String diceFolder){
		int widthU = numPlayers*110;
		ImageIcon tem=null;
		try {
		tem= new ImageIcon(getClass().getResource(diceFolder+"one.gif"));
		} catch (NullPointerException e) {
		    System.out.println("DiceDir: "+diceFolder+" seems not to hold dice images");
		    System.exit(1);
		}
		int widthD= 8* tem.getIconWidth();
		//width += 500; // = 8*dicedir.one.gif.width
		int heightT = tem.getIconHeight();
		try {
		tem= new ImageIcon(getClass().getResource(cardFolder+"title.gif"));
		} catch (NullPointerException e) {
		    System.out.println("DiceDir: "+diceFolder);
		    System.out.println("CardDir: "+cardFolder+" seems not to hold card images");
		    System.exit(1);
		}
		//int height = max((card.height+ dice.height + more), userpanel.height)
		//                  + fontsize*nrows/10;
		heightT += tem.getIconHeight() +120;
		heightT = Math.max(heightT,300 /* UserPanel height*/);
		int width = widthU + widthD;
		int height=heightT + 300;
		if(config.debug)System.out.printf("widthD,HeightT, widthU,totH: %d %d %d %d%n",widthD,heightT,widthU,height);
		//height=700; width=700;
		return new Dimension[] {new Dimension(width,height),
			                new Dimension(widthD,heightT)};
	}

	private void testOptA() {
	    deRoll();
	    setCard(Cards.Name.BONUS_400);
	    setRunning(1050, 50);
	}

	private void testOptB() {
	    setDice("123456 ","111111");
	    setCard(Cards.Name.MUST_BUST);
	    setRunning(0,1500);
	}

	private void initState(){
	    //diceP.setDice("12355","10001");
	    //diceP.setScore(200);
	    //diceR.setDice("1");
	    //diceR.setScore(100);
	    setDice("1235 15","1000");
	    setRunning(150,100);
	}

	public FillrBustGui(int dummy, FBConfig config) {
	    this(config);
	    //initState();
	}

	/** Usage: java -jar <path_to_jar> [-options]<br>
			  where options are:<br>
	 <ul>
		<li>	-p playerName	    contestant name<br>
			                    There are expected to be two or more contestants.
			                    Just repeat the argument for each. Prepend 'ai' to
			                    make the player computer controlled.<br>
	 <li>	-m winningScore	    the score at which someone is delared the winner<br>
	 <li>	-F fontSize	    size of the font for the instruction box<br>
	 <li>	-d diceDir	    directory for dice images: Medium, Big, Orig<br>
	 <li>	-c cardDir	    directory for card images: Orig, Big, Huge<br>
	 </ul>
	 */
	public static void main(String args[]){
		FBConfig config = new FBConfig();
		String[] list = config.players;
		int winner = config.goal;
		boolean doGui = config.gui;
		//boolean debug = false;
		String argee;
		if (args.length >0){
			ArrayList<String> temp = new ArrayList<>();
			for (int i=0; i<args.length;i++) {
				if (args[i].equals("-h")) {
				    System.out.println("\n\n"+cmdHelp+"\n\n");
				    System.exit(0);
				}
				if (args[i].equals("-D")) config.debug = true;
				if (args[i].equals("-g")) doGui = false;
				if (args[i].equals("-g")) config.gui = false;
				else if (args[i].equals("-m")) {
					config.goal = Integer.parseInt(args[i++ +1]);
				}
				else if (args[i].equals("-p")) {
					temp.add(args[i++ + 1]);
				}
				else if (args[i].equals("-F")) {
					config.fontSize = Integer.parseInt(args[i++ +1]);
				}
				else if (args[i].equals("-d")) {
					argee = args[i++ +1];
					if (argee.contains("/"))config.diceDir = argee+"/";
					else config.diceDir = "images/Dice/"+argee+"/";
				}
				else if (args[i].equals("-c")) {
					argee = args[i++ +1];
					if (argee.contains("/"))config.cardDir = argee+"/";
					else config.cardDir = "images/Cards/"+argee+"/";
				}
				else if (args[i].equals("-V")) config.layout = DicePanel_V.LayoutOrientation.HORIZONTAL;
				else if (args[i].equals("-C")) {
					argee = args[i++ +1];
					config.readFile(argee);
				}
				/*
				 */
			}
			if (!temp.isEmpty()) list = temp.toArray(list);
			if (!temp.isEmpty()) config.players = temp.toArray(list);
		}

		if (doGui) {
		    java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
			    new FillrBustGui(1,config).setVisible(true);
			}
		    });
		} else {
			System.out.println("aint doin no stinkin GUI");
			//FillRBustGame myGame = new FillRBustGame(list, winner);
			//myGame.textInterface();
		}
	}
}
