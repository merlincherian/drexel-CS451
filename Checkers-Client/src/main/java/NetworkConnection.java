import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public abstract class NetworkConnection {

    private Consumer<Serializable> onReceiveCallBack;
    private ConnectionThread connThread = new ConnectionThread();


    public NetworkConnection(Consumer<Serializable> onReceiveCallBack){
        this.onReceiveCallBack = onReceiveCallBack;
    }

    public void send_data(String data) throws Exception{
        connThread.dos.writeUTF(data);
    }
    public void send(Serializable data) throws Exception{
        connThread.out.writeObject(data);
    }

    public String read_data() throws Exception{
        return connThread.dis.readUTF();
    }

    public Serializable read() throws Exception{
        return (Serializable) connThread.in.readObject();
    }


    public void startConnection() throws Exception{
        connThread.start();

    }


    public void closeConnection() throws Exception{
        connThread.socket.close();

    }

    protected abstract boolean isServer();

    protected abstract String getIP();

    protected abstract int getPort();


    class ConnectionThread extends Thread{
        private DataInputStream dis;
        private DataOutputStream dos;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Socket socket;

        @Override
        public void run(){
            try(ServerSocket server = isServer()? new ServerSocket(getPort()): null;
            Socket socket = isServer()? server.accept() : new Socket(getIP(), getPort());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream())) {
                this.socket = socket;
                this.out = out;
                this.in = in;
                this.dos = dos;
                this.dis = dis;
                this.socket.setTcpNoDelay(true);
                while (true){
                    String message = dis.readUTF();
                    System.out.println(message);
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
