package microStar.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

import javax.swing.ImageIcon;
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
import microStar.model.VideoFrame;

import org.hibernate.type.LocalDateTimeType;

public class Server {
   

	private static final Logger logger = LogManager.getLogger(Server.class);
    
    private ServerSocket serverSocket;
	private Socket connectionSocket;
	
	private  ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>();
	
    
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
				clientList.add(client);
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
		
		Customer customerObj;
		Employee employeeObj;

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
			customerObj = new Customer();
			CustomerEmail customerEmailObj = new CustomerEmail();
			CustomerPhone customerPhoneObj = new CustomerPhone();
			employeeObj = new Employee();
			LiveChat liveChatObj = new LiveChat();
			Payment paymentObj = new Payment();
			Query queryObj = new Query();
			Response responseObj = new Response();
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
                        List<Query> queryList = new ArrayList<>();
                        List<LiveChat> liveChatList = new ArrayList<>();
                        
                        action = objIs.readObject() + "";
                   
						if (action.equalsIgnoreCase("Customer Login")) {
							customerObj = (Customer) objIs.readObject();
							login = customerObj.authenticate();
							customerObj = customerObj.readCustomer();
							objOs.writeObject(login);
							if(login){
								objOs.writeObject(customerObj);
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
							if(login){
								objOs.writeObject(employeeObj);
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
							queryObj = queryObj.read(customerID);
							if(queryObj!=null){
								objOs.writeObject(queryObj);
								logger.info("Query executed");
							}
							else{
								logger.info("Customer associated with ID has no Query Info in the Database");
							}
						}
						else if (action.equalsIgnoreCase("View All Responses to a Complaint")) {
							complaintObj = (Complaint) objIs.readObject();
							responseList = responseObj.readAll();
							List<Response> responseList1 = new ArrayList<>();
							for(Response r : responseList){
								if(complaintObj.getComplaintID() == r.getComplaintID()){
									responseList1.add(r);
								}
							}
							objOs.writeObject(responseList1);
							logger.info("All Responses to a complaint are fetched successfully");
						}
						else if (action.equalsIgnoreCase("View All Complaints of a Customer")) {
							customerObj = (Customer) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Response> responseList1 = new ArrayList<>();
							List<Response> responseList2 = new ArrayList<>();
							List<Employee> employeeList1 = new ArrayList<>();
							LocalDateTime max = LocalDateTime.MIN;
							List<Complaint> complaintList1 = new ArrayList<>();
							responseList = responseObj.readAll();
							for(Complaint c : complaintList){
								if(c.getCustomerID().equals(customerObj.getCustomerID())) {
									complaintList1.add(c);
									for(Response r : responseList){
										if(r.getComplaintID() == c.getComplaintID()){
											responseList2.add(r);
										}
									}
									//find max in responseList
									for(Response r : responseList2){
										if(max.isBefore(r.getResponseDateTime())){
											responseObj = r;
										}
									}
									responseList2.clear();
									responseList1.add(responseObj);
									employeeObj = new Employee();
									employeeObj.setStaffID(responseObj.getStaffID());
									responseObj = new Response(0,0,null,"null",LocalDateTime.MIN,"NULL");
									employeeList.add(employeeObj);
								}
							}
							for(Employee e : employeeList){
								if(e.getStaffID().equalsIgnoreCase("NULL")){
									employeeList1.add(new Employee());
								}
								else {
									employeeList1.add(e.readEmployee());
								}
							}
							objOs.writeObject(complaintList1);
							//customerObj = customerObj.readCustomer();
							//objOs.writeObject(customerObj);
							objOs.writeObject(responseList1);
							objOs.writeObject(employeeList1);
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
							List<Payment> payments = new ArrayList<>();
							for(Payment p : paymentList){
								if(customerObj.getCustomerID().equals(p.getCustomerID())){
									payments.add(p);
								}
							}
							objOs.writeObject(payments);
							logger.info("Past payments of a customer fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Number of Resolved & Unresolved Complaints")) {
							complaintObj = (Complaint) objIs.readObject();
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
							complaintObj = (Complaint) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Complaint> complaintList1 = new ArrayList<>();
                            List<Customer> customerList = new ArrayList<>();
                            List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
                            List<CustomerEmail> customerEmailList1 = new ArrayList<>();
                            customerPhoneList = customerPhoneObj.readAll();
                            customerEmailList = customerEmailObj.readAll();
							for (Complaint c : complaintList){
								if(c.getComplaintType().equalsIgnoreCase("Payment")){
									complaintList1.add(c);
									customerObj.setCustomerID(c.getCustomerID());
									customerObj = customerObj.readCustomer();
									customerList.add(customerObj);
                                    for(CustomerPhone cp : customerPhoneList){
                                        if(cp.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerPhoneList1.add(cp);
                                            break;
                                        }
                                    }
                                    for(CustomerEmail ce : customerEmailList){
                                        if(ce.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerEmailList1.add(ce);
                                            break;
                                        }
                                    }
								}
							}
							objOs.writeObject(complaintList1);
                            objOs.writeObject(customerList);
                            objOs.writeObject(customerPhoneList1);
                            objOs.writeObject(customerEmailList1);
							logger.info("Payment Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Internet Complaints")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Complaint> complaintList1 = new ArrayList<>();
                            List<Customer> customerList = new ArrayList<>();
                            List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
                            List<CustomerEmail> customerEmailList1 = new ArrayList<>();
                            customerPhoneList = customerPhoneObj.readAll();
                            customerEmailList = customerEmailObj.readAll();
							for (Complaint c : complaintList){
								if(c.getComplaintType().equalsIgnoreCase("Internet")){
									complaintList1.add(c);
                                    customerObj.setCustomerID(c.getCustomerID());
                                    customerObj = customerObj.readCustomer();
                                    customerList.add(customerObj);
                                    for(CustomerPhone cp : customerPhoneList){
                                        if(cp.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerPhoneList1.add(cp);
                                            break;
                                        }
                                    }
                                    for(CustomerEmail ce : customerEmailList){
                                        if(ce.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerEmailList1.add(ce);
                                            break;
                                        }
                                    }
								}
							}
							objOs.writeObject(complaintList1);
                            objOs.writeObject(customerList);
                            objOs.writeObject(customerPhoneList1);
                            objOs.writeObject(customerEmailList1);
							logger.info("Internet Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Cable Complaints")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Complaint> complaintList1 = new ArrayList<>();
                            List<Customer> customerList = new ArrayList<>();
                            List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
                            List<CustomerEmail> customerEmailList1 = new ArrayList<>();
                            customerPhoneList = customerPhoneObj.readAll();
                            customerEmailList = customerEmailObj.readAll();
							for (Complaint c : complaintList){
								if(c.getComplaintType().equalsIgnoreCase("Cable")){
									complaintList1.add(c);
                                    customerObj.setCustomerID(c.getCustomerID());
                                    customerObj = customerObj.readCustomer();
                                    customerList.add(customerObj);
                                    for(CustomerPhone cp : customerPhoneList){
                                        if(cp.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerPhoneList1.add(cp);
                                            break;
                                        }
                                    }
                                    for(CustomerEmail ce : customerEmailList){
                                        if(ce.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerEmailList1.add(ce);
                                            break;
                                        }
                                    }
								}
							}
							objOs.writeObject(complaintList1);
                            objOs.writeObject(customerList);
                            objOs.writeObject(customerPhoneList1);
                            objOs.writeObject(customerEmailList1);
							logger.info("Cable Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Other Complaints")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Complaint> complaintList1 = new ArrayList<>();
                            List<Customer> customerList = new ArrayList<>();
                            List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
                            List<CustomerEmail> customerEmailList1 = new ArrayList<>();
                            customerPhoneList = customerPhoneObj.readAll();
                            customerEmailList = customerEmailObj.readAll();
							for (Complaint c : complaintList){
								if(c.getComplaintType().equalsIgnoreCase("Other")){
									complaintList1.add(c);
                                    customerObj.setCustomerID(c.getCustomerID());
                                    customerObj = customerObj.readCustomer();
                                    customerList.add(customerObj);
                                    for(CustomerPhone cp : customerPhoneList){
                                        if(cp.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerPhoneList1.add(cp);
                                            break;
                                        }
                                    }
                                    for(CustomerEmail ce : customerEmailList){
                                        if(ce.getCustomerID().equals(customerObj.getCustomerID())){
                                            customerEmailList1.add(ce);
                                            break;
                                        }
                                    }
								}
							}
							objOs.writeObject(complaintList1);
                            objOs.writeObject(customerList);
                            objOs.writeObject(customerPhoneList1);
                            objOs.writeObject(customerEmailList1);
							logger.info("Other Complaints fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Complaint details and Customer account info")) {
						    complaintObj = (Complaint) objIs.readObject();
						    complaintObj = complaintObj.readComplaint();
						    customerObj.setCustomerID(complaintObj.getCustomerID());
						    customerObj = customerObj.readCustomer();
						    customerPhoneList = customerPhoneObj.readAll();
							customerEmailList = customerEmailObj.readAll();
							List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
                            List<CustomerEmail> customerEmailList1 = new ArrayList<>();
							for(CustomerPhone cp : customerPhoneList){
							    if(cp.getCustomerID().equals(customerObj.getCustomerID())){
                                    customerPhoneList1.add(cp);
                                }
                            }
                            for(CustomerEmail ce : customerEmailList){
                                if(ce.getCustomerID().equals(customerObj.getCustomerID())){
                                    customerEmailList1.add(ce);
                                }
                            }
							objOs.writeObject(customerObj);
							objOs.writeObject(complaintObj);
							objOs.writeObject(customerPhoneList1);
							objOs.writeObject(customerEmailList1);
							logger.info("Complaint details and associated Customer info fetched successfully");
						}
						else if (action.equalsIgnoreCase("View Customer account info")) {
							customerObj = (Customer) objIs.readObject();
							customerObj = customerObj.readCustomer();
							customerPhoneList = customerPhoneObj.readAll();
							customerEmailList = customerEmailObj.readAll();
                            List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
                            List<CustomerEmail> customerEmailList1 = new ArrayList<>();
                            for(CustomerPhone cp : customerPhoneList){
                                if(cp.getCustomerID().equals(customerObj.getCustomerID())){
                                    customerPhoneList1.add(cp);
                                }
                            }
                            for(CustomerEmail ce : customerEmailList){
                                if(ce.getCustomerID().equals(customerObj.getCustomerID())){
                                    customerEmailList1.add(ce);
                                }
                            }
							objOs.writeObject(customerObj);
							objOs.writeObject(customerPhoneList1);
							objOs.writeObject(customerEmailList1);
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
							System.err.println(complaintObj.getComplaintID());
							staffID = complaintObj.getStaffID();
							System.out.println(staffID);
							complaintObj = complaintObj.readComplaint();
							complaintObj.setStaffID(staffID);
							System.err.println(complaintObj.getComplaintID());
							complaintObj.updateTechnician();
							objOs.writeObject(true);
							logger.info("Complaint updated successfully");
						}
						else if (action.equalsIgnoreCase("View Complaints assigned to a Technician")) {
							complaintObj = (Complaint) objIs.readObject();
							complaintList = complaintObj.readAll();
							List<Complaint> complaintList1 = new ArrayList<>();
							List<Customer> customerList = new ArrayList<>();
							List<CustomerPhone> customerPhoneList1 = new ArrayList<>();
							List<CustomerEmail> customerEmailList1 = new ArrayList<>();
							customerPhoneList = customerPhoneObj.readAll();
							customerEmailList = customerEmailObj.readAll();
							for(Complaint c: complaintList){
								if(c.getStaffID() != null){
									if(c.getStaffID().equals(complaintObj.getStaffID())){
										complaintList1.add(c);
										customerObj.setCustomerID(c.getCustomerID());
										customerObj = customerObj.readCustomer();
										customerList.add(customerObj);
										for(CustomerPhone cp : customerPhoneList){
											if(cp.getCustomerID().equals(customerObj.getCustomerID())){
												customerPhoneList1.add(cp);
												break;
											}
										}
										for(CustomerEmail ce : customerEmailList){
											if(ce.getCustomerID().equals(customerObj.getCustomerID())){
												customerEmailList1.add(ce);
												break;
											}
										}
									}
								}
							}
							objOs.writeObject(complaintList1);
							objOs.writeObject(customerList);
							objOs.writeObject(customerPhoneList1);
							objOs.writeObject(customerEmailList1);
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
								liveChatObj.create(liveChatObj.getCustomerID(), liveChatObj.getStaffID(), liveChatObj.getMessage(), liveChatObj.isSentByCustomer());
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
								liveChatObj.create(liveChatObj.getCustomerID(), liveChatObj.getStaffID(), liveChatObj.getMessage(), liveChatObj.isSentByCustomer());
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
							if (employeeObj != null){
								if(employeeObj.getJob() == 'T'){
									objOs.writeObject(true);
									logger.info("Employee is a Technician");
								}
								else{
									objOs.writeObject(false);
									logger.info("Employee is not a Technician");
								}
							}
							else{
								objOs.writeObject(false);
								logger.info("Employee is not a Technician");
							}

						}
						else if (action.equalsIgnoreCase("Transmit video frame")){			
							ImageIcon videoFrame =  (ImageIcon) objIs.readObject();
							String id =  (String) objIs.readObject();
							String state =  (String) objIs.readObject();
						
							for (ClientHandler c: clientList) {
								if ((c.customerObj.getCustomerID().equals(id) || c.employeeObj.getStaffID().equals(id)) && c.isAlive() == true) {
									c.objOs.writeObject(videoFrame);
									c.objOs.writeObject(id);
									c.objOs.writeObject(state);
									c.objOs.reset();
									break;
								}
							}
							logger.info("Video Frame/s Transmitted");
						}
						else if (action.equalsIgnoreCase("Customer ReadAll LiveChat")){
							customerObj = (Customer) objIs.readObject();
							liveChatList = liveChatObj.readAll();
							employeeList = employeeObj.readAll();
							List<LiveChat> liveChatList1 = new ArrayList<>();
							for(LiveChat l : liveChatList){
								if(l.getCustomerID().equals(customerObj.getCustomerID())){
									liveChatList1.add(l);
								}
							}
							objOs.writeObject(liveChatList1);
							objOs.writeObject(employeeList);
							logger.info("All LiveChats for Customer read");
						}
						else if (action.equalsIgnoreCase("Employee ReadAll LiveChat")){
							employeeObj = (Employee) objIs.readObject();
							liveChatList = liveChatObj.readAll();
							List<Customer> customerList = new ArrayList<>();
							customerList = customerObj.readAll();
							List<LiveChat> liveChatList1 = new ArrayList<>();
							for(LiveChat l : liveChatList){
								if(l.getStaffID().equals(employeeObj.getStaffID())){
									liveChatList1.add(l);
								}
							}
							objOs.writeObject(liveChatList1);
							objOs.writeObject(customerList);
							logger.info("All LiveChats for Employee read");
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
					catch (SocketException ex) {
						//logger.fatal("SocketException occurred");
						//ex.printStackTrace();
					}
					catch (IOException ex) {
						//logger.fatal("IOException occurred");
						//ex.printStackTrace();
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
