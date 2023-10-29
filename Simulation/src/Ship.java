import java.util.Random;
import java.util.Set;
import java.util.ArrayList;
/**
 * Defines the Ship where the simulation takes place
 * An instance of this method defines one ship 
 * The ship is a square matrix where each of its entries is a Tile 
 */
public class Ship {

    private Tile[][] ship;
    private int shipEdgeLength;
    private ArrayList<Tile> neighbors;
    private ArrayList<Tile> deadEnds;

    static final int defaultShipLength = 20;

    /**
     * Default constructor for a Ship with a ship edge length of 50.
     */    
    public Ship()
    {
        this.shipEdgeLength = defaultShipLength;
        ship = new Tile[defaultShipLength][defaultShipLength];
        neighbors = new ArrayList<Tile>();
        deadEnds = new ArrayList<Tile>();
    }

    /**
     * Constructor for a Ship with a custom ship edge length.
     * @param shipEdgeLength The edge length of the ship.
     */
    public Ship(int shipEdgeLength)
    {
        this.shipEdgeLength = shipEdgeLength;
        ship = new Tile[shipEdgeLength][shipEdgeLength];
        neighbors = new ArrayList<Tile>();
        deadEnds = new ArrayList<Tile>();
    }



    /**
     * Generates the ship by setting tiles as open or closed
     * This method generates a randomized ship layout.
     */

    public void formShip()
    {
        for(int row = 0; row < shipEdgeLength; row++)
        {
            for (int col = 0; col < shipEdgeLength; col++)
            {
                ship[row][col] = new Tile();
                ship[row][col].setRow(row);
                ship[row][col].setCol(col);
            }
        }

        Random rand = new Random();


        int xStart = rand.nextInt(shipEdgeLength);
        int yStart = rand.nextInt(shipEdgeLength);
        ship[xStart][yStart].setOpen(true);
        checkNeighbors(ship[xStart][yStart]);

        while(!neighbors.isEmpty())
        {
            int randNeighbor = rand.nextInt(neighbors.size());
            if (!ship[neighbors.get(randNeighbor).getRow()][neighbors.get(randNeighbor).getCol()].getTooManyNeighbors())
            {
                ship[neighbors.get(randNeighbor).getRow()][neighbors.get(randNeighbor).getCol()].setOpen(true);
                checkNeighbors(ship[neighbors.get(randNeighbor).getRow()][neighbors.get(randNeighbor).getCol()]);
            }
            neighbors.remove(randNeighbor);
        }
        createDeadEnds();


        
    }
    /**
     * Checks if a tile has more than one open neighbor and changes its property if it does. 
     * @param tile The Tile for which neighbors need to be checked.
     */

    private void checkNeighbors(Tile tile)
    {
        if(tile.getRow() + 1 < shipEdgeLength && !ship[tile.getRow()][tile.getCol()].getTooManyNeighbors())
        {
            if(ship[tile.getRow() +1][tile.getCol()].getIsNeighbor())
            {
                ship[tile.getRow() +1][tile.getCol()].setTooManyNeighbors();
            }
            else if(!ship[tile.getRow() +1][tile.getCol()].getOpen())
            {
                ship[tile.getRow() +1][tile.getCol()].setIsNeighbor();
                neighbors.add(ship[tile.getRow() +1][tile.getCol()]);
            }
            
        }

        if(tile.getRow() -1 > -1 && !ship[tile.getRow()][tile.getCol()].getTooManyNeighbors())
        {
            if(ship[tile.getRow() -1][tile.getCol()].getIsNeighbor())
            {
                ship[tile.getRow() -1][tile.getCol()].setTooManyNeighbors();
            }
            else if(!ship[tile.getRow() -1][tile.getCol()].getOpen())
            {
                ship[tile.getRow() -1][tile.getCol()].setIsNeighbor();
                neighbors.add(ship[tile.getRow() -1][tile.getCol()]);
            }
        }
        
        if(tile.getCol() -1 > -1 && !ship[tile.getRow()][tile.getCol()].getTooManyNeighbors())
        {
            if(ship[tile.getRow()][tile.getCol() -1].getIsNeighbor())
            {
                ship[tile.getRow()][tile.getCol() -1].setTooManyNeighbors();
            }
            else if(!ship[tile.getRow()][tile.getCol() -1].getOpen())
            {
                ship[tile.getRow()][tile.getCol() -1].setIsNeighbor();
                neighbors.add(ship[tile.getRow()][tile.getCol() -1]);
            }
        }

        if(tile.getCol() +1 < shipEdgeLength && !ship[tile.getRow()][tile.getCol()].getTooManyNeighbors())
        {
            if(ship[tile.getRow()][tile.getCol() +1].getIsNeighbor())
            {
                ship[tile.getRow()][tile.getCol() +1].setTooManyNeighbors();
            }
            else if(!ship[tile.getRow()][tile.getCol() +1].getOpen())
            {
                ship[tile.getRow()][tile.getCol() +1].setIsNeighbor();
                neighbors.add(ship[tile.getRow()][tile.getCol() +1]);
            }
        }
    }
    
