package HotelReservationSystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Staff {
	private int staffId;
	private String fname;
	private String lname;
	
	private Connection connection;
	private JPanel staffPanel;
	private JButton cleanRooms;
	private JButton issuePayment;
	
	public Staff(int staffId, String fname, String lname) {
		this.staffId = staffId;
		this.fname = fname;
		this.lname = lname;
		this.setUpStaffPanel();
	}

	public JPanel getStaffPanel() {
		return staffPanel;
	}
	
	private void setUpStaffPanel() {
		this.connection = Application.app.getConn();
		this.staffPanel = new JPanel();
		this.staffPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.staffPanel.setLayout(new BorderLayout());
		
		JLabel welcomeLabel = new JLabel("Welcome " + this.fname + " " + this.lname);
		welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		
		this.staffPanel.add(welcomeLabel, BorderLayout.NORTH);
		this.staffPanel.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel setUpButtonPanel() {
		this.setUpButtonFunctionality();
		
		JPanel tempPanel = new JPanel();
		this.staffPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		tempPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		tempPanel.add(this.cleanRooms);
		tempPanel.add(this.issuePayment);
		return tempPanel;
	}
	
	private void setUpButtonFunctionality() {
		this.setUpCleanRoomButton();
		this.setUpIssuePaymentButton();
	}
	
	private void setUpCleanRoomButton() {
		this.cleanRooms = new JButton("Clean Rooms");
		String query = "UPDATE * Room " + " SET status = '"
				+ true + " Where room_id = '" + 
				"(Select room_id * from Reservation " + " Where userName = '" + "check_out < CURDATE()); ';";
		
		Statement stmt = this.connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		if (!rs.next()) {
			JOptionPane.showMessageDialog(null, "Incorrect ID", "Error", JOptionPane.ERROR_MESSAGE);
			
		this.cleanRooms.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JPanel cleanRoomPanel = new JPanel();
				cleanRoomPanel.setLayout(null);
				
				JTextField test = new JTextField();
				test.setBounds(188, 50, 100, 20);
				test.setColumns(10);
				
				JLabel test1 = new JLabel("Tester:");
				test1.setBounds(70, 50, 85, 15);
				cleanRoomPanel.add(test);
				cleanRoomPanel.add(test1);
				
				staffPanel.add(cleanRoomPanel, BorderLayout.CENTER);
				staffPanel.revalidate();
			}
		});
	}
	
	private void setUpIssuePaymentButton() {
		this.issuePayment = new JButton("Issue Payments");
		this.issuePayment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JPanel cleanRoomPanel = new JPanel();
				cleanRoomPanel.setLayout(null);
				
				JLabel test1 = new JLabel("Tester:");
				test1.setBounds(70, 50, 85, 15);
				cleanRoomPanel.add(test1);
				
				staffPanel.add(cleanRoomPanel, BorderLayout.CENTER);
				staffPanel.revalidate();
			}
		});
	}
}
