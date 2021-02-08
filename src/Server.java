import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	int port; 
	ServerSocket serverSocket;
	int numPlayers;
	int maxNumPlayers;
	
	double p1x = 100.0;
	double p1y = 100.0;
	double p2x = 100.0;
	double p2y = 150.0;
	
	Socket player1;
	Socket player2;
	ReadClientCoord player1RC;
	ReadClientCoord player2RC;
	WriteClientCoord player1WC;
	WriteClientCoord player2WC;
	
	public Server(int port) {
		numPlayers = 0;
		maxNumPlayers = 2;
		
		try {
			this.port = port;
			serverSocket = new ServerSocket(port);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void startServer() {
		
		try {
			System.out.println("Server is listening on port " + port);
			
			while (numPlayers < maxNumPlayers) {
				
				Socket newSocket = serverSocket.accept();
				numPlayers += 1;
				System.out.println("New user connected");
				
				DataInputStream input = new DataInputStream(newSocket.getInputStream());
				DataOutputStream output = new DataOutputStream(newSocket.getOutputStream());
				
				//the current number of players establishes the players id (1 or 2) and sends it to the client
				output.writeInt(numPlayers);
				
				ReadClientCoord readFromClient = new ReadClientCoord(numPlayers, input);
				WriteClientCoord writeToClient = new WriteClientCoord(numPlayers, output);
		
				if (numPlayers == 1) {
					player1 = newSocket; 
					player1RC = readFromClient;
					player1WC = writeToClient;
				}
				
				else if (numPlayers == 2) {
					player2 = newSocket;
					player2RC = readFromClient;
					player2WC = writeToClient;
					
					new Thread(player1RC).start();
					new Thread(player2RC).start();
					
					new Thread(player1WC).start();
					new Thread(player2WC).start();	
				}
				
			}
			
			
			System.out.println("2 Players have connected");

		} 
		
		catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	//reads a players coordinates
	class ReadClientCoord extends Thread {
		private int ID; 
		private DataInputStream input;
		
		public ReadClientCoord(int ID, DataInputStream input) {
			
			this.ID = ID;
			this.input = input;
				
		}
		
		public void run() {
			
			try {
				while (true) {	
					if (ID == 1) {
						p1x = input.readDouble();
						p1y = input.readDouble();
						
					}
					else if (ID == 2) {
						p2x = input.readDouble();
						p2y = input.readDouble();
					}
					
				}
				
			}
			catch (IOException e) {
				e.printStackTrace();
			}

		}
		
	}
	
	//writes other players coordinates to this player
	class WriteClientCoord extends Thread {
		private int ID; 
		private DataOutputStream output;
		
		public WriteClientCoord(int ID, DataOutputStream output) {
			
			this.ID = ID;
			this.output = output;
				
		}
		
		public void run() {
			
			try {
				while(true) {
					
					if (ID == 1) {
						output.writeDouble(p2x);
						output.writeDouble(p2y);
						output.flush();
						
					}
					
					else if (ID == 2) {
						output.writeDouble(p1x);
						output.writeDouble(p1y);
						output.flush();	
					}
					
					//helps with lag
					Thread.sleep(20);
					
				}
				
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	

	
	public static void main(String [] args) {
		Server server = new Server(6066);
		server.startServer();
		
	}

}
