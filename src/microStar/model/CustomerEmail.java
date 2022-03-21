package microStar.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import microStar.factory.DBConnectorFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomerEmail implements Serializable {
    private String email; //Primary Key
    private String customerID; //Primary Key & Foreign Key

    private static final Logger logger = LogManager.getLogger(CustomerEmail.class);

    public CustomerEmail(){
        this.email = " ";
        this.customerID = " ";
    }

    public CustomerEmail(String email, String customerID){
        this.email = email;
        this.customerID = customerID;
    }

    public CustomerEmail(CustomerEmail e){
        this.email = e.email;
        this.customerID = e.customerID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomerID() {
        return customerID;
    }

    @Override
    public String toString() {
        return "CustomerEmail{" +
                "Email: " + email +
                ", Customer ID: " + customerID +
                '}';
    }

    public void create(String email, String customerID){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "INSERT INTO CustomerEmail(email, customerID) VALUES (?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,email);
            ps.setString(2,customerID);
            ps.executeUpdate();
            logger.info("Customer Email saved");
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
            String sql = "SELECT * FROM CustomerEmail";
            PreparedStatement ps = c.prepareStatement(sql);
            result = ps.executeQuery();
            /*while(result.next()){
                System.out.println(result.getString(1) + " " + result.getString(2) + " " + result.getDouble(3) + " " + result.getString(4) + "\n");
            }*/
            logger.info("All records in CustomerEmail Table read");
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

    public void updateEmail(String prevEmail, String newEmail, String customerID){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "UPDATE CustomerEmail SET email = ? WHERE customerID = ? AND email = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,newEmail);
            ps.setString(2,customerID);
            ps.setString(3,prevEmail);
            ps.executeUpdate();
            logger.info("Customer Email Updated");
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

    public void delete(String email, String customerID) {
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "DELETE FROM CustomerEmail WHERE email = ? AND customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,email);
            ps.setString(2,customerID);
            ps.executeUpdate();
            logger.info("Record from CustomerEmail Table deleted");
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

