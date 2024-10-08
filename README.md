# FillRBust
FillRBust is a card and dice game I have enjoyed with real cards and dice.
As a silly exercize I originally captured the process of playing it in a [Python script](https://github.com/clydetheSlide/FillrBust),
then built a GUI with TKinter.
This repository is a rewrite in Java using Swing and AWT.

## Running the code
From the command line, without any parameters, without a config file, ie
> <prompt\> java -jar <path_to_jar>

The initial playing screen looks like this.

![initial game screen](README_dir/ready2play.jpg)

After turning over a card and rolling the dice, one would be presented with a screen like this.

![game screen 2](README_dir/playing.jpg)

After selecting just the "1" and rolling again, the "1" is fenced off
and the remaining dice are rolled as shown in this screen.

![game screen 3](README_dir/playing2.jpg)

Rules of the game and instructions on how to use the graphical user interface to play it are available from the 'Help' menu. 

Your name probably is not Myself and you may even want to name your opponent something
other than aiFoe. And you may want to play to 10000 points.
And you may prefer the vertical layout.
Command line parameters are available to control that,
> <prompt\> java -jar <path_to_jar> -p YourName -p aiOpponent -m 10000 -V

![game screen 4](README_dir/layoutV.gif)

Any number of players, interactive or computer driven, can be declared.
The same information and more is read from a config file.
The program looks for .fillrbustrc in the directory from where it is started.
An example is provided. It is a simple ASCII file,
so it is easy to modify the appropriate field(s).
