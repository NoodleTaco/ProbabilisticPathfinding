import java.util.HashMap;
import java.util.HashSet;

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


    @Override
    public boolean sense(Tile leak, Tile leakTwo, Ship ship) {
        return probabilityRoll(formula(leak, botPosition, ship));
    }





    @Override
    public void setNoLeakOnBot() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNoLeakOnBot'");
    }
    
}