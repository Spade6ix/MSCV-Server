package microStar.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import microStar.factory.SessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import microStar.factory.DBConnectorFactory;
import microStar.model.Complaint;
import microStar.model.Customer;
import microStar.model.CustomerEmail;
import microStar.model.CustomerPhone;
import microStar.model.Employee;
import microStar.model.LiveChat;
import microStar.model.Payment;
import microStar.model.Query;
import microStar.model.Response;
import org.hibernate.type.LocalDateTimeType;

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    
    private ServerSocket serverSocket;
	private Socket connectionSocket;
	
    
	public Server() {
		try{
			serverSocket = new ServerSocket (9555);
			connectionSocket = new Socket();
			logger.info("Server has started!!!");
			while(true){
				connectionSocket = serverSocket.accept();
				logger.info("Server is starting a thread for a Client");
				//JOptionPane.showMessageDialog(null,"Connection Established","Server Connection Status",JOptionPane.INFORMATION_MESSAGE);
				ClientHandler client = new ClientHandler(connectionSocket);
				client.start();
			}
		}
		catch(IOException x){
			logger.fatal("IOException Occurred");
			x.printStackTrace();
		}
	}
	
	class ClientHandler extends Thread{
		Socket socket;
		ObjectOutputStream objOs;
		ObjectInputStream objIs;

		public ClientHandler(Socket socket){
			try{
				this.socket = socket;
				objOs = new ObjectOutputStream(socket.getOutputStream());
				objIs = new ObjectInputStream(socket.getInputStream());
			}
			catch (IOException e) {
				logger.fatal("IOException Occurred");
				e.printStackTrace();
			}
			catch(Exception e){
				System.err.println("Exception Occurred");
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			String action = "";
			Complaint complaintObj = new Complaint();
			Customer customerObj = new Customer();
			CustomerEmail customerEmailObj = new CustomerEmail();
			CustomerPhone customerPhoneObj = new CustomerPhone();
			Employee employeeObj = new Employee();
			LiveChat liveChatObj = new LiveChat();
			Payment paymentObj = new Payment();
			Query queryObj = new Query();
			Response responseObj = new Response();
			ResultSet result;
			boolean login;
			String customerID;
			try {
				while(true) {
					try {
                        List<Response> responseList;
                        List<Complaint> complaintList;
                        List<Payment> paymentList;
                        List<CustomerEmail> customerEmailList = new ArrayList<>();
                        List<CustomerPhone> customerPhoneList = new ArrayList<>();
                        List<Employee> employeeList = new ArrayList<>();
						action = (String) objIs.readObject();
						if (action.equalsIgnoreCase("Customer Login")) {
							customerObj = (Customer) objIs.readObject();
							login = customerObj.authenticate();
							customerObj = customerObj.readCustomer();
							objOs.writeObject(login);
							objOs.writeObject(customerObj);
							if(login){
								logger.info("Customer Authenticated");
							}
							else{
								logger.info("Customer provided incorrect credentials");
							}
						}
						else if (action.equalsIgnoreCase("Employee Login")) {
							employeeObj = (Employee) objIs.readObject();
							login = employeeObj.authenticate();
							employeeObj = employeeObj.readEmployee();
							objOs.writeObject(login);
							objOs.writeObject(employeeObj);
							if(login){
								logger.info("Employee Authenticated");
							}
							else{
								logger.info("Employee Provided incorrect credentials");
							}
						}
						else if (action.equalsIgnoreCase("Create complaint")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintObj.create();
							objOs.writeObject(true);
							logger.info("Complaint Created");
						}
						else if (action.equalsIgnoreCase("Make Query")) {
							customerID = (String) objIs.readObject();
							result = queryObj.read(customerID);
							if(result!=null){
								objOs.writeObject(result);
								logger.info("Query executed");
							}
							else{
								logger.info("Customer associated with ID has no Query Info in the Database");
							}
						}
						else if (action.equalsIgnoreCase("View All Responses to a complaint")) {
							complaintObj = (Complaint) objIs.readObject();
							responseList = responseObj.readAll();
							for(Response r : responseList){
								if(complaintObj.getComplaintID() != r.getComplaintID()){
									responseList.remove(r);
								}
							}
							objOs.writeObject(responseList);
							logger.info("All Responses to a complaint are fetched successfully");
						}
						else if (action.equalsIgnoreCase("View All Complaints of a Customer")) {
							customerObj = (Customer) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Response> responseList1 = new ArrayList<>();
							LocalDateTime max = LocalDateTime.MIN;
							for(Complaint c : complaintList){
								responseList = responseObj.readAll();
								if(!c.getCustomerID().equals(customerObj.getCustomerID())){
									complaintList.remove(c);
								}
								else{
									for(Response r : responseList){
										if(r.getComplaintID() != c.getComplaintID()){
											responseList.remove(r);
										}
									}
									//find max in responseList
									for(Response r : responseList){
										if(max.isBefore(r.getResponseDateTime())){
											responseObj = r;
										}
									}
									responseList1.add(responseObj);
									employeeObj.setStaffID(responseObj.getStaffID());
									employeeList.add(employeeObj);
								}
							}
							for(Employee e : employeeList){
								e = e.readEmployee();
							}
							objOs.writeObject(complaintList);
							//customerObj = customerObj.readCustomer();
							//objOs.writeObject(customerObj);
							objOs.writeObject(responseList1);
							objOs.writeObject(employeeList);
							logger.info("Past Complaints of a customer fetched successfully");
						}
						else if (action.equalsIgnoreCase("View a Complaint of a Customer")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintObj = complaintObj.readComplaint();
							//customerObj.setCustomerID(complaintObj.getCustomerID());
							//customerObj = customerObj.readCustomer();
							objOs.writeObject(complaintObj);
							//objOs.writeObject(customerObj);
							logger.info("Details of a Complaint fetched successfully");
						}
						else if (action.equalsIgnoreCase("View All Payments made by a Customer")) {
							customerObj = (Customer) objIs.readObject();
							paymentList = paymentObj.readAll();
							for(Payment p : paymentList){
								if(!customerObj.getCustomerID().equals(p.getCustomerID())){
									paymentList.remove(p);
								}
							}
							objOs.writeObject(paymentList);
							logger.info("Past payments of a customer fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Number of Resolved & Unresolved Complaints")) {
							complaintList = complaintObj.readAll();
							int resolved = 0;
							int unresolved = 0;
							for (Complaint c : complaintList){
								if(c.getStatus() == 'U' || c.getStatus() == 'u'){
									unresolved++;
								}
								else if (c.getStatus() == 'R' || c.getStatus() == 'r'){
									resolved++;
								}
							}
							objOs.writeObject(resolved);
							objOs.writeObject(unresolved);
							logger.info("Number of resolved and unresolved complaints calculated successfully");
						}
						else if (action.equalsIgnoreCase("View Payment Complaints")) {
							complaintList = complaintObj.readAll();
							for (Complaint c : complaintList){
								if(!c.getComplaintType().equalsIgnoreCase("Payment")){
									complaintList.remove(c);
								}
							}
							objOs.writeObject(complaintList);
							logger.info("Payment Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Internet Complaints")) {
							complaintList = complaintObj.readAll();
							for (Complaint c : complaintList){
								if(!c.getComplaintType().equalsIgnoreCase("Internet")){
									complaintList.remove(c);
								}
							}
							objOs.writeObject(complaintList);
							logger.info("Internet Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Cable Complaints")) {
							complaintList = complaintObj.readAll();
							for (Complaint c : complaintList){
								if(!c.getComplaintType().equalsIgnoreCase("Cable")){
									complaintList.remove(c);
								}
							}
							objOs.writeObject(complaintList);
							logger.info("Cable Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Other Complaints")) {
							complaintList = complaintObj.readAll();
							for (Complaint c : complaintList){
								if(!c.getComplaintType().equalsIgnoreCase("Other")){
									complaintList.remove(c);
								}
							}
							objOs.writeObject(complaintList);
							logger.info("Other Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Complaint details and Customer account info")) {
						    complaintObj = (Complaint) objIs.readObject();
						    complaintObj = complaintObj.readComplaint();
						    customerObj.setCustomerID(complaintObj.getCustomerID());
						    customerObj = customerObj.readCustomer();
						    result = customerPhoneObj.readAll();
							while(result.next()){
								if(result.getString(2).equalsIgnoreCase(customerObj.getCustomerID())){
									customerPhoneObj.setCustomerID(result.getString(2));
									customerPhoneObj.setPhone(result.getString(1));
									customerPhoneList.add(customerPhoneObj);
								}
							}
							result = customerEmailObj.readAll();
							while(result.next()){
								if(result.getString(2).equalsIgnoreCase(customerObj.getCustomerID())){
									customerEmailObj.setCustomerID(result.getString(2));
									customerEmailObj.setEmail(result.getString(1));
									customerEmailList.add(customerEmailObj);
								}
							}
							objOs.writeObject(customerObj);
							objOs.writeObject(complaintObj);
							objOs.writeObject(customerPhoneList);
							objOs.writeObject(customerEmailList);
							logger.info("Complaint details and associated Customer info fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Customer account info")) {
							customerObj = (Customer) objIs.readObject();
							customerObj = customerObj.readCustomer();
							result = customerPhoneObj.readAll();
							while(result.next()){
								if(result.getString(2).equalsIgnoreCase(customerObj.getCustomerID())){
									customerPhoneObj.setCustomerID(result.getString(2));
									customerPhoneObj.setPhone(result.getString(1));
									customerPhoneList.add(customerPhoneObj);
								}
							}
							result = customerEmailObj.readAll();
							while(result.next()){
								if(result.getString(2).equalsIgnoreCase(customerObj.getCustomerID())){
									customerEmailObj.setCustomerID(result.getString(2));
									customerEmailObj.setEmail(result.getString(1));
									customerEmailList.add(customerEmailObj);
								}
							}
							objOs.writeObject(customerObj);
							objOs.writeObject(customerPhoneList);
							objOs.writeObject(customerEmailList);
							logger.info("Customer info fetched successfully");
						}
						else if (action.equalsIgnoreCase("CSR Create Response")) {
							responseObj = (Response) objIs.readObject();
							responseObj.setResponseDateTime(LocalDateTime.now());
							responseObj.setProposedDateOfVisit(null);
							responseObj.create();
							objOs.writeObject(true);
							logger.info("Customer Service Rep created a Response");
						}
						else if (action.equalsIgnoreCase("Add a Technician ID to a complaint")) {
							String staffID;
							complaintObj = (Complaint) objIs.readObject();
							staffID = complaintObj.getStaffID();
							complaintObj = complaintObj.readComplaint();
							complaintObj.setStaffID(staffID);
							complaintObj.updateTechnician();
							objOs.writeObject(true);
							logger.info("Complaint updated successfully");
						}
						else if (action.equalsIgnoreCase("View Complaints assigned to a Technician")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintList = complaintObj.readAll();
							for(Complaint c: complaintList){
								if(!c.getStaffID().equals(complaintObj.getStaffID())){
									complaintList.remove(c);
								}
							}
							objOs.writeObject(complaintList);
							logger.info("Complaints assigned to a particular technician are fetched successfully");
						}
						else if (action.equalsIgnoreCase("Technician Create Response")) {
							responseObj = (Response) objIs.readObject();
							responseObj.setResponseDateTime(LocalDateTime.now());
							responseObj.create();
							objOs.writeObject(true);
							logger.info("Technician created a Response");
						}
						else if (action.equalsIgnoreCase("Employee Create LiveChat")) {
							if(LocalTime.now().getHour()>=8 && LocalTime.now().getHour()<=19){
								liveChatObj = (LiveChat) objIs.readObject();
								liveChatObj.create(liveChatObj.getCustomerID(), liveChatObj.getStaffID(), liveChatObj.getMessage());
								objOs.writeObject(true);
								logger.info("Live Chat sent to Database successfully");
							}
							else{
								objOs.writeObject(false);
								logger.info("Live Chat not sent to Database because Live Chat period is temporarily expired");
							}
						}
						else if (action.equalsIgnoreCase("Customer Create LiveChat")) {
							if(LocalTime.now().getHour()>=8 && LocalTime.now().getHour()<=19){
								liveChatObj = (LiveChat) objIs.readObject();
								liveChatObj.create(liveChatObj.getCustomerID(), liveChatObj.getStaffID(), liveChatObj.getMessage());
								objOs.writeObject(true);
								logger.info("Live Chat sent to Database successfully");
							}
							else{
								objOs.writeObject(false);
								logger.info("Live Chat not sent to Database because Live Chat period is temporarily expired");
							}
						}
						else if (action.equalsIgnoreCase("Update Complaint Status")) {
							char status;
							complaintObj = (Complaint) objIs.readObject();
							status = complaintObj.getStatus();
							complaintObj = complaintObj.readComplaint();
							complaintObj.setStatus(status);
							complaintObj.updateStatus();
							objOs.writeObject(true);
							logger.info("Complaint updated successfully");
						}
						else if (action.equalsIgnoreCase("Is Employee a Technician?")){
							employeeObj = (Employee) objIs.readObject();
							employeeObj = employeeObj.readEmployee();
							if(employeeObj.getJob() == 'T'){
								objOs.writeObject(true);
								logger.info("Employee is a Technician");
							}
							else{
								objOs.writeObject(false);
								logger.info("Employee is not a Technician");
							}
						}
						/*else{
							objOs.close();
							objIs.close();
							socket.close();
							return;
						}*/
						objOs.flush();
					}
					catch (NullPointerException ex) {
						logger.fatal("NullPointerException occurred");
						ex.printStackTrace();
					}
					catch (ClassNotFoundException ex) {
						logger.fatal("Class Not Found Exception occurred");
						ex.printStackTrace();
					}
					catch (ClassCastException ex) {
						logger.fatal("ClassCast Exception occurred");
						ex.printStackTrace();
					}
					catch (IOException ex) {
						logger.fatal("IOException occurred");
						ex.printStackTrace();
					}
					catch (Exception ex) {
						logger.fatal("Exception occurred");
						ex.printStackTrace();
					}
				}
			}
			catch (Exception ex) {
				logger.fatal("Exception occurred");
				ex.printStackTrace();
			}
			finally {
				SessionFactoryBuilder.closeSessionFactory();
				DBConnectorFactory.closeDatabaseConnection();
			}
		}
	}

}
