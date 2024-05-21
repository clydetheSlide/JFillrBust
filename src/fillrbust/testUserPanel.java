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
	  UserPanel clyde = new UserPanel("Clyde");
	  users.add(clyde);
	  players.add(clyde);
	  UserPanel nancy = new UserPanel("Nancy");
	  users.add(nancy);
	  players.add(nancy);
	  UserPanel audrey = new UserPanel("Audrey");
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
		    updateUserName();
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

	  
	  setLayout(null);
	  setVisible(true);
     }

     void updateUserName(){
		    pname=(pname+1)%(changees.length);
		    currentPanel.changeName(changees[pname]);
     }

     void addPlayer(){
	  UserPanel somebody = new UserPanel("Todd");
	  users.add(somebody);
	  players.add(somebody);
     }

     void updateScore() {
	 pscores=(pscores+1)%(scores.length);
	 players.get(player).toggleTurn();
	 player=(player+1)%(players.size());
	 //currentPanel.newScore(scores[pscores]);
	 players.get(player).toggleTurn();
	 players.get(player).newScore(scores[pscores]);
	 players.get(player).addScore(scores[pscores]);
     }

     public static void main(String args[]){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new testUserPanel().setVisible(true);
            }
        });
     }
}
