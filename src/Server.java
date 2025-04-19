
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ServerSocketFactory;

public class Server {

    // Declare the port number
    private static int port = 3005;

    // Identifies the user number connected
    private static int num_clients = 0;
    private static final Dictionary dictionary = new Dictionary();


    public static void main(String[] args) {
        //an abstract class that provides a way to create ServerSocket objects
        // getDefault() method returns the default system implementation of ServerSocketFactory,
        // which can create normal (non-secure) server sockets
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try (ServerSocket serverSocket = factory.createServerSocket(port)) {
            System.out.println("Waiting for client connection-");

            while (true) //multithreaded server
            {
                try{
                    Socket client = serverSocket.accept();
                    num_clients++;
                    System.out.println("Client " + num_clients + ": Applying for connection!");

                    // Start a new thread for a connection
                    Thread thread = new Thread(new ThreadPerClient(client, dictionary, num_clients));
                    //for debugging purpose
                    //thread.setName("Client-" + num_clients);
                    thread.start();
                }catch (IOException e) {
                    System.out.println("Error: unable to accept a client connection: " + e.getMessage());
                }

            }

        } catch (IOException e) {
            System.out.println("I/O error: server is unable to start: " + e.getMessage());
            //e.printStackTrace();
        }

    }
}


