import java.util.List;

public class RequestMessage { //represents a parsed request from client
    public String action;
    public String word;
    public String meaning;
    public String newMeaning;
    public String oldMeaning;
    //public List<String> meanings;

    public RequestMessage(String action, String word) {
        this.action = action; //querymeaning or removeword
        this.word = word;
    }

    public RequestMessage(String action, String word, String meaning) {
        //for now, addword (and multiple new meanings if there is?)
        // add meaning
        this.action = action;
        this.word = word;
        this.meaning = meaning;
    }
    public RequestMessage(String action, String word, String oldMeaning, String newMeaning) {
        //updatemeaning
        this.action = action;
        this.word = word;
        this.oldMeaning = oldMeaning;
        this.newMeaning = newMeaning;
    }
//    public String getAction() {
//        return action;
//    }
//
//    public String getWord() {
//        return word;
//    }
//
//    public String getMeaning() {
//        return meaning;
//    }
//
//    public String getNewMeaning() {
//        return newMeaning;
//    }
//
//    public String getOldMeaning() {
//        return oldMeaning;
//    }

//    public List<String> getMeanings() {
//        return meanings;
//    }

}
