import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Manager {
	private Connection connection;
	private JPanel managerPanel;
	private JButton addStaff;
	private JButton removeStaff;
	private JButton checkPopularRooms;
	private JButton applyCoupons;
	private JButton checkReport;
	private JButton addRoom;
	
	public Manager() {
		this.connection = Application.app.getConn();
		this.managerPanel = new JPanel();
		this.managerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.managerPanel.setLayout(new BorderLayout());
		this.managerPanel.add(this.setUpLeftSidePanel(), BorderLayout.WEST);
	}

	public JPanel getManagerPanel() {
		return managerPanel;
	}
	
	private JPanel setUpLeftSidePanel() {
		this.setUpButtonFunctionality();
		
		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setLayout(new GridLayout(6, 1, 0, 3));
		leftSidePanel.add(this.addStaff);
		leftSidePanel.add(this.removeStaff);
		leftSidePanel.add(this.checkPopularRooms);
		leftSidePanel.add(this.applyCoupons);
		leftSidePanel.add(this.checkReport);
		leftSidePanel.add(this.addRoom);
		return leftSidePanel;
	}

	private void setUpButtonFunctionality() {
		// TODO Auto-generated method stub
		this.setUpAddStaffButton();
		this.setUpAddRooms();
		this.setUpApplyCouponsButton();
		this.setUpCheckPopRoomButton();
		this.setUpRemoveStaffButton();
		this.setUpCheckReportButton();
	}
	
	private void setUpAddStaffButton() {
		this.addStaff = new JButton("Add Staff");
		this.addStaff.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JPanel bookRoomPanel = new JPanel();
				JLabel firstNameLabel = new JLabel("First Name");
				bookRoomPanel.add(firstNameLabel);
				managerPanel.add(bookRoomPanel, BorderLayout.CENTER);
				managerPanel.revalidate();

			}
		});
	}
	
	private void setUpRemoveStaffButton() {
		this.removeStaff = new JButton("Remove Staff");
		this.removeStaff.addActionListener(new ActionListener() {
			
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
				
				
				managerPanel.add(checkRoomAvailPanel, BorderLayout.CENTER);
				managerPanel.revalidate();
			}
		});
	}
	
	private void setUpCheckPopRoomButton() {
		this.checkPopularRooms = new JButton("Popular Rooms");
		this.checkPopularRooms.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpApplyCouponsButton() {
		this.applyCoupons = new JButton("Issue Coupons");
		this.applyCoupons.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpCheckReportButton() {
		this.checkReport = new JButton("Check Report");
		this.checkReport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setUpAddRooms() {
		this.addRoom = new JButton("Add Rooms");
		this.addRoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
