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
        return probabilityRoll(formula(leak, botPosition, ship));
    }

    private int distanceToLeak(Tile leak, Ship ship, Tile startingTile)
    {
        if(startingTile.equals(leak)){
            return 0;
        }
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
                updateProbabilitiesFromSense(ship, true);
            }
            else{
                updateProbabilitiesFromSense(ship, false);
            }
            getHighestProbabilities();
            bfsInSet(ship, highestProbabilties, botPath, botPosition);
        }
    }

    private void updateProbabilitiesFromSense(Ship ship, boolean beep){

        Iterator<Map.Entry<Tile, Double>> firstRun = tileProbabilities.entrySet().iterator();

        double beepInI = 0;

        //P(beep in i) only needs to be calculated once 
        while(firstRun.hasNext()){
            Map.Entry<Tile, Double> entry = firstRun.next();
            double toAdd = entry.getValue() * formula(entry.getKey(), botPosition, ship);
            beepInI += toAdd;
        }

        //If there was no beep, the probability of there being no beep in i = 1 - beepInI since they have to add up to 1
        if(!beep){
            beepInI = 1 - beepInI;
        }

        Iterator<Map.Entry<Tile, Double>> iterator = tileProbabilities.entrySet().iterator();

        //Adjust each tile's probability by dividing it by 1 - <the original probaiblity of the tile determined to have no leak>
        while(iterator.hasNext()){
            Map.Entry<Tile, Double> entry = iterator.next();
            double leakInJ = entry.getValue();

            double beepInIGivenLeakInJ = formula(entry.getKey(), botPosition, ship);

            //Update the probability of cell j through formula P(leak in j | beep in cell i) = P(beep in cell i | leak in j) * P(leak in j) / P(beep in i)
            tileProbabilities.put(entry.getKey(), leakInJ * beepInIGivenLeakInJ / beepInI);
        }
    }

    private double formula(Tile goal, Tile start, Ship ship ){
        double e = Math.E;
        int distanceToLeak = distanceToLeak(goal, ship, start);

        //Covers the case where the formula is ran on the cell that contains the leak
        if(distanceToLeak == 0){
            distanceToLeak = 1;
        }
        return Math.pow(e, -1 * alpha * (distanceToLeak-1));
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

        double tileProbability = tileProbabilities.get(botPosition);

        tileProbabilities.put(botPosition, 0.0);

        Iterator<Map.Entry<Tile, Double>> iterator = tileProbabilities.entrySet().iterator();

        //Adjust each tile's probability by dividing it by 1 - <the original probaiblity of the tile determined to have no leak>
        while(iterator.hasNext()){
            Map.Entry<Tile, Double> entry = iterator.next();
            tileProbabilities.put(entry.getKey(), entry.getValue() / (1 - tileProbability));
        }
        

    }

    /**
     * Loops through the tileProbabilities and adds the tile(s) with the highest probability to the highestProbabilities set
     */
    private void getHighestProbabilities(){
        double max = 0;
        Iterator<Map.Entry<Tile, Double>> iterator = tileProbabilities.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Tile, Double> entry = iterator.next();
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