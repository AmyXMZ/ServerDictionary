
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
    private static int counter = 0;

    public static void main(String[] args)
    {
        //an abstract class that provides a way to create ServerSocket objects
        // getDefault() method returns the default system implementation of ServerSocketFactory,
        // which can create normal (non-secure) server sockets
        ServerSocketFactory factory = ServerSocketFactory.getDefault();

        try(ServerSocket server = factory.createServerSocket(port))
        {
            System.out.println("Waiting for client connection-");
            //handling multiple clients
            //HashMap<Integer, Clients> clientsHashMap = new HashMap<Integer, Clients>();
            // serverGUI
            //serverUI = new ServerUI(clientsHashMap, serverSocket);

            // Wait for connections.
            while(true)
            {
                Socket client = server.accept();
                counter++;
                System.out.println("Client "+counter+": Applying for connection!");

                // Start a new thread for a connection
                Thread t = new Thread(() -> serveClient(client));
                t.start();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private static void serveClient(Socket client)
    {
        try(Socket clientSocket = client)
        {
            // Input stream
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("CLIENT: "+input.readUTF());

            output.writeUTF("Server: Hi Client "+counter+" !!!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
