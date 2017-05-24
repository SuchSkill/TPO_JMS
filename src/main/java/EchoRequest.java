import java.io.Serializable;

public class EchoRequest implements Serializable{
    String responceQueueName;
    String message;


    public EchoRequest(String responceQueueName, String message) {
        this.responceQueueName = responceQueueName;
        this.message = message;
    }

    @Override
    public String toString() {
        return "EchoRequest{" +
                "responceQueueName='" + responceQueueName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
