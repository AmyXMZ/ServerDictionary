import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Set;

public class ThreadPerClient implements Runnable {
    private Dictionary dictionary;
    private Socket clientSocket;
    private int clientNum;

    private DataInputStream input;
    private DataOutputStream output;
    private static final Set<String> validRequest = Set.of("addword", "removeword", "querymeanings", "addmeaning", "updatemeaning");
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
            String request; //json
            Gson gson = new Gson();
            while (true){
                request = input.readUTF(); //server reads the request sent by client

//                if (request == null || request.equalsIgnoreCase("quit")) {
//                    break;
//                }
                //convert client request to java object from json
                RequestMessage requestObject = gson.fromJson(request, RequestMessage.class);
                String extracted_action = requestObject.getAction();
                System.out.println("Client" + clientNum + " requests: " + extracted_action);
                ResponseMessage response = handleClientRequest(requestObject);
                //convert server response to json
                String jsonResponse = gson.toJson(response);
                //server sends the jsonResponse to client
                output.writeUTF(jsonResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ResponseMessage handleClientRequest(RequestMessage request) {//parsing client's request and retrieve data
        String action = request.getAction().toLowerCase();
        String word = request.getWord().toLowerCase();
        String meaning = request.getMeaning().toLowerCase();
        String oldMeaning = request.getOldMeaning().toLowerCase();
        String newMeaning = request.getNewMeaning().toLowerCase();
        //checking the validity of action
        if (!validRequest.contains(action)){
            return new ResponseMessage("Error", "Incorrect request message. Check the available valid request messages and try again.");
        }
        switch(action){
            case "addword":
                String addWordResult = dictionary.addWord(word, meaning);
                return helpShowStatusMessage(addWordResult);
            case "removeword":
                String removeWordResult = dictionary.removeWord(word);
                return helpShowStatusMessage(removeWordResult);
            case "querymeanings": // a special case, return list of string if successful
                List<String> meanings = dictionary.queryMeanings(word);
                if (meanings.get(0).startsWith("Not a valid word")
                        || meanings.get(0).startsWith("The looked-up word")
                        || meanings.get(0).startsWith("There is no such word")){
                    return new ResponseMessage("Error", meanings.get(0));
                }else{
                    return new ResponseMessage("Success", meanings);
                }

            case "addmeaning":
                String addMeaningResult = dictionary.addMeaning(word, meaning);
                return helpShowStatusMessage(addMeaningResult);
            case "updatemeaning":
                String updateMeaningResult = dictionary.updateMeaning(word, oldMeaning, newMeaning);
                return helpShowStatusMessage(updateMeaningResult);
            default:
                return new ResponseMessage("Error", "Unknown error.");
        }
    }

    //helper function, identify if the return string (except for the function queryMeanings),
    // if return string starts with "Success", the operation is successful,
    // else, return error and the corresponding error message from the functions
    private ResponseMessage helpShowStatusMessage(String result) {
        if (result.startsWith("Success")) {
            return new ResponseMessage("Success");
        } else {
            return new ResponseMessage("Error", result); // Show actual dictionary error
        }
    }


}
