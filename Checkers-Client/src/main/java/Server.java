import java.net.*;
import java.io.*;
import java.util.Random;

public class Server{
	private Socket socket = null;
	private static ServerSocket server = null;
	private DataInputStream in = null;
	private String ip;

	public Server (int port){
		try{
			ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("Server started");
			System.out.println("Use IP address: " + ip);
			System.out.println("Use port: " + port);
			System.out.println("Waiting for client...");
			socket = server.accept();
			System.out.println("Client accepted");
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			String line = "";
			while (!line.equals("Over")){
				try{
					line = in.readUTF();
					System.out.println(line);
				}
				catch(IOException ex){
					System.out.println(ex);
				}
			}
			System.out.println("closing connection");
			socket.close();
			in.close();
			}
			catch(IOException ex){
				System.out.println(ex);
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
