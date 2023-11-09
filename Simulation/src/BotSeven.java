import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BotSeven extends Bot{

    protected double alpha;

    protected HashMap<Tile, Double> tileProbabilities;

    protected HashSet<Tile> highestProbabilties;

    public BotSeven(){}

    public BotSeven(double alpha){
        super();
        this.alpha = alpha;
        tileProbabilities = new HashMap<Tile, Double>();
        highestProbabilties = new HashSet<Tile>();
    }

    public void initalizeProbabilities(Ship ship){
        int numOpenTile = ship.getNumOpenTiles();
        double startingProbability = 1.0/(numOpenTile);
        for(int row = 0; row < ship.getShipEdgeLength(); row++){
            for (int col = 0; col < ship.getShipEdgeLength(); col ++){
                if(ship.getShipTile(row, col).getOpen()){
                    tileProbabilities.put(ship.getShipTile(row, col), startingProbability);
                }
                else{
                    tileProbabilities.put(ship.getShipTile(row, col), 0.0);
                }
            }
        }
    }


    /**
     * Runs sense(Tile leak, ship ship) on the closer leak
     */
    @Override
    public boolean sense(Tile leak, Tile leakTwo, Ship ship) {
        HashSet<Tile> leakSet = new HashSet<Tile>();
        leakSet.add(leak); leakSet.add(leakTwo);
        ArrayList<Tile> list = new ArrayList<Tile>();
        bfsInSet(ship, leakSet, list, botPosition);

        if(list.isEmpty()){
            return false;
        }
        return sense(list.get(list.size()-1), ship);
    }

    @Override
    public boolean sense(Tile leak, Ship ship) {
        return probabilityRoll(formula(leak, botPosition, ship));
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

    /**
     * Gives the distance from a tile to another
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


        bfsInSet(ship, goalSet, path, startingTile);
        int dist = path.size();
        return dist;
    }


    @Override
    public void botAction(Tile leak, Tile leakTwo, Ship ship) {
        if(!botPath.isEmpty()){
            botMove();
            botPath.clear();
            printShipProbabilities(ship);
        }
        else{
            if(sense(leak, leakTwo, ship)){
                System.out.println("beep");
                System.out.println();
                updateProbabilitiesFromSense(ship, true);
            }
            else{
                System.out.println("no beep");
                System.out.println();
                updateProbabilitiesFromSense(ship, false);
            }
            updateHighestProbabilities();
            bfsInSet(ship, highestProbabilties, botPath, botPosition);
            printShipProbabilities(ship);
        }
    }

    protected void updateProbabilitiesFromSense(Ship ship, boolean beep){
        
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
            if(!entry.getKey().getOpen()) {
                continue;
            }
            double leakInJ = entry.getValue();

            //System.out.print("For Tile " + entry.getKey().toString() + " ");

            //System.out.print("P(leak in j) = " + leakInJ + " ");

            double beepInIGivenLeakInJ = formula(entry.getKey(), botPosition, ship);

            if(!beep){
                beepInIGivenLeakInJ = 1 - beepInIGivenLeakInJ;
            }

            //System.out.print("P(beep in i | leak in j) = " + beepInIGivenLeakInJ + " ");

            //Update the probability of cell j through formula P(leak in j | beep in cell i) = P(beep in cell i | leak in j) * P(leak in j) / P(beep in i)
            tileProbabilities.put(entry.getKey(), leakInJ * beepInIGivenLeakInJ / beepInI);

            //System.out.println();
        }
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
    protected void updateHighestProbabilities(){
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

    public void printShipProbabilities(Ship ship){
        DecimalFormat decimalFormat = new DecimalFormat("0.000");

        for(int row = 0; row < ship.getShipEdgeLength(); row ++){
            for(int col = 0; col < ship.getShipEdgeLength(); col ++){
                System.out.print(decimalFormat.format(tileProbabilities.get(ship.getShipTile(row, col))) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String [] args)
	{
        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();

        experimentController.setBotSeven(0.1);

        System.out.println(experimentController.runMultipleLeaksExperiment());





	}

    
}