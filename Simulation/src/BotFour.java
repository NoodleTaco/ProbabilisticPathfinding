
public class BotFour extends BotThree{

    private int numSenses;
    private int numMoves;

    private int sensesLeft;

    public BotFour(){

    }
    
    public BotFour(double alpha, int numSenses, int numMoves){
        super(alpha);
        this.numSenses = numSenses;
        this.sensesLeft = numSenses;
        this.numMoves = numMoves;
    }

    @Override
    public void botAction(Tile leak, Ship ship){
        if(sensesLeft == 0){
            
            botMove();
            if(botPath.size() == 0){
                sensesLeft = numSenses;
            }
            printShipProbabilities(ship);
        }
        else{
            if(sense(leak, ship)){
                System.out.println("beep");
                System.out.println();
                updateProbabilitiesFromSense(ship, true);
            }
            else{
                System.out.println("no beep");
                System.out.println();
                updateProbabilitiesFromSense(ship, false);
            }
            sensesLeft --;
            if(sensesLeft == 0){
                updateHighestProbabilities();
                bfsInSetWithExceptions(ship, highestProbabilties, botPath, botPosition, highestProbabilties);
                trimBotPath();
            }
            printShipProbabilities(ship);
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

        experimentController.setBotFour(0.1, 3, 3);

        experimentController.runExperiment();





	}


}