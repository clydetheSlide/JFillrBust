# not a very smart Makefile

whole:
	javac -d .  FillrBustGui.java FillRBustGame.java
	#javac -d .  FillrBustGui.java

parts:
	make gameparts
	make guiparts

gameparts:
	javac -d .  Player.java
	javac -d .  AIPlayer.java
	javac -d .  Cards.java
	javac -d .  Dice.java
	javac -d .  FBConfig.java

guiparts:
	javac -d .  UserPanel.java
	javac -d .  DicePanel_V.java
	javac -d .  ReservedDice_V.java
	javac -d .  MyDialog.java

tests:
	javac -d .  testDicePanel.java
	javac -d .  testDicePanel_V.java
	javac -d .  testUserPanel.java
	javac -d .  testHorizLayout.java

jar:
	jar cfe fillrbust.jar fillrbust.FillrBustGui fillrbust/*

doc:
	javadoc -d ./docs -cp fillrbust -package *.java
