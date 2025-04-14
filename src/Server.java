
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import javax.net.ServerSocketFactory;

public class Server {

    // Declare the port number
    private static int port = 3005;

    // Identifies the user number connected
    private static int num_clients = 0;
    private static final Dictionary dictionary = new Dictionary();


    public static void main(String[] args)
    {
        //an abstract class that provides a way to create ServerSocket objects
        // getDefault() method returns the default system implementation of ServerSocketFactory,
        // which can create normal (non-secure) server sockets
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try(ServerSocket serverSocket = factory.createServerSocket(port))
        {
            System.out.println("Waiting for client connection-");

            while(true)
            {
                Socket client = serverSocket.accept();
                num_clients++;
                System.out.println("Client "+num_clients+": Applying for connection!");

                // Start a new thread for a connection
                Thread thread = new Thread(new ThreadPerClient(client, dictionary, num_clients));
                thread.start();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void serveClient(Socket client, Dictionary dictionary, int clientNum)
    {
        try(Socket clientSocket = client)
        {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("CLIENT: "+input.readUTF());

            output.writeUTF("Server: Hi Client "+num_clients+" !!!");
            // Initial greeting
            output.writeUTF("📚 Welcome to the Dictionary Server! Type commands like: ADD word:meaning, QUERY word, REMOVE word");

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
