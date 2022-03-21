package microStar.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import microStar.factory.DBConnectorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiveChat implements Serializable{
    private int liveChatID; // Primary Key
    private String customerID; //Foreign Key
    private String staffID; //Foreign Key
    private String message;

    private static final Logger logger = LogManager.getLogger(LiveChat.class);

    public LiveChat(){
        this.liveChatID = 0;
        this.customerID = " ";
        this.staffID = " ";
        this.message = " ";
    }

    public LiveChat(int liveChatID, String customerID, String staffID, String message){
        this.liveChatID = liveChatID;
        this.customerID = customerID;
        this.staffID = staffID;
        this.message = message;
    }

    public LiveChat(LiveChat l){
        this.liveChatID = l.liveChatID;
        this.customerID = l.customerID;
        this.staffID = l.staffID;
        this.message = l.message;
    }

    public void setLiveChatID(int liveChatID) {
        this.liveChatID = liveChatID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getStaffID() {
        return staffID;
    }

    public String getMessage() {
        return message;
    }

    public int getLiveChatID() {
        return liveChatID;
    }

    @Override
    public String toString() {
        return "LiveChat{" +
                "Customer ID: " + customerID +
                ", Staff ID: " + staffID +
                ", Message: " + message +
                '}';
    }

    public void create(String customerID, String staffID, String message){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "INSERT INTO LiveChat(customerID, staffID, message) VALUES (?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,customerID);
            ps.setString(2,staffID);
            ps.setString(3,message);
            ps.executeUpdate();
            logger.info("Live Chat created and saved");
        }
        catch(SQLException s){
            s.printStackTrace();
            logger.error("SQL Exception occurred");
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
    }

    public ResultSet readAll(){
        ResultSet result = null;
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "SELECT * FROM LiveChat";
            PreparedStatement ps = c.prepareStatement(sql);
            result = ps.executeQuery();
            /*while(result.next()){
                System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getDouble(3) + " " + result.getString(4) + "\n");
            }*/
            logger.info("All records in LiveChat Table read");
        }
        catch(SQLException s){
            s.printStackTrace();
            logger.error("SQL Exception occurred");
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        return result;
    }

    public void delete(int liveChatID) {
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "DELETE FROM LiveChat WHERE liveChatID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1,liveChatID);
            ps.executeUpdate();
            logger.info("Record from LiveChat Table deleted");
        }
        catch(SQLException s){
            s.printStackTrace();
            logger.error("SQL Exception occurred");
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
    }
}

