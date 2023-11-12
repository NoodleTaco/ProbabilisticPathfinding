import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class BotEight extends Bot {

    protected double alpha;

    protected HashMap<TilePair, Integer> tilePairDistances;

    protected HashMap<TilePair, Double> tilePairProbabilities;

    protected HashSet<TilePair> highestPairProbability;

    //Represents the first leak the bot found, only initialized when the bot actually finds the leak
    private Tile leak;

    public BotEight(){
        tilePairDistances = new HashMap<TilePair, Integer>();
        highestPairProbability = new HashSet<>();
    }

    public BotEight(double alpha){
        super();
        this.alpha = alpha;
        tilePairDistances = new HashMap<TilePair, Integer>();
        tilePairProbabilities = new HashMap<TilePair, Double>();
        highestPairProbability = new HashSet<>();

    }

    /**
     * Initalizes the probabilities of each tile pair in the ship to 1/number of Pairs
     * Utilizes the tilePairDistances to avoid looping through the ship again
     */
    public void initalizeProbabilities(){
        int numPairs = tilePairDistances.size();

        for(TilePair tilePair: tilePairDistances.keySet()){
            tilePairProbabilities.put(tilePair, 1.0 / numPairs);
        }
        
    }

    @Override
    public void botAction(Tile leak, Tile leakTwo, Ship ship) {
        if(!botPath.isEmpty()){
            //System.out.println("Bot Moving");
            botMove();
        }
        else{
            if(sense(leak, leakTwo, ship)){
                //System.out.println("beep");
                //System.out.println();
                updateProbabilitiesFromSense(ship, true);
            }
            else{
                //System.out.println("no beep");
                //System.out.println();
                updateProbabilitiesFromSense(ship, false);
            }
            HashSet<Tile> highestTilesSet = new HashSet<>();
            for(TilePair tilePair: highestPairProbability){
                //System.out.println("tilePair tileOne: " + tilePair.getTileOne() + " tilePair tileTwo: " + tilePair.getTileTwo() + " leak: " + getFoundLeak() + " botPosition: " + botPosition);
                if(!tilePair.getTileOne().equals(botPosition) && !tilePair.getTileOne().equals(getFoundLeak())){

                    highestTilesSet.add(tilePair.getTileOne());
                }
                else if(!tilePair.getTileTwo().equals(botPosition) && !tilePair.getTileTwo().equals(getFoundLeak())){

                    highestTilesSet.add(tilePair.getTileTwo());
                }
                
            }
            if(highestTilesSet.isEmpty()){
                //System.out.println("What the barnacle");
            }

            Iterator<TilePair> iterator = highestPairProbability.iterator();
            TilePair highestTilePair = iterator.next();
            //System.out.println("Constructing Path: " + "Highest Probability is at least on Tile:  " + highestTilePair  + " with probability: " + tilePairProbabilities.get(highestTilePair)  + " Bot Position: " + botPosition);
            bfsInSet(ship, highestTilesSet, botPath, botPosition);

        }
    }

    protected void updateProbabilitiesFromSense(Ship ship, boolean beep){
        double beepInK = 0;

        //calculate the chance of a beep occuring in tile k once 
        for(TilePair tilePair: tilePairProbabilities.keySet()){
            //System.out.println("beepInK += " + "tilePairProbabilities.get(tilePair): " + tilePairProbabilities.get(tilePair) + " * " + " probabilityBeepInKGivenLeaks(botPosition, tilePair.getTileOne(), tilePair.getTileTwo(), ship)): "+ probabilityBeepInKGivenLeaks(botPosition, tilePair.getTileOne(), tilePair.getTileTwo(), ship));
            beepInK += tilePairProbabilities.get(tilePair) * probabilityBeepInKGivenLeaks(botPosition, tilePair.getTileOne(), tilePair.getTileTwo(), ship);
        }


        if(!beep){
            beepInK = 1 - beepInK;
        }

        //System.out.println("P(beep in K ) = " + beepInK  + "\n");

        double max = 0;
        if(!highestPairProbability.isEmpty()){
            Iterator<TilePair> iterator = highestPairProbability.iterator();
            max = tilePairProbabilities.get(iterator.next());
        }
        

        for(TilePair tilePair: tilePairProbabilities.keySet()){
            double leaksInPair = tilePairProbabilities.get(tilePair);

            double beepInKGivenLeakInPair = probabilityBeepInKGivenLeaksAccountingForFoundLeek(botPosition, tilePair.getTileOne(), tilePair.getTileTwo(), ship);

            if(!beep){
                beepInKGivenLeakInPair = 1 - beepInKGivenLeakInPair;
            }

            //System.out.println("For Tile Pair: " + tilePair.toString() + " P(beep in K | Leak in Pair) = " + beepInKGivenLeakInPair + " P(leaks in Pair) = " + leaksInPair);

            double newPairProbability = leaksInPair * beepInKGivenLeakInPair / beepInK;

            if(newPairProbability > max){
                highestPairProbability.clear();
                highestPairProbability.add(tilePair);
                max = newPairProbability;
            }
            else if(newPairProbability == max) {
                highestPairProbability.add(tilePair);
            }

            

            tilePairProbabilities.put(tilePair, newPairProbability);

        }

        double sum = 0;
        for(TilePair tilePair: tilePairProbabilities.keySet()){
            sum += tilePairProbabilities.get(tilePair);
        }

        //System.out.println("Sum after updateTileProbabilities = " + sum);
        

    }
    /**
     * P( a leak causes a beep in i | leak in cell j AND leak in cell k ) = 1 - (1 - P( j does cause a beep in i | leak in cell j )) * (1 - P( k does cause a beep in i | leak in cell k ))
     * @return
     */
    private double probabilityBeepInKGivenLeaks(Tile beepTile, Tile leakOne, Tile leakTwo, Ship ship){

        return 1.0 - ((1.0 - formula(leakOne, beepTile, ship)) * (1 - formula(leakTwo, beepTile, ship)));
    }

    /*
     * Exactly the same as probabilityBeepInKGivenLeaks except that it accounts for when the leak is already found 
     */
    private double probabilityBeepInKGivenLeaksAccountingForFoundLeek(Tile beepTile, Tile leakOne, Tile leakTwo, Ship ship){
        if(leakOne.equals(leak)){
            //System.out.println("probabilityBeepInKGivenLeaks leak provided, returning formula: " + formula(leakTwo, beepTile, ship));
            return formula(leakTwo, beepTile, ship);
        }
        if(leakTwo.equals(leak)){
            //.out.println("probabilityBeepInKGivenLeaks leak provided, returning formula: " + formula(leakOne, beepTile, ship));
            return formula(leakOne, beepTile, ship);
        }
        return 1.0 - ((1.0 - formula(leakOne, beepTile, ship)) * (1 - formula(leakTwo, beepTile, ship)));
    }

        /**
     * Overloaded sense method for two leaks
     * Run sense on both leaks and if either returns true, returns true
     */
    @Override
    public boolean sense(Tile leak, Tile leakTwo, Ship ship) {
        return sense(leak, ship) || sense(leakTwo, ship);
    }

    /**
     * Same Sense method as Bot 3 
     */
    @Override
    public boolean sense(Tile leak, Ship ship) {
        if(leak == null){
            return false;
        }
        return probabilityRoll(formula(leak, botPosition, ship));
    }

    private double formula(Tile goal, Tile start, Ship ship ){
        double e = Math.E;
        int distanceToTile = distanceToTile(start , goal);

        //Covers the case where the formula is ran on the cell that contains the leak
        if(distanceToTile == 0){
            distanceToTile = 1;
        }
        //System.out.println("Formula probability: " + Math.pow(e, -1 * alpha * (distanceToTile-1)));
        return Math.pow(e, -1 * alpha * (distanceToTile-1));
    }

    private int distanceToTile(Tile startingTile, Tile goal)
    {
        if(startingTile.equals(goal)){
            return 0;
        }
        
        return tilePairDistances.get(new TilePair(startingTile, goal));
    }



    /**
     * Populates the tilePairDistances hashmap with the distance between every pair of Tiles on the ship
     * @param ship Reference to the ship 
     */
    public void initializeTilePairDistances(Ship ship){
        //Conduct an unrestricted bfs on every tile of the ship
        for(int row = 0; row < ship.getShipEdgeLength() ; row++){
            for (int col = 0; col < ship.getShipEdgeLength() ; col++){
                Tile shipTile = ship.getShipTile(row, col);
                //For each open tile in the ship:
                if(shipTile.getOpen()){
                    Queue<Tile> queue = new LinkedList<>();
                    Set<Tile> visited = new HashSet<>();
                    HashMap<Tile, Integer> distTo = new HashMap<>();

                    queue.add(shipTile);
                    visited.add(shipTile);
                    distTo.put(shipTile, 0);

                    //Fully loop through the entire queue to map all the distances from one point
                    while(!queue.isEmpty()){

                        Tile curr = queue.remove();

                        Set<Tile> botNeighbors = new HashSet<Tile>();

                        ship.fillNeighborsSet(curr, botNeighbors);

                        for(Tile neighbor: botNeighbors){
                            if(!visited.contains(neighbor)){
                                tilePairDistances.put( new TilePair(shipTile, neighbor), distTo.get(curr) +1);
                                distTo.put(neighbor,distTo.get(curr) + 1);
                                visited.add(neighbor);
                                queue.add(neighbor);
                            }
                            
                        }
                    }
                    
                }
            }
        }
    }

    @Override
    public void setNoLeakOnBot() {
        //If a tile dose not contain the leak, all pairs containing that tile also cannot contain the leak
        if(leak == null){
            double probabilityOffset = 0;
            for(TilePair tilePair: tilePairProbabilities.keySet()){
                if(tilePair.containsTile(botPosition)){
                    probabilityOffset += tilePairProbabilities.get(tilePair);
                    tilePairProbabilities.put(tilePair, 0.0);
                    highestPairProbability.remove(tilePair);
                }
            }

            for(TilePair tilePair: tilePairProbabilities.keySet()){
                tilePairProbabilities.put(tilePair, tilePairProbabilities.get(tilePair) / (1 -probabilityOffset) );
            }
        }
        //Since the leak has already been found, cross off one pair at a time
        else
        {
            //Ignore if bot is on top of the leak already found
            if(!leak.equals(botPosition)){
                TilePair tilePairRemoved = new TilePair(botPosition, leak);
                double probabilityOffset = tilePairProbabilities.get(tilePairRemoved);
                tilePairProbabilities.put(tilePairRemoved, 0.0);
                highestPairProbability.remove(tilePairRemoved);

                for(TilePair tilePair: tilePairProbabilities.keySet()){
                tilePairProbabilities.put(tilePair, tilePairProbabilities.get(tilePair) / (1 -probabilityOffset) );
                }
            }
            
        }

        double sum = 0;
        for(TilePair tilePair: tilePairProbabilities.keySet()){
            sum += tilePairProbabilities.get(tilePair);
        }

        //System.out.println("Sum after setNOLeakOnBot = " + sum);
        
    }

    //Initalizes th leak reference and sets the probability of all pairs that don't contain the leak already found to 0
    public void setLeakOnBot(){
        leak = botPosition;
        //System.out.println("Found first leak: " + leak);
        double probabilityOffset = 0;
        for(TilePair tilePair: tilePairProbabilities.keySet()){
            if(!tilePair.containsTile(leak)){
                //System.out.println("Tile Pair: " + tilePair + " does not contain leak, setting probability to 0");
                probabilityOffset += tilePairProbabilities.get(tilePair);
                tilePairProbabilities.put(tilePair, 0.0);
                highestPairProbability.remove(tilePair);
            }
        }

        for(TilePair tilePair: tilePairProbabilities.keySet()){
            tilePairProbabilities.put(tilePair, tilePairProbabilities.get(tilePair) / (1 -probabilityOffset) );
        }

        double sum = 0;
        for(TilePair tilePair: tilePairProbabilities.keySet()){
            sum += tilePairProbabilities.get(tilePair);
        }

        //System.out.println("Sum after setLeakOnBot = " + sum + " and leak: " + leak);

    }

    protected void botMove()
    {
        if(botPath.isEmpty())
        {
            return;
        }
        Tile toMove = botPath.remove(0);
        if(toMove.getOpen()){
            botPosition = toMove;
        }
        else{
            //System.out.println("Why he closed tho");
        }

    }

    

    public HashMap<TilePair,Integer> getTilePairDistances(){
        return tilePairDistances;
    }

    public HashMap<TilePair,Double> getTilePairProbabilities(){
        return tilePairProbabilities;
    }

    public HashSet<TilePair> getHighestPairProbability(){
        return highestPairProbability;
    }

    public Tile getFoundLeak(){
        return leak;
    }




    public static void main(String [] args)
	{
        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();

        experimentController.setBotEight(0.1);

        System.out.println(experimentController.runMultipleLeaksExperiment());

        /* 
        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();
        experimentController.setBotEight(0.1);
        experimentController.spawnTwoLeaks();
        
        BotEight botEight = (BotEight)experimentController.getBot();

        experimentController.printShip();

        botEight.updateProbabilitiesFromSense(experimentController.getShip(), true);

        botEight.setNoLeakOnBot();

        double sum = 0;

        for(TilePair tilePair: botEight.getTilePairDistances().keySet()){
            System.out.println("Tile Pair: " + tilePair.toString() + " Distance Between: " + botEight.getTilePairDistances().get(tilePair));
        }
        
        for(TilePair tilePair: botEight.getTilePairProbabilities().keySet()){
            
            System.out.println("Tile Pair:  " + tilePair.toString() + " Starting Probability: " + botEight.getTilePairProbabilities().get(tilePair));
            sum += botEight.getTilePairProbabilities().get(tilePair);
        }
        System.out.println("Sum: " + sum);

        
        for(TilePair tilePair: botEight.getHighestPairProbability()){
            System.out.println("Highest Probability: " + tilePair.toString() + botEight.getTilePairProbabilities().get(tilePair));
        }
        */
        /* 
        BotEight botEight = new BotEight(0.1);
        experimentController.printShip();

        botEight.initializeTilePairDistances(experimentController.getShip());

        botEight.initalizeProbabilities();


        Iterator<Map.Entry<TilePair, Integer>> iterator = botEight.getTilePairDistances().entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<TilePair, Integer> entry = iterator.next();
            System.out.println("Tile Pair: " + entry.getKey().toString() + " Distance: " + entry.getValue());
        }
        double sum = 0;
        for(TilePair tilePair: botEight.getTilePairProbabilities().keySet()){
            
            System.out.println("Tile Pair:  " + tilePair.toString() + " Starting Probability: " + botEight.getTilePairProbabilities().get(tilePair));
            sum += botEight.getTilePairProbabilities().get(tilePair);
        }
        System.out.println("Sum: " + sum);
        */
	}
    
}