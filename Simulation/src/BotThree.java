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
        double startingProbability = 1.0/(ship.getShipEdgeLength() * ship.getShipEdgeLength());
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
    /**
     * Gives the distance from a tile to another
     * Handles exceptions where the goal tile is closed or a corner tile surrounded by closed tiles
     * @param goal The tile being searched for 
     * @param ship The ship
     * @param startingTile The tile where the search begins 
     * @return The shortest path to the tile being searched for 
     */
    private int distanceToTile(Tile goal, Ship ship, Tile startingTile)
    {
        if(startingTile.equals(goal)){
            return 0;
        }
        HashSet<Tile> goalSet = new HashSet<Tile>();
        goalSet.add(goal);
        ArrayList<Tile> path = new ArrayList<Tile>();
        Set<Tile> exception = new HashSet<Tile>();

        //This conditional handles a situation where the probability of a corner closed cell is being calculated and it is surrounded by closed cells
        if(ship.isCorner(startingTile) && !startingTile.getOpen()){
            Set<Tile> cornerSet = new HashSet<Tile>();
            ship.fillClosedNeighborsSet(startingTile, cornerSet);
            //The two nodes neighboring the corner are closed
            if(cornerSet.size() ==2){
                for(Tile tile: cornerSet){
                    exception.add(tile);
                }
            }
        }

        exception.add(goal);
        bfsInSetWithExceptions(ship, goalSet, path, startingTile, exception);
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
                //System.out.println("beep");
                //System.out.println();
                updateProbabilitiesFromSense(ship, true);
            }
            else{
                //System.out.println("no beep");
                //System.out.println();
                updateProbabilitiesFromSense(ship, false);
            }
            updateHighestProbabilities();
            bfsInSetWithExceptions(ship, highestProbabilties, botPath, botPosition, highestProbabilties);
        }
    }

    private void updateProbabilitiesFromSense(Ship ship, boolean beep){
        
        //System.out.println("Updating Probabilities From Sense: \n");

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

        //System.out.println("P(Beep in i ) = " + beepInI + "\n");
        
        Iterator<Map.Entry<Tile, Double>> iterator = tileProbabilities.entrySet().iterator();

        //Adjust each tile's probability by dividing it by 1 - <the original probaiblity of the tile determined to have no leak>
        while(iterator.hasNext()){
            Map.Entry<Tile, Double> entry = iterator.next();
            double leakInJ = entry.getValue();

            //System.out.print("For Tile " + entry.getKey().toString() + " ");

            //System.out.print("P(leak in j) = " + leakInJ + " ");

            double beepInIGivenLeakInJ = formula(entry.getKey(), botPosition, ship);

            //System.out.print("P(beep in i | leak in j) = " + beepInIGivenLeakInJ + " ");

            //Update the probability of cell j through formula P(leak in j | beep in cell i) = P(beep in cell i | leak in j) * P(leak in j) / P(beep in i)
            tileProbabilities.put(entry.getKey(), leakInJ * beepInIGivenLeakInJ / beepInI);

            //System.out.println();
        }
    }

    private double formula(Tile goal, Tile start, Ship ship ){
        double e = Math.E;
        int distanceToTile = distanceToTile(goal, ship, start);

        //Covers the case where the formula is ran on the cell that contains the leak
        if(distanceToTile == 0){
            distanceToTile = 1;
        }
        //System.out.println("Formula probability: " + Math.pow(e, -1 * alpha * (distanceToTile-1)));
        return Math.pow(e, -1 * alpha * (distanceToTile-1));
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
    private void updateHighestProbabilities(){
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

    public HashMap<Tile, Double> getTileProbabilities(){
        return tileProbabilities;
    }

    public HashSet<Tile> getHighestProbabilities(){
        return highestProbabilties;
    }



    public static void main(String [] args)
	{
        /* 
        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();



        experimentController.setBotThree(0.1);
        experimentController.spawnOutsideOfDetection();

        experimentController.printShip();

        BotThree botThree = (BotThree)experimentController.getBot();


        System.out.println("Bot Position: " + experimentController.getBot().getBotPosition().toString() + " Leak Position: " + experimentController.getLeak().toString());

        System.out.println();

        experimentController.getBot().setNoLeakOnBot();

        experimentController.getBot().botAction(experimentController.getLeak(), experimentController.getShip());
        

        for(int row = 0 ; row < experimentController.getShip().getShipEdgeLength(); row ++){
            for(int col = 0; col < experimentController.getShip().getShipEdgeLength(); col ++){
                Tile tile = experimentController.getShip().getShipTile(row, col);
                System.out.println("Tile: " + tile.toString() + " Probability: " +  botThree.getTileProbabilities().get(experimentController.getShip().getShipTile(row, col)));
            }
        }

        System.out.println();

        botThree.updateHighestProbabilities();
        for(Tile tile: botThree.getHighestProbabilities()){
            System.out.println("Tile: " + tile.toString() + " Probability: " +  botThree.getTileProbabilities().get(tile));
        }
        */

        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();
        experimentController.setBotThree(0.1);
        System.out.println(experimentController.runExperiment());

	}

}