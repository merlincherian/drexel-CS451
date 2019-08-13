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
			new Thread() {
				@Override
				public void run() {
					javafx.application.Application.launch(Checkers.class);
				}
			}.start();
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


	public static void main(String args[]){
			System.out.println("Please enter the ip address given by server");
			Scanner in = new Scanner(System.in);
			host = in.nextLine();
			System.out.println("Please enter the port number given by server");
			String p = in.nextLine();
			port = Integer.parseInt(p);
			Client client = new Client(host, port);
	}
}
