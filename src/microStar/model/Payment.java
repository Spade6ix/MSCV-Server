package microStar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@Table(name = "Payment")
public class Payment implements Serializable {
    @Id
    @Column(name = "paymentID")
    private String paymentID; //Primary Key

    @Column(name = "customerID")
    private String customerID; //Foreign Key

    @Column(name = "dateOfPayment")
    private String dateOfPayment;

    @Column(name = "amountPaid")
    private Double amountPaid;

    private static final Logger logger = LogManager.getLogger(Payment.class);

    public Payment(){
        this.paymentID = " ";
        this.customerID = " ";
        this.dateOfPayment = " ";
        this.amountPaid = 0.00;
    }

    public Payment(String paymentID, String customerID, String dateOfPayment, Double amountPaid){
        this.paymentID = paymentID;
        this.customerID = customerID;
        this.dateOfPayment = dateOfPayment;
        this.amountPaid = amountPaid;
    }

    public Payment(Payment p){
        this.paymentID = p.paymentID;
        this.customerID = p.customerID;
        this.dateOfPayment = p.dateOfPayment;
        this.amountPaid = p.amountPaid;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getDateOfPayment() {
        return dateOfPayment;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "Payment ID: " + paymentID +
                ", Customer ID: " + customerID +
                ", Date Of Payment: " + dateOfPayment +
                ", Amount Paid: " + amountPaid +
                '}';
    }

    public void create(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.save(this);
            transaction.commit();
            logger.info("Payment created and saved");
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
            Payment p= (Payment) session.get(Payment.class,this.paymentID);
            p.setDateOfPayment(this.dateOfPayment);
            p.setAmountPaid(this.amountPaid);
            session.update(p);
            transaction.commit();
            logger.info("Payment updated");
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

    public List<Payment> readAll(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        List<Payment> paymentList = new ArrayList<Payment>();

        try{
            transaction = session.beginTransaction();
            paymentList = (List<Payment>) session.createQuery("FROM Payment").getResultList();
            transaction.commit();
            logger.info("All Payments read");
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
        return paymentList;
    }

    public Payment readPayment(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;
        Payment paymentObj = new Payment();

        try{
            transaction = session.beginTransaction();
            paymentObj = (Payment) session.get(Payment.class,this.paymentID);
            transaction.commit();
            logger.info("Payment read");
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
        return paymentObj;
    }

    public void delete(){
        Session session = SessionFactoryBuilder.getSessionFactory().getCurrentSession();;
        Transaction transaction = null;

        try{
            transaction = session.beginTransaction();
            Payment paymentObj = (Payment) session.get(Payment.class,this.paymentID);
            session.delete(paymentObj);
            transaction.commit();
            logger.info("Payment deleted");
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

