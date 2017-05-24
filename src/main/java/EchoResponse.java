import java.io.Serializable;

public class EchoResponse implements Serializable{
    String message;

    public EchoResponse(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EchoResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
