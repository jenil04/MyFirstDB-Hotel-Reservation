import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Staff {
	private Connection connection;
	private JPanel staffPanel;
	private JButton cleanRooms;
	private JButton issuePayment;
	
	public Staff() {
		this.connection = Application.app.getConn();
		
		this.staffPanel = new JPanel();
		this.staffPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.staffPanel.setLayout(new BorderLayout());
		this.staffPanel.add(this.setUpButtonPanel(), BorderLayout.SOUTH);
	}

	public JPanel getStaffPanel() {
		return staffPanel;
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
