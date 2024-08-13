package fillrbust;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Use the JDialog feature of the javax library
 *  to display a message in a seperate window.
 */
class MyDialog extends JDialog
	      // implements ActionListener
	      //,PropertyChangeListener
{
    private JTextArea textarea;
	public MyDialog(Frame aFrame, String words) {
		this(aFrame,words,"Help Box");
	}
    public MyDialog(Frame aFrame, String words, String title) {
        super(aFrame, title, true);

		int rows=2;
		int columns = 25;
	    if (words.length() > 350) {
		    rows=20; columns=60;
	    }
	    else if (words.length() > 200) {
		    rows=7; columns=30;
	    }
		else if (words.length() > 50) {
			rows=5; columns=20;
		}
	textarea = new JTextArea(words,rows,columns);
	   textarea.setLineWrap(true); textarea.setWrapStyleWord(true);
	JScrollPane scrollable = new JScrollPane(textarea);


	JButton dismiss = new JButton("dismiss");
          dismiss.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent fred) {
                    clearAndHide();
                }
          });

	add(scrollable);
	add(dismiss, BorderLayout.SOUTH);
	//setContentPane(scrollable);
	//setContentPane(dismiss);
    }

	public void setFont(int size){
		//textarea.setFont(new Font("Helvetica",Font.BOLD,size));
		//textarea.setFont(new Font("Paint Boy",Font.BOLD,size));
		textarea.setFont(new Font("Gyneric 3D BRK",Font.BOLD,size));

	}

    public void clearAndHide() {
	textarea.setText(null);
	setVisible(false);
    }

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				final JFrame master=new JFrame();
				master.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                master.setSize(70,70);
				master.setLayout(new FlowLayout());
				JButton one = new JButton("push");
				JButton two = new JButton("pull");
				JButton tre = new JButton("else");
				master.getContentPane().add(one);
				master.getContentPane().add(two);
				master.getContentPane().add(tre);
				master.setVisible(true);
				one.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent uno) {
						MyDialog seeit = new MyDialog(master, "just something");
						seeit.pack();
						seeit.setVisible(true);
					}
				});
				tre.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String s = (String)JOptionPane.showInputDialog(
								master,"Enter a new number: "
								//,JOptionPane.PLAIN_MESSAGE,
								//icon,null, "old"
								);
						System.out.println(s);
					}
				});
			}
			});
	}
}
