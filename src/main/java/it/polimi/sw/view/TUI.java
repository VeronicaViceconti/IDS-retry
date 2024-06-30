package it.polimi.sw.view;

import it.polimi.sw.model.*;
import it.polimi.sw.network.Message.ViewMessage.*;
import it.polimi.sw.network.Message.serverMessage.ErrorType;
import it.polimi.sw.model.Card;
import it.polimi.sw.model.Objective;
import it.polimi.sw.model.Player;
import com.vdurmont.emoji.EmojiParser;
import java.awt.geom.Point2D;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * This class represents a text-based user interface (TUI) for a client application.
 * It extends the `View` class and provides functionalities
 * for displaying game information, receiving user input, and potentially interacting with the server.
 */


public class TUI extends  View {
    private final PrintStream output = System.out;
    private final Scanner input;


    private ArrayList<Card> backHand;
    private ArrayList<Card> frontHand;
    private boolean flippedChosen = false; // if true, then place the card from back. if false, then from front

    private final ArrayList<String> chatMessagesStorage;
    /**
     * true may show, no other important prints now
     * false. wait
     */
    private boolean mayShowChat;
    private ArrayList<Objective> allObjectives;

    private ArrayList<Card> timeline;


    String BLUE = "\033[0;34m";

    String RESET = "\033[0m"; // Reset to default color

    String RED = "\u001B[31m";
    /**
     * Default constructor for the TUI class.
     * This constructor likely initializes the scanner for user input and internal data structures.
     */


    public TUI() {
        super();


        input = new Scanner(new InputStreamReader(System.in));


        backHand = new ArrayList<>();
        frontHand = new ArrayList<>();
        chatMessagesStorage = new ArrayList<>();


        availablePositions = new ArrayList<>();
        availablePositions.add(new Point2D.Double()); // 0,0
        timeline = new ArrayList<>();

        allObjectives = new ArrayList<>();
    }

    @Override
    public void addToTimeLine(Card timeline) {
        this.timeline.add(timeline);
    }

    public ArrayList<Card> getTimeline() {
        return timeline;
    }

    /**
     * Disables chat display or processing in the TUI.
     * This method is likely called when the TUI needs to prevent displaying or processing chat messages,
     *  perhaps due to another ongoing operation.
     **/
    @Override
    public void chatWait() {
        mayShowChat = false;
    }


    /**
     * Enables chat display or processing in the TUI.
     * This method is likely called when the TUI can resume displaying or processing chat messages
     *  after a previous wait state.
     *
     * @Override
     */
    @Override
    public void chatUnblockWait() {
        mayShowChat = true;
    }

    @Override
    public void createTabbedManuscripts(ArrayList<String> names) {
        //not used in tui
    }


    /**
     * done
     * print all the cards in hand in one line
     * first it prints all the upper lines of all the cards, enter, middle lines and so on.
     * @param hand hand
     */
    @Override
    public void showPlayerHand(ArrayList<Card> hand) {
        System.out.println("\nCards in hand:\n"); //used for hands of all players


        for(Card c: hand){
            //System.out.println("\nCARTA HAND AL CLIENT: "+c.toString()+"\n");
            topSingleCard(c, true, true);
            System.out.print("   ");
        }
        System.out.print("\n");


        for(Card c: hand){
            middleSingleCard(c);
            System.out.print("   ");
        }
        System.out.print("\n");


        for(Card c: hand){
            bottomSingleCard(c, true, true);
            System.out.print("   ");
        }
        System.out.print("\n");


        //printSpaceOneCard(1);
        for(int i = 1; i <= hand.size(); i++){
            printSpaceOneCard(4);
            System.out.print(i);
            printSpaceOneCard(5);
        }


        System.out.println();
    }


    /**
     * prints all the manuscript starting from the "highest" card and goes down line by line
     * prints the coordinates on left and in the bottom line.
     * @param map map
     */
    @Override
    public void showPlayerTable(HashMap<Card, Integer[]> map) {
        System.out.println("Manuscript: ");


        int mostUp = map.entrySet()
                .stream()
                .max(Comparator.comparingInt(entry -> entry.getValue()[1]))
                .map(entry -> entry.getValue()[1])
                .orElse(-80);
        //System.out.println("A");
        int mostBottom = map.entrySet()
                .stream()
                .min(Comparator.comparingInt(entry -> entry.getValue()[1]))
                .map(entry -> entry.getValue()[1])
                .orElse(80);
        //System.out.println("B");
        int mostLeft = map.entrySet()
                .stream()
                .min(Comparator.comparingInt(entry -> entry.getValue()[0]))
                .map(entry -> entry.getValue()[0])
                .orElse(-80);
        //System.out.println("C");
        //ad the first line topSingle of the top line
        System.out.print("   "); // to adjust according to i
        firstLine(mostUp, mostLeft, map);


        for (int i = mostUp; i >= mostBottom; i--) {


            if(i>=0 && i<10){ // i 1 digit
                System.out.print(i);
                System.out.print("  "); // to adjust according to coordinates line
                middleCalculus(i, mostLeft,map);


                System.out.print("   "); // to adjust according to coordinates line
                lowTopCalculus(i, mostLeft, map); // bottom line intersects the top line of the next row
            }else if((i<80 && i>9) || (i<0 && i>-80) ){ //i of 2 digits
                System.out.print(i);
                System.out.print(" "); // to adjust according to coordinates line


                //System.out.print(" "); // to adjust according to coordinates line


                middleCalculus(i, mostLeft,map);
                System.out.print("   "); // to adjust according to coordinates line
                lowTopCalculus(i, mostLeft, map); // bottom line intersects the top line of the next row
            }else { //3 digits
                System.out.print(i);
                middleCalculus(i, mostLeft, map);


                System.out.print("   "); // to adjust according to coordinates line
                lowTopCalculus(i, mostLeft, map); // bottom line intersects the top line of the next row
            }
        }


        int mostRight = map.entrySet()
                .stream()
                .max(Comparator.comparingInt(entry -> entry.getValue()[0]))
                .map(entry -> entry.getValue()[0])
                .orElse(-80);


        printSpaceOneCard(4);


        //coordinate line x
        for(int i = mostLeft; i <=mostRight; i++){
            System.out.print(i);
            printSpaceOneCard(7);
            if(i%2 == 0){
                System.out.print(" ");
            }
        }


    }


    /**
     * Prints the first line of cards in a specific layout for the TUI.
     * This method likely iterates through a map of cards and their positions,
     *  determining spacing and printing the top part of each card in the first line.
     *
     * @param alt  An alternate parameter whose purpose is not entirely clear from this snippet.
     * @param mostLeft  The X-coordinate of the leftmost card in the line.
     * @param map  A HashMap containing cards as keys and their integer position arrays as values.
     */
    private void firstLine(int alt, int mostLeft, HashMap<Card, Integer[]> map){
        Card searchedUpperCard = null;


        Integer prev = mostLeft;
        Integer currXUp = mostLeft;


        searchedUpperCard = nextCardToPrint(map, alt, -80);


        if(map.get(searchedUpperCard)[0] != mostLeft ){
            printSpaceOneCard(1);
        }


        currXUp = map.get(searchedUpperCard)[0];


        printSpaceOneCard(6 * (currXUp - mostLeft));


        while (searchedUpperCard != null){ // make enough space between cards
            currXUp = map.get(searchedUpperCard)[0];


            if(currXUp - prev > 0) { //it is not the most left card of the game
                //printSpaceOneCard(5*(currXUp - prev-1)+(currXUp - prev-2));
                //printSpaceOneCard(6*(currXUp - prev));
                //printSpaceOneCard(5);
                printSpaceOneCard(6*(currXUp-prev-2));




                for(int i = currXUp -prev; i>(currXUp-prev)/2; i--){
                    System.out.print(" ");
                }
            }


            //we arrived to the card we have to print
            topSingleCard(searchedUpperCard, !(searchedUpperCard.getCorner().get(0).getCovered()),
                    !(searchedUpperCard.getCorner().get(1).getCovered() ));
            printSpaceOneCard(5);


            prev = currXUp;
            searchedUpperCard = nextCardToPrint(map, alt, currXUp);


        }


        System.out.println();
    }


