import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Set;

public class ThreadPerClient implements Runnable {
    private Dictionary dictionary;
    private Socket clientSocket;
    private int clientNum;
    private DataInputStream input;
    private DataOutputStream output;
    private static final Set<String> validRequest = Set.of("addword", "removeword", "querymeanings", "addmeaning", "updatemeaning", "quit");
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
        try { // (server to client)

            output.writeUTF("Server: Hi Client "+ clientNum +" !!!");
            output.writeUTF("This is the Dictionary Server! Enter one of the commands below: addword, removeword, querymeanings, addmeaning, updatemeaning.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            String request;
            while (true){
                request = input.readUTF(); //server reads the request sent by client
                if (request == null || request.equalsIgnoreCase("quit")) {
                    break;
                }
                System.out.println("Client" + clientNum + " requests: " + request);
                String response = returnClientRequest(request, dictionary);
                output.writeUTF(response); //server retrieve data and send it to client
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String returnClientRequest(String request, Dictionary dictionary) throws IOException{//parsing client's request and retrieve data
        request = request.toLowerCase().trim();
        boolean isValidRequest = validRequest.contains(request);
        if (isValidRequest){
            if (request.equals("querymeanings")){

            }
            if (request.equals("addword")){
                output.writeUTF("Enter the word: ");
                String word = input.readUTF();
                output.writeUTF("Enter the meaning of the word: ");
                String meaning = input.readUTF();
                return dictionary.addWord(word, meaning);
            }
            if (request.equals("removeword")){

            }
            if (request.equals("addmeaning")){

            }
            if (request.equals("updatemeaning")){

            }
        }else {
            return "Invalid request. Enter again.";
        }
    }
}
