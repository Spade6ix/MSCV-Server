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

public class CustomerPhone implements Serializable {
    private String phone; //Primary Key
    private String customerID; //Primary Key & Foreign Key

    private static final Logger logger = LogManager.getLogger(CustomerPhone.class);

    public CustomerPhone(){
        this.phone = " ";
        this.customerID = " ";
    }

    public CustomerPhone(String phone, String customerID){
        this.phone = phone;
        this.customerID = customerID;
    }

    public CustomerPhone(CustomerPhone p){
        this.phone = p.phone;
        this.customerID = p.customerID;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getPhone() {
        return phone;
    }

    public String getCustomerID() {
        return customerID;
    }

    @Override
    public String toString() {
        return "CustomerPhone{" +
                "Phone: " + phone +
                ", Customer ID: " + customerID +
                '}';
    }

    public void create(String phone, String customerID){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "INSERT INTO CustomerPhone(phone, customerID) VALUES (?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,phone);
            ps.setString(2,customerID);
            ps.executeUpdate();
            logger.info("Customer Phone saved");
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

    public ArrayList<CustomerPhone> readAll(){
        ResultSet result = null;
        ArrayList<CustomerPhone> customerPhoneArrayList = new ArrayList<>();
        CustomerPhone customerPhone;
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "SELECT * FROM CustomerPhone";
            PreparedStatement ps = c.prepareStatement(sql);
            result = ps.executeQuery();
            while(result.next()){
                customerPhone = new CustomerPhone();
                customerPhone.setPhone(result.getString(1));
                customerPhone.setCustomerID(result.getString(2));
                customerPhoneArrayList.add(customerPhone);
            }
            logger.info("All records in CustomerPhone Table read");
        }
        catch(SQLException s){
            s.printStackTrace();
            logger.error("SQL Exception occurred");
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        return customerPhoneArrayList;
    }

    public void updatePhone(String prevPhone, String newPhone, String customerID){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "UPDATE CustomerPhone SET phone = ? WHERE customerID = ? AND phone = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,newPhone);
            ps.setString(2,customerID);
            ps.setString(3,prevPhone);
            ps.executeUpdate();
            logger.info("Customer Phone Updated");
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

    public void delete(String Phone, String customerID) {
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "DELETE FROM CustomerPhone WHERE phone = ? AND customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,phone);
            ps.setString(2,customerID);
            ps.executeUpdate();
            logger.info("Record from CustomerPhone Table deleted");
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

