import java.io.FileNotFoundException;
import java.io.FileInputStream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Cave {

    private int tiles[][];
    private boolean visible[][];
    public static final int xOffset = 20;
    public static final int yOffset = 20;
    //knowledge base - representation of facts about the world
    public static final int GROUND = 0, PIT=1, WUMPUS=2, GOLD=3, WIND=10, STENCH=20, GLITTER=30, DEAD=404;
    private String path = "C:\\Users\\USER\\eclipse-workspace\\Wumpus\\src\\images\\";
    private Image groundImage,pitImage,wumpusImage,goldImage,windImage,stenchImage,deadImage;

    public Cave(){
        tiles = new int[10][10];
        visible = new boolean[10][10];
        //tiles[0][4] = WUMPUS;
        try {
            FileInputStream inputStream = new FileInputStream(path + "g5.png");
            groundImage = new Image(inputStream,50,50,false,false);
            inputStream = new FileInputStream(path + "pit.png");
            pitImage = new Image(inputStream,50,50,false,false);
            inputStream = new FileInputStream(path + "wumpus.png");
            wumpusImage = new Image(inputStream,50,50,false,false);
            inputStream = new FileInputStream(path + "gold.png");
            goldImage = new Image(inputStream,50,50,false,false);
            inputStream = new FileInputStream(path + "wind2.png");
            windImage = new Image(inputStream,50,50,false,false);
            inputStream = new FileInputStream(path + "stench.png");
            stenchImage = new Image(inputStream,50,50,false,false);
            inputStream = new FileInputStream(path + "dead2.png");
            deadImage = new Image(inputStream,50,50,false,false);
            
            
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        

    }

    public void draw(GraphicsContext gc){

        for(int row = 0; row < tiles.length; row++){
            for(int col = 0; col < tiles.length; col++){
                if(tiles[row][col]==GROUND){
                    gc.drawImage(groundImage, xOffset+(col*50), yOffset+(row*50));
                }
                else if(tiles[row][col]==WUMPUS){
                    gc.drawImage(wumpusImage, xOffset+(col*50), yOffset+(row*50));
                }
                else if(tiles[row][col]==PIT){
                    gc.drawImage(pitImage, xOffset+(col*50), yOffset+(row*50));
                }
                else if(tiles[row][col]==GOLD){
                    gc.drawImage(goldImage, xOffset+(col*50), yOffset+(row*50));
                }
                else if(tiles[row][col]==WIND){
                    gc.drawImage(windImage, xOffset+(col*50), yOffset+(row*50));
                }
                else if(tiles[row][col]==STENCH){
                    gc.drawImage(stenchImage, xOffset+(col*50), yOffset+(row*50));
                }
                else if(tiles[row][col]==DEAD){
                    gc.drawImage(deadImage, xOffset+(col*50), yOffset+(row*50));
                }
                
            
            }
        }

    }

    public Image getGroundImage(){
        return groundImage;
    }
    public Image getGoldImage(){
        return goldImage;
    }
    public Image getWumpusImage(){
        return wumpusImage;
    }
    public Image getPitImage(){
        return pitImage;
    }
    public Image getDeadImage(){
        return deadImage;
    }

    public void setTile(Location location, int tileID){
        if(isValid(location)){
            tiles[location.getRow()][location.getCol()] = tileID;
            if(tileID != 3 && tileID != 404){
                updateTileHints(tileID, location.getRow(), location.getCol());

            }
            
        }
    }

    public void updateTileHints(int tileID, int row, int col){
        Location above = new Location( row - 1, col);
        Location below = new Location( row + 1, col);
        Location left = new Location( row , col - 1);
        Location right = new Location( row, col + 1);

        if(isValid(above)){
            tiles[above.getRow()][above.getCol()] = tileID*10;
        }
        if(isValid(below)){
            tiles[below.getRow()][below.getCol()] = tileID*10;
        }
        if(isValid(left)){
            tiles[left.getRow()][left.getCol()] = tileID*10;
        }
        if(isValid(right)){
            tiles[right.getRow()][right.getCol()] = tileID*10;
        }
    }

    

    public boolean isValid(Location location){
        return location.getRow() >=0 && location.getRow() < tiles.length && location.getCol() >=0 && location.getCol() < tiles.length;
    }

    public int getTileStatus(int row, int col){
            int hint = tiles[row][col];
            return hint;
    }


    
}
