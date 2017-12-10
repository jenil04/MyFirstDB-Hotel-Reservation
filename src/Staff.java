import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
		this.staffPanel.add(this.setUpButtonPanel(), BorderLayout.CENTER);
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
		this.cleanRooms.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = "UPDATE Room "
						+ "SET status = true "
						+ "WHERE room_id = (SELECT room_id FROM Reservation WHERE check_out < CURDATE());";
				
				try {
					Statement stmt = connection.createStatement();
					stmt.execute(query);
					System.out.println(query);
					JOptionPane.showMessageDialog(null, "Payments have been issued","Success",JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null,exp.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	private void setUpIssuePaymentButton() {
		this.issuePayment = new JButton("Issue Payments");
		this.issuePayment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = "INSERT INTO " + "payment " + " "
						+ "(Guest_id, confirmation, room_price, service_price) SELECT guest_id, Confirmation, price, service_price " + 
						"FROM Reservation natural join (Room natural join Roomtype) Where check_in <= curDate() + 3 and Confirmation not in"
						+ " (Select Confirmation from Payment);";
				try {
					Statement stmt = connection.createStatement();
					stmt.execute(query);
					System.out.println(query);
					JOptionPane.showMessageDialog(null, "Payments have been issued","Success",JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null,exp.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
