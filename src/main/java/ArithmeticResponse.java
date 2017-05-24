import java.io.Serializable;

public class ArithmeticResponse implements Serializable{
    public ArithmeticResponse(Double result) {
        this.result = result;
    }

    Double result;
}
