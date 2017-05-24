import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Sender implements Runnable {
    private static final Random random = new Random();
    private String name;
    private List<Double> list;
    private ArithmeticOperation operation;
    private Logger logger;
    private boolean isArithmetic;

    public Sender(Logger logger, String name) {
        this.name = name;
        this.logger = logger;
    }

    public Sender(Logger logger, String name, boolean isArithmetic) {
        this(logger, name);
        this.isArithmetic = isArithmetic;

    }

    public void run() {

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("RequestQueue");
            MessageProducer producer = session.createProducer(destination);

            for (int i = 0; i < 3; i++) {

                Message message;
                if (isArithmetic) {
                    this.list = new ArrayList<Double>();
                    this.operation = randomEnum(ArithmeticOperation.class);
                    list.add(getRandom());
                    list.add(getRandom());
                    ArithmeticRequest arithmeticRequest = new ArithmeticRequest(name, list, operation);
                    message = session.createObjectMessage(arithmeticRequest);
                    logger.info(name + " Put message in the queue: " + arithmeticRequest.numbers
                            + " operation " + arithmeticRequest.operation);

                } else {
                    EchoRequest echoRequest = new EchoRequest(name, "Message numger " + i + " from" + name);
                    message = session.createObjectMessage(echoRequest);
                    logger.info(name + " Put message in the queue: " + echoRequest.message);
                }
//                System.out.println(name + "Put message in the queue: " + message);
                producer.send(message);
                Destination destinationForResponse = session.createQueue(name);
                MessageConsumer consumer = session.createConsumer(destinationForResponse);
                message = consumer.receive();
                if (message instanceof ObjectMessage) {
                    Serializable object = ((ObjectMessage) message).getObject();
                    if (object instanceof EchoResponse) {
                        EchoResponse er = (EchoResponse) object;
                        logger.info(name + " Message get back : " + er.message);
                    }
                    if (object instanceof ArithmeticResponse) {
                        ArithmeticResponse ar = (ArithmeticResponse) object;
                        logger.info(name + " Message get back : " + ar.result);
                    }
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    static Double getRandom() {
        return (double) new Random().nextInt();
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

}