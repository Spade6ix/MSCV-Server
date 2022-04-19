package microStar.model;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

import net.bytebuddy.implementation.bytecode.collection.*;
import org.apache.logging.log4j.LogManager;

import microStar.factory.DBConnectorFactory;
import org.apache.logging.log4j.Logger;

public class Query implements Serializable{
    private String customerID; //Primary Key & Foreign Key
    private String paymentStatus;
    private Double amountDue;
    private String dueDate;

    private static final Logger logger = LogManager.getLogger(Query.class);

    public Query(){
        this.customerID= " ";
        this.paymentStatus = " ";
        this.amountDue = 0.00;
        this.dueDate = " ";
    }

    public Query(String customerID, String paymentStatus, Double amountDue, String dueDate){
        this.customerID = customerID;
        this.paymentStatus = paymentStatus;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
    }

    public Query(Query q){
        this.customerID = q.customerID;
        this.paymentStatus = q.paymentStatus;
        this.amountDue = q.amountDue;
        this.dueDate = q.dueDate;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Double getAmountDue() {
        return amountDue;
    }

    public String getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        return "Query{" +
                "CustomerID: " + customerID +
                ", Payment Status: " + paymentStatus +
                ", Amount Due: " + amountDue +
                ", Due Date: " + dueDate +
                '}';
    }

    public void create(String customerID, String paymentStatus, Double amtDue, String dueDate){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "INSERT INTO QUERY(customerID, paymentStatus, amtDue, dueDate) VALUES (?,?,?,?)";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,customerID);
            ps.setString(2,paymentStatus);
            ps.setDouble(3,amtDue);
            ps.setString(4,dueDate);
            ps.executeUpdate();
            logger.info("New Customer added to the Query Table");
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

    public ArrayList<Query> readAll(){
        ResultSet result = null;
        ArrayList<Query> queryArrayList = new ArrayList<>();
        Query obj;
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "SELECT * FROM Query";
            PreparedStatement ps = c.prepareStatement(sql);
            result = ps.executeQuery();
            while(result.next()){
                obj = new Query();
                obj.setCustomerID(result.getString(1));
                obj.setPaymentStatus(result.getString(2));
                obj.setAmountDue(result.getDouble(3));
                obj.setDueDate(result.getString(4));
                queryArrayList.add(obj);
            }
            logger.info("All records in Query Table read");
        }
        catch(SQLException s){
            s.printStackTrace();
            logger.error("SQL Exception occurred");
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        return queryArrayList;
    }

    public Query read(String customerID){
        ResultSet result = null;
        Query obj = new Query();
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "SELECT * FROM Query WHERE customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,customerID);
            result = ps.executeQuery();
            while (result.next()){
                obj.setCustomerID(result.getString(1));
                obj.setPaymentStatus(result.getString(2));
                obj.setAmountDue(result.getDouble(3));
                obj.setDueDate(result.getString(4));
            }
            logger.info("Record in Query Table read");
        }
        catch(SQLException s){
            s.printStackTrace();
            logger.error("SQL Exception occurred");
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        return obj;
    }

    public void updatePaymentStatus(String customerID, String paymentStatus){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "UPDATE Query SET paymentStatus = ? WHERE customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(2,customerID);
            ps.setString(1,paymentStatus);
            ps.executeUpdate();
            logger.info("Payment Status Updated");
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

    public void updateAmtDue(String customerID, Double amtDue){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "UPDATE Query SET amtDue = ? WHERE customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(2,customerID);
            ps.setDouble(1,amtDue);
            ps.executeUpdate();
            logger.info("Amount Due Updated");
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

    public void updateDueDate(String customerID, String dueDate){
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "UPDATE Query SET dueDate = ? WHERE customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(2,customerID);
            ps.setString(1,dueDate);
            ps.executeUpdate();
            logger.info("Due Date Updated");
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

    public void delete(String customerID) {
        try(Connection c = DBConnectorFactory.getDatabaseConnection()){
            String sql = "DELETE FROM Query WHERE customerID = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,customerID);
            ps.executeUpdate();
            logger.info("Record from Query Table deleted");
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

