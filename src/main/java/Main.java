import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {


    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class);
//        BasicConfigurator.configure();

        for (int i = 0; i < 5; i++) {
            Receiver receiver = new Receiver(logger,"RECEIVER-" + i);
            new Thread(receiver).start();
        }
        for (int i = 0; i < 10; i++) {
            Sender sender;
            if (i % 2 == 0){

                sender = new Sender(logger,"SENDER-" + i, true);
            }
            else
                sender = new Sender(logger,"SENDER-" + i);
            new Thread(sender).start();
        }
    }


}
