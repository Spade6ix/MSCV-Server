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
@Table(name = "Employee")
public class Employee implements Serializable {
    @Id
    @Column(name = "staffID")
    private String staffID; //Primary Key

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "job")
    private char job;

    private static final Logger logger = LogManager.getLogger(Employee.class);

    //Constructors
    public Employee() {
        this.staffID = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.job = 'E';
    }

    public Employee(String staffID, String password, String firstName, String lastName, char job) {
        this.staffID = staffID;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
    }

    public Employee(Employee empModelObj) {
        this.staffID = empModelObj.staffID;
        this.password = empModelObj.password;
        this.firstName = empModelObj.firstName;
        this.lastName = empModelObj.lastName;
        this.job = empModelObj.job;
    }

    //Accessors and mutators
    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getJob() {
        return job;
    }

    public void setJob(char job) {
        this.job = job;
    }

    //ToString
    @Override
    public String toString() {
        return "Staff ID: "+ this.getStaffID()+ "\nName: "+ this.getFirstName()+ " " + this.getLastName() + "\nJob: "+ this.getJob();
    }

    public void create(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.save(this);
            transaction.commit();
            logger.info("Employee created and saved");
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
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Employee emp = (Employee) session.get(Employee.class,this.staffID);
            emp.setFirstName(this.firstName);
            emp.setLastName(this.lastName);
            emp.setJob(this.job);
            session.update(emp);
            transaction.commit();
            logger.info("Employee Name and Job title updated");
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

    public List<Employee> readAll(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        List<Employee> employeeList = new ArrayList<Employee>();

        try{
            transaction = session.beginTransaction();
            employeeList = (List<Employee>) session.createQuery("FROM Employee").getResultList();
            transaction.commit();
            logger.info("All Employees read");
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
        return employeeList;
    }

    public Employee readEmployee(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        Employee employeeObj = new Employee();

        try{
            transaction = session.beginTransaction();
            employeeObj = (Employee) session.get(Employee.class,this.staffID);
            transaction.commit();
            logger.info("Employee read");
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
        return employeeObj;
    }

    public void delete(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Employee employeeObj = (Employee) session.get(Employee.class,this.staffID);
            session.delete(employeeObj);
            transaction.commit();
            logger.info("Employee deleted");
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
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        boolean login = false;

        try{
            if(this.staffID == null || this.password == null){
                return false;
            }
            transaction = session.beginTransaction();
            Employee employeeObj = (Employee) session.get(Employee.class,this.staffID);
            if(Objects.equals(employeeObj.getPassword(), this.password)){
                login = true;
                logger.info("Employee authenticated");
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

