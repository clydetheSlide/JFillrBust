/*
 * Play the game FillRBust
 *   . maintains the state of the game:
 *       players,
 *       dice,
 *       card,
 *       game state,
 *
 *  . enacts the rules
 *  . reports back to the GUI
 *   (which actually drives the game)
 */
package fillrbust;

import java.util.*;

import static java.lang.Thread.sleep;

class FillRBustGame {

	static final String EMPTY_BUTTON = "        ";

	public enum STATES {
		INIT, NEXTPLAYER    //
		, DRAWCARD          // only action is to draw a card
		, DREWVENGEANCE     // can choose yes to start rolling or draw
		, ROLLFIRST         // after drawing a playable card, only option is to roll
							// the dice (poke dice or optionA)
		, ROLLED            // after non-bust roll, can roll again.
							// Depending on other states whether can choose dice
							// or take score and quit
		, FILLED            // after filling, there is the option to score or draw again
		, BUSTED, ROLLSOME, DOUBLETROUBLE, WINNER
	}

	Cards deck = new Cards();
	Cards.Name cardType;
	int bonus;
	Dice dice;
	FillrBustGui gui;
	STATES state = STATES.INIT;
	HashMap<String, Player> players;
	int runningScore, tempScore;
	Player player;
	String[] playerList;
	int currentPlayer;
	boolean vengeance = false;
	boolean mustbust = false;
	boolean mustfill = false;   // used with vengeance and doubletrouble
	int doubletrouble = 0;
	int max_score;
	int high_score = 0;
	String highScorer = "";
	boolean aiResponder = false;
	boolean debug = false; //true;
	int nupdate;

	public FillRBustGame(String[] plist, int max) {
		this.max_score = max;
		playerList = plist;
		players = new HashMap<String, Player>(4);
		for (String each : plist) {
			if (each.indexOf("ai") == 0) {
				int risk = 3;
				int end = each.length();
				char rc = each.charAt(end - 1);
				if ("1234567890".indexOf(rc) >= 0) {
					risk = Character.getNumericValue(rc);
					end = end - 1;
				}
				players.put(each, new AIPlayer(each.substring(0, end), risk));
			} else
				players.put(each, new Player(each));
		}
		currentPlayer = 0;
		player = players.get(plist[currentPlayer]);
	}

	public void addPlayer(String name){
		ArrayList<String> temp = new ArrayList<String>();
		for(String each : playerList) temp.add(each);
		temp.add(name);
		playerList = temp.toArray(playerList);
		if (name.indexOf("ai") == 0) {
			int risk = 3;
			int end = name.length();
			char rc = name.charAt(end - 1);
			if ("1234567890".indexOf(rc) >= 0) {
				risk = Character.getNumericValue(rc);
				end = end - 1;
			}
			players.put(name, new AIPlayer(name.substring(0, end), risk));
		} else
			players.put(name, new Player(name));
	}

	public String[] getPlist() {
		return playerList;
	}

	public Player nextPlayer() {
		currentPlayer = (currentPlayer + 1)%playerList.length;
		//System.out.println("in Game.nextPlayer:");
		//for(String each: playerList)System.out.println(each);
		//System.out.println(currentPlayer);
		return players.get(playerList[currentPlayer]);
	}

	public int setGui(FillrBustGui gui) {
		this.gui = gui;
		//System.out.println("I know the gui, now");
		//System.out.println(gui);
		state = STATES.DRAWCARD;
		return currentPlayer;
	}

	public STATES getState() {
		return state;
	}

	public Cards.Name getCard() {
		return deck.draw();
	}

	public void newDice() {
		dice = new Dice();
	}

	public String getRolled() {
		return dice.getRolled();
	}

	public void selectDice(String mask) {
		dice.selected(mask);
	}

	public String getReserved() {
		return dice.getUsed();
	}

