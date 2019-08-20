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
			Scanner scan = new Scanner(System.in);
			socket = new Socket(address, port);
			System.out.println("Connected");
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		
		//loop performs exchange of information between client and client handler
		while (true){
			System.out.println(input.readUTF());
			String tosend = scan.nextLine();
			output.writeUTF(tosend);

			//If client sends Over, close connection, break loop
			if(tosend.equals("Over")){
				System.out.println("Closing this connection: " + socket);
				socket.close();
				System.out.println("Connection close");
				break;
		}
		//printing info as requested by client
		String received = input.readUTF();
		System.out.println(received);
		}
		//closing resources
			scan.close();
			input.close();
			output.close();
		}
		catch(Exception ex){
			System.out.println(ex);
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
