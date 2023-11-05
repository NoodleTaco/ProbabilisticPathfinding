import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Iterator;

public class BotThree extends Bot{

    protected double alpha;

    protected HashMap<Tile, Double> tileProbabilities;

    protected HashSet<Tile> highestProbabilties;

    public BotThree(){

    }

    public BotThree(double alpha){
        super();
        this.alpha = alpha;
        tileProbabilities = new HashMap<Tile, Double>();
        highestProbabilties = new HashSet<Tile>();
    }

    public void initalizeProbabilities(Ship ship){
        double startingProbability = 1/(ship.getShipEdgeLength() * ship.getShipEdgeLength());
        for(int row = 0; row < ship.getShipEdgeLength(); row++){
            for (int col = 0; col < ship.getShipEdgeLength(); col ++){
                tileProbabilities.put(ship.getShipTile(row, col), startingProbability);
            }
        }
    }



    @Override
    public boolean sense(Tile leak, Ship ship) {
        int distanceToLeak = distanceToLeak(leak, ship, botPosition);

        double e = Math.E;

        double probability = Math.pow(e, -1 * alpha * (distanceToLeak-1));

        return probabilityRoll(probability);
    }

    private int distanceToLeak(Tile leak, Ship ship, Tile startingTile)
    {
        HashSet<Tile> leakSet = new HashSet<Tile>();
        leakSet.add(leak);
        ArrayList<Tile> path = new ArrayList<Tile>();
        bfsInSet(ship, leakSet, path, startingTile);
        int dist = path.size();
        return dist;
    }

    @Override
    public void botAction(Tile leak, Ship ship) {
        if(!botPath.isEmpty()){
            botMove();
            botPath.clear();
        }
        else{
            if(sense(leak, ship)){
                //TODO update all probabilities when you get a beep
            }
            else{
                //TODO update all probabilities if you didn't get a beep
            }
            getHighestProbabilities();
            bfsInSet(ship, highestProbabilties, botPath, botPosition);
        }
    }

    protected void botMove()
    {
        if(botPath.isEmpty())
        {
            return;
        }
        botPosition = botPath.remove(0);

    }



    /**
     * Updates probabilities when the current tile the bot inhabits is not the leak 
     */
    @Override
    public void setNoLeakOnBot() {
        tileProbabilities.put(botPosition, 0.0);
        //TODO update all probabilities based on one tile's probability being set to 0
    }

    /**
     * Loops through the tileProbabilities and adds the tile(s) with the highest probability to the highestProbabilities set
     */
    private void getHighestProbabilities(){
        double max = 0;
        Iterator<Map.Entry<Tile, Double>> iterator = tileProbabilities.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Tile, Double> entry = iterator.next();
            Tile tile = entry.getKey();
            Double probability = entry.getValue();
            if(entry.getValue() > max)
            {
                highestProbabilties.clear();
                highestProbabilties.add(entry.getKey());
                max = entry.getValue();
            }
            else if(entry.getValue() == max){
                highestProbabilties.add(entry.getKey());
            }
        }
    }

}