    /**
     * Adds open tiles adjacent to a select tile to a list
     * @param tile The tile whose neighbors are being considered
     * @param list The list where tile objects are being added 
     */
    public void fillNeighborsList(Tile tile, ArrayList<Tile> list)
    {
        if(tile.getRow() + 1 < shipEdgeLength && getShipTile(tile.getRow() + 1, tile.getCol()).getOpen() && !list.contains(getShipTile(tile.getRow() + 1, tile.getCol()))) 
        {
            list.add(getShipTile(tile.getRow() + 1, tile.getCol()));
        }

        if(tile.getRow() - 1 > -1 && getShipTile(tile.getRow() - 1, tile.getCol()).getOpen() && !list.contains(getShipTile(tile.getRow() - 1, tile.getCol())))
        {
            list.add(getShipTile(tile.getRow() - 1, tile.getCol()));
        }

        if(tile.getCol() + 1 < shipEdgeLength && getShipTile(tile.getRow() , tile.getCol() + 1).getOpen() && !list.contains(getShipTile(tile.getRow() , tile.getCol() + 1)))
        {
            list.add(getShipTile(tile.getRow(), tile.getCol() + 1));
        }

        if(tile.getCol() - 1 > -1 && getShipTile(tile.getRow() , tile.getCol() -1).getOpen() && !list.contains(getShipTile(tile.getRow() , tile.getCol() - 1)))
        {
            list.add(getShipTile(tile.getRow(), tile.getCol() -1));
        }
        
    }

    /**
     * Adds open tiles adjacent to a select tile to a set
     * @param tile The tile whose neighbors are being considered
     * @param set The set where tile objects are being added 
     */
    public void fillNeighborsSet(Tile tile, Set<Tile> set)
    {
        if(tile.getRow() + 1 < shipEdgeLength && getShipTile(tile.getRow() + 1, tile.getCol()).getOpen()) 
        {
            set.add(getShipTile(tile.getRow() + 1, tile.getCol()));
        }

        if(tile.getRow() - 1 > -1 && getShipTile(tile.getRow() - 1, tile.getCol()).getOpen() )
        {
            set.add(getShipTile(tile.getRow() - 1, tile.getCol()));
        }

        if(tile.getCol() + 1 < shipEdgeLength && getShipTile(tile.getRow() , tile.getCol() + 1).getOpen() )
        {
            set.add(getShipTile(tile.getRow(), tile.getCol() + 1));
        }

        if(tile.getCol() - 1 > -1 && getShipTile(tile.getRow() , tile.getCol() -1).getOpen() )
        {
            set.add(getShipTile(tile.getRow(), tile.getCol() -1));
        }
    }



    
    /**
     * Prints a visual representation of the ship, used for testing.
     * Different colors are used to represent different tile states.
     * Yellow indicates an open tile that is a dead end
     * White indicates an open tile
     * Green indicates a closed tile that has too many neighbors to be opened
     * Purple indicates that the tile is neighboring an open square: Not seen in the final stage of the ship
     * Red indicates a closed tile 
     */ 
    private void printShip()
    {

        String reset = "\u001B[0m";
        String red = "\u001B[31m";
        String green = "\u001B[32m";
        String yellow = "\u001B[33m";
        String white = "\u001B[37m";
        String purple = "\u001B[35m";
        
        for(int row = 0; row < shipEdgeLength; row++)
        {
            for (int col = 0; col < shipEdgeLength; col++)
            {
                
                if(ship[row][col].getOpen())
                {
                    if(ship[row][col].getIsDeadEnd())
                    {
                        System.out.print(yellow + "■ " + reset);
                    }
                    else
                    {
                        System.out.print(white + "■ " + reset);
                    }
                }

                else if(ship[row][col].getTooManyNeighbors())
                {
                    System.out.print(green + "■ " + reset);
                }

                else if(ship[row][col].getIsNeighbor())
                {
                    System.out.print(purple + "■ " + reset);
                }

                else if(!ship[row][col].getOpen())
                {
                    System.out.print(red + "■ " + reset);
                }
            }
            System.out.println();
        }
    }

    /**
     * Prints the list of neighboring tiles for debugging purposes.
     */
    private void printNeighborsList()
    {
        for(int i = 0; i < neighbors.size(); i++)
        {
            System.out.print(neighbors.get(i).getRow() + ", " + neighbors.get(i).getCol());
            System.out.println();
        }
        
    }

