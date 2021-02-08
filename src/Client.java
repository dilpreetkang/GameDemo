import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Client extends Application{

static Player thisPlayer = new Player (0,0,0,Color.BLACK);
static Player otherPlayer =new Player (0,0,0,Color.BLACK);
String hostname = "localhost";
int port = 6066;
static int ID;
Socket socket;	

ReadServerCoord playerRS;
WriteServerCoord playerWS;
	
static Circle thisPlayer1 = new Circle(0, 0, 0, Color.BLACK);
static Circle otherPlayer1 = new Circle(0, 0, 0, Color.BLACK);

Double p1x = 0.0;
Double p1y = 0.0;
Double p2x = 0.0;
Double p2y = 0.0;

Pane root = new Pane();	



	@Override 
	public void start(Stage stage) {
		stage.setTitle("GAME NAME");
		stage.setScene(new Scene(root,350,350));
		System.out.println(ID);
		if (ID == 1) {
			thisPlayer = new Player(100, 100, 10, Color.RED);
			thisPlayer1 = thisPlayer.drawPlayer();
			
			otherPlayer = new Player(100, 150, 10, Color.PURPLE);
			otherPlayer1 = otherPlayer.drawPlayer();
			
			root.getChildren().addAll(thisPlayer1, otherPlayer1);
		}
		
		else if (ID == 2) {
			thisPlayer = new Player(100, 150, 10, Color.PURPLE);
			thisPlayer1 = thisPlayer.drawPlayer();
			
			otherPlayer = new Player(100, 100, 10, Color.RED);
			otherPlayer1= otherPlayer.drawPlayer();
			
			root.getChildren().addAll(thisPlayer1, otherPlayer1);
			
		}
	
		stage.show();
		
		stage.getScene().setOnKeyPressed(e -> { 
			
			switch (e.getCode()) {
			
			case DOWN:
				thisPlayer.yMovement(10.0);
				thisPlayer1.setCenterY(thisPlayer1.getCenterY() + 10);			
				break;
		    case UP:
		    	thisPlayer.yMovement(-10.0);
		        thisPlayer1.setCenterY(thisPlayer1.getCenterY() - 10);
		        break;
		        
		    case LEFT:
		    	thisPlayer.xMovement(-10.0);
		    	thisPlayer1.setCenterX(thisPlayer1.getCenterX() - 10);
		        break;
		        
		    case RIGHT:
		    	thisPlayer.xMovement(10.0);
		    	thisPlayer1.setCenterX(thisPlayer1.getCenterX() + 10);
		        break;

			default:
				break;	
			
			}
		});
	}
	

	public void connection() {
		try {
			socket = new Socket(hostname ,port);
			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			
			//the server has sent the players id and here we read it
			ID = input.readInt();

			playerRS = new ReadServerCoord(input);
			playerWS = new WriteServerCoord(output);

			new Thread(playerRS).start();
			new Thread(playerWS).start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
		
	//reads other players coordinates from server and updates other players position
	class ReadServerCoord extends Thread {
		
		private DataInputStream input; 
		
		public ReadServerCoord(DataInputStream input) {
			this.input = input;
			
		}
		
		public void run() {		
			int x = 0;
			
			try {
				while (true) {
					
					if(otherPlayer != null) {
						if (x==0) System.out.println("other player is not null");
						x++;
						otherPlayer.setX(input.readDouble());
						otherPlayer1.setCenterX(otherPlayer.getX());
						otherPlayer.setY(input.readDouble());
						otherPlayer1.setCenterY(otherPlayer.getY());
					}
				}
				
			}
			
			catch (IOException e) {
				e.printStackTrace();
			}
		}	
				
	}
	
	
	//writes this players coordinates to the server
	class WriteServerCoord extends Thread {
		
		private DataOutputStream output; 
		
		public WriteServerCoord(DataOutputStream output) {
			this.output = output;
			
		}
		
		public void run() {			
			int x = 0;
			try {
				
				while (true) {
					if (thisPlayer != null) {
						if (x==0) System.out.println("this player is not null");
						x++;
						output.writeDouble(thisPlayer.getX());
						output.writeDouble(thisPlayer.getY());
						output.flush();
					}
				}

			}
			 catch(IOException e) {
				 e.printStackTrace();
			 }
		}	
	}

	public static void main (String [] args) {
		Client cli = new Client();
		cli.connection();
		launch(args);
		
	}
	
}
