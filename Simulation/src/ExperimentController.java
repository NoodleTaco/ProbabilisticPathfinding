import java.util.ArrayList;
import java.util.Random;
import java.util.HashSet;

public class ExperimentController{
    private Ship ship;

    private Bot bot;

    private Tile botSpawnLocation;

    private Tile leak;


    public ExperimentController()
    {
        ship = new Ship();
    }

    /**
     * Forms ship and generates the starting positions of the bot, button, and fire
     */
    public void spawnOutsideOfDetection()
    {
        
        

        Random rand = new Random();

        while(true)
        {
            Tile tile = ship.getShipTile(rand.nextInt(ship.getShipEdgeLength()), rand.nextInt(ship.getShipEdgeLength()));

            if(tile.getOpen())
            {
                bot.setBotPosition(tile);
                break;
            }
        }
        
        while(true)
        {
            leak = ship.getShipTile(rand.nextInt(ship.getShipEdgeLength()), rand.nextInt(ship.getShipEdgeLength()));
            if (!bot.sense(leak, ship) && leak.getOpen())
            {
                break;
            }
        }
        /* 
        System.out.println("Bot Starting Position: " + bot.toString());
        System.out.println("Button Starting Position: " + button.toString());
        System.out.println("Leak Starting Position: " + leak.toString());
        */
    }

    private boolean testSense(Tile leak, Tile botStartingPosition, int k)
    {
        int startX = Math.max(botStartingPosition.getRow() - k, 0);
        int endX = Math.min(botStartingPosition.getRow() + k,ship.getShipEdgeLength() -1);
        
        int startY = Math.max(botStartingPosition.getCol() - k, 0);
        int endY = Math.min(botStartingPosition.getCol() + k,ship.getShipEdgeLength() -1);

        return leak.getRow() >= startX && leak.getRow() <= endX && leak.getCol() >= startY && leak.getCol() <= endY;

    }

    public int runExperiment()
    {
        int numActions = 0;
        //int count = 25;

        spawnOutsideOfDetection();

        printShip();

        System.out.println();
        while(true)
        {
            if(bot.getBotPosition().equals(leak))
            {
                break;
            }

            bot.setNoLeakOnBot();

            bot.botAction(leak, ship);

            numActions++;


            //count--;

            printShip();

            System.out.println();
        }


        return numActions;
    }

    public void setBotOne(int k)
    {
        bot = new BotOne(k);
    }

    public void setBotTwo(int k)
    {
        bot = new BotTwo(k);
        BotTwo botTwo = (BotTwo)bot;
        botTwo.setSenseLocations(ship);
        
    }

    public Bot getBot()
    {
        return bot;
    }

    public Tile getLeak()
    {
        return leak;
    }

    public Ship getShip()
    {
        return ship;
    }
     /**
     * Prints a visual representation of the ship for testing purposes.
     * Orange (looks yellow to me) indicates a tile on fire
     * Blue represents the bot
     * Purple represents the button
     * Red Represents the path the bot has traced out to reach the button
     * White represents a plain open tile
     * Green represents a closed tile
     */
    private void printShip()
    {
        String reset = "\u001B[0m";
        String lightYellow = "\u001B[93m";
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String blue = "\u001B[34m";
        String white = "\u001B[37m";
        String purple = "\u001B[35m";
        String orange = "\u001B[33m";
        String black = "\u001B[30m";


        //BotOne botOne = (BotOne)bot;

        BotTwo botTwo = (BotTwo)bot;

        for(int row = 0; row < ship.getShipEdgeLength(); row++)
        {
            for (int col = 0; col < ship.getShipEdgeLength(); col++)
            {

                if(bot.getBotPosition().equals(ship.getShipTile(row, col)))
                {
                    System.out.print(blue + "■ " + reset);
                }

                else if(leak.equals(ship.getShipTile(row, col)))
                {
                    System.out.print(purple + "■ " + reset);
                }

                else if(bot.getBotPath().contains(ship.getShipTile(row, col)))
                {
                    System.out.print(red + "■ " + reset);
                }

                else if(botTwo.getNonLeakTiles().contains(ship.getShipTile(row, col)))
                {
                     System.out.print(lightYellow + "■ " + reset);
                }

                else if(botTwo.getSenseLocations().contains(ship.getShipTile(row, col)))
                {
                    if(ship.getShipTile(row, col).getOpen())
                    {
                        System.out.print(black + "■ " + reset);
                    }
                    else 
                    {
                        System.out.print(green + "■ " + reset);
                    }

                }

                else if(ship.getShipTile(row, col).getOpen())
                {
                    System.out.print(white + "■ " + reset);
                }

                else 
                {
                    System.out.print(green + "■ " + reset);
                }
            }
            System.out.println();
        }
    }

    public static void main(String [] args)
	{
        /*
        ExperimentController experimentController = new ExperimentController();
        experimentController.setBotOne(5);
        System.out.println(experimentController.runExperiment());
        */

        /*
        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();
        experimentController.setBotTwo(3);
        experimentController.spawnOutsideOfDetection();
        BotTwo botTwo = (BotTwo)experimentController.getBot();
        botTwo.printSenseLocations();
         */
        

        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();
        experimentController.setBotTwo(2);
        System.out.println(experimentController.runExperiment());

        


        
        
	}


}