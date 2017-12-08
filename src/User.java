import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class User extends JFrame{
	private int guestId;
	private String fname;
	private String lname;
	private int numOfGuests;
	
	private JPanel userPanel;
	private JLabel welcomeLabel;
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
		this.welcomeLabel = new JLabel();
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
		this.setUpWelcomeLabel(this.fname, this.lname);
		
		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setLayout(new GridLayout(8, 1, 0, 3));
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
	
	private void setUpWelcomeLabel(String fn, String ln) {
		this.welcomeLabel.setText(("Welcome " + fn + " " + ln));
		this.welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 16));
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
				
				BorderLayout layout = (BorderLayout) userPanel.getLayout();
				if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
					userPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				}
								
				userPanel.add(checkRoomAvailPanel, BorderLayout.CENTER);
				userPanel.revalidate();
			}

			private ActionListener executeCheckAvailability() {
				// TODO Auto-generated method stub
				return new ActionListener() {
					JTable table = null;
					JScrollPane scrollPane = null;

					DefaultTableModel model = null;
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Fix the query. May be add an outer join instead with a where clause. Capacity needs to fetched from user profile. 
						JPanel tempPanel = new JPanel();
						if (checkIn.equals("") || checkOut.equals("")) {
							JOptionPane.showMessageDialog(null, "Please fill in all the Fields", "Error", JOptionPane.ERROR_MESSAGE);
						} else {
							
							table = new JTable();
							table.setDefaultEditor(Object.class, null);
							scrollPane = new JScrollPane();
							scrollPane.setViewportView(table);

							model = (DefaultTableModel) table.getModel();
							model.addColumn("Room ID");
							model.addColumn("Room Type");
							model.addColumn("Price");
							model.addColumn("Capacity");
							
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
								int i = 0;
								while (rs.next()) {
									model.addRow(new Object[0]);
									model.setValueAt(rs.getInt("room_id"), i, 0);
									model.setValueAt(rs.getString("type_of_room"), i, 1);
									model.setValueAt(rs.getInt("price"), i, 2);
									model.setValueAt(rs.getInt("capacity"), i, 3);
									i++;
								}
								tempPanel.add(scrollPane);
								checkRoomAvailPanel.add(tempPanel, BorderLayout.CENTER);
								checkRoomAvailPanel.revalidate();
								stmt.close();
							} catch (Exception exp){
								exp.printStackTrace();
								JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
							}
						}
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
			JPanel checkResInfoPanel = null ;
			JTable table = null;
			JScrollPane scrollPane = null;

			DefaultTableModel model = null;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearCenterPanel();
				checkResInfoPanel = new JPanel();
				table = new JTable();
				table.setDefaultEditor(Object.class, null);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(table);
				
				model = (DefaultTableModel) table.getModel();
				model.addColumn("Confirmation #");
				model.addColumn("Room Number");
				model.addColumn("Service Price Due");
				model.addColumn("Check In");
				model.addColumn("Check Out");
				
				try {
					String query = "SELECT * "
							+ "FROM  Reservation "
							+ "Where Reservation.guest_id  = " + guestId + ";";
					
					Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(query);
	
					int i = 0;
					while (rs.next()) {
						model.addRow(new Object[0]);
						model.setValueAt(rs.getInt("confirmation"), i, 0);
						model.setValueAt(rs.getString("room_id"), i, 1);
						model.setValueAt(rs.getString("service_price"), i, 2);
						model.setValueAt(rs.getString("check_in"), i, 3);
						model.setValueAt(rs.getString("check_out"), i, 4);
						i++;
					}
					checkResInfoPanel.add(scrollPane);
					userPanel.add(checkResInfoPanel, BorderLayout.CENTER);
					userPanel.revalidate();
					stmt.close();
					
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
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

			JPanel updateInfoPanel;
			
			JTextField firstName;
			JTextField lastName;
			JTextField userName;
			JTextField password;
			JTextField email;
			JTextField numOfGuests;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearCenterPanel();
				
				firstName = new JTextField();
				firstName.setColumns(10);
				lastName = new JTextField();
				lastName.setColumns(10);
				userName = new JTextField();
				userName.setColumns(10);
				password = new JTextField();
				password.setColumns(10);
				email = new JTextField();
				email.setColumns(10);
				numOfGuests = new JTextField();
				numOfGuests.setColumns(10);
				
				updateInfoPanel = setUpUpdatePanel();
				userPanel.add(updateInfoPanel, BorderLayout.CENTER);
				userPanel.revalidate();
			}
			
			private JPanel setUpUpdatePanel() {
				JPanel resultPanel = new JPanel(new GridLayout(7, 1));
				
				JPanel panel1 = new JPanel();
				panel1.add(new JLabel("First Name"));
				panel1.add(firstName);
				
				JPanel panel2 = new JPanel();
				panel2.add(new JLabel("Last Name"));
				panel2.add(lastName);
				
				JPanel panel3 = new JPanel();
				panel3.add(new JLabel("Username"));
				panel3.add(userName);
				
				JPanel panel4 = new JPanel();
				panel4.add(new JLabel("Password"));
				panel4.add(password);
				
				JPanel panel5 = new JPanel();
				panel5.add(new JLabel("Email"));
				panel5.add(email);
				
				JPanel panel6 = new JPanel();
				panel6.add(new JLabel("# of Guests"));
				panel6.add(numOfGuests);
				
				JPanel panel7 = new JPanel();
				JButton updateButton = new JButton("Update Profile");
				updateButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						if (isEmpty()) {
							JOptionPane.showMessageDialog(null, "Please fill in all the Fields" ,"Error",JOptionPane.ERROR_MESSAGE);
						} else {
							String query = " UPDATE Guests SET";
							query += (firstName.getText().equals("")) ? "" : " first_name = '" + firstName.getText() + "', ";
							query += (lastName.getText().equals("")) ? "" : " last_name = '" + lastName.getText() + "', ";
							query += (userName.getText().equals("")) ? "" : " username = '" + userName.getText() + "', ";
							query += (password.getText().equals("")) ? "" : " password = '" + password.getText() + "', ";
							query += (email.getText().equals("")) ? "" : " cus_email = '" + email.getText() + "', ";
							query += (numOfGuests.getText().equals("")) ? "" : " num_of_guests = " + Integer.parseInt(numOfGuests.getText()) + ", ";
							
							query = query.substring(0, query.length()-2) + " WHERE guest_id = " + guestId + ";";
							try {
								Statement stmt = connection.createStatement();
								stmt.executeUpdate(query);
								
								setUpWelcomeLabel((firstName.getText().equals("")) ? fname : firstName.getText(),
													(lastName.getText().equals("")) ? lname : lastName.getText());
								
								JOptionPane.showMessageDialog(null, "Profile has been Updtaed","Success",JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception exp) {
								JOptionPane.showMessageDialog(null,exp.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				panel7.add(updateButton);
				
				resultPanel.add(panel1);
				resultPanel.add(panel2);
				resultPanel.add(panel3);
				resultPanel.add(panel4);
				resultPanel.add(panel5);
				resultPanel.add(panel6);
				resultPanel.add(panel7);
				return resultPanel;
			}
			
			private boolean isEmpty() {
				return (firstName.getText().equals("")) &&
						(lastName.getText().equals("")) &&
						(userName.getText().equals("")) && 
						(password.getText().equals("")) &&
						(email.getText().equals("")) &&
						(numOfGuests.getText().equals(""));
			}
		});
	}
	
	// Need to clear the center of the Borderlayout, because it comes in the
	// way with other JPanel. 
	private void clearCenterPanel() {
		BorderLayout layout = (BorderLayout) userPanel.getLayout();
		if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
			userPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		}
	}
}




















