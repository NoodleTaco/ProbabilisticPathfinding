public class BotOne extends Bot{
    private int k;

    public BotOne() {}


    public BotOne(int k)
    {
        this.k = k;
    }

    public boolean sense(Tile leak, Ship ship)
    {
        int detectionArea = 2*k-1;
            
        int startX = Math.max(botPosition.getRow() - detectionArea, 0);
        int endX = Math.min(botPosition.getRow() + detectionArea,ship.getShipEdgeLength());
        
        int startY = Math.max(botPosition.getCol() - detectionArea, 0);
        int endY = Math.min(botPosition.getCol() + detectionArea,ship.getShipEdgeLength());

        return leak.getRow() >= startX && leak.getRow() <= endX && leak.getCol() >= startY && leak.getCol() <= endY;


    }


}