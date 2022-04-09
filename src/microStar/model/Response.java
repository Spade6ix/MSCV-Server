package microStar.model;

import java.io.Serializable;
import microStar.factory.SessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Response")
public class Response implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "responseID")
    private int responseID; //Primary Key

    @Column(name = "complaintID")
    private int complaintID; //Foreign Key

    @Column(name = "proposedDateOfVisit")
    private LocalDateTime proposedDateOfVisit;

    @Column(name = "responseDetails")
    private String responseDetails;

    @Column(name = "responseDateTime")
    private LocalDateTime responseDateTime;

    @Column(name = "staffID")
    private String staffID; //Foreign Key //ID of Staff member that made Response

    private static final Logger logger = LogManager.getLogger(Response.class);

    public Response(){
        this.responseID = 0;
        this.complaintID = 0;
        this.proposedDateOfVisit = null;
        this.responseDetails = " ";
        this.responseDateTime = null;
        this.staffID = " ";
    }

    public Response(int responseID, int complaintID, LocalDateTime proposedDateOfVisit, String responseDetails, LocalDateTime responseDateTime, String staffID){
        this.responseID = responseID;
        this.complaintID = complaintID;
        this.proposedDateOfVisit = proposedDateOfVisit;
        this.responseDetails = responseDetails;
        this.responseDateTime = responseDateTime;
        this.staffID = staffID;
    }

    public Response(Response r){
        this.responseID = r.responseID;
        this.complaintID = r.complaintID;
        this.proposedDateOfVisit = r.proposedDateOfVisit;
        this.responseDetails = r.responseDetails;
        this.responseDateTime = r.responseDateTime;
        this.staffID = r.staffID;
    }

    public void setResponseID(int responseID) {
        this.responseID = responseID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public void setProposedDateOfVisit(LocalDateTime proposedDateOfVisit) {
        this.proposedDateOfVisit = proposedDateOfVisit;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    public void setResponseDateTime(LocalDateTime responseDateTime) {
        this.responseDateTime = responseDateTime;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public int getResponseID() {
        return responseID;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public LocalDateTime getProposedDateOfVisit() {
        return proposedDateOfVisit;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public LocalDateTime getResponseDateTime() {
        return responseDateTime;
    }

    public String getStaffID() {
        return staffID;
    }

    @Override
    public String toString() {
        return "Response{" +
                "Response ID: " + responseID +
                ", Complaint ID: " + complaintID +
                ", Proposed Date Of Visit: " + proposedDateOfVisit +
                ", Response Details: " + responseDetails +
                ", Response Date & Time: " + responseDateTime +
                ", Staff ID: " + staffID +
                '}';
    }

    public void create(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.save(this);
            transaction.commit();
            logger.info("Response created and saved");
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
            Response r= (Response) session.get(Response.class,this.responseID);
            r.setResponseDetails(this.responseDetails);
            r.setResponseDateTime(this.responseDateTime);
            r.setProposedDateOfVisit(this.proposedDateOfVisit);
            session.update(r);
            transaction.commit();
            logger.info("Response updated");
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

    public List<Response> readAll(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        List<Response> responseList = new ArrayList<Response>();

        try{
            transaction = session.beginTransaction();
            responseList = (List<Response>) session.createQuery("FROM Response").getResultList();
            transaction.commit();
            logger.info("All Responses read");
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
        return responseList;
    }

    public Response readResponse(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        Response responseObj = new Response();

        try{
            transaction = session.beginTransaction();
            responseObj = (Response) session.get(Response.class,this.responseID);
            transaction.commit();
            logger.info("Response read");
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
        return responseObj;
    }

    public void delete(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Response responseObj = (Response) session.get(Response.class,this.responseID);
            session.delete(responseObj);
            transaction.commit();
            logger.info("Response deleted");
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

