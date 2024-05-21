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
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/* FillrBustGui
 * Shows the state of the game by displaying graphical depictions of
 *     the players scores
 *     the card
 *     the dice
 *     the instructions to the user
 *  The state of the game is maintained by the game itself.
 *  So the action buttons, ie the card, the dice and the choice buttons
 *  just query the game for its state and update the graphical depictions.
 *  
 */

class FillrBustGui extends JFrame{

    String therules="see official rules from Bowman Games Inc: instr.pdf\n"+
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

    String theGUI="The Graphical User Interface (GUI) has been developed to lead you through the game. "+
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

    String diceHelp="             Dice:\n"+
"The dice are displayed and modeled as typical cubic dice.\n"+
"Clicking any unrolled die will generate the first roll using all six dice. "+
"Subsequent to the first roll, clicking will select and de-select the individual dice to be reserved for scoring. "+
"Selected dice are shown red and will not be rolled. "+
"Of the rolled dice, at least one must be reserved for scoring (it's a rule). "+
"Dice that were reserved and not rolled are displayed with a yellow border and grouped to the right.";

    String cardHelp="             Card:\n"+
"The cards are displayed and modeled as a shuffled Fill'RBust deck. "+
"When it is appropriate to draw a card, clicking on the card will show the next card in the shuffled deck. "+
"When all 54 cards have been exhausted, the deck is reshuffled.";

    String optionHelp="           Option Buttons:\n"+
"There are two option buttons because there are usually two options from which the player must choose. "+
"The effect of clicking the option depends on the state of the game. "+
"The button text describes the effect consisely. "+
"The instruction box below them describe the decision a bit more. "+
"Sometimes there is only one option, so one button is disabled.";

    String goalHelp="You can change the score at which someone is called the winner\n"+
"(and the rest are not).\n"+
"At the end of the game, ie when someone's score exceeds the goal and the 'Winner' screen appears,\n"+
"right clicking in it will allow you to change the goal. This allows for sore losers!";

    String playerHelp="Any time during a game, another player can jump in - with zero score of course.\n\n"+
"Prepending 'ai' to a name makes that player computer controlled. "+
"A number at the end of an ai player name indicates the risk that player will accept for decisions "+
"such as whether to roll again, take vengeance, or continue after a FILL. Default value is 5. "+
"Right click allows name change. ERROR ALERT: can't change player from real to AI.";

    String quitHelp=" Duh! It quits; goes away; exits; beats a hasty retreat, makes like a tree and leaves, makes like a buffalo turd and hits the dusty trail.\n\n Which part of quit don't you understand?";

    String helpHelp="Obviously you found that 'specific' help describes individual buttons.\n "+
"'Rules' describes the rules of the game.\n"+
"'Synopsis' describes how the game is implemented with this Graphical User Interface";

    String aboutHelp="FillRBust\n\n"
		    +"javax.swing, java.awt\n"
		    +"Version 1.0  May 14, 2024\n"
		    +"Clyde Gumbert, mizugana@gmail.com\n"
		    +"I had fun and learned some stuff building it.\n"
		    +"If you recognize areas of improvement,\n"
		    +"please send me useful suggestions how to do it.\n"
		    +"I doubt any of this can hurt your computer, "
		    +"but if it does it's on you for trusting unskilled "
		    +"labor like me. "
		    ;

	JFrame frame;
    JPanel top;
    JScrollPane detailsP;
    JTextArea details;
    JPanel data;
    JPanel users;
    JPanel actions;
    DicePanel diceP;
    JPanel scores;
    JPanel choices;
    JMenuBar mbar;
    JMenu game;
    JMenu help;

    ArrayList<UserPanel> players;
	int currentPlayer;
    JButton winningScore;
    ArrayList<JToggleButton> dice;
    JLabel running, potential;
    JButton optionA, optionB, card;
    JMenuItem quit,rc,save,addPlayer,target,load;
    JMenuItem rules, synopsis, specific, about;

    FillRBustGame.STATES gameSTATE = FillRBustGame.STATES.INIT;
    FillRBustGame myGame;
	int UPDATE_PERIOD = 100; //milliseconds
	static String cardFolder = "images/";

    void buildDataGui(JPanel master, String[] playerNames, int targetScore)
    {
		master.setLayout(new BoxLayout(master,BoxLayout.Y_AXIS));
		winningScore = new JButton(String.format("%d",targetScore));
		users = new JPanel();
		users.setLayout(new BoxLayout(users,BoxLayout.X_AXIS));
		players = new ArrayList<UserPanel>();
		for (String each: playerNames){
			UserPanel entry = new UserPanel(each);
			players.add(entry);
			users.add(entry);
		}
		master.add(winningScore);
		master.add(users);
    }

