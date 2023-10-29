import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Abstract class for all the different bots 
 * @author Donald Yubeaton
 */
public abstract class Bot{
    
    protected Tile botPosition;
    protected ArrayList<Tile> botPath;


    /**
     * No Arg Constructor
     */
    public Bot()
    {
        botPath = new ArrayList<Tile>();
    }

    protected void bfsNotInSet(Ship ship, HashSet<Tile> set)
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

            if(curr.getOpen() && !set.contains(curr))
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

    protected void bfsInSet(Ship ship, HashSet<Tile> set)
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

            if(curr.getOpen() && set.contains(curr))
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

    public void setBotPosition(Tile tile)
    {
        this.botPosition = tile;
    }

    public Tile getBotPosition()
    {
        return botPosition;
    }

    public ArrayList<Tile> getBotPath()
    {
        return botPath;
    }

    public abstract boolean sense(Tile leak, Ship ship);

    public abstract void botAction(Tile leak, Ship ship);

    public abstract void setNoLeakOnBot();




}