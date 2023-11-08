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
        if(this.getTileOne().equals(other.getTileOne()) && this.getTileTwo().equals(other.getTileTwo())){
            return true;
        }
        if(this.getTileOne().equals(other.getTileTwo()) && this.getTileOne().equals(other.getTileTwo())){
            return true;
        }
        return false;
    }

}