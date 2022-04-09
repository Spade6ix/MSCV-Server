package microStar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import microStar.factory.SessionFactoryBuilder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;

@Entity
@Table(name = "Complaint")
public class Complaint implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaintID")
    private int complaintID; //Primary Key

    @Column(name = "complaintType")
    private String complaintType; //(Payment, Internet, Cable, Other)

    @Column(name = "complaintDetails")
    private String complaintDetails;

    @Column(name = "status")
    private char status; //(R/U)

    @Column(name = "customerID")
    private String customerID; //(Foreign Key)

    @Column(name = "staffID")
    private String staffID; //(Foreign Key) //TechnicianID

    private static final Logger logger = LogManager.getLogger(Complaint.class);

    public Complaint(){
        this.complaintID = 0;
        this.complaintType = "";
        this.complaintDetails = "";
        this.status = 'U';
        this.customerID = "";
        this.staffID = "";
    }

    public Complaint(int complaintID, String complaintType, String complaintDetails, char status, String customerID, String staffID) {
        this.complaintID = complaintID;
        this.complaintType = complaintType;
        this.complaintDetails = complaintDetails;
        this.status = status;
        this.customerID = customerID;
        this.staffID = staffID;
    }

    public Complaint(Complaint complaint) {
        this.complaintID = complaint.complaintID;
        this.complaintType = complaint.complaintType;
        this.complaintDetails = complaint.complaintDetails;
        this.status = complaint.status;
        this.customerID = complaint.customerID;
        this.staffID = complaint.staffID;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String toString() {
        return "Complaint ID: " + complaintID + "\n"
                + "Complaint Type: " + complaintType + "\n"
                + "Complaint Detail: " + complaintDetails + "\n"
                + "Status: " + status + "\n"
                + "Customer ID: " + customerID + "\n"
                + "Staff ID: " + staffID;
    }

    public void create(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.save(this);
            transaction.commit();
            logger.info("Complaint created and saved");
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

    public void updateTechnician(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Complaint com = (Complaint) session.get(Complaint.class,this.complaintID);
            com.setStaffID(this.staffID);
            session.update(com);
            transaction.commit();
            logger.info("Complaint updated");
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

    public void updateStatus(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Complaint com = (Complaint) session.get(Complaint.class,this.complaintID);
            com.setStatus(this.status);
            session.update(com);
            transaction.commit();
            logger.info("Complaint updated");
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

    public List<Complaint> readAll(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        List<Complaint> complaintList = new ArrayList<Complaint>();

        try{
            transaction = session.beginTransaction();
            complaintList = (List<Complaint>) session.createQuery("FROM Complaint").getResultList();
            transaction.commit();
            logger.info("All Complaints read");
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
        return complaintList;
    }

    public Complaint readComplaint(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        Complaint complaintObj = new Complaint();

        try{
            transaction = session.beginTransaction();
            complaintObj = (Complaint) session.get(Complaint.class,this.complaintID);
            transaction.commit();
            logger.info("Complaint read");
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
        return complaintObj;
    }

    public void delete(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Complaint com = (Complaint) session.get(Complaint.class,this.complaintID);
            session.delete(com);
            transaction.commit();
            logger.info("Complaint deleted");
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
}

