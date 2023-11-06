import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
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

    protected void bfsNotInSet(Ship ship, HashSet<Tile> set, ArrayList<Tile> list, Tile startingTile)
    //Hashsets are iterated without a particular order, this provides functionality that ties in distance are broken randomly. 
    {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Tile> parent = new HashMap<>();
        Set<Tile> botNeighbors = new HashSet<Tile>();

        queue.add(startingTile);
        visited.add(startingTile);
        parent.put(startingTile, null);

        Tile currentTile = startingTile;

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
            list.add(currentTile);
            currentTile = parent.get(currentTile);
        }

        

        Collections.reverse(list);

        list.remove(0);

        
    }

    protected void bfsInSet(Ship ship, HashSet<Tile> set, ArrayList<Tile> list, Tile startingTile)
    //Hashsets are iterated without a particular order, this provides functionality that ties in distance are broken randomly. 
    {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Tile> parent = new HashMap<>();
        Set<Tile> botNeighbors = new HashSet<Tile>();

        queue.add(startingTile);
        visited.add(startingTile);
        parent.put(startingTile, null);

        Tile currentTile = startingTile;
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
            list.add(currentTile);
            currentTile = parent.get(currentTile);
        }

        

        Collections.reverse(list);

        list.remove(0);

        
    }

    protected void bfsInSetWithExceptions(Ship ship, HashSet<Tile> set, ArrayList<Tile> list, Tile startingTile, Set<Tile> exceptions)
    //Hashsets are iterated without a particular order, this provides functionality that ties in distance are broken randomly. 
    {
        Queue<Tile> queue = new LinkedList<>();
        Set<Tile> visited = new HashSet<>();
        Map<Tile, Tile> parent = new HashMap<>();
        Set<Tile> botNeighbors = new HashSet<Tile>();

        queue.add(startingTile);
        visited.add(startingTile);
        parent.put(startingTile, null);

        Tile currentTile = startingTile;
        while(!queue.isEmpty())
        {
            Tile curr = queue.poll();

            if(set.contains(curr))
            {
                currentTile = curr;
                break;
            }

            ship.fillNeighborsListException(curr, botNeighbors, exceptions);

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
            list.add(currentTile);
            currentTile = parent.get(currentTile);
        }

        

        Collections.reverse(list);

        list.remove(0);

        
    }

    public boolean probabilityRoll(double probability)
    {
        Random rand = new Random();
        double randomValue = rand.nextDouble();         
        return randomValue < probability;
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