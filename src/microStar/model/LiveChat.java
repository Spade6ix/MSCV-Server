package microStar.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import microStar.factory.DBConnectorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiveChat implements Serializable{
    private int liveChatID; // Primary Key
    private String customerID; //Foreign Key
    private String staffID; //Foreign Key
    private String message;
    private boolean sentByCustomer;

    private static final Logger logger = LogManager.getLogger(LiveChat.class);

    public LiveChat(){
        this.liveChatID = 0;
        this.customerID = " ";
        this.staffID = " ";
        this.message = " ";
        this.sentByCustomer = false;
    }

    public LiveChat(int liveChatID, String customerID, String staffID, String message, boolean sentByCustomer){
        this.liveChatID = liveChatID;
        this.customerID = customerID;
        this.staffID = staffID;
        this.message = message;
        this.sentByCustomer = sentByCustomer;
    }

    public LiveChat(LiveChat l){
        this.liveChatID = l.liveChatID;
        this.customerID = l.customerID;
        this.staffID = l.staffID;
        this.message = l.message;
        this.sentByCustomer = l.sentByCustomer;
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

    public boolean isSentByCustomer() {
        return sentByCustomer;
    }

    public void setSentByCustomer(boolean sentByCustomer) {
        this.sentByCustomer = sentByCustomer;
    }

    @Override
    public String toString() {
        return "LiveChat{" +
                "Customer ID: " + customerID +
                ", Staff ID: " + staffID +
                ", Message: " + message +
                ", Sent By Customer? " + sentByCustomer +
                '}';
    }

    public void create(String customerID, String staffID, String message, boolean sentByCustomer){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "INSERT INTO LiveChat(customerID, staffID, message, sentByCustomer) VALUES (?,?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,customerID);
            ps.setString(2,staffID);
            ps.setString(3,message);
            ps.setBoolean(4,sentByCustomer);
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

    public ArrayList<LiveChat> readAll(){
        ResultSet result = null;
        ArrayList<LiveChat> liveChatArrayList = new ArrayList<>();
        LiveChat obj = new LiveChat();
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "SELECT * FROM LiveChat";
            PreparedStatement ps = c.prepareStatement(sql);
            result = ps.executeQuery();
            while(result.next()){
                obj.setLiveChatID(result.getInt(1));
                obj.setCustomerID(result.getString(2));
                obj.setStaffID(result.getString(3));
                obj.setMessage(result.getString(4));
                obj.setSentByCustomer(result.getBoolean(5));
                liveChatArrayList.add(obj);
            }
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
        return liveChatArrayList;
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

