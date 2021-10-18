import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;

public class App extends Application{

	private String path = "C:\\Users\\USER\\eclipse-workspace\\Wumpus\\src\\images\\";
	ArrayList<String> input = new ArrayList<String>();
	private Image playerImage;
	Cave cave= new Cave();
	private int mouseX,mouseY;
	private int currentlySelectedTile = -1;
	Player player = new Player(cave);
	public int score = 0;
	public boolean goldFound = false;
	public boolean isWumpusDead = false;
	public boolean isAgentDead = false;
	int rPrev = 9, cPrev = 0, n = 10;
	int[][] isVisited = new int[10][10];
	int[][] isBreeze = new int[10][10];
	int[][] isStench = new int[10][10];
	Board [][] knowledgeBoard = new Board[10][10];
	Location cLocation;
	Location pLocation;
	int cRow,cCol,pRow,pCol;


	@Override
	public void start(Stage stage) throws Exception {

		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				isBreeze[i][j] = 0;
			}
		}
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				isStench[i][j] = 0;
			}
		}

		stage.setTitle("Wumpus world");
		FileInputStream inputStream = new FileInputStream(path + "boy4.png");
		playerImage = new Image(inputStream,50,50,false,false);

		Group root = new Group();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		Canvas canvas = new Canvas(800,600);
		root.getChildren().add(canvas);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		Button button = new Button();
		//Setting text to the button
		button.setText("Start Game");
		button.setLayoutX(650);
		button.setLayoutY(350);
		root.getChildren().add(button);
		button.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {

				for(int i=0; i<10; i++){
					for(int j=0; j<10; j++){
						isVisited[i][j] = 0;
					}
				}

				//System.out.println("Button pressed");
				AImove();

			}

		});

		// Button manualPlayButton = new Button(); // Setting text to the button
		// manualPlayButton.setText("Manual Play");
		// manualPlayButton.setLayoutX(650);
		// manualPlayButton.setLayoutY(400);
		// root.getChildren().add(manualPlayButton);

		// manualPlayButton.setOnAction(new EventHandler<ActionEvent>(){
		//     @Override
		//     public void handle(ActionEvent event) {
		//         processInput();

		//     }

		// });

		Button randomButton = new Button(); // Setting text to the button
		randomButton.setText("Random World");
		randomButton.setLayoutX(650);
		randomButton.setLayoutY(450);
		root.getChildren().add(randomButton);

		randomButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override

			public void handle(ActionEvent event) {
				// System.out.println("Button works?");
				String path = "src/images/";

				// try {
				//     FileInputStream inputStream = new FileInputStream(path + "pit.png");
				//     pitImage = new Image(inputStream, 50, 50, false, false);
				// } catch (FileNotFoundException e) {
				//     // TODO Auto-generated catch block
				//     e.printStackTrace();
				// }

				Random random2 = new Random();
				int numberOfPit = random2.nextInt(3 - 2) + 2;

				for (int i = 0; i < numberOfPit; i++) {

					int randomRow = random2.nextInt(9 - 0) + 0;
					int randomCol = random2.nextInt(9 - 0) + 0;
					Location location = new Location(randomRow, randomCol);
					cave.setTile(location, 1);

					// drawRandomWorld(gc, randomRow, randomCol);
				}

				int randomRow = random2.nextInt(9 - 0) + 0;
				int randomCol = random2.nextInt(9 - 0) + 0;
				Location location = new Location(randomRow, randomCol);
				cave.setTile(location, 2);

				int randomRow2 = random2.nextInt(9 - 0) + 0;
				int randomCol2 = random2.nextInt(9 - 0) + 0;
				Location location2 = new Location(randomRow2, randomCol2);
				cave.setTile(location2, 3);
			}

		});




		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				String code =event.getCode().toString();
				if(!input.contains(code)){
					input.add(code);
				}

			}

		});

		scene.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				mouseX = (int)event.getX();
				mouseY = (int)event.getY();

				if(mouseX >= 650 && mouseX <= 700 && mouseY >=80 && mouseY <=130){
					currentlySelectedTile = cave.PIT;

				}
				if(mouseX >= 650 && mouseX <= 700 && mouseY >=130 && mouseY <=180){
					currentlySelectedTile = cave.GOLD;

				}
				if(mouseX >= 650 && mouseX <= 700 && mouseY >=180 && mouseY <=230){
					currentlySelectedTile = cave.WUMPUS;

				}
				if(mouseX >= 650 && mouseX <= 700 && mouseY >=230 && mouseY <=280){
					currentlySelectedTile = cave.GROUND;

				}

				if(currentlySelectedTile != -1){
					Location clickLocation = convertClickToLocation(mouseX, mouseY);
					if(cave.isValid(clickLocation)){
						cave.setTile(clickLocation, currentlySelectedTile);
						currentlySelectedTile = -1;

					}


				}


			}

		});

		scene.setOnMouseMoved(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				mouseX = (int)event.getX();
				mouseY = (int)event.getY();

			}

		});

		new AnimationTimer(){

			@Override
			public void handle(long now) {

				//gc.setFill(Color.LIGHT_GRAY);
				gc.fillRect(0, 0, 800, 600);


				processInput();

				cave.draw(gc);
				drawToolBar(gc);
				player.draw(gc);
				//processInput();

				//gc.drawImage(playerImage, 10, 10);

			}

		}.start();;

		stage.show();
		//AImove();



	}



	public void AImove(){




		Timer timer = new Timer();
		int begin = 0;
		int timeInterval = 200;
		timer.schedule(new TimerTask() {
			int counter = 0;
			@Override
			public void run() {

				moveUsingLogic();
				counter++;
				if (goldFound == true  || isAgentDead == true){
					timer.cancel();
					//System.out.println("Score : " + score);
				}
			}
		}, begin, timeInterval);




	}

	public void moveUsingLogic(){

		//while(goldFound == false && isAgentDead == false ){

		//pRow = player.getPlayerLocation().getRow();
		//pCol = player.getPlayerLocation().getCol();

		int hint = player.checkStatus();
		if(hint  == 0){
			//System.out.println("OK");
			Random random = new Random();
			int low = 1;
			int high = 5;
			int result = random.nextInt(high-low) + low;

			if(result == 1){

				pRow = player.getPlayerLocation().getRow();
				pCol = player.getPlayerLocation().getCol();
				// if(cave.isValid(new Location(pRow,pCol+1))){
				//     if(isVisited[pRow][pCol+1] == 0){
				//         player.moveRight();
				//         isVisited[pRow][pCol+1] = 1;

				//     }
				// }

				player.moveRight();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				cRow = player.getPlayerLocation().getRow();
				cCol = player.getPlayerLocation().getCol();

				checkTile(player.checkStatus());
				// if(player.checkStatus() == 3){
				//     goldFound = true;
				//     System.out.println("Gold found");

				// }
			}
			else if(result == 2){
				pRow = player.getPlayerLocation().getRow();
				pCol = player.getPlayerLocation().getCol();
				// if(cave.isValid(new Location(pRow,pCol-1))){
				//     if(isVisited[pRow][pCol-1] == 0){
				//         player.moveLeft();
				//         isVisited[pRow][pCol-1] = 1;

				//     }
				// }

				player.moveLeft();
				score = score - 1;

				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				cRow = player.getPlayerLocation().getRow();
				cCol = player.getPlayerLocation().getCol();

				checkTile(player.checkStatus());

			}
			else if(result == 3){
				pRow = player.getPlayerLocation().getRow();
				pCol = player.getPlayerLocation().getCol();
				// if(cave.isValid(new Location(pRow-1,pCol))){
				//     if(isVisited[pRow-1][pCol] == 0){
				//         player.moveUp();
				//         isVisited[pRow-1][pCol] = 1;
				//     }
				// }

				player.moveUp();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				cRow = player.getPlayerLocation().getRow();
				cCol = player.getPlayerLocation().getCol();

				checkTile(player.checkStatus());
			}
			else{
				pRow = player.getPlayerLocation().getRow();
				pCol = player.getPlayerLocation().getCol();
				// if(cave.isValid(new Location(pRow+1,pCol))){
				//     if(isVisited[pRow+1][pCol] == 0){
				//         player.moveDown();
				//         isVisited[pRow+1][pCol] = 1;
				//     }
				// }

				player.moveDown();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				cRow = player.getPlayerLocation().getRow();
				cCol = player.getPlayerLocation().getCol();

				checkTile(player.checkStatus());
			}

			//score = score - 1000;
		}


		else if(hint  == 1){
			System.out.println("PIT, Game Over");
			score = score - 1000;
			System.out.println("Score : " + score);
			isAgentDead = true;

		}
		else if(hint  == 2){
			System.out.println("Wumpus, Game Over");
			score = score - 1000;
			System.out.println("Score : " + score);
			// AudioClip scream = new AudioClip(this.getClass().getResource("scream.wav").toString());
			// scream.play();
			String musicFile = "C:\\Users\\USER\\eclipse-workspace\\Wumpus\\src\\images\\scream.wav";     // For example

			Media sound = new Media(new File(musicFile).toURI().toString());
			MediaPlayer mediaPlayer = new MediaPlayer(sound);
			mediaPlayer.play();
			isAgentDead = true;

		}
		else if(hint  == 3){
			score = score + 1000;
			System.out.println("Gold found");
			System.out.println("Score : " + score);
			isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
			goldFound = true;
		}
		else if(hint  == 10){
			isBreeze[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
			//System.out.println("Breeze");
			Location previous = new Location(pRow, pCol);
			Location current = new Location(player.getPlayerLocation().getRow(), player.getPlayerLocation().getCol());

			isPitPresent(player.getPlayerLocation().getRow(), player.getPlayerLocation().getCol(),previous,current);
			//cRow = player.getPlayerLocation().getRow();
			//cCol = player.getPlayerLocation().getCol();
			int rowTemp = player.getPlayerLocation().getRow();
			int colTemp = player.getPlayerLocation().getCol(); 
			//System.out.println("Current : " + rowTemp + "," + colTemp);
			//System.out.println("Previous : " + pRow + "," + pCol);

			player.move(new Location(pRow, pCol));
			score = score - 1;
			isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
			int rowTemp2 = player.getPlayerLocation().getRow();
			int colTemp2 = player.getPlayerLocation().getCol();


			int rowTemp3 = player.getPlayerLocation().getRow();
			int colTemp3 = player.getPlayerLocation().getCol();

			pRow = rowTemp;
			pCol = colTemp;
			cRow = rowTemp3;
			cCol = colTemp3;
			//checkPit(pRow, pCol, cRow, cCol);


		}
		else if(hint  == 20){
			System.out.println("Stench");
			isStench[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
			cRow = player.getPlayerLocation().getRow();
			cCol = player.getPlayerLocation().getCol(); 

			Location previous = new Location(pRow, pCol);
			Location current = new Location(player.getPlayerLocation().getRow(), player.getPlayerLocation().getCol());

			isWumpusPresent(player.getPlayerLocation().getRow(), player.getPlayerLocation().getCol(),previous,current);

			player.move(new Location(pRow, pCol));
			score = score - 1;
			isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;

		}
		else if(hint  == 30){
			System.out.println("Glitter");
		}

		//}

	}

	public void isWumpusPresent(int row, int column, Location previous, Location current){

		if(previous.getRow() > current.getRow()){
			//from down to up
			if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row-1,column+1)) &&  cave.isValid(new Location(row-2, column))){
				if(isStench[row-1][column-1] == 1 || isStench[row-1][column+1] == 1 || isStench[row-2][column] == 1){
					if(isVisited[row-1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row-1) + "," + column);
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row-1, column);
						cave.setTile(location, 404);
						score = score - 10;
						cave.setTile(location, 0);
						// System.out.println("Score " + score);

						isWumpusDead = true;
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row,column+2))){
				if(isStench[row-1][column+1] == 1 || isStench[row+1][column+1] == 1 || isStench[row][column+2] == 1){
					if(isVisited[row-1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + row + "," + (column+1));
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row, column+1);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}

				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row,column-2))){
				if(isStench[row-1][column-1] == 1 || isStench[row+1][column-1] == 1 || isStench[row][column-2] == 1){
					if(isVisited[row-1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + row + "," + (column-1));
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row, column-1);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}



		}
		else if(previous.getRow() < current.getRow()){
			//from up to down
			if(cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row+2,column))){
				if(isStench[row+1][column-1] == 1 || isStench[row+1][column+1] == 1 || isStench[row+2][column] == 1){
					if(isVisited[row+1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row+1) + "," + column);
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row+1, column);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}

				}

			}

			else if(cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row,column+2))){
				if(isStench[row-1][column+1] == 1 || isStench[row+1][column+1] == 1 || isStench[row][column+2] == 1){

					if(isVisited[row][column+1] == 0) {
						playScream();
						System.out.println("Wumpus in " + row + "," + (column+1));
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row, column+1);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row,column-2))){
				if(isStench[row-1][column-1] == 1 || isStench[row+1][column-1] == 1 || isStench[row][column-2] == 1){

					if(isVisited[row][column-1] == 0) {
						playScream();
						System.out.println("Wumpus in " + row + "," + (column-1));
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row, column-1);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}


		}
		else if(previous.getCol() > current.getCol()){
			//right to left

			if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row,column-2))){
				if(isStench[row-1][column-1] == 1 || isStench[row+1][column-1] == 1 || isStench[row][column-2] == 1){

					if(isVisited[row][column-1] == 0) {
						playScream();
						System.out.println("Wumpus in " + row + "," + (column-1));
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row, column-1);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}
			else if(cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row+2,column))){
				if(isStench[row+1][column+1] == 1 || isStench[row+1][column-1] == 1 || isStench[row+2][column] == 1){

					if(isVisited[row+1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row+1) + "," + column);
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row+1, column);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row-2,column))){
				if(isStench[row-1][column-1] == 1 || isStench[row-1][column+1] == 1 || isStench[row-2][column] == 1){

					if(isVisited[row-1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row-1) + "," + column);
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row-1, column);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}

		}
		else if(previous.getCol() < current.getCol()){
			//left to right
			if(cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row,column+2))){
				if(isStench[row-1][column+1] == 1 || isStench[row+1][column+1] == 1 || isStench[row][column+2] == 1){

					if(isVisited[row][column+1] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row) + "," + (column+1));
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row, column+1);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}
			else if(cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row+2,column))){
				if(isStench[row+1][column+1] == 1 || isStench[row+1][column-1] == 1 || isStench[row+2][column] == 1){

					if(isVisited[row+1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row+1) + "," + column);
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row+1, column);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row-2,column))){
				if(isStench[row-1][column-1] == 1 || isStench[row-1][column+1] == 1 || isStench[row-2][column] == 1){

					if(isVisited[row-1][column] == 0) {
						playScream();
						System.out.println("Wumpus in " + (row-1) + "," + column);
						System.out.println("Agent shoots arrow, wumpus is dead");
						Location location = new Location(row-1, column);
						cave.setTile(location, 404);
						score = score - 10;
						//System.out.println("Score " + score);
						cave.setTile(location, 0);
						isWumpusDead = true;
					}
				}

			}


		}



	}

	public void playScream(){
		String musicFile = "C:\\Users\\USER\\eclipse-workspace\\Wumpus\\src\\images\\coffin.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();

	}

	public void isPitPresent(int row, int column, Location previous, Location current){

		if(previous.getRow() > current.getRow()){
			//from down to up
			if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row-1,column+1)) &&  cave.isValid(new Location(row-2, column))){
				if(isBreeze[row-1][column-1] == 1 || isBreeze[row-1][column+1] == 1 || isBreeze[row-2][column] == 1){
					if(isVisited[row-1][column] == 0) {
						System.out.println("Pit in " + (row-1) + "," + column);
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row,column+2))){
				if(isBreeze[row-1][column+1] == 1 || isBreeze[row+1][column+1] == 1 || isBreeze[row][column+2] == 1){
					if(isVisited[row-1][column] == 0) {
						System.out.println("Pit in " + row + "," + (column+1));
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row,column-2))){
				if(isBreeze[row-1][column-1] == 1 || isBreeze[row+1][column-1] == 1 || isBreeze[row][column-2] == 1){
					if(isVisited[row-1][column] == 0) {
						System.out.println("Pit in " + row + "," + (column-1));
					}
				}

			}



		}
		else if(previous.getRow() < current.getRow()){
			//from up to down
			if(cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row+2,column))){
				if(isBreeze[row+1][column-1] == 1 || isBreeze[row+1][column+1] == 1 || isBreeze[row+2][column] == 1){
					if(isVisited[row+1][column] == 0) {
						System.out.println("Pit in " + (row+1) + "," + column);
					}

				}

			}

			else if(cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row,column+2))){
				if(isBreeze[row-1][column+1] == 1 || isBreeze[row+1][column+1] == 1 || isBreeze[row][column+2] == 1){

					if(isVisited[row][column+1] == 0) {
						System.out.println("Pit in " + row + "," + (column+1));
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row,column-2))){
				if(isBreeze[row-1][column-1] == 1 || isBreeze[row+1][column-1] == 1 || isBreeze[row][column-2] == 1){

					if(isVisited[row][column-1] == 0) {
						System.out.println("Pit in " + row + "," + (column-1));
					}
				}

			}


		}
		else if(previous.getCol() > current.getCol()){
			//right to left

			if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row,column-2))){
				if(isBreeze[row-1][column-1] == 1 || isBreeze[row+1][column-1] == 1 || isBreeze[row][column-2] == 1){

					if(isVisited[row][column-1] == 0) {
						System.out.println("Pit in " + row + "," + (column-1));
					}
				}

			}
			else if(cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row+2,column))){
				if(isBreeze[row+1][column+1] == 1 || isBreeze[row+1][column-1] == 1 || isBreeze[row+2][column] == 1){

					if(isVisited[row+1][column] == 0) {
						System.out.println("Pit in " + (row+1) + "," + column);
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row-2,column))){
				if(isBreeze[row-1][column-1] == 1 || isBreeze[row-1][column+1] == 1 || isBreeze[row-2][column] == 1){

					if(isVisited[row-1][column] == 0) {
						System.out.println("Pit in " + (row-1) + "," + column);
					}
				}

			}

		}
		else if(previous.getCol() < current.getCol()){
			//left to right
			if(cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row,column+2))){
				if(isBreeze[row-1][column+1] == 1 || isBreeze[row+1][column+1] == 1 || isBreeze[row][column+2] == 1){

					if(isVisited[row][column+1] == 0) {
						System.out.println("Pit in " + (row) + "," + (column+1));
					}
				}

			}
			else if(cave.isValid(new Location(row+1,column+1)) && cave.isValid(new Location(row+1,column-1)) && cave.isValid(new Location(row+2,column))){
				if(isBreeze[row+1][column+1] == 1 || isBreeze[row+1][column-1] == 1 || isBreeze[row+2][column] == 1){

					if(isVisited[row+1][column] == 0) {
						System.out.println("Pit in " + (row+1) + "," + column);
					}
				}

			}
			else if(cave.isValid(new Location(row-1,column-1)) && cave.isValid(new Location(row-1,column+1)) && cave.isValid(new Location(row-2,column))){
				if(isBreeze[row-1][column-1] == 1 || isBreeze[row-1][column+1] == 1 || isBreeze[row-2][column] == 1){

					if(isVisited[row-1][column] == 0) {
						System.out.println("Pit in " + (row-1) + "," + column);
					}
				}

			}


		}



	}



	public void checkTile(int hint){

		if(hint  == 0){
			//System.out.println("OK");

		}
		else if(hint  == 1){
			System.out.println("PIT, Game Over");
			score = score - 1000;
			isAgentDead = true;

		}
		else if(hint  == 2){
			System.out.println("Wumpus, Game Over");
			// AudioClip scream = new AudioClip(this.getClass().getResource("scream.wav").toString());
			// scream.play();
			score = score - 1000;
			isAgentDead = true;
			//score = score - 1000;
		}
		else if(hint  == 3){
			System.out.println("Gold found");
			score = score + 1000;
			System.out.println("Score " + score);
			goldFound = true;

		}
		else if(hint  == 10){
			//System.out.println("Breeze");
		}
		else if(hint  == 20){
			//System.out.println("Stench");
		}
		else if(hint  == 30){
			//System.out.println("Glitter");
		}

	}

	public void processInput(){
		for(int i=0;i <input.size(); i++){
			if(input.get(i).equals("RIGHT")){
				player.moveRight();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				player.checkStatus();
				input.remove(i);
				i--;
			}
			else if(input.get(i).equals("LEFT")){
				player.moveLeft();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				player.checkStatus();
				input.remove(i);
				i--;
			}
			else if(input.get(i).equals("UP")){
				player.moveUp();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				player.checkStatus();
				input.remove(i);
				i--;
			}
			else if(input.get(i).equals("DOWN")){
				player.moveDown();
				score = score - 1;
				isVisited[player.getPlayerLocation().getRow()][player.getPlayerLocation().getCol()] = 1;
				player.checkStatus();
				input.remove(i);
				i--;
			}
			else {

				input.remove(i);
				i--;
			}
		}
	}

	public void drawToolBar(GraphicsContext gc){
		gc.fillText("Toolbar", 650, 60);
		gc.drawImage(cave.getPitImage(), 650, 80);
		gc.drawImage(cave.getGoldImage(), 650, 130);
		gc.drawImage(cave.getWumpusImage(), 650, 180);
		gc.drawImage(cave.getGroundImage(), 650, 230);

		if(currentlySelectedTile != -1){

			if(currentlySelectedTile == cave.PIT){
				gc.drawImage(cave.getPitImage(), mouseX-25, mouseY-25);
			}
			else if(currentlySelectedTile == cave.WUMPUS){
				gc.drawImage(cave.getWumpusImage(), mouseX-25, mouseY-25);
			}
			else if(currentlySelectedTile == cave.GOLD){
				gc.drawImage(cave.getGoldImage(), mouseX-25, mouseY-25);
			}
			else if(currentlySelectedTile == cave.GROUND){
				gc.drawImage(cave.getGroundImage(), mouseX-25, mouseY-25);
			}

		}


	}

	// public void checkBreeze(int r, int c){
	//     if(cave.getTileStatus(r, c) == 10){
	//         if(r == 0 && c==0){
	//             knowledgeBoard[r+1][c].tileID = 1;
	//             knowledgeBoard[r][c+1].tileID = 1;

	//         }
	//         else if(r == 0 && c==9){
	//             knowledgeBoard[r+1][c].tileID = 1;
	//             knowledgeBoard[r][c-1].tileID = 1;

	//         }
	//         else if(r == 9 && c==0){
	//             knowledgeBoard[r][c+1].tileID = 1;
	//             knowledgeBoard[r-1][c].tileID = 1;

	//         }
	//         else if(r == 9 && c==9){
	//             knowledgeBoard[r][c-1].tileID = 1;
	//             knowledgeBoard[r-1][c].tileID = 1;

	//         }
	//         else if(r == 0){

	//             knowledgeBoard[r][c+1].tileID = 1;
	//             knowledgeBoard[r][c-1].tileID = 1;
	//             knowledgeBoard[r+1][c].tileID = 1;

	//         }
	//         else if(r == 9){
	//             knowledgeBoard[r][c+1].tileID = 1;
	//             knowledgeBoard[r][c-1].tileID = 1;
	//             knowledgeBoard[r-1][c].tileID = 1;

	//         }
	//         else if(c==0){
	//             knowledgeBoard[r-1][c].tileID = 1;
	//             knowledgeBoard[r][c+1].tileID = 1;
	//             knowledgeBoard[r+1][c].tileID = 1;

	//         }
	//         else if(c==9){
	//             knowledgeBoard[r-1][c].tileID = 1;
	//             knowledgeBoard[r+1][c].tileID = 1;
	//             knowledgeBoard[r][c-1].tileID = 1;

	//         }

	//         else {
	//             knowledgeBoard[r-1][c].tileID = 1;
	//             knowledgeBoard[r+1][c].tileID = 1;
	//             knowledgeBoard[r][c-1].tileID = 1;
	//             knowledgeBoard[r][c+1].tileID = 1;

	//         }


	//     }
	// }

	public Location convertClickToLocation(int x, int y){
		int row = (y-Cave.xOffset)/50;
		int col = (x-Cave.yOffset)/50;
		Location location = new Location(row, col);
		return location;
	}


	public static void main(String[] args) throws Exception {
		launch(args);
	}



}