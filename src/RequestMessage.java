import java.util.List;

public class RequestMessage { //represents a parsed request from client
    public String action;
    public String word;
    public String meaning;
    public String newMeaning;
    public String oldMeaning;
    //public List<String> meanings;
    public String getAction() {
        return action;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getNewMeaning() {
        return newMeaning;
    }

    public String getOldMeaning() {
        return oldMeaning;
    }

//    public List<String> getMeanings() {
//        return meanings;
//    }

}
