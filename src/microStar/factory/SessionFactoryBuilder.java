package microStar.factory;

import microStar.model.Customer;
import microStar.model.Employee;
import microStar.model.Complaint;
import microStar.model.Payment;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class SessionFactoryBuilder {
    private static SessionFactory session = null;
    private static final Logger logger = LogManager.getLogger(SessionFactoryBuilder.class);

    public static SessionFactory getSessionFactory(){
        if(session == null){
            try{
                session = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class)
                        .addAnnotatedClass(Employee.class)
                        .addAnnotatedClass(Complaint.class)
                        .addAnnotatedClass(Payment.class)
                        .buildSessionFactory();
                logger.info("Session established");
            }
            catch(HibernateException h){
                h.printStackTrace();
                logger.error("Hibernate Exception Occurred");
            }
            catch(RuntimeException r){
                r.printStackTrace();
                logger.error("Runtime Exception Occurred");
            }
            catch(Exception e){
                e.printStackTrace();
                logger.error("Exception Occurred");
            }
        }
        return session;
    }

    public static void closeSessionFactory(){
        if(session != null){
            try{
                session.close();
                logger.info("Session Closed");
            }
            catch(HibernateException h){
                h.printStackTrace();
                logger.error("Hibernate Exception Occurred");
            }
            catch(RuntimeException r){
                r.printStackTrace();
                logger.error("Runtime Exception Occurred");
            }
            catch(Exception e){
                e.printStackTrace();
                logger.error("Exception Occurred");
            }
        }
    }
}

