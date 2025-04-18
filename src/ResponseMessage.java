import java.util.List;

public class ResponseMessage { //need this for client side, also create a JsonRequest in the client project

    public String errorMessage;
    public String status;

    public List<String> meanings;

    public ResponseMessage(String status) {
        //if the request is successful, simply return the success status
        //this is for addword, removeword, addmeaning, and updatemeaning
        this.status = status;
    }

    public ResponseMessage(String status, String errorMessage) { //if not success
        this.status = status;
        this.errorMessage = errorMessage;
    }
    public ResponseMessage(String status, List<String> meanings){
        //success, and the request is querymeanings
        this.status = status;
        this.meanings = meanings;
    }


}
