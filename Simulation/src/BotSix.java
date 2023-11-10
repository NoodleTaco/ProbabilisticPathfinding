import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class BotSix extends BotFive {

    protected boolean exploringSense;

    private HashSet<Tile> senseLocations;

    private HashSet<Tile> possibleSenseTiles;

    public BotSix() {}

    public BotSix(int k){
        super(k);
        senseLocations = new HashSet<Tile>();
        possibleSenseTiles = new HashSet<Tile>();
        exploringSense = false;
    }

    public void botAction(Tile leak, Tile leakTwo, Ship ship ){
        if(!exploringSense){
            if(!senseLocations.isEmpty()){
                if(botPath.isEmpty() && senseLocations.contains(botPosition))
                {
                    senseLocations.remove(botPosition);
                    if(sense(leak, leakTwo, ship))
                    {
                        System.out.println("Beep");
                        exploringSense = true;
                        fillPossibleSenseTiless(ship);
                    }
                    else
                    {
                        System.out.println("No Beep");
                        fillNonLeakTilesClose(ship);
                        bfsInSet(ship, senseLocations, botPath, botPosition);
                    }
                }
                else if(botPath.isEmpty())
                {
                    bfsInSet(ship, senseLocations, botPath, botPosition);
                    botMove();
                }
                else
                {
                    botMove();
                }
            }
            else{
                System.out.println("Sense Locations empty, still going");
                if(botPath.isEmpty())
                {
                    if(sense(leak, leakTwo, ship))
                    {
                        System.out.println("Beep");
                        exploringSense = true;
                        fillPossibleSenseTiless(ship);
                    }
                    else
                    {
                        System.out.println("No Beep");
                        fillNonLeakTilesClose(ship);
                    }
                    bfsNotInSet(ship, nonLeakTiles,botPath, botPosition);
                }
                else
                {
                    botMove();
                }
            }
        }
        else{
            System.out.println("Sense Detected, finding leak...");

            if(botPath.isEmpty()){
                bfsInSet(ship, possibleSenseTiles, botPath, botPosition);
            }

            botMove();

            if(botPosition.equals(leak) || botPosition.equals(leakTwo)){
                System.out.println("Leak found");
                possibleSenseTiles.clear();
                exploringSense = false;
            }

            

        }


    }
    /**
     * Fills the possibleSenseTiless set with all open tiles possibly containing the leak in the bot's sense range
     * @param ship Reference of the experiment's ship
     */
    private void fillPossibleSenseTiless(Ship ship){
        int startX = Math.max(botPosition.getRow() - k, 0);
        int endX = Math.min(botPosition.getRow() + k,ship.getShipEdgeLength() -1);
        
        int startY = Math.max(botPosition.getCol() - k, 0);
        int endY = Math.min(botPosition.getCol() + k,ship.getShipEdgeLength() -1);

        for (int x = startX; x <= endX; x++)
        {
            for (int y = startY; y<= endY; y++)
            {
                if(ship.getShipTile(x, y).getOpen() && !nonLeakTiles.contains(ship.getShipTile(x, y)))
                {
                    possibleSenseTiles.add(ship.getShipTile(x, y));
                }
                
            }
        }
    }




    public void setSenseLocationsBotSix(Ship ship)
    {
        int range = ship.getShipEdgeLength() / ((2*k)+1); 

        boolean extra = false;

        if(ship.getShipEdgeLength() % ((2*k)+1) != 0 )
        {
            extra = true;
            range ++;
        }
        int row = 0;
        int col = 0;

        int rowToCompute = 0;
        int colToCompute = 0;


        for(int x = 0; x < range ; x++)
        {
            if(x == range-1 && extra)
            {
                //System.out.println("Final Row with Extra: row = " + row);
                rowToCompute = row;
            }
            else
            {
                rowToCompute = row + k;
            }

            for(int y = 0; y < range ; y++)
            {
                if(y == range-1 && extra)
                {
                    //System.out.println("Final Col with Extra: col = " + col);
                    colToCompute = col;
                }
                else
                {

                    colToCompute = col + k;
                }

                Tile tileToAdd = ship.getShipTile(rowToCompute, colToCompute);
                tileToAdd = getOpenNeighborIfClosed(tileToAdd, ship);

                if(tileToAdd != null){
                    senseLocations.add(tileToAdd);
                }
                col += ((2*k)+1);
            }

            row += ((2*k)+1);
            col = 0;
        }
    }

        private Tile getOpenNeighborIfClosed(Tile tile, Ship ship)
    {
        if(tile.getOpen())
        {
            return tile;
        }
        Random rand = new Random();
        ArrayList<Tile> list = new ArrayList<Tile>();
        ship.fillNeighborsList(tile, list);

        if(list.isEmpty())
        {
            return null;
        }
        else
        {
            return list.get(rand.nextInt(list.size()));
        }
    }

    @Override
    public void setNoLeakOnBot() {
        nonLeakTiles.add(botPosition);
        possibleSenseTiles.remove(botPosition);
    }

    public HashSet<Tile> getSenseLocationsBotSix()
    {
        return senseLocations;
    }

    public static void main(String [] args)
	{

        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();

        experimentController.setBotSix(3);

        System.out.println(experimentController.runMultipleLeaksExperiment());





	}

}