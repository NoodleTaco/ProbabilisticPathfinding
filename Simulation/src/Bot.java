import java.util.ArrayList;

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
    public Bot(){
    }

    public void setBotPosition(Tile tile)
    {
        this.botPosition = tile;
    }

    public Tile getBotPosition()
    {
        return botPosition;
    }

    public abstract boolean sense(Tile leak, Ship ship);





}