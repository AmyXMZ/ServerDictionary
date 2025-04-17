public class JsonResponse { //need this for client side, also create a JsonRequest in the client project

    private String message;
    private Object data;

    public JsonResponse(String message, Object data) {

        this.message = message;
        this.data = data;
    }
}
