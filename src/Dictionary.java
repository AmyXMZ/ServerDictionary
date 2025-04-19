import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Dictionary { // this class will handle dictionary data
    private static final String dict_file_path = "dictionaryJSON"; // path from content root
    private ConcurrentHashMap<String, List<String>> word_meaning_hashmap;
    private Gson gson = new Gson();

    // constructor for this class
    public Dictionary() {
        word_meaning_hashmap = new ConcurrentHashMap<String, List<String>>();
        loadDictFile(); //load the dictionary file as Dictionary is initialized
    }

    //methods for this class:
    public void loadDictFile(){
        //read from file and convert JSON to concurrentHashMap
        try {
            String from_json = Files.readString(Path.of("dictionaryJSON"));
            Type hashMap = new TypeToken<ConcurrentHashMap<String, List<String>>>() {}.getType();
            //word_meaning_hashmap = gson.fromJson(from_json, hashMap);
            ConcurrentHashMap<String, List<String>> rawHM = gson.fromJson(from_json, hashMap); //gson converted might result in immutable list
            // Convert all lists to mutable ArrayLists
            word_meaning_hashmap = new ConcurrentHashMap<>();
            for (Map.Entry<String, List<String>> entry : rawHM.entrySet()) {
                word_meaning_hashmap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDictFile(){
        // make changes to the dictionary file and save it as JSON
        // convert from concurrentHM to JSON
        String to_json = gson.toJson(word_meaning_hashmap);
        try {
            Files.writeString(Path.of("dictionaryJSON"), to_json);
        } catch (IOException e) {
            System.out.println("Unable to save to dictionary file.");
            e.printStackTrace();
        }
    }

//    public synchronized String addWord(String word, List<String> meanings){ ///test if the concurrent client access works
//        word = word.toLowerCase();
//        List<String> to_lower_meanings = new ArrayList<>();
//        for (String item : meanings) {
//            to_lower_meanings.add(item.toLowerCase());
//        }
//        meanings = to_lower_meanings;
//        boolean isValidWord = word.matches("[a-z]+");
//        boolean isValidMeaning = meanings.stream()
//                .allMatch(i -> i.matches("[a-z .,()'\";:?!\\-]+"));
//        if (isValidWord && isValidMeaning){
//            if (word_meaning_hashmap.containsKey(word)) {
//                return "Duplicate word!";
//            }else{ //add it to the dictionary file
//                word_meaning_hashmap.put(word, meanings);
//                writeDictFile();
//                return "Success: word added!";
//            }
//        }else{
//            return "Not an English word or a valid meaning. Try again.";
//        }
//    }
    //this below method accept one word and one meaning (both are of type String)
    public synchronized String addWord(String word, String meaning){ ///test if the concurrent client access works
        word = word.toLowerCase();

        meaning = meaning.toLowerCase();
        boolean isValidWord = word.matches("[a-z]+");
        boolean isValidMeaning = meaning.matches("[a-z .,()'\";:?!\\-]+");
        if (isValidWord && isValidMeaning){
            if (word_meaning_hashmap.containsKey(word)) {
                return "Duplicate word!";
            }else{ //add it to the dictionary file
                List<String> meanings = new ArrayList<>();
                meanings.add(meaning);
                word_meaning_hashmap.put(word, meanings);
                writeDictFile();
                return "Success: word added!";
            }
        }else{
            return "Not an English word or a valid meaning. Try again.";
        }
    }

    public synchronized String removeWord(String word){ //test if a word deleted by one client is not visible to other clients of the server
        word = word.toLowerCase();
        boolean isValidWord = word.matches("[a-z]+");
        if (isValidWord){
            if (word_meaning_hashmap.containsKey(word)){
                word_meaning_hashmap.remove(word);
                writeDictFile();
                return "Success: word has been removed!";
            }else {
                return "Word not found!";
            }
        }else{
            return "Not a valid word. Try again.";
        }
    }

    public synchronized List<String> queryMeanings (String word){
        word = word.toLowerCase();
        boolean isValidWord = word.matches("[a-z]+");
        if (isValidWord){
            if (word_meaning_hashmap.containsKey(word)){
                List<String> meanings_list = word_meaning_hashmap.get(word);
                if (meanings_list != null){
                    return new ArrayList<>(meanings_list); //convert to JSON?
                }else{//note that List.of results in immutable list!!
                    return new ArrayList<>(List.of("The looked-up word does not have any associated meaning!"));
                }
            }else{
                return new ArrayList<>(List.of("There is no such word in the dictionary!"));
            }
        }else{
            return new ArrayList<>(List.of("Not a valid word. Try again."));
        }
    }

    public synchronized String addMeaning(String word, String newMeaning){
        word = word.toLowerCase();
        boolean isValidWord = word.matches("[a-z]+") && word_meaning_hashmap.containsKey(word); //word contains only letters and exists
        newMeaning = newMeaning.toLowerCase();
        boolean isValidMeaning = newMeaning.matches("[a-z .,()'\";:?!\\-]+");

        if (isValidWord){
            List<String> existingMeanings = word_meaning_hashmap.get(word);
            boolean meaning_exist = false;
            for (String m : existingMeanings) {
                if (m.equalsIgnoreCase(newMeaning)) {
                    meaning_exist = true;
                    break;
                }
            }
            if (!newMeaning.trim().isEmpty() && !newMeaning.matches("[.,()'\";:?!\\-]+") && isValidMeaning){ //contain more than just spaces, puctuations or numbers
                if (meaning_exist == false){
                    //the meaning does not exist, add it to the dictionary
                    existingMeanings.add(newMeaning);
                    writeDictFile();
                    return "Success: new meaning added!";
                }else{
                    //meaning exist
                    return "The meaning exists. No action taken.";
                }
            }else{
                return "Not a valid meaning!";
            }
        }else{
            return "Not a valid word or existing word. Try again.";
        }
    }

    public synchronized String updateMeaning(String word, String oldMeaning, String newMeaning){
        word = word.toLowerCase();
        boolean isValidWord = word.matches("[a-z]+") && word_meaning_hashmap.containsKey(word); //word contains only letters and exists
        newMeaning = newMeaning.toLowerCase();
        oldMeaning = oldMeaning.toLowerCase();
        boolean isValidMeaning = newMeaning.matches("[a-z .,()'\";:?!\\-]+") && !newMeaning.trim().isEmpty() && !newMeaning.matches("[.,()'\";:?!\\-]+");
        if (isValidWord){
            List<String> existing_meanings = word_meaning_hashmap.get(word);
            if (existing_meanings != null){
                if (isValidMeaning) {
                    for (int i = 0; i < existing_meanings.size(); i++) {
                        if (existing_meanings.get(i).equals(oldMeaning)) {
                            existing_meanings.set(i, newMeaning);
                            writeDictFile();
                            return "Success: meaning updated!";
                        }
                    }
                }else{
                    return "Not a valid meaning!";
                }
                return "This meaning is not found!";
            }else{
                return "No associated meanings for this word!";
            }
        }else{
            return "Not a valid word or not an existing word. Try again.";
        }
    }
    public static void main(String[] args){
        Dictionary dict = new Dictionary();
        String testWord = "type";
        String testMeanings = "a kind of people that someone likes"; //newMeaning
        String oldMeaning = "a kind of people or personality that someone likes";
        //List<String> testMeanings = List.of("a representative case", "a sample used for explanation");
        //String result = dict.addWord(testWord,testMeanings);
        //List<String> result = dict.queryMeanings(testWord);
        String result = dict.updateMeaning(testWord, oldMeaning, testMeanings);
        //String result = dict.removeWord(testWord);
        System.out.println(result);

    }
}