    /**
     * Prints the list of dead-end tiles for debugging purposes.
     */
    private void printDeadEndsList()
    {
        //System.out.println("Number of Dead ends: " + deadEnds.size());
        for(int i = 0; i < deadEnds.size(); i++)
        {
            System.out.print(deadEnds.get(i).getRow() + ", " + deadEnds.get(i).getCol());
            System.out.println();
        }
    }

    /**
     * Identifies and marks dead-end tiles in the ship.
     */
    private void findDeadEnds()
    {
        for(int row = 0; row < shipEdgeLength; row++)
        {
            for (int col = 0; col < shipEdgeLength; col++)
            {
                isDeadEnd(row, col);
            }
        }
        

    }

    /**
     * Checks if a specific tile is a dead-end and updates its status.
     * @param row The row index of the tile.
     * @param col The column index of the tile.
     */
    private void isDeadEnd(int row, int col)
    {
        if((row < shipEdgeLength && row > -1 && col < shipEdgeLength && col > -1))
        {
            if(ship[row][col].getOpen())
            {
                int count = 0;
                if(row + 1 < shipEdgeLength && ship[row +1][col].getOpen())
                {
                    count ++;
                }

                if(row - 1 > -1 && ship[row -1][col].getOpen())
                {
                    count ++;
                }               
                
                if(col -1 > -1 && ship[row][col-1].getOpen())
                {
                    count ++;
                }

                if(col +1 < shipEdgeLength && ship[row][col+1].getOpen())
                {
                    count ++;
                }           
                if(count == 1)
                {
                    ship[row][col].setIsDeadEnd(true);
                    if(!deadEnds.contains(ship[row][col]))
                    {
                        deadEnds.add(ship[row][col]);
                    }
                }
                else
                {
                    ship[row][col].setIsDeadEnd(false);
                    if(deadEnds.contains(ship[row][col]))
                    {
                        deadEnds.remove(ship[row][col]);
                    }
                }                
            }


        }


    }

    /**
     * Opens approximetly half of the dead ends in the ship
     */
    private void createDeadEnds()
    {
        findDeadEnds();

        int startingDeadEnds = deadEnds.size();

        Random rand = new Random();
        

        while(deadEnds.size() > startingDeadEnds/2)
        {
            Tile tile = deadEnds.get(rand.nextInt(deadEnds.size()));
            ArrayList<Tile> deadEndNeighbors = new ArrayList<Tile>();
            if(tile.getRow() + 1 < shipEdgeLength && !ship[tile.getRow() +1][tile.getCol()].getOpen())
            {
                deadEndNeighbors.add(ship[tile.getRow()+1][tile.getCol()]);
            }

            if(tile.getRow() - 1 > -1 && !ship[tile.getRow() -1][tile.getCol()].getOpen())
            {
                deadEndNeighbors.add(ship[tile.getRow()-1][tile.getCol()]);
            }               
            
            if(tile.getCol() -1 > -1 && !ship[tile.getRow()][tile.getCol()-1].getOpen())
            {
                deadEndNeighbors.add(ship[tile.getRow()][tile.getCol()-1]);
            }

            if(tile.getCol() +1 < shipEdgeLength && !ship[tile.getRow()][tile.getCol()+1].getOpen())
            {
                deadEndNeighbors.add(ship[tile.getRow()][tile.getCol()+1]);
            }

            
            int randTile = rand.nextInt(deadEndNeighbors.size());
            deadEndNeighbors.get(randTile).setOpen(true);
            tile.setIsDeadEnd(false);
            deadEnds.remove(tile);
            isDeadEnd( deadEndNeighbors.get(randTile).getRow() +1,  deadEndNeighbors.get(randTile).getCol());
            isDeadEnd( deadEndNeighbors.get(randTile).getRow() -1 , deadEndNeighbors.get(randTile).getCol());
            isDeadEnd( deadEndNeighbors.get(randTile).getRow() , deadEndNeighbors.get(randTile).getCol() -1);
            isDeadEnd( deadEndNeighbors.get(randTile).getRow() , deadEndNeighbors.get(randTile).getCol() +1);

        }
    }


    /**
     * Returns the edge length of the ship.
     * @return The edge length of the ship.
     */
    public int getShipEdgeLength()
    {
        return shipEdgeLength;
    }

    /**
     * Returns a Tile object from the ship grid at the specified row and column.
     * @param row The row index of the Tile.
     * @param col The column index of the Tile.
     * @return The Tile object at the specified coordinates.
     */
    public Tile getShipTile(int row, int col)
    {
        return ship[row][col];
    }

    

    

    /**
     * Main method for testing the Ship class.
     * 
     * @param args 
     * @throws Exception
     */
    public static void main(String[] args) throws Exception 
    {
        Ship ship = new Ship();
        ship.formShip();
        System.out.println();
        ship.printShip();

    }
    
}
