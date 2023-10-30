import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BotOne extends Bot{
    protected int k;

    protected HashSet<Tile> nonLeakTiles;

    public BotOne() {}


    public BotOne(int k)
    {
        super();
        this.k = k;
        nonLeakTiles = new HashSet<Tile>();
    }

    public boolean sense(Tile leak, Ship ship)
    {
        
        int startX = Math.max(botPosition.getRow() - k, 0);
        int endX = Math.min(botPosition.getRow() + k,ship.getShipEdgeLength() -1);
        
        int startY = Math.max(botPosition.getCol() - k, 0);
        int endY = Math.min(botPosition.getCol() + k,ship.getShipEdgeLength() -1);

        return leak.getRow() >= startX && leak.getRow() <= endX && leak.getCol() >= startY && leak.getCol() <= endY;


    }

    public void botAction(Tile leak, Ship ship)
    {
        if(botPath.isEmpty())
        {
            if(sense(leak, ship))
            {
                System.out.println("FOUND IT \n RAHHHHHHHHHHH \n RAHHHHHHHHHHH \n RAHHHHHHHHHHH");
                fillNonLeakTilesFar(ship);
            }
            else
            {
                fillNonLeakTilesClose(ship);
            }
            bfsNotInSet(ship, nonLeakTiles);
        }

        else
        {
            botMove();
        }
    }

    private void botMove()
    {
        if(botPath.isEmpty())
        {
            return;
        }
        botPosition = botPath.remove(0);

    }

    private void fillNonLeakTilesClose(Ship ship)
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

    private void fillNonLeakTilesFar(Ship ship)
    {
        
            
        int startX = Math.max(botPosition.getRow() - k, 0);
        int endX = Math.min(botPosition.getRow() + k,ship.getShipEdgeLength() -1);
        
        int startY = Math.max(botPosition.getCol() - k, 0);
        int endY = Math.min(botPosition.getCol() + k,ship.getShipEdgeLength() -1);

        for (int x = 0; x < ship.getShipEdgeLength(); x++)
        {
            for (int y = 0; y< ship.getShipEdgeLength(); y++)
            {
                if(ship.getShipTile(x, y).getOpen() && !(x >= startX && x <= endX && y >= startY && y <= endY))
                {
                    nonLeakTiles.add(ship.getShipTile(x, y));
                }
            }
        }
    }

    



    public HashSet<Tile> getNonLeakTiles()
    {
        return nonLeakTiles;
    }

    public void setNoLeakOnBot()
    {
        nonLeakTiles.add(botPosition);
    }

    


}