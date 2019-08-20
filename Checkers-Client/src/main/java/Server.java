import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class Server{
	private Socket socket = null;
	private static ServerSocket server = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private String ip;

	public Server (int port) throws IOException{
		try{
			ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Server started");
			System.out.println("Use IP address: " + ip);
			System.out.println("Use port: " + port);
			System.out.println("Waiting for client...");
			//running infinite loop for getting client requests
			while (true){
				socket = server.accept();
				System.out.println("A new client is connected: " + socket);
				//input and output streams
				in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
				out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				System.out.println("Assigning new thread for this client");
				//Create new thread object
				Thread t = new ClientHandler(socket, in, out);
				//Invoking start() method
				t.start();
			}
		}
		catch(IOException ex){
			socket.close();
			ex.printStackTrace();
		}
	}
	
	//generates a random port number between 48620 and 49150 
	private static int createPortOne(String address){
		Random rand = new Random();
		int port = 0;
		port = rand.nextInt(49150-48620) + 48620;
		try{
		server  = new ServerSocket(port);
		}
		catch(Exception ex){
			System.out.println("Port is taken, another one is being generated");
			createPortOne(address);
			}	
		return port;
	}
	   
	public static void main(String args[]){
		try{
		String adr = InetAddress.getLocalHost().getHostAddress();		
		int port1 = createPortOne(adr);
		Server server = new Server(port1);
	}		
		catch (Exception ex){
			System.out.println(ex);		
		}					
	}
}

		//ClientHandler Class
		class ClientHandler extends Thread{
			final DataInputStream dis;
			final DataOutputStream dos;
			final Socket s;

			//Constructor
			public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){
				this.s = s;
				this.dis = dis;
				this.dos = dos;
			}
			
			@Override 
			public void run(){
				String received;
				String toreturn;
				while (true){
					try{
						//receive answer from client
						received = dis.readUTF();
						if (received.equals("Exit")){
							System.out.println("Client " + this.s + " sends exit...");
							System.out.println("Closing this connection");
							this.s.close();
							System.out.println("Connection closed");
							break;
						}
						//Create some type of move object so that based on the answer from the client
						//server can write the corresponding move/validity of move to output stream
						switch (received){
							default: 
								dos.writeUTF("Invalid move");
								break;						
						}
					}
					catch (IOException ex){
						ex.printStackTrace();
					}
				}
				try{
					//closing resources
					this.dis.close();
					this.dos.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		