	/**
	 * this is where the rules of the game are codified.
	 * For each state of the game, determine how to respond
	 * to an action button
	 */
	public void update(String whoDunnit) {
		nupdate  +=1;
		//System.out.println(state);
		//System.out.println(whoDunnit+ " in update "+nupdate);
		if (state == STATES.INIT) {
			//System.out.println("I will setOptionA now.");
			gui.setOptionA(EMPTY_BUTTON);
			gui.setOptionB(EMPTY_BUTTON);
			gui.deRoll();
			gui.setRunning(0,0);
			//gui.setCurrentPlayer(player.getName());
			gui.addDetails(String.format(
			      "\n%s, it is your turn.\nDraw a card.\n", player.getName()));
			state = STATES.DRAWCARD;
			//return;
		} else
		if (whoDunnit.indexOf("CARD")==0) {
			if (state == STATES.DRAWCARD) {
				Cards.Name card = deck.draw();
				// for debugging, here's a method to force the card type
				if(whoDunnit.split(" ").length >1) {
					String icard = whoDunnit.split(" ")[1];
					switch (icard) {
						case "m": card = Cards.Name.MUST_BUST; break;
						case "d": card = Cards.Name.DOUBLE_TROUBLE; break;
						case "f": card = Cards.Name.FILL_1000; break;
						case "v": card = Cards.Name.VENGEANCE; break;
						case "b": card = Cards.Name.BONUS_300; break;
						case "n": card = Cards.Name.NO_DICE; break;
					}
				}
				gui.setCard(card);
				gui.addDetails("You drew " + card + ".\n");
				gui.deRoll();
				gui.setRunning(0,0);
				bonus = 0;
				if (card.equals(Cards.Name.NO_DICE)) {
					tempScore = 0;
					runningScore = 0;
					player = nextPlayer();
					aiResponder=getAI(player);
					gui.setCurrentPlayer(player.getName());
					gui.addDetails(String.format(
							"\n%s, it is your turn.\nDraw a card.\n", player.getName()));
				} else if (card.equals(Cards.Name.VENGEANCE)) {
					if (player.getName().equals(highScorer)) {
						gui.addDetails(" You can't take vengeance on yourself;\n" +
								" Draw another card\n");
					} else if (high_score<2500){
					    gui.addDetails(" Nobody has enough points to warrant vengeance;\n"+
								" Draw another card\n");
					} else {
						gui.addDetails(String.format(" %s has %d points. Do you want to take vengeance?\n Or would you rather draw another card?\n", highScorer, high_score));
						state = STATES.DREWVENGEANCE;
						gui.setOptionB("Vengeance");
						gui.setOptionA("Draw card");
					}
				} else {
					mustbust = false;
					vengeance = false;
					mustfill = false;
					doubletrouble = 0;
					state = STATES.ROLLFIRST;
					gui.setOptionA("Roll Dice");
					gui.setOptionB(EMPTY_BUTTON);
					gui.addDetails(" Roll the dice.\n");
					if (card.equals(Cards.Name.MUST_BUST)) {
						mustbust = true;
						gui.addDetails(" Roll with no risk\n");
					} else if (card.equals(Cards.Name.FILL_1000)) {
						mustfill = true;
						bonus = 1000;
						gui.addDetails(" You don't get any points until you fill it.\n");
					} else if (card.equals(Cards.Name.DOUBLE_TROUBLE)) {
						doubletrouble = 2;
						gui.addDetails(" You don't get any points until you fill it. TWICE!\n");
					} else {
						bonus = Cards.getBonus(card);
					}
				}
			}
		} else

		if (whoDunnit.equals("OPTION_A")) {
			if (state == STATES.ROLLFIRST) {
				dice = new Dice();
				String rolled = dice.getRolled();
				gui.setDice(rolled + " ", dice.getMasked() + " ");
				gui.setSelected(dice.getMasked());
				int score = dice.getScore();
				if (score == 0) {
					if (mustbust) {
						player.update(runningScore);
						gui.addScore(runningScore);
						gui.addDetails(String.format(
								" You ended your Must Bust turn making %d points.\n", runningScore));
						if (checkUpdateHigh(player)) {
							announceWinner();
							nupdate-=1;
							return;
						}
						mustbust=false;
					} else {
						gui.addDetails(" You busted on the initial roll!\n");
					}
					tempScore = 0;
					runningScore = 0;
					player = nextPlayer();
					aiResponder=getAI(player);
					gui.setCurrentPlayer(player.getName());
					gui.addDetails(String.format(
							"\n%s, it is your turn.\nDraw a card.\n", player.getName()));
					gui.setOptionA(EMPTY_BUTTON);
					gui.setOptionB(EMPTY_BUTTON);
					state = STATES.DRAWCARD;
				} else if (dice.isFilled()) {
					gui.addDetails("!! You filled it on the first roll!! \n");
					if (mustbust) {
						runningScore += score;
						gui.addDetails("  Keep rolling.\n");
						gui.setRunning(runningScore,0);
					} else if (vengeance) {
						players.get(highScorer).update(-2500);
						player.update(score);
						int newScore = players.get(highScorer).getScore();
						gui.updateScore(highScorer, newScore);
						gui.addDetails(String.format(
								" And %s is dropped to %d points.\n", highScorer, newScore));
						newScore = player.getScore();
						if (newScore > max_score) {
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.updateScore(newScore);
						gui.addDetails(String.format(
								"%s, your new score is %d.\n", player.getName(), player.getScore()));
						updateHighScorer();
						tempScore = 0;
						runningScore = 0;
						gui.addDetails(String.format(
								"\n%s, you get another turn.\nDraw a card.\n", player.getName()));
						state = STATES.DRAWCARD;
						gui.setOptionA(EMPTY_BUTTON);
						gui.setOptionB(EMPTY_BUTTON);
						vengeance = false;
					} else if (mustfill) {
						runningScore += score + 1000;
						gui.setRunning(runningScore,0);
						if (player.getScore() + runningScore > max_score) {
							player.update(runningScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" You can accept this score of %d and end your turn\n" +
								" or you can risk it and take a card to continue your turn\n",
								runningScore));
						state = STATES.FILLED;
						mustfill = false;
						gui.setOptionA("Score");
						gui.setOptionB("Continue");
					} else if (doubletrouble == 2) {
						runningScore += score * 2;
						gui.setRunning(runningScore,0);
						gui.addDetails(" You must fill it yet again to get points. Roll again.\n");
						doubletrouble = 1;
						state = STATES.ROLLFIRST;
					} else if (doubletrouble == 1) {
						runningScore += score * 2;
						player.update(runningScore);
						int newScore = player.getScore();
						gui.updateScore(newScore);
						if (checkUpdateHigh(player)) {
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" That is the second fill for a total of %d points for this turn\n" +
								" Now you can start another turn without risking this one. Draw a Card",
								runningScore));
						doubletrouble = 0;
						state = STATES.DRAWCARD;
						gui.setOptionA(EMPTY_BUTTON);
						gui.setOptionB(EMPTY_BUTTON);
					} else {
						runningScore += score + bonus;
						gui.setRunning(runningScore,0);
						if (player.getScore() + runningScore > max_score) {
							player.update(runningScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" You can accept this score of %d and end your turn\n" +
										" or you can risk it and take a card to continue your turn\n",
								runningScore));
						gui.setOptionA("Score");
						gui.setOptionB("Continue");
						state = STATES.FILLED;
					}
				} else {
					tempScore = score;
					gui.setOptionA("Roll dice");
					if (mustbust) {
						gui.setOptionB(EMPTY_BUTTON);
					} else if (vengeance || mustfill || doubletrouble > 0) {
						if (doubletrouble > 0)tempScore+=score;
						gui.setOptionB(EMPTY_BUTTON);
						gui.addDetails(" Select dice to keep and");
					} else {
						if (player.getScore() + runningScore +tempScore> max_score) {
							player.update(runningScore+tempScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.setOptionB("Score");
						gui.addDetails(" Score what has been accumulated so far\n" +
								"or risk it, select dice to keep and");
					}
					gui.addDetails(" roll again.\n");
					gui.setRunning(runningScore, tempScore);
					state = STATES.ROLLED;
				}
			} else if (state == STATES.DREWVENGEANCE) {
				// draw a card
				gui.setOptionA(EMPTY_BUTTON);
				gui.setOptionB(EMPTY_BUTTON);
				state = STATES.DRAWCARD;
			} else if (state == STATES.FILLED) {
				// take score
				player.update(runningScore);
				gui.addScore(runningScore);
				int newScore = player.getScore();
				gui.updateScore(newScore);
				gui.addDetails(String.format(" Your score is now %d.\n",newScore ));
				if (checkUpdateHigh(player)) {
					announceWinner();
					nupdate-=1;
					return;
				}
				// new player
				tempScore = 0;
				runningScore = 0;
				player = nextPlayer();
				aiResponder=getAI(player);
				gui.setCurrentPlayer(player.getName());
				gui.addDetails(String.format(
						"\n%s, it is your turn.\nDraw a card.\n", player.getName()));
				gui.setOptionA(EMPTY_BUTTON);
				gui.setOptionB(EMPTY_BUTTON);
				state = STATES.DRAWCARD;
			} else if (state == STATES.ROLLED) {
				//roll again
				runningScore += tempScore;
				tempScore = 0;
				dice.rolagain();
				gui.setDice(dice.getRolled() +" "+dice.getReserved(), dice.getMasked());
				tempScore = dice.getScore();
				gui.setSelected(dice.getMasked());   // this should be sufficient
				if (tempScore == 0) {
					gui.addDetails(" Aww, you busted.\n");
					if (mustbust) {
						player.update(runningScore);
						gui.addScore(runningScore);
						gui.addDetails(String.format(
							" You ended your Must Bust with %d points.\n",runningScore));
						if (checkUpdateHigh(player)) {
							announceWinner();
							nupdate-=1;
							return;
						}
						mustbust=false;
					}
					// set up next player
					tempScore = 0;
					runningScore = 0;
					player = nextPlayer();
					aiResponder=getAI(player);
					gui.setCurrentPlayer(player.getName());
					gui.addDetails(String.format(
							"\n%s, it is your turn.\nDraw a card.\n", player.getName()));
					gui.setOptionA(EMPTY_BUTTON);
					gui.setOptionB(EMPTY_BUTTON);
					state = STATES.DRAWCARD;
				} else if (dice.isFilled()) {
					gui.addDetails("!! You filled it! \n");
					gui.setRunning(runningScore, tempScore);
					if (mustbust) {
						runningScore += tempScore;
						gui.addDetails("  Keep rolling.\n");
						tempScore=0;
						gui.setRunning(runningScore,tempScore);
						state = STATES.ROLLFIRST;
					} else if (vengeance) {
						players.get(highScorer).update(-2500);
						player.update(runningScore+tempScore);
						int newScore = players.get(highScorer).getScore();
						gui.updateScore(highScorer, newScore);
						gui.addScore(highScorer, -2500);
						gui.addDetails(String.format(
								" And %s is dropped to %d points.\n", highScorer, newScore));
						newScore = player.getScore();
						gui.updateScore(newScore);
						gui.addDetails(String.format(
								"%s, your new score is %d.\n", player.getName(), player.getScore()));
						if (newScore > max_score) {
							announceWinner();
							nupdate-=1;
							return;
						}
						updateHighScorer();
						tempScore = 0;
						runningScore = 0;
						gui.addDetails(String.format(
								"\n%s, you get another turn.\nDraw a card.\n", player.getName()));
						state = STATES.DRAWCARD;
						gui.setOptionA(EMPTY_BUTTON);
						gui.setOptionB(EMPTY_BUTTON);
						vengeance = false;
					} else if (doubletrouble == 2) {
						runningScore += tempScore * 2;
						tempScore=0;
						gui.setRunning(runningScore, tempScore);
						gui.addDetails(" You must fill it yet again to get points. Roll again.\n");
						doubletrouble = 1;
						state = STATES.ROLLFIRST;
					} else if (doubletrouble == 1) {
						runningScore += tempScore * 2;
						tempScore=0;
						gui.setRunning(runningScore, tempScore);
						player.update(runningScore);
						int newScore = player.getScore();
						gui.updateScore(newScore);
						if (checkUpdateHigh(player)) {
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" That is the second fill for a total of %d points for this turn\n" +
										" Now you can start another turn without risking this one. Draw a Card",
								runningScore));
						doubletrouble = 0;
						state = STATES.DRAWCARD;
						gui.setOptionA(EMPTY_BUTTON);
						gui.setOptionB(EMPTY_BUTTON);
					} else  {
						runningScore += tempScore + bonus;
						gui.setRunning(runningScore, tempScore);
						if (player.getScore() + runningScore > max_score) {
							player.update(runningScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" You can accept this score of %d and end your turn\n" +
										" or you can risk it and take a card to continue your turn\n",
								runningScore));
						state = STATES.FILLED;
						mustfill = false;
						gui.setOptionA("Score");
						gui.setOptionB("Continue");
					}
				} else {
					gui.setRunning(runningScore, tempScore);
					if (mustbust) {
						gui.setOptionA("Roll dice");
						gui.setOptionB(EMPTY_BUTTON);
					} else if (vengeance || mustfill || doubletrouble > 0) {
						if (doubletrouble>0){
							tempScore*=2;
							gui.setRunning(runningScore, tempScore);
						}
						gui.setOptionA("Roll dice");
						gui.setOptionB(EMPTY_BUTTON);
						gui.addDetails(" Select dice to keep and");
					} else {
						if (player.getScore() + runningScore +tempScore> max_score) {
							player.update(runningScore+tempScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.setOptionA("Roll dice");
						gui.setOptionB("Score");
						gui.addDetails(" Score what has been accumulated so far\n" +
								"or risk it, select dice to keep and");
					}
					gui.addDetails(" roll again.\n");
					state = STATES.ROLLED;
				}
			} else if (state == STATES.WINNER) {
				System.exit(0);
				// options to start new game, extend game
			}
		} else

		if (whoDunnit.equals("OPTION_B")) {
			if (state == STATES.FILLED) {
				// continue by drawing a card
				gui.setOptionA(EMPTY_BUTTON);
				gui.setOptionB(EMPTY_BUTTON);
				gui.addDetails(" Risky! Draw a Card.\n");
				state = STATES.DRAWCARD;
			} else if (state == STATES.DREWVENGEANCE) {
				vengeance = true;
				state = STATES.ROLLFIRST;
				runningScore = 0;
				tempScore = 0;
				gui.setOptionA("Roll Dice");
				gui.setOptionB(EMPTY_BUTTON);
				gui.addDetails(" Sic 'em\n");
			} else if (state == STATES.ROLLED) {
				if (!mustbust) {
					// score
					player.update(runningScore + tempScore);
					gui.addScore(runningScore + tempScore);
					int newScore = player.getScore();
					gui.updateScore(newScore);
					gui.addDetails(String.format(" Your score is now %d.\n",newScore ));
					if (checkUpdateHigh(player)) {
						announceWinner();
						nupdate-=1;
						return;
					}
					// new player
					tempScore = 0;
					runningScore = 0;
					player = nextPlayer();
					aiResponder = getAI(player);
					gui.setCurrentPlayer(player.getName());
					gui.addDetails(String.format(
							"\n%s, it is your turn.\nDraw a card.\n", player.getName()));
					gui.setOptionA(EMPTY_BUTTON);
					gui.setOptionB(EMPTY_BUTTON);
					state = STATES.DRAWCARD;
				}
			} else if (state == STATES.WINNER) {
				System.exit(0);
				// options to start new game, extend game
			}
		} else

		if (whoDunnit.indexOf("DICE") == 0) {
			if (state == STATES.ROLLFIRST) {
				// roll them all
				// for debugging, here's a method to force the roll
				if(whoDunnit.split(" ").length >1) {
					dice = new Dice(whoDunnit.split(" ")[1]);
				}else
				dice = new Dice();
				String rolled = dice.getRolled();
				gui.setDice(rolled + " ", dice.getMasked() + " ");
				gui.setSelected(dice.getMasked());
				int score = dice.getScore();
				if (score == 0) {
					if (mustbust) {
						player.update(runningScore);
						gui.addScore(runningScore);
						gui.addDetails(String.format(
								" You ended your Must Bust turn making %d points.\n", runningScore));
						if (checkUpdateHigh(player)) {
							announceWinner();
							nupdate-=1;
							return;
						}
					} else {
						gui.addDetails(" You busted on the initial roll!\n");
					}
					tempScore = 0;
					runningScore = 0;
					gui.setRunning(runningScore, tempScore);
					player = nextPlayer();
					aiResponder=getAI(player);
					gui.setCurrentPlayer(player.getName());
					gui.addDetails(String.format(
							"\n%s, it is your turn.\nDraw a card.\n", player.getName()));
					gui.setOptionA(EMPTY_BUTTON);
					gui.setOptionB(EMPTY_BUTTON);
					state = STATES.DRAWCARD;
				} else if (dice.isFilled()) {
					gui.addDetails("!! You filled it on the first roll!! \n");
					if (mustbust) {
						runningScore += score;
						gui.addDetails("  Keep rolling.\n");
						gui.setRunning(runningScore, 0);
					} else if (vengeance) {
						players.get(highScorer).update(-2500);
						player.update(score);
						int newScore = players.get(highScorer).getScore();
						gui.updateScore(highScorer, newScore);
						gui.addDetails(String.format(
								" And %s is dropped to %d points.\n", highScorer, newScore));
						updateHighScorer();
						newScore = player.getScore();
						gui.updateScore(newScore);
						gui.addDetails(String.format(
								"%s, your new score is %d.\n", player.getName(), player.getScore()));
						if (newScore > max_score) {
							announceWinner();
							nupdate-=1;
							return;
						}
						tempScore = 0;
						runningScore = 0;
						gui.setRunning(runningScore, tempScore);
						gui.addDetails(String.format(
								"\n%s, you get another turn.\nDraw a card.\n", player.getName()));
						state = STATES.DRAWCARD;
						gui.setOptionA(EMPTY_BUTTON);
						gui.setOptionB(EMPTY_BUTTON);
						vengeance = false;
					} else if (mustfill) {
						runningScore += score + 1000;
						if (player.getScore() + runningScore > max_score) {
							player.update(runningScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" You can accept this score of %d and end your turn\n" +
										" or you can risk it and take a card to continue your turn\n",
								runningScore));
						state = STATES.FILLED;
						mustfill = false;
						gui.setRunning(runningScore, 0);
						gui.setOptionA("Score");
						gui.setOptionB("Continue");
					} else if (doubletrouble == 2) {
						runningScore += score * 2;
						gui.addDetails(" You must fill it yet again to get points. Roll again.\n");
						doubletrouble = 1;
						state = STATES.ROLLFIRST;
					} else if (doubletrouble == 1) {
						runningScore += score * 2;
						player.update(runningScore);
						int newScore = player.getScore();
						gui.updateScore(newScore);
						if (checkUpdateHigh(player)) {
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" That is the second fill for a total of %d points for this turn\n" +
										" Now you can start another turn without risking this one. Draw a Card",
								runningScore));
						doubletrouble = 0;
						state = STATES.DRAWCARD;
						gui.setOptionA(EMPTY_BUTTON);
						gui.setOptionB(EMPTY_BUTTON);
					} else {
						runningScore += score + bonus;
						if (player.getScore() + runningScore > max_score) {
							player.update(runningScore);
							announceWinner();
							nupdate-=1;
							return;
						}
						gui.addDetails(String.format(
								" You can accept this score of %d and end your turn\n" +
										" or you can risk it and take a card to continue your turn\n",
								runningScore));
						gui.setOptionA("Score");
						gui.setOptionB("Continue");
						state = STATES.FILLED;
						gui.setRunning(runningScore, 0);
					}
				} else {
					tempScore = score;
					gui.setOptionA("Roll dice");
					if (mustbust) {
						gui.setOptionB(EMPTY_BUTTON);
					} else if (vengeance || mustfill || doubletrouble > 0) {
						if (doubletrouble>0)tempScore=score*2;
						gui.setOptionB(EMPTY_BUTTON);
						gui.addDetails(" Select dice to keep and");
					} else {
						gui.setOptionB("Score");
						gui.addDetails(" Score what has been accumulated so far\n" +
								"or risk it, select dice to keep and");
					}
					gui.addDetails(" roll again.\n");
					gui.setRunning(runningScore, tempScore);
					state = STATES.ROLLED;
				}
			} else if (state == STATES.ROLLED) {
				// select dice
				if (mustbust) {
					gui.addDetails("Must Bust rules remove your choice along with the risk. Just Roll with it.\n");
					gui.setDice(dice.getRolled()+" "+dice.getReserved(), dice.getMasked());
					 gui.setSelected(dice.getMasked());   // this should be sufficient
					gui.setRunning(runningScore, tempScore);
				} else {
					String mask = gui.getDiceSelection();
					if(whoDunnit.split(" ").length >1) mask = whoDunnit.split(" ")[1];
					dice.selected(mask);
					tempScore = dice.getScore();
					if(doubletrouble>0) tempScore*=2;
					if (tempScore <1) {
						gui.addDetails("You must score something. Select again.\n");
						dice.deselected();
						gui.setDice(dice.getRolled()+" "+dice.getReserved(), dice.getMasked());
						tempScore = dice.getScore();
						if(doubletrouble>0) tempScore*=2;
					}
					gui.setRunning(runningScore, tempScore);
				}
			}
		} else
		if (whoDunnit.indexOf("MAX") == 0) {
			max_score = Integer.valueOf(whoDunnit.split(" ")[1]);
			// TODO show that in GUI
		}
		//if (aiResponder) gui.URrepaint();
		//if (aiResponder && nupdate<2) aiInterface();
		nupdate-=1;

	}

	/**
	 * set the score to be the winner
	 * @param num
	 */
	public void setMaxScore(int num){
		max_score = num;
	}
	public int getMaxScore() {return max_score;}

	void announceWinner () {
		gui.addDetails(String.format(" !! %s won !!", player.getName()));
		gui.declareWinner(player.getName());
		state = STATES.WINNER;
	}
	/**
	 * check which player has the highest score.
	 * update globals highScorer and high_score
	 */
	void updateHighScorer() {
		String newHiOne = "";
		int newHi = 0;
		for (String each : players.keySet()) {
			int sc = players.get(each).getScore();
			if (sc > newHi) {
				newHi = sc;
				newHiOne = each;
			}
		}
		gui.addDetails(String.format("The high score is %d.\n", newHi));
		high_score = newHi;
		if (newHiOne.equals(highScorer))
			gui.addDetails(highScorer + " is still the one to beat\n");
		else {
			highScorer = newHiOne;
			gui.addDetails(highScorer + " has usurped the lead.\n");
		}
	}

	private boolean checkUpdateHigh(Player player) {
		int newScore = player.getScore();
		gui.updateScore(newScore);
		if( newScore > high_score) {
			high_score = newScore;
			highScorer = player.getName();
			gui.addDetails(String.format(
					" %s has the new high score of %d\n", highScorer, high_score));
		}
		if (newScore > max_score) return true;
		return false;
	}

	/**
	 * Determine if the player is computer controlled.
	 * @param player
	 * @return true if it is controlled
	 */
	public boolean getAI(Player player) {
		return (player instanceof AIPlayer);
	}

	Random randy = new Random(new Date().getTime());

	/**
	 * determine the response for a situation
	 * using the assigned risk of the player
	 * @return response
	 */
	public String aiResponse(){
	    AIPlayer p = (AIPlayer) this.player;
	    return aiResponse(p,1200);
	}
	static final int RISK_RANGE = 16;
	public String aiResponse(Player p){
	    return aiResponse(p,1200);
	}
	public String aiResponse(Player p, int delay){
		AIPlayer player = (AIPlayer) p;
		try {
			sleep(delay);
		} catch (InterruptedException e){
			int fred = 4;
		}
		if (state == STATES.DRAWCARD) return "c";
		if (state == STATES.ROLLFIRST) return "d";
		if (state == STATES.WINNER) return "q";
		int prob = randy.nextInt(RISK_RANGE);       // [0 -> RISK_RANGE]  median <=RISK_RANGE/2
		if (debug) System.out.println("rand = "+prob);
		prob += player.getRisker()-4;
		if(debug) System.out.println("rand+player_risk = "+prob);
		// higher prob -> higher conservativeness
		if (state == STATES.ROLLED){
			if (mustbust || mustfill ||vengeance || doubletrouble>0) return "a";  // until I develop smarts to select dice just roll what's there
			int pDiff = probOffset();
			if(debug)System.out.println("state offset ="+pDiff);
			int rDiff = (int)(RISK_RANGE*(1.-rollRisk(6-dice.getReserved().length())));
			if(debug)System.out.println("dice offset ="+rDiff);
			int choice = prob+pDiff+rDiff;
			if(debug)System.out.println("choice param = "+choice +"; >"+RISK_RANGE/2 +"==> roll");
			if (prob+pDiff>RISK_RANGE/2) return "a";  // higher risk -> roll
			else return "b";
		}
		if (state == STATES.FILLED){
			if (mustbust || doubletrouble>0) return "a";
			int pDiff = probOffsetCV();
			if(debug)System.out.println("state offset ="+pDiff);
			int choice = prob+pDiff;
			if(debug)System.out.println("choice param = "+choice +"; >"+(RISK_RANGE/2 +1) +"==> continue");
			if (prob+pDiff>(RISK_RANGE/2 +1)) return "b";  // higher risk -> continue
			else return "a";         // score
		}
		if (state == STATES.DREWVENGEANCE){
			int pDiff = probOffsetCV();
			if(debug)System.out.println("state offset ="+pDiff);
			int choice = prob+pDiff;
			if(debug)System.out.println("choice param = "+choice +"; >"+(RISK_RANGE/2 +1) +"==> vengeance");
			if (prob+pDiff>(RISK_RANGE/2 +1)) return "b";  // higher risk -> take vengeance
			else return "a";         // draw again
		}

		return "q";
	}

	/**
	 * offset the probability
	 * to take into account:
	 *   the score that is at risk
	 *   the proximity of the player to the winning score
	 *   the proximity of the leader to the winning score
	 *   the proximity of the player to the leading player
	 *   the number of dice left to roll
	 */
	int probOffset() {
	    int proboff=0;
	    if (tempScore < 200) {
		    if(debug)System.out.println(tempScore+" is a dinky roll; take more risk");
	       	proboff+=1;
	    }
	    if (tempScore > 400){
		    if(debug)System.out.println(tempScore+" is a big roll; take less risk");
	       	proboff-=1;
	    }
	    if (runningScore+tempScore <200){
		    if(debug)System.out.println(runningScore+tempScore+" is a dinky score; take more risk");
	       	proboff+=2;
	    }
	    if (runningScore+tempScore > 800){
		if(debug)System.out.println(runningScore+tempScore+" is a big score; take less risk");
	       	proboff-=2;
	    }
	    if (max_score - player.getScore() <1000){
		    if(debug)System.out.println(player.getScore()+" is close to winning; take less risk");
	       	proboff -=1;
	    }
	    if (max_score - high_score <1000){
		    if(debug)System.out.println(" leader close to winning; take more risk");
	       	proboff +=1;
	    }
	    if (high_score - player.getScore() >1000){
		    if(debug)System.out.println(" you are far behind; take more risk");
	       	proboff +=1;
	    }
	    return proboff;
	}

	/**
	 * offset the probability
	 * to take into account:
	 *   the score that is at risk
	 *   the proximity of the player to the winning score
	 *   the proximity of the leader to the winning score
	 *   the proximity of the player to the leading player
	 *   the number of dice left to roll
	 */
	int probOffsetCV() {
		int proboff=0;
		if (max_score - player.getScore()-runningScore <1000){
			if(debug)System.out.println(player.getScore()+" is close to winning; take less risk");
			proboff -=1;
		}
		if (max_score - high_score <1000){
			if(debug)System.out.println(" leader close to winning; take more risk");
			proboff +=1;
		}
		if (high_score - player.getScore()-runningScore >1000){
			if(debug)System.out.println(" you are far behind; take more risk");
			proboff +=1;
		}
		return proboff;
	}

	/** probability of a scoring roll
	 *
	 * @param ndice number of dice to roll
	 * @return probability (0-> 1), 1 is sure thing
	 */
	private double rollRisk(int ndice)
	{
		double prob =1.-Math.pow(.66667,(double)ndice);
		if(debug) System.out.println("prob of success for "+ndice+" dice is "+prob);
		return (prob);
	}

	/**
	 * play the game automatically
	 */
	void aiInterface() {
		String stuff;
		//while
		if
		(aiResponder) {
			try {
				sleep(800);
			}
			catch (IllegalArgumentException e){
				int fred = 4;
			}
			catch (InterruptedException f){
				int fred = 5;
			}
			stuff = aiResponse(player);
			if (stuff.indexOf('q') >= 0) aiResponder = false;
			if (stuff.indexOf('a') >= 0) update("OPTION_A");
			if (stuff.indexOf('b') >= 0) update("OPTION_B");
			if (stuff.indexOf('c') >= 0) {
				if (debug)update("CARD "
						+stuff.substring(1));
				else update("CARD");
			}
			if (stuff.indexOf('d') >= 0) update("DICE "
					+stuff.substring(1));
			if (stuff.indexOf('m') >= 0) update("MAX_SCORE "
					+stuff.substring(1));
		}

	}

	void textInterface() {
		boolean notdone = true;
		Scanner sc=null;
		if (!aiResponder) {
			sc = new Scanner(System.in);
		}
		String stuff;
		while (notdone) {
			if (state == STATES.WINNER) {
				notdone = false;
				break;
			}
			if (!aiResponder) {
				stuff = sc.nextLine();
			}else{
				stuff = aiResponse(player);
			}
			if (stuff.indexOf('q') >= 0) notdone = false;
			if (stuff.indexOf('a') >= 0) update("OPTION_A");
			if (stuff.indexOf('b') >= 0) update("OPTION_B");
			if (stuff.indexOf('c') >= 0) {
				if (debug)update("CARD "
						+stuff.substring(1));
				else update("CARD");
			}
			if (stuff.indexOf('d') >= 0) update("DICE "
					+stuff.substring(1));
			if (stuff.indexOf('m') >= 0) update("MAX_SCORE "
					+stuff.substring(1));
		}

	}

	void tupdate(String s) {
	    System.out.println(s);
	}

	public Player getPlayer(){
	    return player;
	}
	
	/**
	 * Artificially set te states of the game
	 * for testing purposes
	 * STATE, mustbust,mustfil,vengeance,doubleTrouble
	 */
	public void setState(STATES state, boolean mb, boolean mf, boolean v, boolean dt,int ndice) {
	    this.state=state;
	    mustbust=mb;
	    mustfill=mf;
	    vengeance=v;
	    doubletrouble=dt?2:0;
		tSetND(ndice);
	}
	public void setState(STATES state, boolean mb, boolean mf, boolean v, boolean dt) {
		setState(state, mb, mf, v, dt, 6) ;
	}
	void tSetND(int nd){
		String ds = "";
		for (int ii=0;ii<nd;ii++) {
			if (ii<4)ds+="1";
			else ds+="5";
		}
		dice = new Dice(ds);
	}
	void tSetTR(int ts, int rs){
		tempScore = ts;
		runningScore = rs;
	}

	void stealthtextInterface() {
		boolean notdone = true;
		Scanner sc=null;
		if (!aiResponder) {
			sc = new Scanner(System.in);
		}
		String stuff;
		while (notdone) {
			if (state == STATES.WINNER) {
				notdone = false;
				break;
			}
			if (!aiResponder) {
				stuff = sc.nextLine();
			}else{
				stuff = aiResponse(player);
			}
			if (stuff.indexOf('q') >= 0) notdone = false;
			if (stuff.indexOf('a') >= 0) tupdate("OPTION_A");
			if (stuff.indexOf('b') >= 0) tupdate("OPTION_B");
			if (stuff.indexOf('c') >= 0) {
				if (debug)update("CARD "
						+stuff.substring(1));
				else tupdate("CARD");
			}
			if (stuff.indexOf('d') >= 0) tupdate("DICE "
					+stuff.substring(1));
			if (stuff.indexOf('m') >= 0) tupdate("MAX_SCORE "
					+stuff.substring(1));
		}

	}

	public static void main(String[] args) {
		boolean debug = false;
		//for(String each : args) System.out.println(each);
		String[] list = {"Clyde", "aiNancy"};
		int max = 2000;
		if (args.length >0){
			ArrayList<String> temp = new ArrayList<String>();
			for (int i=0; i<args.length;i++) {
				if (args[i].equals("-d")) debug = true;
				else if (args[i].equals("-p")) {
					temp.add(args[i++ + 1]);
				}
				else if (args[i].equals("-m")) {
					max = Integer.valueOf(args[i++ +1]);
				}
			}
			if (temp.size() > 0) list = temp.toArray(list);
		}
		if(debug)for(String each : list) System.out.println(list);
		//System.exit(0);
		FillRBustGame it = new FillRBustGame(list, max);
		it.debug = debug;
		it.textInterface();
	}

}
