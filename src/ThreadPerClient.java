import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadPerClient implements Runnable {
    private Dictionary dictionary;
    private Socket clientSocket;
    private int clientNum;
    private DataInputStream input;
    private DataOutputStream output;
    // constructor for this class:
    public ThreadPerClient(Socket socket, Dictionary dictionary, int clientNum){
        this.clientSocket = socket;
        this.dictionary = dictionary;
        this.clientNum = clientNum;
    }

    @Override
    public void run() {

        try {
            input = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.writeUTF("Server: Hi Client "+ clientNum +" !!!");
            output.writeUTF("Welcome to the Dictionary Server! Type commands like: ADD word:meaning, QUERY word, REMOVE word");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String request;
            request = input.readUTF();
            while (request != null){
                System.out.println("Client" + clientNum + " requests: " + request);
                String response = clientRequest(request, dictionary);
                output.writeUTF(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static String clientRequest(String request, Dictionary dictionary){//parsing client's request and retrieve data
        request = request.toLowerCase().trim();
        boolean isValidRequest = request.matches("[a-z]+");

    }
}
