package microStar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import microStar.factory.SessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Customer")
public class Customer implements Serializable {
    @Id
    @Column(name = "customerID")
    private String customerID; //Primary Key

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "address")
    private String address;

    private static final Logger logger = LogManager.getLogger(Customer.class);

    public Customer(){
        customerID = " ";
        password = " ";
        firstName = " ";
        lastName = " ";
        address = " ";
    }

    public Customer(String customerID, String password, String firstName, String lastName, String address){
        this.customerID = customerID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Customer(Customer c){
        this.customerID = c.customerID;
        this.password = c.password;
        this.firstName = c.firstName;
        this.lastName = c.lastName;
        this.address = c.address;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "Customer ID: " + customerID +
                ", First Name: " + firstName +
                ", Last Name: " + lastName +
                ", Address: " + address +
                '}';
    }

    public void create(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.save(this);
            transaction.commit();
            logger.info("Customer created and saved");
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            logger.error("RunTime exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        finally{
            session.close();
        }
    }

    public void update(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Customer cust = (Customer) session.get(Customer.class,this.customerID);
            cust.setFirstName(this.firstName);
            cust.setLastName(this.lastName);
            cust.setAddress(this.address);
            session.update(cust);
            transaction.commit();
            logger.info("Customer Name and Address is updated");
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            logger.error("RunTime exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        finally{
            session.close();
        }
    }

    public List<Customer> readAll(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        List<Customer> customerList = new ArrayList<Customer>();

        try{
            transaction = session.beginTransaction();
            customerList = (List<Customer>) session.createQuery("FROM Customer").getResultList();
            transaction.commit();
            logger.info("All Customers read");
        }
        catch(ClassCastException c){
            c.printStackTrace();
            logger.error("ClassCast exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            logger.error("RunTime exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        finally{
            session.close();
        }
        return customerList;
    }

    public Customer readCustomer(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        Customer customerObj = new Customer();

        try{
            transaction = session.beginTransaction();
            customerObj = (Customer) session.get(Customer.class,this.customerID);
            transaction.commit();
            logger.info("Customer read");
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            logger.error("RunTime exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        finally{
            session.close();
        }
        return customerObj;
    }

    public void delete(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Customer customerObj = (Customer) session.get(Customer.class,this.customerID);
            session.delete(customerObj);
            transaction.commit();
            logger.info("Customer deleted");
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            logger.error("RunTime exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        finally{
            session.close();
        }
    }

    public boolean authenticate(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        boolean login = false;

        try{
            if(this.customerID == null || this.password == null){
                return false;
            }
            transaction = session.beginTransaction();
            Customer customerObj = (Customer) session.get(Customer.class,this.customerID);
            if(Objects.equals(customerObj.getPassword(), this.password)){
                login = true;
                logger.info("Customer authenticated");
            }
            transaction.commit();
        }
        catch(RuntimeException ex){
            ex.printStackTrace();
            logger.error("RunTime exception occurred");
            if(transaction != null){
                transaction.rollback();
                logger.error("Transaction rolled back");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            logger.error("Exception occurred");
        }
        finally{
            session.close();
        }
        return login;
    }
}