	/**
	 * Use components to determine size of the GUI areas
	 * @param numPlayers
	 * @param cardFolder
	 * @param diceFolder
	 * @return array of dimensions:<br>
	 *                           0-> total<br>
	 *                           1-> actions
	 */
	Dimension[] getDims(int numPlayers, String cardFolder, String diceFolder){
		int widthU = numPlayers*80;
		ImageIcon tem;
		System.out.println("DiceDir: "+diceFolder);
		tem= new ImageIcon(getClass().getResource(diceFolder+"one.gif"));
		int widthD= 8* tem.getIconWidth();
		//width += 500; // = 8*dicedir.one.gif.width
		int heightT = tem.getIconHeight();
		tem= new ImageIcon(getClass().getResource(cardFolder+"title.gif"));
		//int height = max((card.height+ dice.height + more), userpanel.height)
		//                  + fontsize*nrows/10;
		heightT += tem.getIconHeight() +120;
		heightT = Math.max(heightT,300 /* UserPanel height*/);
		int width = widthU + widthD;
		int height=heightT + 300;
		System.out.printf("widthD,HeightT, widthU,totH: %d %d %d %d%n",widthD,heightT,widthU,height);
		//height=700; width=700;
		return new Dimension[] {new Dimension(width,height),
			                new Dimension(widthD,heightT)};
	}

	public FillrBustGui(String[] names, int targetScore) {
		//this(names, targetScore);
    }

