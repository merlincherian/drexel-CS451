import java.net.*;
import java.io.*;

public class Server{
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private InetAddress ip = null;
	private int port;

	public Server (int port){
		try{
			server = new ServerSocket(port);
			ip = InetAddress.getLocalHost().getHostAddress();
			System.out.println("server started");
			System.out.println("Use IP address: " + ip);
			System.out.println("Use port: 5000");
			System.out.println("waiting for client...");
			socket = server.accept();
			System.out.println("client accepted");
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
		private int createPortOne(String address){
			try{
				port = rand.nextInt(65500);
				Socket s = new Socket(address, port);
				s.close();
			}
			catch(Exception ex){
				System.out.println("Port is taken, another one is being generated");
				port += 1;
				createPortOne(address, port);
			}
		}
		public static void main(String args[]){
			String adr = InetAddress.getLocalHost().getHostAddress();
			int port1 = createPortOne(adr);
			Server server = new Server(port1);
		}
	}
