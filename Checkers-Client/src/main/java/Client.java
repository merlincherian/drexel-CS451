import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
	private Socket socket = null;
	private static int port;
	private static String host = "";
	private DataInputStream input = null;
	private DataOutputStream output = null;

	public Client(String address, int port){
		try{
			socket = new Socket(address, port);
			System.out.println("Connected");
			input = new DataInputStream(System.in);
			output = new DataOutputStream(socket.getOutputStream());
		}
		catch (UnknownHostException ex){
			System.out.println(ex);
		}
		catch (IOException i){
			System.out.println(i);
		}
		String line = "";
		while(!line.equals("Over")){
			try{
				line = input.readLine();
				output.writeUTF(line);
			}
			catch (IOException e){
				System.out.println(e);
			}
		}
		try{
			input.close();
			output.close();
			socket.close();
		}
		catch(IOException i){
			System.out.println(i);
		}
	}

	private boolean isPortInUse(String host, int port){
		try{
			Socket s = new Socket(host, port);
			s.close();
			return false;
		}
		catch(Exception ex){
			return true;
		}
	}

	public static void main(String args[]){
     // while (isPortInUse(host, port)){
		//	port += 1;
		 // }
		  //get the IP address of remote server 
		  //InetAddress host = InetAddress.getByName();
			System.out.println("Please enter the ip address given by server");
			Scanner in = new Scanner(System.in);
			host = in.nextLine();
			System.out.println("Please enter the port given by server");
			String p = in.nextLine();
			port = Integer.parseInt(p);
			Client client = new Client(host, port);
	}
}
