import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class User extends JFrame{
	private int guestId;
	private String fname;
	private String lname;
	private int numOfGuests;
	
	private JPanel userPanel;
	private JButton bookRoom;
	private JButton cancelReservation;
	private JButton checkRoomAvail;
	private JButton requestService;
	private JButton payment;
	private JButton checkResInfo;
	private JButton updateInfo;
	private Connection connection;
	
	// Remember information about a user.
	public User(int guestId, String fname, String lname, int numOfGuests) {
		this.guestId = guestId;
		this.fname = fname;
		this.lname = lname;
		this.numOfGuests = numOfGuests;
		this.setUpUserPanel();
	}
	
	public JPanel getUserPanel() {
		return userPanel;
	}
	
	private void setUpUserPanel() {
		this.connection = Application.app.getConn();
		this.userPanel = new JPanel();
		this.userPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.userPanel.setLayout(new BorderLayout());
		this.userPanel.add(this.setUpLeftSidePanel(), BorderLayout.WEST);
	}

	private JPanel setUpLeftSidePanel() {
		this.setUpButtonFunctionality();
		
		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setLayout(new GridLayout(8, 1, 0, 3));
		
		JLabel welcomeLabel = new JLabel("Welcome " + this.fname + " " + this.lname);
		welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		
		leftSidePanel.add(welcomeLabel);
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
			
			JPanel checkRoomAvailPanel = null;
			JTextField checkOut = null;
			JTextField checkIn = null;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				checkRoomAvailPanel = new JPanel();
				checkRoomAvailPanel.setLayout(new BorderLayout());
				
				JPanel tempPanel = new JPanel();
				checkIn = new JTextField();
				checkIn.setColumns(10);
				JLabel checkInLabel = new JLabel("Check In: (yyyy-mm-dd)");
				
				checkOut = new JTextField();
				checkOut.setColumns(10);
				JLabel checkOutLabel = new JLabel("Check Out: (yyyy-mm-dd)");
				
				JButton checkButton = new JButton("Check Availability");
				checkButton.addActionListener(executeCheckAvailability());
				tempPanel.add(checkInLabel);
				tempPanel.add(checkIn);
				tempPanel.add(checkOutLabel);
				tempPanel.add(checkOut); 
				tempPanel.add(checkButton);
				checkRoomAvailPanel.add(tempPanel, BorderLayout.NORTH);
				
				userPanel.add(checkRoomAvailPanel, BorderLayout.CENTER);
				userPanel.revalidate();
			}

			private ActionListener executeCheckAvailability() {
				// TODO Auto-generated method stub
				return new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Fix the query. May be add an outer join instead with a where clause. Capacity needs to fetched from user profile. 
						JPanel tempPanel = new JPanel();
						if (checkIn.equals("") || checkOut.equals("")) {
							JOptionPane.showMessageDialog(null, "Please fill in all the Fields", "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								String query = "SELECT * "
										+ "FROM Room NATURAL JOIN RoomType "
										+ "WHERE room_id NOT IN "
											+ "(SELECT room_id "
											+ "FROM Reservation "
											+ "WHERE (check_in BETWEEN '" + checkIn.getText() + "' AND '"
													+ checkOut.getText() + "') OR "
													+ "(check_out BETWEEN '" + checkIn.getText() + "' AND '"
													+ checkOut.getText() + "'))"
											+ " AND status = true and capacity >= " + numOfGuests + ";";

								Statement stmt = connection.createStatement();
								ResultSet rs = stmt.executeQuery(query);
								String displayInformation = String.format("|%-20s|%-20s|%-20s|%-20s|\n","Room ID", "Room Type", "Price", "Capacity");
								displayInformation += new String(new char[displayInformation.length()]).replace("\0", "_") + "\n";
								while (rs.next()) {
									displayInformation += String.format("|%-20d|%-20s|%-20d|%-20d|\n",
																			rs.getInt("room_id"), 
																			rs.getString("type_of_room"),
																			rs.getInt("price"),
																			rs.getInt("capacity"));
								}
								System.out.println(displayInformation);
								JTextArea content = new JTextArea();
								content.setText(displayInformation);
								JScrollPane informationPane = new JScrollPane(content, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
								tempPanel.add(informationPane);
								stmt.close();
							} catch (Exception exp){
								exp.printStackTrace();
								JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
						checkRoomAvailPanel.add(tempPanel, BorderLayout.CENTER);
						checkRoomAvailPanel.revalidate();
					}
				};
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




