	//public FillrBustGui(String[] names, int targetScore)
	public FillrBustGui(FBConfig config)
    {
	   this.config = config;
	   //myGame = new FillRBustGame(names, targetScore);
	   myGame = new FillRBustGame(config.players, config.goal);
	   //System.out.print("FillrBustGui built myGame");
	   Dimension[] frameDims = getDims(config.players.length, config.cardDir, config.diceDir);
	   cardFolder = config.cardDir;
       frame = new JFrame("Play FillRBust");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //  _______________________________________
		 //frame.setSize(700,700);                              //  |        Card          |               |
         frame.setSize(frameDims[0].width, frameDims[0].height);//  |                      |               |
	   mbar = new JMenuBar();                                   //  |____________________  |   UserPanel   |
	   game = new JMenu("Game");                             //  ||      dice panel   | |               |
	   target = new JMenuItem("Change Winning Score");     //  ||-------------------- |               |
	   addPlayer = new JMenuItem("add player");            //  |      scores          |               |
	   //save = new JMenuItem("save game");                     //  |      choices         |               |
	   //load = new JMenuItem("load saved game");               //  |--------------------------------------|
	   rc = new JMenuItem("write rc file");                //  |                                      |
	   quit = new JMenuItem("quit");                       //  |           details                    |
	     game.add(target); game.add(addPlayer);                 //  |                                      |
		 //game.add(save);                                      //  |______________________________________|
	     //game.add(load);
		 game.add(rc); game.add(quit);
	   help = new JMenu("Help");
	   rules = new JMenuItem("Rules");
	   synopsis = new JMenuItem("Synopsis");
	   specific = new JMenuItem("specific");
	   about = new JMenuItem("About");
	     help.add(rules); help.add(synopsis); help.add(specific); help.add(about);
	   mbar.add(game);
	   mbar.add(help);
	   frame.setJMenuBar(mbar);
	    // top: w=700, h=250
           top = new JPanel();
		top.setBounds(0,0,frameDims[0].width,frameDims[1].height);

	   details = new JTextArea(150,12);
	   //details.setFont(new Font("Helvetica",Font.PLAIN, config.fontSize));
	   // details.setFont(new Font("Living by Numbers",Font.PLAIN, config.fontSize));
	   //details.setFont(new Font("Karma Suture",Font.PLAIN, config.fontSize));
	   details.setFont(new Font("Ænigma Scrawl 4 BRK",Font.PLAIN, config.fontSize));
	   details.setEditable(false);
	   detailsP = new JScrollPane(details);
		detailsP.setBounds(0,frameDims[1].height,
					frameDims[0].width,frameDims[0].height-frameDims[1].height);
		details.append("Follow these instructions:\n");

	   data = new JPanel();
		data.setBounds(frameDims[1].width,0,
				frameDims[0].width-frameDims[1].width, frameDims[1].height);
	    //buildDataGui(data, names, targetScore);
	   buildDataGui(data, config.players, config.goal);

	   actions = new JPanel();
		actions.setBounds(0,0,frameDims[1].width,frameDims[1].height);
		int ypos = 0;
		//actions.setLayout(new BoxLayout(actions,BoxLayout.Y_AXIS));
		actions.setBorder(BorderFactory.createLineBorder(Color.RED));
		actions.setBackground(Color.BLACK);
	    //Image titleImg = Toolkit.getDefaultToolkit().getImage("title.gif");not
	      ImageIcon imi =new ImageIcon(getClass().getResource(cardFolder+"title.gif"));
          card = new JButton(new ImageIcon(getClass().getResource(cardFolder+"title.gif")));
	    card.setBounds(200,ypos, imi.getIconWidth(), imi.getIconHeight());  // TODO geom based on image size
		card.setToolTipText(cardHelp);
		ypos+=imi.getIconHeight();
	    diceP=new DicePanel(config.diceDir);
	    imi =new ImageIcon(getClass().getResource(config.diceDir+"one.gif"));
	    diceP.setBounds(0,ypos,frameDims[1].width,imi.getIconHeight());  // TODO geom based on image size
	    ypos+=imi.getIconHeight();
	    diceP.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		diceP.setToolTipText(diceHelp);
	    scores = new JPanel(new FlowLayout(FlowLayout.CENTER,20,2));
	    scores.setBounds(100,ypos,300,25);  // TODO geom based on previous geom
		ypos+=30;
	    scores.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
	    scores.setBackground(Color.BLACK);
	    running = new JLabel("running score = none");
		running.setForeground(Color.WHITE);
	    potential = new JLabel("potential score =     0");
	    potential.setForeground(Color.WHITE);
	    scores.add(potential);
	    scores.add(running);
	    choices = new JPanel();
	    //choices.setLayout(new BoxLayout(choices,BoxLayout.Y_AXIS));
	    choices.setLayout(new FlowLayout(FlowLayout.CENTER));
	    choices.setBounds(200,ypos,100,95);
	    choices.setBackground(Color.BLACK);
		ypos+=125;
	    choices.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		choices.setToolTipText(optionHelp);
          optionA = new JButton("quit");
          optionB = new JButton("optionB");
	    //actions2.setBounds(300,270,100,30);
	    choices.add(optionA);
	    choices.add(optionB);
          actions.add(card);
          actions.add(diceP);
          actions.add(scores);
          actions.add(choices);

	    top.add(actions);
	    top.add(data);

	    frame.getContentPane().add(top);
	    frame.getContentPane().add(detailsP);
	    //System.out.print("FillrBustGui built GUI PARTS");

	    quit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    System.exit(1);
                }
	    });
	    optionA.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
		    myGame.update("OPTION_A");
                }
	    });
	    optionB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
		    myGame.update("OPTION_B");
                }
	    });
	    card.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent fred) {
		    if (gameSTATE == FillRBustGame.STATES.INIT){
				init_game();
		    }else if (gameSTATE == FillRBustGame.STATES.DRAWCARD){
				//Image sepImg = Toolkit.getDefaultToolkit()
			    //          .getImage(mapCardImage(myGame.getCard()));
				//System.out.println(myGame.getCard());
		        //card.setIcon(new ImageIcon(sepImg));
				//gameSTATE = myGame.getState();
			    myGame.update("CARD");
		    }
		    //details.append("Roll the dice.\n");
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

	    actions.setLayout(null);
	    top.setLayout(null);
	    frame.setLayout(null);
          frame.setVisible(true);
	    //System.out.print("FillrBustGui FINISHED");
	    init_game();
		new javax.swing.Timer(UPDATE_PERIOD, updateTask).start();

    }



    public FillrBustGui() {
	     String[] list = {"Clyde","Nancy"};
	     int winner = 5000;
		myGame = new FillRBustGame(list,winner);

    }

	void URrepaint() {
		//System.out.println("repaint                     repaint");
		myGame.aiInterface();
		//detailsP.repaint();
	}

     //  game action methods: {
    void init_game() {
	 currentPlayer = myGame.setGui(this);
	 gameSTATE=myGame.getState();
	 players.get(currentPlayer).toggleTurn();
	 //System.out.println("init_game was here; currentPlayer "+currentPlayer);
	 addDetails(String.format("%s, it is your turn.\n Draw a card.\n",
			 players.get(currentPlayer).getUName()));
    }

    void setOptionA(String option) {
	    optionA.setText(option);
     }

    void deRoll(){
	    //System.out.println("deRoll");
		diceP.setDice("000000 ", "0000000");
    }

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

    void addDetails(String line){
	    details.append(line);
    }

    void setOptionB(String option) {
	    optionB.setText(option);
		//System.out.println("tried to set OptionB");
     }
    //  game action methods: }

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

	public void ComplainLoudly(String complaint, String topic, int large) {
		MyDialog showHelp = new MyDialog(frame, complaint,topic);
		showHelp.setFont(large);
		showHelp.pack();
		showHelp.setVisible(true);
	}
	public void ComplainLoudly(String complaint, String topic) {
		MyDialog showHelp = new MyDialog(frame, complaint,topic);
		showHelp.pack();
		showHelp.setVisible(true);
	}
	public void ComplainLoudly(String complaint) {
		MyDialog showHelp = new MyDialog(frame, complaint);
		showHelp.pack();
		showHelp.setVisible(true);
	}

	public void addScore(int round) {
		players.get(currentPlayer).addScore(round);
	}
	public void addScore(String name, int score) {
		for( UserPanel each : players) {
			if (each.getUName().equals(name)){
				each.addScore(score);
				break;
			}
		}
	}

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

	public void setRunning(int running, int potential) {
		this.running.setText(String.format("running score = %d",running));
		this.potential.setText(String.format("potential score = %d",potential));
	}

	public String getDiceSelection() {
		return diceP.getSelected();
	}

	public void setSelected(String mask) {
		diceP.setSelected(mask);
	}

	public void setDice(String set, String mask){
		diceP.setDice(set,mask);
	}

	public void declareWinner(String name) {
		ComplainLoudly(String.format("  !!! %s is the big winner !!!",name),"WINNER", 44);
	}

	/**
	 * Dialog panel to set a new value for the winning score
	 */
	public void getNewTarget (){
		//System.out.println("'Change the score to win' is unimplemented.\n");
		String s = (String)JOptionPane.showInputDialog(
				frame,"Enter a new max score: "
				,myGame.getMaxScore()
		);
		int maxScore = myGame.getMaxScore();
		if (s!=null){
			try {
				maxScore = Integer.parseInt(s);
				winningScore.setText(s);
				myGame.setMaxScore(maxScore);
			}catch (NumberFormatException e)  {
				ComplainLoudly("Score is a NUMBER", "ERROR");
			}
		}
	}

	public void saveGame() {
		//notTODO
	}

	public void readGame() {
		//notTODO
	}

	public void makeRCfile() {
		//TODO
		config.writeConfig(frame, myGame.getMaxScore(), myGame.getPlist());
	}

	public void toggleHoverHelp() {
		//TODO
	}

	public void addNewPlayer() {
		String s = (String)JOptionPane.showInputDialog(
				frame,"Enter the name of the new player: \n"
				+" prepend 'ai' if player is computer controlled.\n"
				+" if last character is digit, it sets the riskiness."
		);
		UserPanel entry = new UserPanel(s);
		players.add(entry);
		users.add(entry);
		myGame.addPlayer(s);
		System.out.println("'add new player' is unimplemented.\n");
		System.out.println("User Panel size:"+users.getSize());
		for (UserPanel each: players) System.out.println(each.getSize());
		// TODO Learn how to make frame expand to accomodate more users or scroll
	}

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

	FBConfig config;
	public static void main(String[] args){
		/*
		try {
			Process process = Runtime.getRuntime().exec("pwd ");
			String s;
			int exitCode1 = process.waitFor();
            System.out.println("Exit Code: " + exitCode1);
			BufferedReader br = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println("line: " + s);
            process.waitFor();
            System.out.println ("exit: " + process.exitValue());
            process.destroy();
		}
		catch (IOException e) {
			int fred = 4;
		}
		catch (InterruptedException e) {
			int fred = 4;
		}
		//StreamGobbler streamGobbler =
        //    new StreamGobbler(process.getInputStream(), System.out::println);
		//Future<?> future = executorService.submit(streamGobbler);

		 */
		FBConfig config = new FBConfig();
		String[] list = config.players;
		int winner = config.goal;
		boolean doGui = config.gui;
		//String[] list = {"Clyde","aiNancy4"};
		//int winner = 5000;
		//boolean doGui = true;
		boolean debug = false;
		if (args.length >0){
			ArrayList<String> temp = new ArrayList<String>();
			for (int i=0; i<args.length;i++) {
				if (args[i].equals("-d")) debug = true;
				if (args[i].equals("-g")) doGui = false;
				if (args[i].equals("-g")) config.gui = false;
				else if (args[i].equals("-m")) {
					config.goal = Integer.parseInt(args[i++ +1]);
				}
				/*
				 */
				else if (args[i].equals("-p")) {
					temp.add(args[i++ + 1]);
				}
			}
			if (!temp.isEmpty()) list = temp.toArray(list);
			if (!temp.isEmpty()) config.players = temp.toArray(list);
		}
		if (doGui) {
			int finalWinner = winner;
			String[] finalList = list;
			java.awt.EventQueue.invokeLater(new Runnable() {
			  public void run() {
				//new FillrBustGui(finalList, finalWinner).setVisible(true);
				new FillrBustGui(config).setVisible(true);
			  }
			});
		} else {
			System.out.println("aint doin no stinkin GUI");
			FillRBustGame myGame = new FillRBustGame(list, winner);
			myGame.textInterface();
		}
    }

}
