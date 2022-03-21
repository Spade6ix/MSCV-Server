package microStar.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

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

public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    
    private ServerSocket serverSocket;
	private Socket connectionSocket;
	private ObjectInputStream objIs; 
	private ObjectOutputStream objOs;
	private static Connection dbConn = null;
	private Statement stmt; 
	private ResultSet result = null;
	
    
	public Server() {
		this.createConnection();
		this.waitForRequests();
	}
	
	private void createConnection() {
		try {
			serverSocket = new ServerSocket (9555);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void waitForRequests() { 
		String action = ""; 
		getDatabaseConnection();
		
		Complaint complaintObj = null;
		Customer customerObj = null;
		CustomerEmail customerEmailObj = null;
		CustomerPhone customerPhoneObj = null;
		Employee employeeObj = null;
		LiveChat liveChatObj = null;
		Payment paymentObj = null;
		Query queryObj = null;
		Response responseObj = null;
		
		try {
			while(true) {
				connectionSocket = serverSocket.accept();
				this.configureStreams();
				try {
					action = (String) objIs.readObject();
					
					if (action.equalsIgnoreCase("Add complaint")) {
						
					}else if (action.equalsIgnoreCase("Find complaint by id")) {
						//Remember Get last date of response and person who provided the reponse also
						
					}else if (action.equalsIgnoreCase("Find complaint by staffID")) { 
					
					}else if (action.equalsIgnoreCase("Find complaints by category")) {
						
					}else if (action.equalsIgnoreCase("Count complaint by status")) {
					
					}else if (action.equalsIgnoreCase("Find all complaints")) {
						
					}else if (action.equalsIgnoreCase("Find Customer")) {
						
					}else if (action.equalsIgnoreCase("Customer Login")) {
						//We can get customer email and phone here also
						
					}else if (action.equalsIgnoreCase("Employee Login")) {
						
					}else if (action.equalsIgnoreCase("Find employee")) {
						
					}else if (action.equalsIgnoreCase("Assign complaint")) {
						
					}else if (action.equalsIgnoreCase("Start live chat")) {
						
					}else if (action.equalsIgnoreCase("Find past payments")) {
						
					}else if (action.equalsIgnoreCase("Get account status")) {
						
					}else if (action.equalsIgnoreCase("Add response")) {
						
					}
					
//				-----------  EXAMPLE : Not included    ------------------------------	
//					if (action.equals("Add Student")){
//						stuObj = (Student) objIs.readObject();
//						addStudentToFile(stuObj);
//						objOs.writeObject (true);
//					}else if (action.equals("Find Student")) {
//						String stuld = (String) objIs.readObject();
//						// call method to find the student based on the id number
//					
//						stuObj = findStudentById(stuld);
//						objOs.writeObject(stuObj);
//					}
				}catch (ClassNotFoundException ex) {
					ex.printStackTrace(); 
				} catch (ClassCastException ex) { 
					ex.printStackTrace();
				}
				this.closeConnection();
			}

		} catch (EOFException ex) {
			System.out.println("Client has terminated connections with the server");
			ex.printStackTrace();
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static Connection getDatabaseConnection() {
		return DBConnectorFactory.getDatabaseConnection();
	}
    
	private void configureStreams () {
		try {
			objOs = new ObjectOutputStream(connectionSocket.getOutputStream()); 
			objIs = new ObjectInputStream(connectionSocket.getInputStream());
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnection() {
		try {
			objIs.close();
			objOs.close();
			connectionSocket.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
