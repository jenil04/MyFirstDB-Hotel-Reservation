import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class User extends JFrame{
	private JPanel userPanel;
	private JButton bookRoom;
	private JButton cancelReservation;
	private JButton checkRoomAvail;
	private JButton requestService;
	private JButton payment;
	private JButton checkResInfo;
	private JButton updateInfo;
	private Connection connection;
	
	public User() {
		this.connection = Application.app.getConn();
		this.userPanel = new JPanel();
		this.userPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.userPanel.setLayout(new BorderLayout());
		this.userPanel.add(this.setUpLeftSidePanel(), BorderLayout.WEST);
	}

	public JPanel getUserPanel() {
		return userPanel;
	}
	
	private JPanel setUpLeftSidePanel() {
		this.setUpButtonFunctionality();
		
		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setLayout(new GridLayout(7, 1, 0, 3));
		leftSidePanel.add(this.checkRoomAvail);
		leftSidePanel.add(this.bookRoom);
		leftSidePanel.add(this.requestService);
		leftSidePanel.add(this.checkResInfo);
		leftSidePanel.add(this.cancelReservation);
		leftSidePanel.add(this.payment);
		leftSidePanel.add(this.updateInfo);
		return leftSidePanel;
	}
	
	private void setUpButtonFunctionality() {
		this.setUpBookRoomButton();
		this.setUpCancelReservationButton();
		this.setUpCheckResInfoButton();
		this.setUpPaymentButton();
		this.setUpCheckRoomAvailButton();
		this.setUpUpdateInfoButton();
		this.setUpRequestServiceButton();
	}
	
	private void setUpBookRoomButton() {
		this.bookRoom = new JButton("Book a Room");
		this.bookRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JPanel bookRoomPanel = new JPanel();
				JLabel firstNameLabel = new JLabel("First Name");
				bookRoomPanel.add(firstNameLabel);
				userPanel.add(bookRoomPanel, BorderLayout.CENTER);
				userPanel.revalidate();
			}
		});
	}
	
	private void setUpCheckRoomAvailButton() {
		this.checkRoomAvail = new JButton("Check Availability");
		this.checkRoomAvail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JPanel checkRoomAvailPanel = new JPanel();
				checkRoomAvailPanel.setLayout(null);
				
				JTextField userName = new JTextField();
				userName.setBounds(188, 50, 100, 20);
				userName.setColumns(10);
				
				JLabel userNameLabel = new JLabel("Check In:");
				userNameLabel.setBounds(70, 50, 85, 15);
				checkRoomAvailPanel.add(userName);
				checkRoomAvailPanel.add(userNameLabel);
				
				
				userPanel.add(checkRoomAvailPanel, BorderLayout.CENTER);
				userPanel.revalidate();
			}
		});
	}
	
	private void setUpRequestServiceButton() {
		this.requestService = new JButton("Request Service");
		this.requestService.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpCheckResInfoButton() {
		this.checkResInfo = new JButton("Check Reservations");
		this.checkResInfo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpCancelReservationButton() {
		this.cancelReservation = new JButton("Cancel Reservation");
		this.cancelReservation.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpPaymentButton() {
		this.payment = new JButton("Make Payment");
		this.payment.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpUpdateInfoButton() {
		this.updateInfo = new JButton("Update Information");
		this.updateInfo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}




















