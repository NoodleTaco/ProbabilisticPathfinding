import java.util.HashSet;

public class BotTwo extends BotOne{

    private HashSet<Tile> senseLocations;


    public BotTwo() {}

    public BotTwo(int k)
    {
        super(k);
        senseLocations = new HashSet<Tile>();
    }

    public void setSenseLocations(Ship ship)
    {
        int range = ship.getShipEdgeLength() / ((2*k)+1); 

        boolean extra = false;

        if(ship.getShipEdgeLength() % ((2*k)+1) != 0 )
        {
            extra = true;
        }
        int row = 0;
        int col = 0;

        int modEndRow = 0;
        int modEndCol = 0;

        for(int x = 0; x < range ; x++)
        {
            if(extra && x == range-1)
            {
                modEndRow = 1;
            }
            else
            {
                modEndRow = 0;
            }

            for(int y = 0; y < range ; y++)
            {
                if(extra && y == range-1)
                {
                    modEndCol = 1;
                }
                else
                {
                    modEndCol = 0;
                }
                
                
                senseLocations.add(ship.getShipTile(row + k, col + k));

                col += ((2*k)+1);
            }

            row += ((2*k)+1);
            col = 0;
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