    /**
     * Prints the middle lines of cards in a specific layout for the TUI.
     * This method likely iterates through a map of cards and their positions,
     *  determining spacing and printing the middle part of each card.
     *
     * @param alt  An alternate parameter whose purpose is not entirely clear from this snippet.
     * @param mostLeft  The X-coordinate of the leftmost card in the line.
     * @param map  A HashMap containing cards as keys and their integer position arrays as values.
     */
    private void middleCalculus (int alt, int mostLeft, HashMap<Card, Integer[]> map){
        Card searchedCard = null;
        int prevX = mostLeft;
        Integer currX = mostLeft;


        searchedCard = nextCardToPrint(map, alt, -80);
        currX = map.get(searchedCard)[0];


        if(map.get(searchedCard)[0] != mostLeft ){
            printSpaceOneCard(1);
        }


        printSpaceOneCard(6 * (currX - mostLeft));


        while (searchedCard != null){ // make enough space between cards
            currX = map.get(searchedCard)[0];


            if(currX-prevX > 1) { //it is not the most left card of the game
                //printSpaceOneCard(6*(currX - mostLeft));
                //printSpaceOneCard(5);
                printSpaceOneCard(6*(currX-prevX-2));


                for(int i = currX -prevX; i>1; i--){
                    System.out.print(" ");
                }
            }


            //currX = map.get(searchedCard)[0];
            middleSingleCard(searchedCard);
            printSpaceOneCard(5);


            prevX = currX;
            searchedCard = nextCardToPrint(map, alt, currX);
        }
/*           if(currX-prevX > 1) { //it is not the most left card of the game
               //printSpaceOneCard(6*(currX - mostLeft));
               printSpaceOneCard(5);
               printSpaceOneCard(6*(currX-prevX-2));


               for(i = currX -prevX; i>0; i--){
                   System.out.print(" ");
               }
           }*/
        System.out.println();
    }


    /**
     *
     * @param alt altitude
     * @param mostLeft smallest x coordinate in the grid
     * @param map map
     */
    private void lowTopCalculus(int alt, int mostLeft, HashMap<Card, Integer[]> map) {
        Card searchedUpperCard;
        Card searchedLowerCard;
        boolean nextCard = true; //true up, false down


        Integer prev = mostLeft;
        int next = mostLeft;


        Integer currXUp = mostLeft;
        Integer currXDown = mostLeft;


        searchedUpperCard = nextCardToPrint(map, alt, -80);
        searchedLowerCard = nextCardToPrint(map, alt - 1, -80);
        if(searchedUpperCard == null){
            nextCard = false;
        }else if(searchedLowerCard == null){
            nextCard = true;
        }else{
        nextCard = map.get(searchedUpperCard)[0] < map.get(searchedLowerCard)[0];}

        int minDist; //dist till next card

        if(nextCard) {
            currXUp = map.get(searchedUpperCard)[0]; //cannot be null in the beginning
            minDist = currXUp - mostLeft;
            currXDown = (searchedLowerCard != null) ? map.get(searchedLowerCard)[0] : null; //will be null in the last row and might be null in the middle
        }else{
            currXDown = map.get(searchedLowerCard)[0];
            minDist = currXDown-mostLeft;
            currXUp = (searchedUpperCard != null) ? map.get(searchedUpperCard)[0] : null;
        }

/*        if (currXDown != null) {
            minDist = currXUp > currXDown ? currXDown : currXUp;
            minDist = minDist - mostLeft;
            //System.out.print(" ");
        }*/
        printSpaceOneCard(6 * (minDist)); //non ricordo perche


        for(int h = minDist; h>minDist/2; h--){
            System.out.print(" ");
        }

/*
        Integer[] coordinates = map.get(searchedUpperCard);
        boolean leftPresent = positionOccupied(map,coordinates[0]-1,coordinates[1]-1);
        boolean rightPresent = positionOccupied(map, coordinates[0]+1,coordinates[1]-1);
*/

        //they cannot be both null, since in each row there is at least one card
        while (searchedUpperCard != null || searchedLowerCard != null) { // make enough space between cards
            //if both null, then it would not enter in while loop
/*

            if (currXDown == null) {
                minDist = currXUp - prev;
                nextCard = true;
                next = currXUp;
            } else if (currXUp == null) {
                minDist = currXDown - prev;
                nextCard = false;
                next = currXDown;


            } else {
                next = Math.min(currXUp, currXDown);
                nextCard = currXUp <= currXDown;
                minDist = next - prev;
            }*/ //portarlo alla fine

            boolean L = true;
            boolean R = true;
            //we arrived to the card we have to print
            if (nextCard) { //next one is from the row above

                Integer[] coordinates = map.get(searchedUpperCard);
                boolean leftPresent = positionOccupied(map,coordinates[0]-1,coordinates[1]-1);
                boolean rightPresent = positionOccupied(map, coordinates[0]+1,coordinates[1]-1); // occupied = present = to control
                Card controlledCard = null;


                if(leftPresent){
                    for(Map.Entry<Card,Integer[]> entry: map.entrySet()){
                        if(entry.getValue()[0].equals(coordinates[0]-1) && entry.getValue()[1].equals(coordinates[1]-1)){
                            controlledCard = entry.getKey();
                            break;
                        }
                    }
                    if(this.findIndexOf(searchedUpperCard) < this.findIndexOf(controlledCard)){
                        L = false;
                    }
                }
                if(rightPresent){
                    for(Map.Entry<Card,Integer[]> entry: map.entrySet()){
                        if(entry.getValue()[0].equals(coordinates[0]+1) && entry.getValue()[1].equals(coordinates[1]-1)){
                            controlledCard = entry.getKey();
                            break;
                        }
                    }
                    if(findIndexOf(searchedUpperCard) < findIndexOf(controlledCard)){ //if the printed one was added later
                        R = false;
                    }
                }

                bottomSingleCard(searchedUpperCard,  L, R);
                currXUp = map.get(searchedUpperCard)[0];

                prev = currXUp;
/*
                searchedUpperCard = nextCardToPrint(map, alt, currXUp);
                if(currXDown!= null) {
                    searchedLowerCard = nextCardToPrint(map, alt - 1, currXDown);
                }
                currXUp = (searchedUpperCard != null) ? map.get(searchedUpperCard)[0] : null;
*/


            } else { //from the row under

                Integer[] coordinates = map.get(searchedLowerCard);
                boolean leftPresentD = positionOccupied(map,coordinates[0]-1,coordinates[1]+1);
                boolean rightPresentD = positionOccupied(map, coordinates[0]+1,coordinates[1]+1); // occupied = present = to control
                Card controlledCardD = null;

                if(leftPresentD){
                    for(Map.Entry<Card,Integer[]> entry: map.entrySet()){
                        if(entry.getValue()[0].equals(coordinates[0]-1) && entry.getValue()[1].equals(coordinates[1]+1)){
                            controlledCardD = entry.getKey();
                            break;
                        }
                    }
                    if(findIndexOf(searchedLowerCard) < findIndexOf(controlledCardD)){
                        L = false;
                    }
                }
                if(rightPresentD){
                    for(Map.Entry<Card,Integer[]> entry: map.entrySet()){
                        if(entry.getValue()[0].equals(coordinates[0]+1) && entry.getValue()[1].equals(coordinates[1]+1)){
                            controlledCardD = entry.getKey();
                            break;
                        }
                    }
                    if(findIndexOf(searchedLowerCard) < findIndexOf(controlledCardD)){ //if the printed one was added later
                        R = false;
                    }
                }

                topSingleCard(searchedLowerCard, L, R);
                currXDown = map.get(searchedLowerCard)[0]; //will be null in the last row and might be null in the middle

                prev = currXDown;
/*
                if(currXUp != null) {
                    searchedUpperCard = nextCardToPrint(map, alt, currXUp);
                }else{
                    currXDown = 100;//test se funziona
                }
                searchedLowerCard = nextCardToPrint(map, alt - 1, currXDown);
                currXDown = (searchedLowerCard != null) ? map.get(searchedLowerCard)[0] : null;

*/

            }

            searchedUpperCard = nextCardToPrint(map, alt, prev);
            searchedLowerCard = nextCardToPrint(map, alt - 1, prev);

            //to understand which one is the next one
            if(searchedUpperCard == null){
                nextCard = false; //next one is lower one
            }else if(searchedLowerCard == null){
                nextCard = true;
            }else {
                nextCard = map.get(searchedUpperCard)[0] < map.get(searchedLowerCard)[0];
            }

/*            if(currXDown != null && currXUp!= null){
                next = Math.min(currXUp, currXDown);
                minDist = next - prev;
            }else if (currXDown != null) {
                minDist = currXDown - prev;
            }
            else if (currXUp != null) {
                minDist = currXUp - prev;
            }*/

            if(nextCard){
                minDist = map.get(searchedUpperCard)[0] - prev;
            }else if (searchedLowerCard != null) {
                minDist = map.get(searchedLowerCard)[0] - prev;
            }else{ //mp mpre cards
                minDist = 0;
            }

            if(minDist > 1) { //it is not the most left card of the game
                printSpaceOneCard(5);
                printSpaceOneCard(6*(minDist-2));
                for(int k = minDist; k>minDist/2; k--){
                    System.out.print(" ");
                }
            }
        }


        System.out.println();
    }


    /**
     * @param map map
     * @param altitude y
     * @param moreThen x of the prevousely printed card
     * @return the next card in the line
     */
    private Card nextCardToPrint(HashMap<Card, Integer[]> map, int altitude, int moreThen){
        Card wantedCard = null;
        Integer smallestFirstElement = 80;


        for (Map.Entry<Card, Integer[]> entry : map.entrySet()) {
            Integer[] values = entry.getValue();
            if (values[1] == altitude && values[0] < smallestFirstElement && values[0] > moreThen) {
                smallestFirstElement = values[0];
                wantedCard = entry.getKey();
            }
        }


        //if returns null, then the line is over, I need to go to the new line
        return wantedCard;
    }


