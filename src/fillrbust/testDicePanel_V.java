package fillrbust;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.*;

/**
 * Displays the initial unset set of dice<br>
 * Sets the dice<br>
 * Gets which are selected<br>
 * Sets which are selected<br>
 * Sets action for each die
 */
class testDicePanel_V extends JFrame {
	JPanel controls;
	DicePanel_V diceP;
	String saveSelec;
	boolean swap = true;
	ArrayList<JToggleButton> dice;
	ReservedDice_V diceR;
	ArrayList<JLabel> diceL;
	DicePanel_V.LayoutOrientation layout = DicePanel_V.LayoutOrientation.HORIZONTAL;

	public testDicePanel_V() {
		this(true);
	}

	public testDicePanel_V(DicePanel_V.LayoutOrientation layout) {
		this(true, layout);
	}

	public testDicePanel_V(boolean active) {
		this(active, DicePanel_V.LayoutOrientation.VERTICAL);
	}

	public testDicePanel_V(boolean active, DicePanel_V.LayoutOrientation layout) {
		this.layout = layout;
		//JFrame frame = new JFrame("Play FillRBust");
		setTitle("Test dice display");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setForeground(Color.BLACK);

		// build dice panel
		System.out.println(System.getProperty("user.dir"));
		//diceP = new DicePanel("images/Dice/Medium");
		if (active) {
			diceP = new DicePanel_V("images/", layout);
			//System.out.println(diceP);
			dice = diceP.getDice();
			for (AbstractButton each : dice) {
				each.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent fred) {
						System.out.println("die pushed: " + fred.getActionCommand());
					}
				});
			}
		} else {
			diceR = new ReservedDice_V("images/", layout);
		}

		// put controls in panel
		controls = new JPanel();
		controls.setBackground(Color.BLACK);

		JButton optionA = new JButton("quit");
		JButton optionB = new JButton("Roll Dice");
		JButton optionC = new JButton("get Sel");
		controls.add(optionA);
		controls.add(optionB);
		controls.add(optionC);

		// place the two panels:
		// put dice on top
		Dimension dim;
		if (active) {
			dim = diceP.getPSize();
		} else {
			dim = diceR.getPSize();
		}
		int fWid = (int) dim.getWidth();
		int pHit = (int) dim.getHeight();
		if (active) {
			diceP.setBounds(0, 0, fWid, pHit);
		} else {
			diceR.setBounds(0, 0, fWid, pHit);
		}
		// show control buttons under
		controls.setBounds(0, pHit, fWid, 100);
		setSize(fWid, 100 + pHit);
		getContentPane().add(controls);
		if (active) {
			getContentPane().add(diceP);
		} else {
			getContentPane().add(diceR);
		}

		// add controls for buttons
		optionA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent fred) {
				System.exit(1);
			}
		});
		if (active) {
			optionB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent fred) {
					diceP.setDice("111355", "111011");
					diceP.setScore("1010");
				}
			});
		} else {
			optionB.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent fred) {
					diceR.setDice("111355");
					diceR.setScore("1010");
				}
			});
		}
		if (active) {
			optionC.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent fred) {
					if (swap) {
						saveSelec = diceP.getSelected();
						System.out.println(saveSelec);
						diceP.setScore("1100");
					} else {
						String notSel = "";
						for (char cc : saveSelec.toCharArray()) {
							if (cc == '1') notSel += '0';
							else notSel += '1';
						}
						diceP.setSelected(notSel);
						diceP.setScore("200");
					}
					swap = !swap;
				}
			});
		} else {
		}

		//controls.setLayout(null);
		setLayout(null);
		setVisible(true);
	}

	public testDicePanel_V(String set, String selected) {
		this(DicePanel_V.LayoutOrientation.HORIZONTAL);
		diceP.setDice(set, selected);
		diceP.setScore("500");
	}

	public testDicePanel_V(boolean active, String set) {
		this(active);
		diceR.setDice(set);
		diceR.setScore("500");
	}

	public testDicePanel_V(boolean active, String set, String selected) {
		this(active);
		if (active) {
			diceP.setDice(set, selected);
			diceP.setScore("500");
		} else {
			diceR.setDice(set);
			diceR.setScore("500");
		}
	}

	/**
	 * Usage: testDicePanel_V [options]<br>
	 * where options are:<br>
	 * [set mask]  dice are selectable;<br>
	 * set is the string representing the dice values (1-6),<br>
	 * mask represents initial selection: 1 - selected, 0 - not<br>
	 * -r [set] dice are not selectable<br>
	 */
	public static void main(String args[]) {
		String set, selected;
		//if (args.length == 2)
		if (args.length > 0) {
			if (args[0].charAt(0) == '-') {
				System.out.println("args");
				if (args[0].equals("-r")) {
					System.out.println("-r");
					if (args.length > 1) {
						System.out.println(" -r args");
						set = args[1];
						java.awt.EventQueue.invokeLater(new Runnable() {
							public void run() {
								new testDicePanel_V(false, set).setVisible(true);
							}
						});
					} else {
						java.awt.EventQueue.invokeLater(new Runnable() {
							public void run() {
								new testDicePanel_V(false).setVisible(true);
							}
						});
					}
				}
			} else {
				set = args[0];
				selected = args[1];
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						new testDicePanel_V(set, selected).setVisible(true);
					}
				});
			}
		} else {
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new testDicePanel_V().setVisible(true);
				}
			});
		}
	}

}
