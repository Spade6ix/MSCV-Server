package microStar.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import microStar.factory.DBConnectorFactory;
import microStar.factory.SessionFactoryBuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Driver extends JFrame implements ActionListener{
	
    private static final Logger logger = LogManager.getLogger(Driver.class);
    private ServerControl serverControl;
    private int toggle = 0;
    private JButton start_stop = new JButton("Start/Stop");
    private JLabel text = new JLabel("Server Not Running");
    
    
    
    
    public static void main(String[] args) {
        new Driver();
    }
    
    
    
    
    public Driver() {
    	this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(new Color(0x333333));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		
		//Text
	    gbc.gridx = 1;
	    gbc.gridy = 1;
	    gbc.anchor = GridBagConstraints.CENTER;
	    text.setFont(new Font("Calibri", Font.PLAIN, 40));
	    text.setForeground(new Color(0xbfbfbf));
	    this.add(text, gbc);
		
		
		//Start/Stop
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		start_stop.setFont(new Font("Calibri", Font.PLAIN, 25));
		start_stop.setVerticalTextPosition(SwingConstants.CENTER);
		start_stop.setForeground(new Color(0x333333));
		start_stop.setBackground(new Color(0x6666ff));
		start_stop.setBorder(null);
		start_stop.setPreferredSize(new Dimension(115, 45));
		start_stop.addActionListener(this);
		this.add(start_stop, gbc);
		
		
		//setup window
        this.setTitle("Server Control");
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    
    
    

    
    
    
    
    
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() == start_stop) {
			if(toggle == 0) {
				toggle = 1;
				start_stop.setBackground(new Color(0x43c6e0));
			}
			else {
				toggle = 0;
				start_stop.setBackground(new Color(0x6666ff));
			}
			
			if(toggle == 1) {
				serverControl = new ServerControl();
				serverControl.start();
				text.setText("Server is Running");
			}
			else{
				serverControl.stopServer();
				text.setText("Server Not Running");
			}
		}
		
	}

	
	

	
	class ServerControl extends Thread{
		private Server server;
		
		@Override
		public void run(){
			if (toggle == 1) {
				
				try{ // start server
		            server = new Server();
		            server.startServer();
		        }
		        catch(Exception x){
		            logger.fatal("Exception Occurred");
		            x.printStackTrace();
		        }
		        finally{
		            DBConnectorFactory.closeDatabaseConnection();
		            SessionFactoryBuilder.closeSessionFactory();
		            logger.info("Database Connections Closed");
		        }
			}
		}
		
		
		public void stopServer() {
	    	Socket connectionSocket = null;
			try {
				server.state = 0;
				connectionSocket = new Socket("localhost", 9555);
				connectionSocket.close();
				DBConnectorFactory.closeDatabaseConnection();
	            SessionFactoryBuilder.closeSessionFactory();
	            logger.info("Database Connections Closed");
			} 
			catch (UnknownHostException e1) {
				e1.printStackTrace();
			} 
			catch (IOException e1) {
				e1.printStackTrace();
			}
			
	    }
	}

	
}
