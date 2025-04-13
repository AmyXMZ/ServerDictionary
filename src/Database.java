import netscape.javascript.JSObject;

public class Database {
    private JSObject dict;
    private final String path;

    public Database(JSObject dict, String path){
        this.dict = dict;
        this.path = path;
    }
}
