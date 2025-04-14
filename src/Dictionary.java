import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import com.google.gson.Gson;

public class Dictionary { // this class will handle dictionary data
    private static final String dict_file_path = "dictionaryJSON"; // path from content root
    private ConcurrentHashMap<String, List<String>> word_meaning_hashmap;
    private Gson gson = new Gson();
}
