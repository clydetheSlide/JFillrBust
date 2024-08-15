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
 *  Display the initial unset set of dice<br>
 *  Set the dice<br>
 *  Get which are selected<br>
 *  Set which are selected<br>
 *  Set action for each die
 */

class testDicePanel extends JFrame{
    JPanel top;
    DicePanel diceP;
    String saveSelec;
    boolean swap = true;
    ArrayList<JToggleButton> dice;

      public testDicePanel() {
           //JFrame frame = new JFrame("Play FillRBust");
           setTitle("Play FillRBust");
           setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           setSize(700,180);
	   setBackground(Color.BLACK);
	    // top: w=900, h=250
           top = new JPanel();
		top.setBounds(0,0,700,80);
	  top.setBackground(Color.BLACK);

	  System.out.println(System.getProperty("user.dir"));
	   diceP = new DicePanel("images/Dice/Medium");
		diceP.setBounds(0,80,700,70);
	    //System.out.println(diceP);
	  dice = diceP.getDice();
	  for (AbstractButton each: dice){
	      each.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    System.out.println("die pushed: "+fred.getActionCommand());
                }
              });
	  }

          JButton optionA = new JButton("quit");
          JButton optionB = new JButton("Roll Dice");
          JButton optionC = new JButton("get Sel");
	  top.add(optionA);
	  top.add(optionB);
	  top.add(optionC);

          getContentPane().add(top);
          getContentPane().add(diceP);

          optionA.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    System.exit(1);
                }
          });
          optionB.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
		    diceP.setDice("1113 55","1110011");
                }
          });
          optionC.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
		    if (swap){
			saveSelec = diceP.getSelected();
			System.out.println(saveSelec);
		    }else{
			String notSel = "";
			for (char cc : saveSelec.toCharArray()){
			    if (cc=='1')notSel+='0';
			    else notSel+='1';
			}
			diceP.setSelected(notSel);
		    }
		    swap=!swap;
                }
          });

	  //top.setLayout(null);
	  setLayout(null);
          setVisible(true);
     }
      public testDicePanel(String set, String selected) {
	  this();
	  diceP.setDice(set,selected);
     }

      public static void main(String args[]){
	  String set, selected;
	  if (args.length == 2){
		set = args[0];
		selected = args[1];
	    java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
		    new testDicePanel(set,selected).setVisible(true);
		}
	    });
	  }
	  else {
	    java.awt.EventQueue.invokeLater(new Runnable() {
		public void run() {
		    new testDicePanel().setVisible(true);
		}
	    });
	  }
      }
	  
}
