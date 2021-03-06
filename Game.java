/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 * 
 * @author Chad Pearson
 * 

 */
import java.util.Stack;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room lastRoom;
    private Room cliff;
    private Room win;
    private Room fail;
    private int timer = 0;
    private Stack multiLastRooms = new Stack();
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, campus, westf1, westf2, westf3, library, colCenter, finaid, hunHall, hun1, hun2, comp1, comp2, comp3, comp4, enrollment;
      
        // create the rooms
        outside = new Room("outside the main entrance of RVCC");
        campus = new Room("on the main campus");
        westf1 = new Room("in West Building, Floor 1");
        westf2 = new Room("in West Building, Floor 2");
        westf3 = new Room("in West Building, Floor 3");
        library = new Room("in the Library");
        colCenter = new Room("in the College Center");
        finaid = new Room("in the Financial Aid office");
        hunHall = new Room("in Hunterdon Hall");
        hun1 = new Room("in room H123");
        hun2 = new Room("in room H110");
        comp1 = new Room("in room W110");
        comp2 = new Room("in room W210");
        comp3 = new Room("in room W213");
        comp4 = new Room("in room W306");
        enrollment = new Room("in the enrollment office");
        
        // initialise room exits
        outside.setExit("north", campus);
        outside.setExit("east", library);
        outside.setExit("west", westf1);
        
        library.setExit("west", outside);
        library.setExit("north", campus);

        westf1.setExit("east", outside);
        westf1.setExit("north", campus);
        westf1.setExit("west", westf2);
        westf1.setExit("south", comp1);
        
        comp1.setExit("north", westf1);
        
        westf2.setExit("east", westf1);
        westf2.setExit("west", westf3);
        westf2.setExit("north", comp2);
        westf2.setExit("south", comp3);
        
        comp2.setExit("south", westf2);
        comp3.setExit("north", westf2);
        
        westf3.setExit("east", westf2);
        westf3.setExit("north", comp4);
        
        comp4.setExit("south", westf3);

        campus.setExit("west", westf1);
        campus.setExit("east", library);
        campus.setExit("north", hunHall);
        campus.setExit("south", outside);

        hunHall.setExit("west", colCenter);
        hunHall.setExit("south", campus);
        hunHall.setExit("east", hun2);
        hunHall.setExit("north", hun1);
        
        hun1.setExit("south", hunHall);
        hun2.setExit("west", hunHall);

        colCenter.setExit("west", finaid);
        colCenter.setExit("north", enrollment);
        colCenter.setExit("south", campus);
        colCenter.setExit("east", hunHall);
        
        enrollment.setExit("south", colCenter);
        
        finaid.setExit("east", colCenter);
        
        //initialize items
        Item book = new Item("a psychology textbook left behind.", 0.1);
        Item phone = new Item("an iPhone 4. It has a passcode lock.", 0.1);
        Item pencil = new Item("a regular pencil.", 0.1);
        Item gummy = new Item("a singular squished gummy bear.", 0.1);
        Item paper = new Item("someone's old schedule. The first class is English 1.", 0.1);
        Item stopwatch = new Item("a stopwatch. The timer was stopped at 80 minutes.", 0.1);
        
        //add items to room
        library.addItem(book);
        colCenter.addItem( phone);
        campus.addItem(pencil);
        hunHall.addItem(gummy);
        hunHall.addItem(paper);
        comp4.addItem(stopwatch);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }
    

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case LOOK:
                look();
                break;
                
            case BACK:
                back();
                break;
                
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
        /**
    *  This allows the player to look around the room and recieve the long
    *  description over again.
    */
    private void look()
    {
        System.out.println(currentRoom.getLongDescription());   
    }
    
    private void eat()
    {
        System.out.println("You have just eaten and no longer feel hungry");
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            lastRoom = currentRoom;
            multiLastRooms.push (lastRoom);
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
       /**
     * Added this method that uses a stack to remember which rooms you have been in, and it orders it for easy access.
     * Also, trying to use the back command at the start of the game gives you an error statement if you try
     */
    private void back()
    {
        if (multiLastRooms.empty())
        {
            System.out.println("Well you wouldn't be lost if you could remember where you were before this...");
        }
        
        else
        {
            currentRoom = (Room) multiLastRooms.pop();
            System.out.println("You retrace your foot steps and find your way back to where you were earlier.");
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
