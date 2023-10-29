import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BotOne extends Bot{
    private int k;

    private HashSet<Tile> nonLeakTiles;

    public BotOne() {}


    public BotOne(int k)
    {
        super();
        this.k = k;
        nonLeakTiles = new HashSet<Tile>();
    }

    public boolean sense(Tile leak, Ship ship)
    {
        int detectionArea = 2*k-1;
            
        int startX = Math.max(botPosition.getRow() - detectionArea, 0);
        int endX = Math.min(botPosition.getRow() + detectionArea,ship.getShipEdgeLength());
        
        int startY = Math.max(botPosition.getCol() - detectionArea, 0);
        int endY = Math.min(botPosition.getCol() + detectionArea,ship.getShipEdgeLength());

        return leak.getRow() >= startX && leak.getRow() <= endX && leak.getCol() >= startY && leak.getCol() <= endY;


    }

    public void botAction(Tile leak, Ship ship)
    {
        if(botPath.isEmpty())
        {
            if(sense(leak, ship))
            {
                System.out.println("found you");
                fillNonLeakTilesFar(ship);
            }
            else
            {
                fillNonLeakTilesClose(ship);
            }
            bfsPossibleLeak(ship);
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
        int detectionArea = 2*k-1;
            
        int startX = Math.max(botPosition.getRow() - detectionArea, 0);
        int endX = Math.min(botPosition.getRow() + detectionArea,ship.getShipEdgeLength());
        
        int startY = Math.max(botPosition.getCol() - detectionArea, 0);
        int endY = Math.min(botPosition.getCol() + detectionArea,ship.getShipEdgeLength());

        for (int x = startX; x < endX; x++)
        {
            for (int y = startY; y< endY; y++)
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
        int detectionArea = 2*k-1;
            
        int startX = Math.max(botPosition.getRow() - detectionArea, 0);
        int endX = Math.min(botPosition.getRow() + detectionArea,ship.getShipEdgeLength());
        
        int startY = Math.max(botPosition.getCol() - detectionArea, 0);
        int endY = Math.min(botPosition.getCol() + detectionArea,ship.getShipEdgeLength());

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

    private void bfsPossibleLeak(Ship ship)
    //Hashsets are iterated without a particular order, this provides functionality that ties in distance are broken randomly. 
    {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Tile> parent = new HashMap<>();
        Set<Tile> botNeighbors = new HashSet<Tile>();

        queue.add(botPosition);
        visited.add(botPosition);
        parent.put(botPosition, null);

        Tile currentTile = getBotPosition();

        while(!queue.isEmpty())
        {
            Tile curr = queue.poll();

            if(curr.getOpen() && !nonLeakTiles.contains(curr))
            {
                currentTile = curr;
                break;
            }



            ship.fillNeighborsSet(curr, botNeighbors);

            for(Tile tile : botNeighbors)
            {
                if(!visited.contains(tile))
                {
                    queue.add(tile);
                    visited.add(tile);
                    parent.put(tile, curr);
                }
            }

            botNeighbors.clear();
        }

        while(currentTile != null)
        {
            botPath.add(currentTile);
            currentTile = parent.get(currentTile);
        }

        

        Collections.reverse(botPath);

        botPath.remove(0);

        
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