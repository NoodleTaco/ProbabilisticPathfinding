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

public class BotNine extends BotEight{

    private int numSenses;
    private int numMoves;

    private int sensesLeft;

    public BotNine(){}

    public BotNine(double alpha,  int numSenses, int numMoves){
        super(alpha);
        this.numSenses = numSenses;
        this.sensesLeft = numSenses;
        this.numMoves = numMoves;
    }

    @Override
    public void botAction(Tile leak, Tile leakTwo, Ship ship){
        if(sensesLeft == 0){
            
            botMove();
            if(botPath.size() == 0){
                sensesLeft = numSenses;
            }
            //printShipProbabilities(ship);
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

            

            sensesLeft --;
            if(sensesLeft == 0){
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
                bfsInSet(ship, highestTilesSet, botPath, botPosition);
                trimBotPath();
            }
        }
    }

    /**
     * Reduces botPath's size to at most numMoves
     */
    private void trimBotPath(){
        while(botPath.size() > numMoves){
            botPath.remove(botPath.size() -1);
        }
    }

    public static void main(String [] args)
	{
        ExperimentController experimentController = new ExperimentController();
        experimentController.getShip().formShip();

        experimentController.setBotNine(0.1,3,5);

        System.out.println(experimentController.runMultipleLeaksExperiment());





	}

    
}