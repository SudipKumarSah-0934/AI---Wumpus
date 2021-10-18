import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
public class Player {
    private String path = "C:\\Users\\USER\\eclipse-workspace\\Wumpus\\src\\images\\";
    private Location location;
    private Image playerImage;
    private Cave cave;
    public int score = 0;
    

    public Player(Cave cave){
        this.cave = cave;
        location = new Location(9, 0);

        try {
            FileInputStream inputStream = new FileInputStream(path + "boy4.png");
            playerImage = new Image(inputStream,50,50,false,false);
            
            
            
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext gc){
        gc.drawImage(playerImage, location.getCol()*50 + Cave.xOffset, location.getRow()*50 + Cave.yOffset);
    }

    public void moveRight(){
        Location rLocation = new Location(location.getRow(), location.getCol() + 1);

        if(cave.isValid(rLocation)){
            location.setCol(location.getCol() + 1);

        }
        
        
    }
    public void moveLeft(){
        Location lLocation = new Location(location.getRow(), location.getCol() - 1);

        if(cave.isValid(lLocation)){
            location.setCol(location.getCol() - 1);

        }
        
    }
    public void moveUp(){
        Location uLocation = new Location(location.getRow()-1, location.getCol());
        //checking bound
        if(cave.isValid(uLocation)){
            location.setRow(location.getRow() - 1);

        }
        
    }
    public void moveDown(){

        Location dLocation = new Location(location.getRow()+1, location.getCol());

        if(cave.isValid(dLocation)){
            location.setRow(location.getRow() + 1);

        }
        
    }

    public void move(Location locationP){

        //Location dLocation = new Location(location.getRow()+1, location.getCol());

        if(cave.isValid(locationP)){
            location.setRow(locationP.getRow());
            location.setCol(locationP.getCol());
            //System.out.println("moved in : " + location.getRow() + "," + location.getCol());

        }
        
    }

    public int checkStatus(){
        
        int row = location.getRow();
        int col = location.getCol();
        int hint = cave.getTileStatus(row, col);
        
        return hint;

    }

    public Location getPlayerLocation(){
        return location;
    }
    
}