    /**
     * Prints a specific number of space blocks for card layout formatting in the TUI.
     *
     * @param numBlocks The number of space blocks to print.
     */
    private void printSpaceOneCard(int numBlocks){ //full card is 7 blocks. smtms we need only 5 or 6
        for(int i = 0; i<numBlocks; i++){
            System.out.print("  "); //to confirm
        }
    }
    /**
     * Prints the top part of a single card in the TUI using emoji characters.
     * This method likely considers the card type (ResourceCard, GoldCard, or InitialCard),
     * its color, corner information (resources, objects, visibility),
     * and side (front or back) to determine the appropriate emoji representation for printing.
     *
     * @param c The Card object to be printed.
     * @param L  A flag indicating if the left corner should be printed.
     * @param R  A flag indicating if the right corner should be printed.
     */


    private void topSingleCard(Card c, boolean L, boolean R){
        int i;
        String[] emojiColour= new String[5];
        emojiColour[0]=":broken_heart:";
        emojiColour[1]=":orange_heart:";
        emojiColour[2]=":blue_heart:";
        emojiColour[3]=":purple_heart:";
        emojiColour[4]=":green_heart:";
        String emojiCornerBack=":white_large_square:";
        String[] emojiCornerFront =new String[7];
        emojiCornerFront[0]=":mushroom:";
        emojiCornerFront[1]=":wolf:";
        emojiCornerFront[2]=":butterfly:";
        emojiCornerFront[3]=":four_leaf_clover:";
        emojiCornerFront[4]=":scroll:"; //manuscript
        emojiCornerFront[5]=":lower_left_fountain_pen:";//piuma
        emojiCornerFront[6]=":test_tube:";//provetta



        if(c instanceof ResourceCard){
            if(L){
                if(c.getSide()==2){
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                }else{
                    if(c.getCorner().getFirst().getResource()!=null){
                        switch (c.getCorner().getFirst().getResource()){
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                                break;
                        }
                    }else if(c.getCorner().getFirst().getObject()!=null){
                        switch (c.getCorner().getFirst().getObject()){
                            case MANUSCRIPT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                                break;
                            case QUILL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5])+" ");
                                break;
                            case INKWELL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                                break;
                        }
                    }else if(c.getCorner().getFirst().getVisible()){
                        System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                    }else{
                        switch (c.getcolour()){
                            case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                                break;
                            case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                                break;
                            case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                                break;
                            case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                        }
                    }


                }


            }
            switch (c.getcolour()){
                case "red": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));}
                    break;
                case "green": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));}
                    break;
                case "blue": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));}
                    break;
                case "purple": for(i=0;i<2;i++) {
                    System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                }
                break;
            }


            if(c.getPoints()!=0 && c.getSide()==1){
                //System.out.print("");
                System.out.print(" 1");
                //System.out.print(" ");
            }else{
                switch (c.getcolour()){
                    case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                        break;
                    case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                        break;
                    case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                        break;
                    case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                        break;
                }
            }


            switch (c.getcolour()){
                case "red": for(i=0;i<2;i++){/*if(i==0){System.out.print("");}*/ System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));}
                    break;
                case "green": for(i=0;i<2;i++){/*if(i==0){System.out.print("");}*/System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));}
                    break;
                case "blue": for(i=0;i<2;i++){/*if(i==0){System.out.print("");}*/System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));}
                    break;
                case "purple": for(i=0;i<2;i++){
                    /*if(i==0){System.out.print("");}*/System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));}
                        break;
            }


            if(R){
                if(c.getSide()==2){
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                }else{
                    if(c.getCorner().get(1).getResource()!=null){
                        switch (c.getCorner().get(1).getResource()){
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                                break;
                        }
                    }else if(c.getCorner().get(1).getObject()!=null){
                        switch (c.getCorner().get(1).getObject()){
                            case MANUSCRIPT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                                break;
                            case QUILL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5])+" ");
                                break;
                            case INKWELL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                                break;
                        }
                    }else if(c.getCorner().get(1).getVisible()){
                        System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                    }else{
                        switch (c.getcolour()){
                            case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                                break;
                            case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                                break;
                            case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                                break;
                            case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                        }
                    }
                }
            }


        }else if(c instanceof GoldCard){
            if(L) {
                if (c.getSide() == 2) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else if (c.getCorner().getFirst().getObject() != null) {
                    switch (c.getCorner().getFirst().getObject()) {
                        case MANUSCRIPT:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                            break;
                        case QUILL:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5])+" ");
                            break;
                        case INKWELL:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                            break;
                    }
                } else if (c.getCorner().getFirst().getVisible()) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else {
                    switch (c.getcolour()) {
                        case "red":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                            break;
                        case "green":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                            break;
                        case "blue":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                            break;
                        case "purple":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[3])); break;
                    }
                }
            }
            switch (c.getcolour()) {
                case "red":
                    System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                    break;
                case "green":
                    System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                    // System.out.print("");
                    break;
                case "blue":
                    System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                    //System.out.print("");
                    break;
                case "purple":
                    System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                    //System.out.print("");
                    break;
            }


            if(c.getPoints()!=0 && c.getSide()==1){
                switch (c.getPoints()){
                    case 1:
                        System.out.print(" 1");
                        break;
                    case 2:
                        System.out.print(" 2");
                        break;
                    case 3:
                        System.out.print(" 3");
                        break;
                    case 5:
                        System.out.print(" 5");
                        break;
                }
            }else{
                switch (c.getcolour()) {
                    case "red":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                        break;
                    case "green":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                        break;
                    case "blue":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                        break;
                    case "purple":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                        break;
                }
            }
            if(!(c.getPointsMultiply() == null || (c.getPointsMultiply().isEmpty() && c.getSide()==1))){
                switch(c.getPointsMultiply()){
                    case "QUILL": System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5]));
                        break;
                    case "MANUSCRIPT": System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                        break;
                    case "INKWELL":
                        System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                        break;
                    case "CORNER":System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                        break;
                }
            }else{
                switch (c.getcolour()) {
                    case "red":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                        break;
                    case "green":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                        break;
                    case "blue":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                        break;
                    case "purple":
                        System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                }
            }


            switch (c.getcolour()){
                case "red": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));}
                    break;
                case "green": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));}
                    break;
                case "blue": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));}
                    break;
                case "purple": for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));}
            }


            if (R) {
                if (c.getSide() == 2) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else if (c.getCorner().get(1).getObject() != null) {
                    switch (c.getCorner().get(1).getObject()) {
                        case MANUSCRIPT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4])); break;
                        case QUILL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5])+" "); break;
                        case INKWELL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6])); break;
                    }
                } else if (c.getCorner().get(1).getVisible()) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else {
                    switch (c.getcolour()) {
                        case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0])); break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4])); break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2])); break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3])); break;
                    }
                }
            }


        }else if(c instanceof InitialCard){
            if(L) {
                if (c.getCorner().getFirst().getResource() != null) {
                    switch (c.getCorner().getFirst().getResource()) {
                        case FUNGI:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                } else {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                }
            }

                for(i=0;i<5;i++){
                    System.out.print(EmojiParser.parseToUnicode(emojiColour[1]));
                }


            if(R){
                if(c.getCorner().get(1).getResource()!=null){
                    switch (c.getCorner().get(1).getResource()){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                }else{
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                }
            }
        }
    }


    /**
     * Prints the middle part of a single card in the TUI using emoji characters.
     * This method considers the card type (ResourceCard, GoldCard, or InitialCard)
     * and its side (front or back) to determine the appropriate emoji representation for printing.
     *
     * @param c The Card object to be printed.
     */
    private void middleSingleCard(Card c) {
        if (c instanceof GoldCard || c instanceof ResourceCard) {
            if (c.getcolour().equals("red")) {
                String emojiFront = ":broken_heart:";
                String emojiBack = ":mushroom:";


                if (c.getSide() == 1) {
                    int i;
                    for (i = 0; i < 7; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                } else {
                    int i;
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));


                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                }


            } else if (c.getcolour().equals("green")) {
                String emojiFront = ":green_heart:";
                String emojiBack = ":four_leaf_clover:";


                if (c.getSide() == 1) {
                    int i;
                    for (i = 0; i < 7; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                } else {
                    int i;
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                }


            } else if (c.getcolour().equals("blue")) {
                String emojiFront = ":blue_heart:";
                String emojiBack = ":wolf:";


                if (c.getSide() == 1) {
                    int i;
                    for (i = 0; i < 7; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                } else {
                    int i;
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                }


            } else if (c.getcolour().equals("purple")) {
                String emojiFront = ":purple_heart:";
                String emojiBack = ":butterfly:";


                if (c.getSide() == 1) {
                    int i;
                    for (i = 0; i < 7; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                } else {
                    int i;
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));
                    for (i = 0; i < 3; i++) {
                        System.out.print(EmojiParser.parseToUnicode(emojiFront));
                    }
                }


            }


        } else if (c instanceof InitialCard) {
            String emojiBack = ":orange_heart:";
            String[] emojiFront =new String[4];
            emojiFront[0]=":mushroom:";
            emojiFront[1]=":wolf:";
            emojiFront[2]=":butterfly:";
            emojiFront[3]=":four_leaf_clover:";


            if (c.getSide() == 1) { //front
                int i;
                for (i = 0; i < 7; i++) {
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));
                }
            } else { //back
                int i;
                for (i = 0; i < 2; i++) {
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));
                }






                switch(c.getPermanentResource().size()){
                    case 1:
                        System.out.print(EmojiParser.parseToUnicode(emojiBack));
                        switch (c.getPermanentResource().getFirst()){ //all of them have at least one
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiFront[1]));
                                break;
                        }
                        System.out.print(EmojiParser.parseToUnicode(emojiBack));


                        break;
                    case 2:
                        switch (c.getPermanentResource().getFirst()){ //all of them have at least one
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiFront[1]));
                                break;
                        }
                        System.out.print(EmojiParser.parseToUnicode(emojiBack));


                        switch (c.getPermanentResource().get(1)){
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiFront[1]));
                                break;
                        }
                        break;
                    case 3:
                        switch (c.getPermanentResource().getFirst()){ //all of them have at least one
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiFront[1]));
                                break;
                        }
                        switch (c.getPermanentResource().get(1)){
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiFront[1]));
                                break;
                        }
                        switch (c.getPermanentResource().get(2)){
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiFront[1]));
                                break;
                        }
                        break;


                    default:
                }


                for (i = 0; i < 2; i++) {
                    System.out.print(EmojiParser.parseToUnicode(emojiBack));
                }
            }


        }
    }


    /**
     * Prints the bottom part of a single card in the TUI using emoji characters.
     * This method considers the card type (ResourceCard, GoldCard, or InitialCard)
     * and its side (front or back) to determine the appropriate emoji representation for printing.
     *
     * @param c The Card object to be printed.
     * @param L Flag indicating whether to print the left corner of the card.
     * @param R Flag indicating whether to print the right corner of the card.
     */


    private void bottomSingleCard(Card c, boolean L, boolean R){
        int i;

        String[] emojiColour= new String[5];
        emojiColour[0]=":broken_heart:";
        emojiColour[1]=":orange_heart:";
        emojiColour[2]=":blue_heart:";
        emojiColour[3]=":purple_heart:";
        emojiColour[4]=":green_heart:";
        String emojiCornerBack=":white_large_square:";
        String[] emojiCornerFront =new String[7];
        emojiCornerFront[0]=":mushroom:";
        emojiCornerFront[1]=":wolf:";
        emojiCornerFront[2]=":butterfly:";
        emojiCornerFront[3]=":four_leaf_clover:";
        emojiCornerFront[4]=":scroll:"; //manuscript
        emojiCornerFront[5]=":lower_left_fountain_pen:";//piuma
        emojiCornerFront[6]=":test_tube:";//provetta




        if(c instanceof ResourceCard) {
            if (L) {
                if (c.getSide() == 2) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else {
                    if (c.getCorner().get(2).getResource() != null) {
                        switch (c.getCorner().get(2).getResource()) {
                            case FUNGI:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                                break;
                            case PLANT:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                                break;
                            case INSECT:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                                break;
                            case ANIMAL:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                                break;
                        }
                    } else if (c.getCorner().get(2).getObject() != null) {
                        switch (c.getCorner().get(2).getObject()) {
                            case MANUSCRIPT:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                                break;
                            case QUILL:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5]));
                                break;
                            case INKWELL:
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                                break;
                        }
                    } else if (c.getCorner().get(2).getVisible()) {
                        System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                    } else {
                        switch (c.getcolour()) {
                            case "red":
                                System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                                break;
                            case "green":
                                System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                                break;
                            case "blue":
                                System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                                break;
                            case "purple":
                                System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                                break;
                        }
                    }


                }


            }
            switch (c.getcolour()) {
                case "red":
                    for(i=0;i<5;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));}
                    break;
                case "green":
                    for(i=0;i<5;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));}
                    break;
                case "blue":
                    for(i=0;i<5;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));}
                    break;
                case "purple":
                    for(i=0;i<5;i++){System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));}
                    break;
            }
            if(R){
                if(c.getSide()==2){
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                }else{
                    if(c.getCorner().get(3).getResource()!=null){
                        switch (c.getCorner().get(3).getResource()){
                            case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                                break;
                            case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                                break;
                            case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                                break;
                            case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                                break;
                        }
                    }else if(c.getCorner().get(3).getObject()!=null){
                        switch (c.getCorner().get(3).getObject()){
                            case MANUSCRIPT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                                break;
                            case QUILL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5]));
                                break;
                            case INKWELL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                                break;
                        }
                    }else if(c.getCorner().get(3).getVisible()){
                        System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                    }else{
                        switch (c.getcolour()){
                            case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                                break;
                            case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                                break;
                            case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                                break;
                            case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3])); break;
                        }
                    }
                }
            }




        }else if(c instanceof GoldCard){
            if(L) {
                if (c.getSide() == 2) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else if (c.getCorner().get(2).getObject() != null) {
                    switch (c.getCorner().get(2).getObject()) {
                        case MANUSCRIPT:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4]));
                            break;
                        case QUILL:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5])+" ");
                            break;
                        case INKWELL:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6]));
                            break;
                    }
                } else if (c.getCorner().get(2).getVisible()) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else {
                    switch (c.getcolour()) {
                        case "red":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                            break;
                        case "green":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                            break;
                        case "blue":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                            break;
                        case "purple":
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[3])); break;
                    }
                }
            }
            if(c.getSide()==1){
                if(((GoldCard) c).getNecessaryResource().size()==5){
                    switch (((GoldCard) c).getNecessaryResource().getFirst()){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }


                    switch (((GoldCard) c).getNecessaryResource().get(1)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (((GoldCard) c).getNecessaryResource().get(2)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (((GoldCard) c).getNecessaryResource().get(3)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (((GoldCard) c).getNecessaryResource().get(4)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                }else if(c.getNecessaryResource().size()==4){


                    switch (c.getcolour()){
                        case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                            break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                            break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                            break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                            break;
                    }


                    switch (c.getNecessaryResource().getFirst()){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }


                    switch (c.getNecessaryResource().get(1)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (c.getNecessaryResource().get(2)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (c.getNecessaryResource().get(3)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }


                }else if(c.getNecessaryResource().size()==3){


                    switch (c.getcolour()){
                        case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                            break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                            break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                            break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                            break;
                    }


                    switch (c.getNecessaryResource().getFirst()){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }


                    switch (c.getNecessaryResource().get(1)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (c.getNecessaryResource().get(2)){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                    switch (c.getcolour()){
                        case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                            break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                            break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                            break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                            break;
                    }
                }


            }else if(c.getSide()==2) {
                for(i=0;i<5;i++){
                    switch (c.getcolour()){
                        case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0]));
                            break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4]));
                            break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2]));
                            break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3]));
                            break;
                    }
                }
            }
            if (R) {
                if (c.getSide() == 2) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else if (c.getCorner().get(3).getObject() != null) {
                    switch (c.getCorner().get(3).getObject()) {
                        case MANUSCRIPT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[4])); break;
                        case QUILL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[5])); break;
                        case INKWELL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[6])); break;
                    }
                } else if (c.getCorner().get(3).getVisible()) {
                    System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                } else {
                    switch (c.getcolour()) {
                        case "red": System.out.print(EmojiParser.parseToUnicode(emojiColour[0])); break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(emojiColour[4])); break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(emojiColour[2])); break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(emojiColour[3])); break;
                    }
                }
            }


        }else if(c instanceof InitialCard){
            if(L){
                if(c.getSide()==2){ //back
                    switch(c.getPermanentResource().size()){
                        case 1:
                            if(c.getCorner().get(2).getResource() == null){
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                            }else{
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2])); //insect
                            }
                            break;
                        case 2:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                            break;

                        case 3:
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[1]));
                            break;
                    }
                }else{
                    switch (c.getCorner().get(2).getResource()){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }


                }
            }


            for(i=0;i<5;i++){
                System.out.print(EmojiParser.parseToUnicode(emojiColour[1]));
            }


            if(R){
                if(c.getSide()==2){
                    switch(c.getPermanentResource().size()){
                        case 1:
                            if(c.getCorner().get(2).getResource() == null){
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                            }else{
                                System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0])); //fungi
                            }
                            break;
                        case 2:
                            System.out.print(EmojiParser.parseToUnicode(emojiCornerBack));
                            break;
                        case 3:
                            System.out.print(EmojiParser.parseToUnicode(emojiColour[1]));
                            break;
                    }
                }else{
                    switch (c.getCorner().get(3).getResource()){
                        case FUNGI: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[0]));
                            break;
                        case PLANT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[3]));
                            break;
                        case INSECT: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[2]));
                            break;
                        case ANIMAL: System.out.print(EmojiParser.parseToUnicode(emojiCornerFront[1]));
                            break;
                    }
                }
            }


        }


    }

    /**
     * done


     * to add if empty
     * print deck and two cards on the common table
     * first prints gold, then resource


     * if any parameter is null, that means that the deck is finished, then the showCommonTable does not print anything
     * @param faceDownGold deck
     * @param faceDownResource deck
     * @param faceupGold open gold
     * @param faceupResource open res
     */
    @Override
    public void showCommonTable(Card faceDownGold, Card faceDownResource, ArrayList<Card> faceupGold, ArrayList<Card> faceupResource) {
        System.out.println("Common table. Gold and Resource cards:");
        if(faceDownGold != null){
            topSingleCard(faceDownGold, true, true);
            printSpaceOneCard(2);
        }


        for(Card c: faceupGold){
            if(c != null){
                topSingleCard(c, true, true);
                printSpaceOneCard(2);
            }
        }
        System.out.println();


        if(faceDownGold != null) {
            middleSingleCard(faceDownGold);
            printSpaceOneCard(2);
        }


        for(Card c: faceupGold){
            if(c != null){
                middleSingleCard(c);
                printSpaceOneCard(2);
            }
        }
        System.out.println();


        if(faceDownGold != null) {
            bottomSingleCard(faceDownGold, true, true);
            printSpaceOneCard(2);
        }


        for(Card c: faceupGold){
            if(c != null){
                bottomSingleCard(c, true, true);
                printSpaceOneCard(2);
            }
        }
        System.out.println();
        System.out.println();


        //resource cards
        if(faceDownResource != null){
            topSingleCard(faceDownResource, true, true);
            printSpaceOneCard(2);
        }


        for(Card c: faceupResource){
            if(c != null){
                topSingleCard(c, true, true);
                printSpaceOneCard(2);
            }
        }
        System.out.println();


        if(faceDownResource != null) {
            middleSingleCard(faceDownResource);
            printSpaceOneCard(2);
        }


        for(Card c: faceupResource){
            if(c != null){
                middleSingleCard(c);
                printSpaceOneCard(2);
            }
        }
        System.out.println();


        if(faceDownResource != null) {
            bottomSingleCard(faceDownResource, true, true);
            printSpaceOneCard(2);
        }


        for(Card c: faceupResource){
            if(c != null){
                bottomSingleCard(c, true, true);
                printSpaceOneCard(2);
            }
        }
        System.out.println();
    }


    /**
     * Displays the common objectives in the text-based user interface (TUI).
     * This method modifies the order in which the objectives are displayed
     * for better visualization in a horizontal layout.
     *
     * @param obj An ArrayList containing the common objectives to be displayed.
     */
    @Override
    public void showCommonObjectives(ArrayList<Objective> obj) {


        allObjectives.addAll(obj);


        System.out.println("Common objectives: ");


        topSingleCardObjective(obj.getFirst());
        printSpaceOneCard(2);
        topSingleCardObjective(obj.get(1));
        System.out.print("\n");


        middleSingleCardObjective(obj.getFirst());
        printSpaceOneCard(2);
        middleSingleCardObjective(obj.get(1));
        System.out.print("\n");


        bottomSingleCardObjective(obj.getFirst());
        printSpaceOneCard(2);
        bottomSingleCardObjective(obj.get(1));
        System.out.print("\n");
    }


    /**
     * Displays the available positions on the game board to the user in the text-based user interface (TUI).
     * The method formats the output to show a maximum of 10 positions per line for better readability.
     */
    @Override
    public void showAvailablePositions() {
        System.out.println("These are possible positions:");
        int count = 0;


        for (Point2D point : availablePositions) {
            System.out.print("(" + (int)point.getX() + ", " + (int)point.getY() + ")");
            count++;


            if (count % 10 == 0) {
                System.out.println(); // max 10 positions per line for readability
            } else {
                System.out.print("; ");
            }
        }


        if (count % 10 != 0) { //new line at the end
            System.out.println();
        }
    }


    /**
     * Displays the player's private objective in the TUI.
     *
     * @param obj The Objective object representing the player's private objective.
     */
    @Override
    public void showPrivateObjective(Objective obj) {
        System.out.println("Your private objective: ");
        topSingleCardObjective(obj);
        System.out.print("\n");
        middleSingleCardObjective(obj);
        System.out.print("\n");
        bottomSingleCardObjective(obj);


    }

    /**
     * Displays a single game chat message in the TUI, considering a flag to control if chat messages can be shown.
     *
     * @param message The String message to be displayed in the chat.
     */
    @Override
    public void showGameChat(String message) {
        if(mayShowChat){
            showLastMex(); //all stored if present
            System.out.println(message);
        }else{
            //suppongo che solo un thread entra qua.
            chatMessagesStorage.add(message);
        }
    }


    @Override
    public void initGame() {//for GUI.


    }

    @Override
    public void initializePions(ArrayList<Pion> pions) {

    }



    /**
     * Welcome message
     */
    @Override
    public void showGameStart() {
        System.out.println("\n \n \n \n \n Welcome to CODEX NATURALIS!");
        System.out.println("Master the art of four elements, gather resources and create the strongest manuscript!");
        System.out.println("Here are the basic rules:");
        System.out.println();
        System.out.println("1. Each player starts with a set of cards and resources.");
        System.out.println("2. On your turn, you must perform two actions: play a card and draw a card.");
        System.out.println("3. Play cards to gain points or fulfill endgame objectives.");
        System.out.println("4. The game ends when a player reaches 20 points or if both decks are empty.");
        System.out.println("5. The player with the most points at the end of the game wins.");


        System.out.println();
        System.out.println("Let's get started and have fun! \n \n");
    }


    /**
     * points, resources, objects acquired by now
     * @param p player
     */
    @Override
    public void showPlayerState(Player p) {
        System.out.println("Nickname: "+p.getNickName()+", Points: "+p.getPoints());


        System.out.println("Number of Resource and Object:");
        printResourceOrObject(p,p.getNumOfResourceAndObject());
    }


    /**
     * process of drawing card, called by the UpdatePlayersReply.
     */
    @Override
    public void drawCard() {
        //table is already shown by message
        //same for hand
        //chat is not a problem. managed within mex


        System.out.println("Draw a card. \n(Follow one of the schemes: 'Draw, Facedown, TypeofDeck'; 'Draw, Faceup, TypeCard, numberofCard')");
        chatUnblockWait();
        try {
            readDrawCardInput();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Sets the game board data and displays it in the Text-based User Interface (TUI).
     *
     * @param fdG The face-down green card on the game board.
     * @param fdR The face-down red card on the game board.
     * @param fuG The ArrayList of face-up green cards on the game board.
     * @param fuR The ArrayList of face-up red cards on the game board.
     * @param commonObj The ArrayList of Objective objects representing the common objectives.
     */
    @Override
    public void setBoardData(Card fdG, Card fdR, ArrayList<Card> fuG, ArrayList<Card> fuR,ArrayList<Objective> commonObj) {
        showCommonTable(fdG,fdR, fuG,fuR);
        showCommonObjectives(commonObj);
    }
    /**
     * Adds points to a player's score and displays the updated score in the TUI.
     *
     * @param p The Player object whose score needs to be updated.
     * @param points The number of points to be added to the player's score.
     */
    @Override
    public void addPoints(Player p, int points) {
        int newPoints=p.getPoints()+points;
        p.setPoints(newPoints);
        System.out.print("Game over! You got: ");


        System.out.print(EmojiParser.parseToUnicode(":parking:")+":"+newPoints);
    }


    @Override
    public void addFlippedCard(ArrayList<Card> c) {
// not used in tui
    }


    /**
     * Informs the user that it is not their turn yet and suggests using the chat feature.
     * This method displays a message in the Text-based User Interface (TUI) indicating that the user needs to wait for their turn.
     * It also provides a hint about using the chat functionality by suggesting a specific format ("Chat. Message").
     */
    @Override
    public void notYourTurn() {
        System.out.println("It is not your turn yet, wait a bit more!" +
                "P.S.: try our chat (Follow the scheme: 'Chat. Message')");
    }


    @Override
    public void cardToFlip(String side) {
//not used in tui
    }
    /**
     * This method likely flips a card in the player's hand from back to front or vice versa.
     * It iterates through the backHand list to find a card with a matching ID and class to the card in the frontHand at the specified index.
     * If a match is found, the cards are swapped between the two lists.
     *
     * @throws RemoteException (presumably for remote communication errors)
     */
    private void cardToFlip() throws RemoteException {
/*        Card tmpold = frontHand.get(card - 1);


       for (int i = 0; i < backHand.size(); i++) {
           if (backHand.get(i).getId() == tmpold.getId() &&
                   backHand.get(i).getClass().equals(tmpold.getClass())) {
               Card tmpnew = backHand.get(i);


               frontHand.add(tmpnew);
               backHand.add(tmpold);


               backHand.remove(i);
               frontHand.remove(card - 1);


               break;
           }
       }*/


        if(flippedChosen){//user wants to see front
            flippedChosen = false;
            showPlayerHand(frontHand);


        }else{
            flippedChosen = true;
            showPlayerHand(backHand);
        }


/*        if(oldSide.equals("1")) { //la carta da girare è lato front, mando il retro
           this.addFlippedCard(backHand);
       }else {
           this.addFlippedCard(frontHand);
       }*/


    }


    /**
     * Prompts the user to choose their second private objective from two options.
     *
     * @param o1 The first Objective option.
     * @param o2 The second Objective option.
     * @throws RemoteException (presumably for remote communication errors)
     */
    @Override
    public void secObjtoChoose(Objective o1, Objective o2) {
        int lives = 0;


        System.out.println("\nChoose one of these objectives. Be wise, later you cannot change it!");
        System.out.println("Answer 1 or 2");


        topSingleCardObjective(o1);
        printSpaceOneCard(2);
        topSingleCardObjective(o2);
        System.out.print("\n");


        middleSingleCardObjective(o1);
        printSpaceOneCard(2);
        middleSingleCardObjective(o2);
        System.out.print("\n");


        bottomSingleCardObjective(o1);
        printSpaceOneCard(2);
        bottomSingleCardObjective(o2);
        System.out.print("\n");


        int enter = 0;
        boolean validInput = false;
        do{
            lives++;


            switch(lives) {
                case (1):
                    output.println(" Choose the objective you want [1 or 2].");
                    break;
                case(2):
                    output.println(" Don't let me ask twice! Choose the objective you want [1 or 2]. ");
                    break;
                case(3):
                    output.println(" 1 or 2?. One more wrong answer and I will choose for you!");
                    break;
                case(4):
                    output.println("I told you! Now I will choose the worse one!");
                    enter = (int) (Math.random() * 2) + 1;
                    break;
            }
            try {
                enter = Integer.parseInt(input.nextLine());
                System.out.println("Riga->"+enter);
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Per favore, inserisci 1 o 2!");
            }
            if (enter == 1 || enter == 2) {
                validInput = true;
            }else
                System.out.println("Input non valido. Per favore, inserisci 1 o 2.");


        }while(!validInput);


        notify(new SendingPrivateObjective(enter == 1? o1:o2));
        showPrivateObjective(enter == 1? o1:o2);
        System.out.println("\nI would  choose another one, but it is your game.\nWait until other players make their choice..");
        allObjectives.add(enter == 1? o1:o2);


    }


    /**
     * Notifies the player that it is their turn to play a card.
     *
     * @param man A map representing the current state of the game board, where keys are cards and values are arrays of integers representing resource values.
     * @param hand A list representing the player's hand, containing the cards the player can play.
     * @param handBack An optional list representing the player's back hand, containing cards the player cannot see.
     * @throws RemoteException If there is an error communicating with the remote game server.
     */
    @Override
    public void myTurnNotification(HashMap<Card, Integer[]> man, ArrayList<Card> hand,  ArrayList<Card> handBack) throws RemoteException {

        flippedChosen = false;


        if(hand != null && handBack != null){
/*            this.frontHand = new ArrayList<>(hand);
           this.backHand = new ArrayList<>(handBack);*/
            frontHand = hand;
            backHand = handBack;
        }


        if(!man.isEmpty())
            showPlayerTable(man);
        showPlayerHand(hand);
        showAvailablePositions();
        System.out.println("It's your turn! Choose a card from your hand and where do you want to place it.\n " +
                "Follow one of the schemes:'Place, numberOfTheCard, x, y'; 'Flip.'");
        chatUnblockWait();


        readPlayCardInput();
    }


    /**
     * Announces the winner(s) of the game.
     *
     * @param winner A list containing the players who have won the game.
     */
    @Override
    public void announceWinner(List<Player> winner) {
        if (winner.size() == 1) {
            System.out.println(winner.getFirst().getNickName() + " wins!");


        } else if (winner.size() > 1) {
            String winnerNames = winner.stream()
                    .map(Player::getNickName)
                    .collect(Collectors.joining(", "));
            System.out.println(winnerNames + " win!");
        }
    }
    /**
     * Updates the game state and informs the player about the played card and points.
     *
     * @param p The player who played the card.
     * @param c The card that was played.
     * @param x The x-coordinate of the played card's position on the game board (optional).
     * @param y The y-coordinate of the played card's position on the game board (optional).
     * @param numOfResourceAndObject A map containing the updated resource and object counts after playing the card.
     * @param hand An optional list representing the player's updated hand.
     * @param handBack An optional list representing the player's updated back hand.
     * @param points The player's current points after playing the card.
     * @throws RemoteException If there is an error communicating with the remote game server.
     */
    @Override
    public void updatePlayerPlayCard(Player p, Card c, Integer x, Integer y, HashMap<String, Integer> numOfResourceAndObject, ArrayList<Card> hand, ArrayList<Card> handBack, Integer points) throws RemoteException {
        if(hand != null && handBack != null){
            this.frontHand = new ArrayList<>(hand);
            this.backHand = new ArrayList<>(handBack);
        }


        System.out.println("dentro UpdatePlayerPlayCard"+frontHand.toString()+"\n"+backHand.toString());
        System.out.println("Updated manuscript: ");
        showPlayerTable(p.getMap());


        System.out.println(" \n Nice choice! Your points:" + points +"\n");
    }


    /**
     * Handles errors that occur during gameplay and informs the player.
     *
     * @param type The type of error encountered.
     * @throws RemoteException If there is an error communicating with the remote game server.
     */
    @Override
    public void dealWithError(ErrorType type) {
        switch (type) {
            case NOT_THE_RIGHT_MOMENT_TO_PLACE_CARD:
                System.out.println("Error: Draw card, not play card!\n");
                try {
                    readDrawCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case CARD_NOT_IN_HAND:
                System.out.println("Don't try to fool me! You don't have that card!\n");
                try {
                    readPlayCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case USED_CARD:
                System.out.println("Don't try to fool me! You have already used that card! \n");
                try {
                    readPlayCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case COVERED_TWO_CORNERS:
                System.out.println("You cannot put the card that way. Retry.\n");
                try {
                    readPlayCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case NO_CORNERS_COVERED:
                System.out.println("Card has to cover at least one corner in manuscript. Try a different position. \n");
                try {
                    readPlayCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }                break;
            case HIDDEN_CORNER_COVERED:
                System.out.println("A card cannot cover a hidden corner. Try a different position. \n");
                try {
                    readPlayCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case NOT_ENOUGH_RESOURCES:
                System.out.println("You don't have enough resources to put that card. Retry with another one. \n");
                try {
                    readPlayCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
/*            case INCORRECT_PARAMETRES:
               System.out.println("Error: INCORRECT_PARAMETRES. Turn off the game and go to study!");
               break;*/
/*            case NOT_YOUR_TURN:
               System.out.println("It is not your turn! Wait! \n");
               break;*/
            case PLAYER_HAS_3_CARDS:
                System.out.println("You already have so many cards! Don't be so greedy!\n");
                break;
            case EMPTY_DECK:
                System.out.println("The chosen deck is empty. Try another one. \n");
                try {
                    readDrawCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case INCORRECT_PARAMETRES_DRAWCARD:
                System.out.println("Something went wrong. Draw card again. \n");
                try {
                    readDrawCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case INCORRECT_PARAMETRES_PLACECARD:
                System.out.println("Something went wrong. Place card again. \n");
                try {
                    readDrawCardInput();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
            case NOT_THE_RIGHT_MOMENT_TO_DRAW_CARD:
                System.out.println("Stop making the mess! Relax and follow the flow! " +
                        "P.S.:it is not the greatest moment to place a card.");
                break;
            case NO_DESTINATION:
                System.out.println("Your private message did not reach the destination. I don't recognise the name");
        }
    }
    /**
     * Saves the player's back hand (cards they cannot see).
     *
     * @param handBack A list of cards representing the player's back hand.
     */
    @Override
    public void savePlayerHandBack(ArrayList<Card> handBack) {
        backHand = handBack;
    }


    /**
     * * Prints information about a single objective in a specific format.
     *
     * @param o The objective to print information about.
     */
    private  void topSingleCardObjective(Objective o){
        int i;
        if(o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ObjectFilling) || o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ResourceFilling)){
            System.out.print("");
            switch(o.getPoints()){
                case 2: //System.out.print(EmojiParser.parseToUnicode(":two:")); System.out.print("");
                    System.out.print(" 2");
                    break;
                case 3:  //System.out.print(EmojiParser.parseToUnicode(":three:")); System.out.print("");
                    System.out.print(" 3");
                    break;
            }
            //System.out.print(" ");
            for(i=0;i<4;i++){
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            }
        }else if(o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ThreeSameColourPlacing)){
            System.out.print("");
            switch(o.getPoints()){
                case (2):
                    System.out.print(" 2");
                    break;
                case (3):
                    System.out.print(" 3");
                    break;
            }
            //System.out.print(" ");
            if(o.getCondition().getFirstOrientationCornerCheck()==1){
                switch (o.getCondition().getColour1()){
                    case "red": System.out.print(EmojiParser.parseToUnicode(":broken_heart:")); break;
                    case "green": System.out.print(EmojiParser.parseToUnicode(":green_heart:")); break;
                    case "blue": System.out.print(EmojiParser.parseToUnicode(":blue_heart:")); break;
                    case "purple": System.out.print(EmojiParser.parseToUnicode(":purple_heart:")); break;
                }
                for(i=0;i<3;i++) {
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                }
            }else{
                for(i=0;i<2;i++) {
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                }
                switch (o.getCondition().getColour1()){
                    case "red": System.out.print(EmojiParser.parseToUnicode(":broken_heart:")); break;
                    case "green": System.out.print(EmojiParser.parseToUnicode(":green_heart:")); break;
                    case "blue": System.out.print(EmojiParser.parseToUnicode(":blue_heart:")); break;
                    case "purple": System.out.print(EmojiParser.parseToUnicode(":purple_heart:")); break;
                }
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            }


        }else{
            System.out.print("");
            switch(o.getPoints()){
                case 2:
                    System.out.print(" 2");
                    break;
                case 3:
                    System.out.print(" 3");
                    break;
            }
            //System.out.print(" ");


            switch(o.getCondition().getId()){ //piu brutto di originale, pero' funziona. bottom line e' piu bello
                case (5):
                    System.out.print(EmojiParser.parseToUnicode(":broken_heart:"));
                    for(i=0;i<3;i++) {
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    break;
                case(6):
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    System.out.print(EmojiParser.parseToUnicode(":green_heart:"));


                    for(i=0;i<2;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    break;
                case(7):
                    for(i=0;i<2;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    System.out.print(EmojiParser.parseToUnicode(":broken_heart:"));
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    break;
                case(8):
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    System.out.print(EmojiParser.parseToUnicode(":blue_heart:"));
                    for(i=0;i<2;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    break;
            }
        }
    }
    /**
     * Prints information about a single objective in a specific format for the middle section of the game board.
     *
     * @param o The objective to print information about.
     */
    private void middleSingleCardObjective(Objective o) {
        int i;
        if (o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ResourceFilling)){
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            switch(o.getCondition().getResourcesRequiredToObtainPoints().getFirst()){
                case PLANT: for(i=0;i<3;i++){System.out.print(EmojiParser.parseToUnicode(":four_leaf_clover:"));} break;
                case ANIMAL: for(i=0;i<3;i++){System.out.print(EmojiParser.parseToUnicode(":wolf:"));} break;
                case FUNGI: for(i=0;i<3;i++){System.out.print(EmojiParser.parseToUnicode(":mushroom:"));} break;
                case INSECT: for(i=0;i<3;i++){System.out.print(EmojiParser.parseToUnicode(":butterfly:"));} break;
            }
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));


        }else if(o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ObjectFilling) && o.getPoints()==2){
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            switch(o.getCondition().getObjectsRequiredToObtainPoints().getFirst()){
                case INKWELL: for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(":test_tube:"));} break;
                case QUILL: for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(":lower_left_fountain_pen:" + " "));} break;
                case MANUSCRIPT: for(i=0;i<2;i++){System.out.print(EmojiParser.parseToUnicode(":scroll:"));} break;
            }
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
        }else if(o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ObjectFilling) && o.getPoints()==3){
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            System.out.print(EmojiParser.parseToUnicode(":lower_left_fountain_pen:"+" "));
            System.out.print(EmojiParser.parseToUnicode(":test_tube:"));
            System.out.print(EmojiParser.parseToUnicode(":scroll:"));
            System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));


        }else{
            for(i=0;i<2;i++){
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            }
            switch(o.getCondition().getTypeOfObjective()){
                case TwoSameColourOneDiffersPlacing:
                    switch (o.getCondition().getColour1()){
                        case "red": System.out.print(EmojiParser.parseToUnicode(":broken_heart:")); break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(":green_heart:")); break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(":blue_heart:")); break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(":purple_heart:")); break;
                    }
                    break;
                case ThreeSameColourPlacing:
                    switch (o.getCondition().getColour1()){
                        case "red": System.out.print(EmojiParser.parseToUnicode(":broken_heart:")); break;
                        case "green": System.out.print(EmojiParser.parseToUnicode(":green_heart:")); break;
                        case "blue": System.out.print(EmojiParser.parseToUnicode(":blue_heart:")); break;
                        case "purple": System.out.print(EmojiParser.parseToUnicode(":purple_heart:")); break;
                    }
                    break;
            }


            for(i=0;i<2;i++){
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            }
        }
    }
    /**
     * Prints information about a single objective in a specific format for the bottom section of the game board.
     *
     * @param o The objective to print information about.
     */
    private void bottomSingleCardObjective(Objective o){
        int i;
        if (o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ObjectFilling) || o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ResourceFilling)){
            for(i=0;i<5;i++){
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            }
        }else if(o.getCondition().getTypeOfObjective().equals(ObjectiveTypes.ThreeSameColourPlacing)){
            if(o.getCondition().getFirstOrientationCornerCheck()==2){
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                switch (o.getCondition().getColour1()){
                    case "red": System.out.print(EmojiParser.parseToUnicode(":broken_heart:")); break;
                    case "green": System.out.print(EmojiParser.parseToUnicode(":green_heart:")); break;
                    case "blue": System.out.print(EmojiParser.parseToUnicode(":blue_heart:")); break;
                    case "purple": System.out.print(EmojiParser.parseToUnicode(":purple_heart:")); break;
                }
                for(i=0;i<3;i++){
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                }
            }else{
                for(i=0;i<3;i++){
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                }
                switch (o.getCondition().getColour1()){
                    case "red": System.out.print(EmojiParser.parseToUnicode(":broken_heart:")); break;
                    case "green": System.out.print(EmojiParser.parseToUnicode(":green_heart:")); break;
                    case "blue": System.out.print(EmojiParser.parseToUnicode(":blue_heart:")); break;
                    case "purple": System.out.print(EmojiParser.parseToUnicode(":purple_heart:")); break;
                }
                System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
            }
        }else{
            switch(o.getCondition().getId()){ //piu brutto di originale, pero' funziona. bottom line e' piu bello
                case (5):
                    for(i=0;i<2;i++) {
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    System.out.print(EmojiParser.parseToUnicode(":green_heart:"));
                    for(i=0;i<2;i++) {
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    break;
                case(6):
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    System.out.print(EmojiParser.parseToUnicode(":purple_heart:"));


                    for(i=0;i<3;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    break;
                case(7):
                    for(i=0;i<2;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }                    System.out.print(EmojiParser.parseToUnicode(":blue_heart:"));
                    for(i=0;i<2;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    break;
                case(8):
                    for(i=0;i<3;i++){
                        System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));
                    }
                    System.out.print(EmojiParser.parseToUnicode(":purple_heart:"));
                    System.out.print(EmojiParser.parseToUnicode(":white_large_square:"));


                    break;
            }
        }
    }


    /**
     * Reads and processes player input for playing cards during their turn.
     *
     * This method waits for the player to enter a valid input and then sends a message to the game server
     * depending on the action chosen by the player. Valid inputs include:
     *
     *  * Placing a card: "Place, numberOfTheCard, x, y" - Plays the chosen card (front or back depending on `flippedChosen` state)
     *                      at the specified position (x, y).
     *  * Flipping a card: "Flip." - Flips the chosen card from back to front or vice versa.
     *  * Sending a chat message: "Chat. Message" - Sends a public chat message.
     *  * Sending a private chat message: "Chat.Private. Name. Message" - Sends a private chat message to the specified player.
     *
     * @throws RemoteException If there is an error communicating with the game server.
     */
    @Override
    public void readPlayCardInput() throws RemoteException {
        //AtomicBoolean receivedInput = new AtomicBoolean(false);


        Pattern pattern = Pattern.compile(
                "^\\s*Place\\s*,\\s*(1|2|3)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*$" +
                        "|^\\s*Flip\\.\\s*$" +
                        "|^\\s*Chat\\.\\s*(Private\\s*\\.\\s*(\\w+)\\s*\\.\\s*(.+)|(.+))\\s*$",
                Pattern.CASE_INSENSITIVE
        );
       /*ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


       Runnable task1 = () -> {
           System.out.println("Everyone is waiting! Choose a card!");
       };*/
        if(!chatMessagesStorage.isEmpty()) {
            System.out.println("Last messages from chat: \n");
            showLastMex();
        }

        chatUnblockWait();
        while (true) { //make a control! changed break position


            //executor.schedule(task1, 90, TimeUnit.SECONDS);


            String input = this.input.nextLine();
            //receivedInput.set(true);


            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                if (matcher.group(1) != null) {
                    int numberOfTheCard = Integer.parseInt(matcher.group(1));
                    int x = Integer.parseInt(matcher.group(2));
                    int y = Integer.parseInt(matcher.group(3));
                    Point2D point = new Point2D.Double(x, y);
                    if(availablePositions.contains(point)){ //position is available
                        if(flippedChosen){ //user chooses back
                            notify(new SendingCardPlayed(backHand.get(numberOfTheCard -1), new Integer[]{x, y}));
                        }
                        else{ //chooses front
                            notify(new SendingCardPlayed(frontHand.get(numberOfTheCard -1), new Integer[]{x, y}));
                        }




                        //executor.shutdown();
                        break;
                    }else{
                        System.out.println("Position is not available. Card has to cover at least one corner. No hidden corners may be covered. ");
                    }


                } else if (input.trim().equalsIgnoreCase("Flip."))  { //flip
                    //System.out.println("Flip command recognized: " + matcher.group(4));


                    cardToFlip(); //it also calls show player hand


                } else if (matcher.group(4) != null) { //to add the difference between common and private chat. if private, then call sendPrivateChat, if common,then directly notify
                    if (matcher.group(5) != null) {// Chat.Private.
                        String name = matcher.group(5);
                        String message = matcher.group(6);
                        notify(new SendingChatMessage(message,name ));
                        System.out.println("Private message to "+ name + ": " + message); //correct to be here


                    } else {                       // Chat. Public
                        String message = matcher.group(7);
                        notify(new SendingChatMessage(message));
                    }
                }


            } else {
                System.out.println("Insert valid input. Follow one of the schemes: \n" +
                        "'Place, numberOfTheCard, x, y'; 'Flip.'; 'Chat. Message' or 'Chat. Private. Name. Message'");
                //receivedInput.set(false);
            }
        }
        //executor.shutdown();
    }


    /**
     * Reads and processes player input for drawing cards during their turn.
     *
     * This method waits for the player to enter a valid input and then sends a message to the game server
     * depending on the type and number of cards the player wants to draw. Valid inputs include:
     *
     *  * Drawing a facedown deck: "Draw, Facedown, TypeOfDeck" - Requests to draw a card from the specified deck (Gold or Resource) facedown.
     *  * Drawing a specific faceup card: "Draw, Faceup, TypeCard, numberOfCard" - Requests to draw a specific number of cards (numberOfCard)
     *      of the chosen type (Gold or Resource) from the faceup deck.
     *  * Sending a chat message: "Chat. Message" - Sends a public chat message.
     *  * Sending a private chat message: "Chat.Private. Name. Message" - Sends a private chat message to the specified player.
     *
     * @throws RemoteException If there is an error communicating with the game server.
     */
    @Override
    public void readDrawCardInput () throws RemoteException {
        //AtomicBoolean receivedInput = new AtomicBoolean(false);


        Pattern pattern = Pattern.compile(
                "^\\s*(Draw)\\s*,\\s*(Facedown)\\s*,\\s*(Gold|Resource)" +
                        "|^\\s*(Draw)\\s*,\\s*(Faceup)\\s*,\\s*(Gold|Resource)\\s*,\\s*(1|2)" +
                        "|^\\s*Chat\\.\\s*(Private\\s*\\.\\s*(\\w+)\\s*\\.\\s*(.+)|(.+))\\s*$",
                Pattern.CASE_INSENSITIVE
        );


           /*ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


           Runnable task1 = () -> {
               if (!receivedInput.get()) {
                   System.out.println("Everyone is waiting! Choose a card!");
               }
           };*/
        System.out.println("Last messages from chat: \n");
        showLastMex();


        chatUnblockWait();
        while (true) {
            String input = this.input.nextLine();
            //receivedInput.set(true);


            Matcher matcher = pattern.matcher(input);
            if(matcher.matches()) {
                if (matcher.group(1) != null && matcher.group(1).equalsIgnoreCase("Draw") && matcher.group(2).equalsIgnoreCase("Facedown")) {
                    String typeOfDeck = matcher.group(3);
                    if (typeOfDeck.equalsIgnoreCase("Gold")) {
                        notify(new SendingCardToDraw(1));
                        System.out.println("\nYour turn is over. Wait for the next turn. \n");


                    } else {
                        notify(new SendingCardToDraw(2));
                        System.out.println("\nYour turn is over. Wait for the next turn. \n");
                    }
                    break;


                } else if (matcher.group(4) != null && matcher.group(4).equalsIgnoreCase("Draw") && matcher.group(5).equalsIgnoreCase("Faceup")) {
                    String typeCard = matcher.group(6);
                    int numberOfCard = Integer.parseInt(matcher.group(7));
                    if (typeCard.equalsIgnoreCase("Gold")) {
                        notify(new SendingCardToDraw(1, numberOfCard));
                        System.out.println("\nYour turn is over. Wait for the next turn. \n");


                    } else {
                        notify(new SendingCardToDraw(2, numberOfCard));
                    }
                    System.out.println("\nYour turn is over. Wait for the next turn. \n");
                    break;


                } else if (matcher.group(8) != null) {
                    if (matcher.group(9) != null) { // Chat.Private.
                        String name = matcher.group(9);
                        String message = matcher.group(10);
                        notify(new SendingChatMessage(message,name ));
                        System.out.println("Private message to " + name + ": " + message); //correct to be here


                    } else {                        // Chat. Public
                        String message = matcher.group(11);
                        notify(new SendingChatMessage(message));
                    }
                    //readDrawCardInput();


                }
            }else{
                System.out.println("Insert valid input. Follow one of the schemes: \n" +
                        "'Draw, Facedown, TypeofDeck'; 'Draw, Faceup, TypeCard, numberofCard'; 'Chat. Message' or 'Chat. Private. Name. Message'");
            }
            //executor.shutdown();
        }

    }


    /**
     * Prints information about player's resources and objects.
     *
     * This method iterates over a HashMap containing resource/object names (`String`)
     * and their corresponding quantities (`Integer`).
     *
     * @param p The player whose resources and objects are being printed.
     * @param numOfResourceAndObject A HashMap containing resource/object names as keys and their quantities as values.
     */
    public void printResourceOrObject(Player p,HashMap<String, Integer> numOfResourceAndObject){
        String resource;
        Integer number;


        for (Map.Entry<String,Integer> coppia: numOfResourceAndObject.entrySet()) {
            resource=coppia.getKey();
            number=coppia.getValue();
            if(resource.equals("PLANT")){
                System.out.print(EmojiParser.parseToUnicode(":four_leaf_clover:")+":"+number);
                System.out.print("\n");
            }else if(resource.equals("ANIMAL")){
                System.out.print(EmojiParser.parseToUnicode(":wolf:")+":"+number);
                System.out.print("\n");
            }else if(resource.equals("INSECT")){
                System.out.print(EmojiParser.parseToUnicode(":butterfly:")+":"+number);
                System.out.print("\n");
            }else if(resource.equals("FUNGI")){
                System.out.print(EmojiParser.parseToUnicode(":mushroom:")+":"+number);
                System.out.print("\n");
            }else if(resource.equals("QUILL")){
                System.out.print(EmojiParser.parseToUnicode(":lower_left_fountain_pen:")+":"+number);
                System.out.print("\n");
            }else if(resource.equals("MANUSCRIPT")){
                System.out.print(EmojiParser.parseToUnicode(":scroll:")+":"+number);
                System.out.print("\n");
            }else if(resource.equals("INKWELL")){
                System.out.print(EmojiParser.parseToUnicode(":test_tube:")+":"+number);
                System.out.print("\n");
            }


        }


    }


    /**
     * Displays the most recent messages from the chat history.
     *
     * This method checks if the `chatMessagesStorage` list is not empty. If it is, it prints a message indicating there are recent messages
     * and then iterates through the list, printing each message and removing it from the list.
     */
    public void showLastMex(){
        if(!chatMessagesStorage.isEmpty()){
            System.out.println("Last messages from chat: ");


            for(String m: chatMessagesStorage){
                System.out.println(m);
                chatMessagesStorage.remove(m);
            }
        }
    }


    /**
     * Requests additional game data from the server and displays common objectives.
     *
     * This method calls the `showCommonObjectives` method to display common objectives (likely for all players)
     * and then calls the superclass's `requestOtherData` method to retrieve additional game data.
     */
    public void requestOtherData() {
        showCommonObjectives(allObjectives);
        super.requestOtherData();
    }

    @Override
    public void gameAlmostFinished(){
        System.out.println("\n Wow! You are a great player! \nLet's wait other players to finish and see who was the best one.");
    }

    private boolean positionOccupied(HashMap<Card, Integer[]> manuscript, int x, int y) {
        Integer[] positionToFind = {x, y};

        for (Integer[] value : manuscript.values()) {
            if (Arrays.equals(value, positionToFind)) {
                return true;
            }
        }
        return false;
    }

    private int findIndexOf(Card c){
        for(int i=0;i<timeline.size();i++){
            if(timeline.get(i).equals(c)){
                return i;
            }
        }
        return -1;
    }
}