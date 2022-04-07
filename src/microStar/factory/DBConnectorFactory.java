package microStar.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectorFactory {
    private static Connection connection = null;
    private static final Logger logger = LogManager.getLogger(DBConnectorFactory.class);

    public static Connection getDatabaseConnection(){
        connection = null;
        if (connection == null) {
            try{
                String url = "jdbc:mysql://localhost:3306/MicroStar_CableVision";
                connection = DriverManager.getConnection(url, "root", "");
                logger.info("Connection established");
            }
            catch(SQLException s){
                s.printStackTrace();
                logger.error("SQL Exception Occurred");
            }
            catch(Exception e){
                e.printStackTrace();
                logger.error("Exception Occurred");
            }
        }
        return connection;
    }

    public static void closeDatabaseConnection(){
        if (connection != null){
            try{
                connection.close();
                logger.info("Database Connection closed");
            }
            catch(SQLException s){
                s.printStackTrace();
                logger.error("SQL Exception Occurred");
            }
            catch(Exception e){
                e.printStackTrace();
                logger.error("Exception Occurred");
            }
        }
    }
}


