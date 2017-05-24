import java.io.Serializable;
import java.util.List;

public class ArithmeticRequest implements Serializable {
    String responceQueueName;
    List<Double> numbers;
    ArithmeticOperation operation;

    public ArithmeticRequest(String responceQueueName, List<Double> numbers, ArithmeticOperation operation) {
        this.responceQueueName = responceQueueName;
        this.numbers = numbers;
        this.operation = operation;
    }
}
