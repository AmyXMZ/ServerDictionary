import java.util.List;

public class RequestMessage { //represents a parsed request from client
    public String action;
    public String word;
    public String meaning;
    public String newMeaning;
    public String oldMeaning;
    public List<String> meanings;
}
