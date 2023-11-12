import java.util.Arrays;
import java.util.Comparator;

public class TilePair{
    private Tile tileOne;
    private Tile tileTwo;


    public TilePair(){}
    
    public TilePair(Tile tileOne, Tile tileTwo){
        this.tileOne = tileOne;
        this.tileTwo = tileTwo;
    }

    public Tile getTileOne(){
        return tileOne;
    }

    public Tile getTileTwo(){
        return tileTwo;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final TilePair other = (TilePair)obj;
        return (this.getTileOne().equals(other.getTileOne()) && this.getTileTwo().equals(other.getTileTwo())) ||
           (this.getTileOne().equals(other.getTileTwo()) && this.getTileTwo().equals(other.getTileOne()));
    }

    @Override
    public String toString() {
        return "(" + this.tileOne.toString() + " , " + this.tileTwo.toString() + ")";
    }

    
    @Override
    public int hashCode() {
        int result = 17; // Start with a prime number

        Tile[] tiles = new Tile[]{tileOne, tileTwo};
        Arrays.sort(tiles, Comparator.comparing(Tile::getRow).thenComparing(Tile::getCol));
    
        result = 31 * result + tiles[0].hashCode();
        result = 31 * result + tiles[1].hashCode();
    
        return result;
    }

    public boolean containsTile(Tile tile){
        return tileOne.equals(tile) || tileTwo.equals(tile);
    }

    /**
     * Takes in a tile as a parameter and if the pair contains that tile parameter, it returns the other tile in the pair
     * @param tile
     * @return
     */
    public Tile getOtherTile(Tile tile){
        if(tileOne.equals(tile)){
            return tileTwo;
        }
        if(tileTwo.equals(tile)){
            return tileOne;
        }
        else{
            return null;
        }
    }

    public static void main(String [] args)
	{
        Tile tileOne = new Tile();
        tileOne.setRow(0); 
        tileOne.setCol(0);
        Tile tileTwo = new Tile();
        tileTwo.setRow(0);
        tileTwo.setCol(2);

        

        TilePair tilePairOne = new TilePair(tileOne, tileTwo);

        TilePair tilePairTwo = new TilePair(tileTwo, tileOne);

        System.out.println(tilePairOne.toString() + "   " + tilePairTwo.toString());

        System.out.println(tilePairOne.equals(tilePairTwo));

        



	}


    

}