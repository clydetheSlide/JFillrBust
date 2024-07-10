package fillrbust;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class testUserPanel extends JFrame{
    static int HEIGHT = 400;
    static int WIDTH = 900;
    static String[] changees = {"George","Fred","Ethyl","Ren"};
    int pname=0;
    static int[] scores = {10,1200,500,150,3};
    int pscores=0;
    UserPanel currentPanel;
    JPanel users;
    int player;

    ArrayList<UserPanel> players = new ArrayList<UserPanel>(4);

      public testUserPanel() {
           setTitle("test UserPanel");
           setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           setSize(WIDTH,HEIGHT);
           JPanel left = new JPanel();
		left.setBounds(0,0,(int)(WIDTH*.3),HEIGHT);
	   users = new JPanel();
		users.setBounds((int)(WIDTH*.3),0,(int)(WIDTH*.7),HEIGHT);
		//users.setOpaque(true); users.setBackground(new Color(255,0,255));
	    // top: w=900, h=300
          JButton quit = new JButton("quit");
          JButton data1 = new JButton("data 1");
          JButton data2 = new JButton("data 2");
          JButton data3 = new JButton("data 3");
	  left.add(quit);
	  left.add(data1);
	  left.add(data2);
	  left.add(data3);
	  // now the purpose of this test
	  UserPanel clyde = new UserPanel("Clyde", 0);
	  users.add(clyde);
	  players.add(clyde);
	  UserPanel nancy = new UserPanel("Nancy",1);
	  users.add(nancy);
	  players.add(nancy);
	  UserPanel audrey = new UserPanel("Audrey",2);
	  users.add(audrey);
	  players.add(audrey);
	  currentPanel = nancy;
	  player = 0;
	  players.get(player).toggleTurn();

          getContentPane().add(left);
          getContentPane().add(users);
          quit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    System.exit(1);
                }
          });
          data1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    //System.out.println("poked button1");
					//updateUserName();
	                summary();
                }
          });
          data2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    //System.out.println("poked button2");
		    updateScore();
                }
          });
          data3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    //System.out.println("poked button2");
		    addPlayer();
                }
          });

		  for (UserPanel each: players) {
			  each.myButton().addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent modme)
				  {
					  //System.out.println("user name button");
					  //System.out.println(modme);
					  updateUserName(each);
				  }
			  });
		  }
	  
	  setLayout(null);
	  setVisible(true);
     }

	/** change name of the current player */
	void updateUserName(UserPanel up){
		pname=(pname+1)%(changees.length);
		up.changeName(changees[pname]);
	}

	/** change name of the current player */
     void updateUserName(){
		    currentPanel.changeName(changees[pname]);
			updateUserName(currentPanel);
     }

	 /** add a new member to the set of players */
     void addPlayer(){
	  UserPanel somebody = new UserPanel("Todd", players.size());
	  users.add(somebody);
	  players.add(somebody);
	     somebody.myButton().addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent modme)
		     {
			     //System.out.println("user name button");
			     //System.out.println(modme);
			     updateUserName(somebody);
		     }
	     });
     }

	 /** Change score of current player
	  * and switch to next player
	  * */
     void updateScore() {
	 pscores=(pscores+1)%(scores.length);
	 players.get(player).toggleTurn();
	 player=(player+1)%(players.size());
	 //currentPanel.newScore(scores[pscores]);
	 players.get(player).toggleTurn();
	 players.get(player).newScore(scores[pscores]);
	 players.get(player).addScore(scores[pscores]);
     }

	/** summarize users' scores
	 */
	public void summary() {
		for (UserPanel each:players) {
			System.out.println(each.getUName()+" has a score of "+each.getScore());
		}
	}

	public static void main(String[] args){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new testUserPanel().setVisible(true);
            }
        });
     }
}
