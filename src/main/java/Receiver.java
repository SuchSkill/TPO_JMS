import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Receiver implements Runnable{
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private Destination destinationForResponse = null;
    private MessageProducer producer = null;
    private MessageConsumer consumer = null;
    private String name;
    private Logger logger;

    public Receiver(Logger logger, String name) {
        this.name = name;
        this.logger = logger;
        this.logger = logger;
    }

    public void run() {
        try {
            factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            connection = factory.createConnection();
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("RequestQueue");
            consumer = session.createConsumer(destination);
            while (true) {
                Message message = consumer.receive();
                if (message instanceof ObjectMessage) {
                    Serializable object = ((ObjectMessage) message).getObject();
                    if (object instanceof EchoRequest) {
                        EchoRequest er = (EchoRequest) object;
                        logger.info(name + " resived message: " + er.message);

                        Thread.sleep(getRandomSleepTime());

                        EchoResponse echoResponse = new EchoResponse(er.message+" edited by "+name);
                        destinationForResponse = session.createQueue(er.responceQueueName);
                        producer = session.createProducer(destinationForResponse);
                        message = session.createObjectMessage(echoResponse);
                    }else if(object instanceof ArithmeticRequest){
                        ArithmeticRequest arithmeticRequest = (ArithmeticRequest) object;
                        logger.info(name + " resived message: " + arithmeticRequest.numbers
                                + " operation" + arithmeticRequest.operation);
                        Thread.sleep(getRandomSleepTime());
                        ArithmeticResponse arithmeticResponse = calculate(arithmeticRequest);
                        destinationForResponse = session.createQueue(arithmeticRequest.responceQueueName);
                        producer = session.createProducer(destinationForResponse);
                        message = session.createObjectMessage(arithmeticResponse);
                    }
                    producer.send(message);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ArithmeticResponse calculate(ArithmeticRequest arithmeticRequest) {
        Double result = 0d;
        List<Double> numbers = arithmeticRequest.numbers;
        switch (arithmeticRequest.operation){
            case ADDITION:{
                for (Double number : numbers) {
                    result+=number;
                }
                break;
            }
            case SUBSTRACTION:{
                result = numbers.get(0)- numbers.get(1);
                break;
            }
            case MULTIPLICATION:{
                result = 1d;
                for (Double number : numbers) {
                    result*=number;
                }
                break;
            }
            case DEVISION:{
                result = numbers.get(0)/ numbers.get(1);
                break;
            }
        }
        return new ArithmeticResponse(result);
    }

    private int getRandomSleepTime() {
        return 3000+(new Random().nextInt(3)*1000);
    }

}