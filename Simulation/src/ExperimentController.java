import java.util.ArrayList;
import java.util.Random;

public class ExperimentController{
    private Ship ship;

    private Bot bot;

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
        
        ship.formShip();

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

        BotOne botOne = (BotOne)bot;

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

                else if(botOne.getNonLeakTiles().contains(ship.getShipTile(row, col)))
                {
                     System.out.print(lightYellow + "■ " + reset);
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
        ExperimentController experimentController = new ExperimentController();
        experimentController.setBotOne(4);
        System.out.println(experimentController.runExperiment());
        
        
	}


}