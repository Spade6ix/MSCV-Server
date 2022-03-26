package microStar.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import microStar.factory.DBConnectorFactory;
import microStar.factory.SessionFactoryBuilder;

import java.io.IOException;

public class Driver {
    private static final Logger logger = LogManager.getLogger(Driver.class);

    public static void main(String[] args) {
        try{
            new Server();
        }
        catch(Exception x){
            logger.fatal("Exception Occurred");
            x.printStackTrace();
        }
        finally{
            DBConnectorFactory.closeDatabaseConnection();
            SessionFactoryBuilder.closeSessionFactory();
            logger.info("Database Connections Closed");
        }
    }
}
