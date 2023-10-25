/**
 * An instance of this class represents one element in the Ship matrix
 */
public class Tile {
    private boolean isOpen;
    private boolean isNeighbor;
    private boolean tooManyNeighbors;
    private boolean isDeadEnd;
    private int row;
    private int col;

    /**
     * Default constructor for a Tile.
     */
    public Tile ()
    {
        isOpen = false;
        isNeighbor = false;
        tooManyNeighbors = false;
        isDeadEnd = false;
    }

    /**
     * Gives the open status of this tile
     * @return true if the tile is open, false otherwise.
     */
    public boolean getOpen()
    {
        return isOpen;
    }


    /**
     * Get the status of whether this tile is adjacent to an open Tile
     * @return True if the tile is a neighbor, false otherwise.
     */
    public boolean getIsNeighbor()
    {
        return isNeighbor;
    }

    /**
     * Get the status of whether this tile is adjacent to more than one neighbor
     * @return True if the tile has too many neighbors, false otherwise.
     */
    public boolean getTooManyNeighbors()
    {
        return tooManyNeighbors;
    }

    /**
     * Get the status of whether this tile is a dead end.
     * @return True if the tile is a dead end, false otherwise.
     */
    public boolean getIsDeadEnd()
    {
        return isDeadEnd;
    }

    /**
     * Set the status of whether this tile is open.
     * @param open True to mark the tile as open, false to mark it as closed.
     */
    public void setOpen(boolean open)
    {
        isOpen = open;
    }
    /**
     * Set this tile as having too many neighbors
     */
    public void setTooManyNeighbors()
    {
        tooManyNeighbors = true;
    }

    /**
     * Set a tile as having an adjacent neighbor
     */
    public void setIsNeighbor()
    {
        isNeighbor = true;
    }

    /**
     * Set the status of whether this tile is a dead end.
     * @param deadEnd True to mark the tile as a dead end, false otherwise.
     */
    public void setIsDeadEnd(boolean deadEnd)
    {
        isDeadEnd = deadEnd;
    }

    /**
     * Set the row index of this tile.
     * @param row The row index to set.
     */
    public void setRow(int row)
    {
        this.row = row;
    }

    /**
     * Set the column index of this tile.
     * @param col The column index to set.
     */
    public void setCol(int col)
    {
        this.col = col;
    }

    /**
     * Get the row index of this tile.
     * @return The row index of the tile.
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Get the column index of this tile.
     * @return The column index of the tile.
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Checks if this tile is equal to another object based on its position in the ship
     * @param obj The object to compare with this tile.
     * @return True if the objects are equal in terms of row and column indices, false otherwise.
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Tile other = (Tile)obj;
        if(this.getRow() == other.getRow() && this.getCol() == other.getCol())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * Returns a string representation of this tile in the format "(row, col)".
     * @return A string representation of this tile's row and column indices.
     */
    @Override
    public String toString() {
        return "(" + this.row + " , " + this.col + ")";
    }

}
