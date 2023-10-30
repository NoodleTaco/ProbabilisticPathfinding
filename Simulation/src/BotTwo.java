import java.util.HashSet;
import java.util.Random;
import java.util.ArrayList;

public class BotTwo extends BotOne{

    private HashSet<Tile> senseLocations;

    private boolean firstSenseFound;


    public BotTwo() {}

    public BotTwo(int k)
    {
        super(k);
        senseLocations = new HashSet<Tile>();
        firstSenseFound = false;
    }

    public void botAction(Tile leak, Ship ship)
    {
        if(!firstSenseFound)
        {
            if(botPath.isEmpty() && senseLocations.contains(botPosition))
            {
                senseLocations.remove(botPosition);
                if(sense(leak, ship))
                {
                    System.out.println("FOUND IT \n RAHHHHHHHHHHH \n RAHHHHHHHHHHH \n RAHHHHHHHHHHH");
                    fillNonLeakTilesFar(ship);
                    firstSenseFound = true;
                }
                else
                {
                    fillNonLeakTilesClose(ship);
                    bfsInSet(ship, senseLocations);
                }
            }
            else if(botPath.isEmpty())
            {
                bfsInSet(ship, senseLocations);
                botMove();
            }
            else
            {
                botMove();
            }
        }
        else
        {
            if(botPath.isEmpty())
            {
                bfsNotInSet(ship, nonLeakTiles);
            }
            botMove();
        }
    }

    public void setSenseLocations(Ship ship)
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
                System.out.println("Final Row with Extra: row = " + row);
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
                    System.out.println("Final Col with Extra: col = " + col);
                    colToCompute = col;
                }
                else
                {

                    colToCompute = col + k;
                }

                Tile tileToAdd = ship.getShipTile(rowToCompute, colToCompute);
                tileToAdd = getOpenNeighborIfClosed(tileToAdd, ship);

                senseLocations.add(tileToAdd);
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
            return tile;
        }
        else
        {
            return list.get(rand.nextInt(list.size()));
        }
    }


    public HashSet<Tile> getSenseLocations()
    {
        return senseLocations;
    }

    public void printSenseLocations()
    {
        for(Tile tile: senseLocations)
        {
            System.out.println(tile.toString());
        }
    }

    
}