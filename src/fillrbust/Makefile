SOURCE = AIPlayer.java \
Cards.java \
Dice.java \
FBConfig.java \
MyDialog.java \
Player.java \
UserPanel.java \
DicePanel.java

APPSOURCE =FillRBustGame.java \
FillrBustTextui.java \
FillrBustGui.java

TESTSOURCE = testCards.java \
testDice.java \
testDicePanel.java \
testPlayer.java \
testProbAI.java \
testUserPanel.java

#OUT = ../../out
OUT = .
OUTPATH = $(OUT)/fillrbust
PARTS = $(OUTPATH)/$(SOURCE:.java=.class)
#PARTS = $(SOURCE:.java=.class)
TESTS = $(OUTPATH)/$(TESTSOURCE:.java=.class)

.java.class:
	javac -d $(OUT) $*.java

#parts:$(PARTS)
parts:
	javac -d $(OUT)  Player.java 
	javac -d $(OUT)  AIPlayer.java 
	javac -d $(OUT)  Cards.java 
	javac -d $(OUT)  Dice.java 
	javac -d $(OUT)  FBConfig.java 
	javac -d $(OUT)  MyDialog.java 
	javac -d $(OUT)  UserPanel.java 
	javac -d $(OUT)  DicePanel.java

tests:$(TESTSOURCE)
	javac -d $(OUT) $(TESTSOURCE) 

app:$(APPSOURCE)
	javac -d $(OUT) $(APPSOURCE)

jar:fillrbust.jar
	make fillrbust.jar

fillrbust.jar: $(OUT)/FillrBustGui.class
	jar cfe fillrbust.jar fillrbust.FillrBustGui fillrbust/*
