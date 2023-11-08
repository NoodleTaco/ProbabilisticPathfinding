import java.util.HashSet;

public class BotFive extends Bot{
    protected int k;

    protected HashSet<Tile> nonLeakTiles;

    public BotFive(){

    }

    public BotFive(int k){
        super();
        this.k = k;
        nonLeakTiles = new HashSet<Tile>();
    }

    public boolean sense(Tile leak, Tile leakTwo, Ship ship)
    {
        
        int startX = Math.max(botPosition.getRow() - k, 0);
        int endX = Math.min(botPosition.getRow() + k,ship.getShipEdgeLength() -1);
        
        int startY = Math.max(botPosition.getCol() - k, 0);
        int endY = Math.min(botPosition.getCol() + k,ship.getShipEdgeLength() -1);

        if(leak != null)
        {
            if(leak.getRow() >= startX && leak.getRow() <= endX && leak.getCol() >= startY && leak.getCol() <= endY){
                return true;
            }
        }

        if(leakTwo != null){
            if(leakTwo.getRow() >= startX && leakTwo.getRow() <= endX && leakTwo.getCol() >= startY && leakTwo.getCol() <= endY){
                return true;
            }
        }

        return false;

    }

    public void botAction(Tile leak, Tile leakTwo, Ship ship)
    {
        if(botPath.isEmpty())
        {
            if(!sense(leak, leakTwo, ship))
            {
                System.out.println("No Beep");
                fillNonLeakTilesClose(ship);
            }
            bfsNotInSet(ship, nonLeakTiles, botPath, botPosition);
        }
        else
        {
            botMove();
        }
    }


    /**
     * Adds all tiles that are within the bot's scanning range to the nonLeakTiles set
     * @param ship Reference of the ship where the experiment occurs
     */
    protected void fillNonLeakTilesClose(Ship ship)
    {    
        int startX = Math.max(botPosition.getRow() - k, 0);
        int endX = Math.min(botPosition.getRow() + k,ship.getShipEdgeLength() -1);
        
        int startY = Math.max(botPosition.getCol() - k, 0);
        int endY = Math.min(botPosition.getCol() + k,ship.getShipEdgeLength() -1);

        for (int x = startX; x <= endX; x++)
        {
            for (int y = startY; y<= endY; y++)
            {
                if(ship.getShipTile(x, y).getOpen())
                {
                    nonLeakTiles.add(ship.getShipTile(x, y));
                }
                
            }
        }
    }


    /**
     * Moves the bot by setting botPosition to point to the next Tile in botPath 
     */
    protected void botMove()
    {
        if(botPath.isEmpty())
        {
            return;
        }
        botPosition = botPath.remove(0);
    }


    @Override
    public void setNoLeakOnBot() {
        nonLeakTiles.add(botPosition);
    }

    public HashSet<Tile> getNonMultipleLeakTiles()
    {
        return nonLeakTiles;
    }

    public static void main(String [] args)
	{

        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();

        experimentController.setBotFive(3);

        System.out.println(experimentController.runMultipleLeaksExperiment());





	}

}