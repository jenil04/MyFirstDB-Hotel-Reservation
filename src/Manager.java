import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.CallableStatement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

public class Manager {
	private int managerId;
	private String fname;
	private String lname;
	
	private Connection connection;
	private JPanel managerPanel;
	private JButton addStaff;
	private JButton removeStaff;
	private JButton checkPopularRooms;
	private JButton applyCoupons;
	private JButton checkReport;
	private JButton addRoom;
	private JButton checkEmployees;
	private JButton archive;
	
	public Manager(int managerId, String fname, String lname) {
		this.managerId = managerId;
		this.fname = fname;
		this.lname = lname;
		this.setUpManagerPanel();
	}

	public JPanel getManagerPanel() {
		return managerPanel;
	}
	
	private void setUpManagerPanel() {
		this.connection = Application.app.getConn();
		this.managerPanel = new JPanel();
		this.managerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.managerPanel.setLayout(new BorderLayout());
		this.managerPanel.add(this.setUpLeftSidePanel(), BorderLayout.WEST);
	}

	private JPanel setUpLeftSidePanel() {
		this.setUpButtonFunctionality();

		JPanel leftSidePanel = new JPanel();
		leftSidePanel.setLayout(new GridLayout(9, 1, 0, 3));
		
		JLabel welcomeLabel = new JLabel("Welcome " + this.fname + " " + this.lname);
		welcomeLabel.setFont(new Font("Verdana", Font.BOLD, 16));
		
		leftSidePanel.add(welcomeLabel);
		leftSidePanel.add(this.addStaff);
		leftSidePanel.add(this.removeStaff);
		leftSidePanel.add(this.checkPopularRooms);
		leftSidePanel.add(this.applyCoupons);
		leftSidePanel.add(this.checkReport);
		leftSidePanel.add(this.addRoom);
		leftSidePanel.add(this.checkEmployees);
		leftSidePanel.add(this.archive);
		
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
		this.setUpCheckEmployees();
		this.setUpArchive();
	}

	private void setUpAddStaffButton() {
		this.addStaff = new JButton("Add Staff");
		this.addStaff.addActionListener(new ActionListener() {

			JTextField userName = null;
			JTextField password = null;
			JTextField firstName = null;
			JTextField lastName = null;

			JTable table = null;
			JScrollPane scrollPane = null;

			DefaultTableModel model = null;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearCenterPanel();
				JPanel bookRoomPanel = new JPanel();
				JLabel userNameLabel = new JLabel("Username:");
				JLabel passwordLabel = new JLabel("Password:");
				JLabel firstNameLabel = new JLabel("First Name:");
				JLabel lastNameLabel = new JLabel("Last Name:");

				userName = new JTextField();
				userName.setColumns(10);

				password = new JTextField();
				password.setColumns(10);

				firstName = new JTextField();
				firstName.setColumns(10);

				lastName = new JTextField();
				lastName.setColumns(10);

				table = new JTable();
				table.setDefaultEditor(Object.class, null);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(table);

				model = (DefaultTableModel) table.getModel();
				model.addColumn("ID");
				model.addColumn("Username");
				model.addColumn("Password");
				model.addColumn("First Name");
				model.addColumn("Last Name");

				try {
					String query2 = "SELECT * FROM Staff";
					Statement stmt2 = connection.createStatement();
					ResultSet rs = stmt2.executeQuery(query2);

					int i = 0;
					while (rs.next()) {
						model.addRow(new Object[0]);
						model.setValueAt(rs.getInt("staff_Id"), i, 0);
						model.setValueAt(rs.getString("first_name"), i, 1);
						model.setValueAt(rs.getString("last_name"), i, 2);
						model.setValueAt(rs.getString("username"), i, 3);
						model.setValueAt(rs.getString("password"), i, 4);
						i++;
					}

					stmt2.close();
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				JButton addButton = new JButton("Add Staff");
				addButton.addActionListener(executeAddStaff());

				bookRoomPanel.add(userNameLabel);
				bookRoomPanel.add(userName);
				bookRoomPanel.add(passwordLabel);
				bookRoomPanel.add(password);
				bookRoomPanel.add(firstNameLabel);
				bookRoomPanel.add(firstName);
				bookRoomPanel.add(lastNameLabel);
				bookRoomPanel.add(lastName);
				bookRoomPanel.add(addButton);

				bookRoomPanel.add(scrollPane);

				managerPanel.add(bookRoomPanel, BorderLayout.CENTER);
				managerPanel.revalidate();

			}

			private ActionListener executeAddStaff() {
				// TODO Auto-generated method stub
				return new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (userName.getText().equals("") || password.getText().equals("")
								|| firstName.getText().equals("") || lastName.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Please fill in all the Fields", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								String query = "INSERT INTO Staff(first_name, last_name, username, password) VALUES('"
										+ firstName.getText() + "', '" + lastName.getText() + "', '"
										+ userName.getText() + "', '" + password.getText() + "');";

								Statement stmt = connection.createStatement();
								stmt.executeUpdate(query);
								stmt.close();

								String query2 = "SELECT * FROM Staff WHERE staff_Id=(SELECT MAX(staff_Id) FROM Staff)";
								Statement stmt2 = connection.createStatement();
								ResultSet rs = stmt2.executeQuery(query2);

								int i = model.getRowCount();
								while (rs.next()) {
									model.addRow(new Object[0]);
									model.setValueAt(rs.getInt("staff_Id"), i, 0);
									model.setValueAt(rs.getString("first_name"), i, 1);
									model.setValueAt(rs.getString("last_name"), i, 2);
									model.setValueAt(rs.getString("username"), i, 3);
									model.setValueAt(rs.getString("password"), i, 4);
									i++;
								}

								stmt2.close();

								/*
								 * for(int i=0; i<50; i++){ model.addRow(new
								 * Object[0]); model.setValueAt(i+1, i, 0);
								 * model.setValueAt("test1", i, 1);
								 * model.setValueAt("test2", i, 2);
								 * model.setValueAt("test3", i, 3);
								 * model.setValueAt("test4", i, 4); }
								 */

								JOptionPane.showMessageDialog(null, "Successfully added staff.", "Success",
										JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception exp) {
								JOptionPane.showMessageDialog(null, exp.getMessage(), "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				};
			}
		});
	}

	private void setUpRemoveStaffButton() {
		this.removeStaff = new JButton("Delete Staff");
		this.removeStaff.addActionListener(new ActionListener() {

			JTextField id = null;

			JTable table = null;
			JScrollPane scrollPane = null;

			DefaultTableModel model = null;

			@Override
			public void actionPerformed(ActionEvent e) {
				clearCenterPanel();
				// TODO Auto-generated method stub

				JPanel removeStaffPanel = new JPanel();
				JLabel idLabel = new JLabel("id:");
				id = new JTextField();
				id.setColumns(10);

				table = new JTable();
				table.setDefaultEditor(Object.class, null);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(table);

				model = (DefaultTableModel) table.getModel();
				model.addColumn("ID");
				model.addColumn("Username");
				model.addColumn("Password");
				model.addColumn("First Name");
				model.addColumn("Last Name");

				try {
					String query2 = "SELECT * FROM Staff";
					Statement stmt2 = connection.createStatement();
					ResultSet rs = stmt2.executeQuery(query2);

					int i = 0;
					while (rs.next()) {
						model.addRow(new Object[0]);
						model.setValueAt(rs.getInt("staff_Id"), i, 0);
						model.setValueAt(rs.getString("first_name"), i, 1);
						model.setValueAt(rs.getString("last_name"), i, 2);
						model.setValueAt(rs.getString("username"), i, 3);
						model.setValueAt(rs.getString("password"), i, 4);
						i++;
					}

					stmt2.close();
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				JButton deleteButton = new JButton("Delete Staff");
				deleteButton.addActionListener(executeDeleteStaff());

				removeStaffPanel.add(idLabel);
				removeStaffPanel.add(id);
				removeStaffPanel.add(deleteButton);

				removeStaffPanel.add(scrollPane);

				managerPanel.add(removeStaffPanel, BorderLayout.CENTER);
				managerPanel.revalidate();

			}

			private ActionListener executeDeleteStaff() {
				// TODO Auto-generated method stub
				return new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (id.getText().equals("")){
							JOptionPane.showMessageDialog(null, "Please fill in all the Fields", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								String query = "DELETE FROM Staff WHERE staff_id = '"
										+ id.getText() + "';";

								Statement stmt = connection.createStatement();
								int deletedRow = stmt.executeUpdate(query);
								//need to refresh table after deleting
								if(deletedRow == 1)
								{
									JOptionPane.showMessageDialog(null, "Successfully deleted staff.", "Success",
											JOptionPane.INFORMATION_MESSAGE);
								}
								else if(deletedRow == 0)
								{
									JOptionPane.showMessageDialog(null, "Failed to delete staff.", "Error",
											JOptionPane.INFORMATION_MESSAGE);
								}
								stmt.close();


							} catch (Exception exp) {
								JOptionPane.showMessageDialog(null, exp.getMessage(), "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				};
			}
		});
	}
	//need to test, no data in archive yet
	private void setUpCheckPopRoomButton() {
		this.checkPopularRooms = new JButton("Popular Rooms");
		this.checkPopularRooms.addActionListener(new ActionListener() {
			JTable table = null;
			JScrollPane scrollPane = null;
	
			DefaultTableModel model = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				clearCenterPanel();
				
				JPanel checkPopularRoomsPanel = new JPanel();
				table = new JTable();
				table.setDefaultEditor(Object.class, null);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(table);
		
				model = (DefaultTableModel) table.getModel();
				model.addColumn("Type of Room");
				model.addColumn("# of times booked");
		
				try {
					String query2 = "SELECT type_of_room, count(type_of_room) AS most_booked "
							+ "FROM Archive JOIN Room ON Archive.room_id = Room.room_id "
							+ "GROUP BY type_of_room "
							+ "ORDER BY count(type_of_room) DESC";
					Statement stmt2 = connection.createStatement();
					ResultSet rs = stmt2.executeQuery(query2);
		
					int i = 0;
					while (rs.next()) {
						model.addRow(new Object[0]);
						model.setValueAt(rs.getString("type_of_room"), i, 0);
						model.setValueAt(rs.getInt("most_booked"), i, 1);
						i++;
					}
		
					stmt2.close();
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				checkPopularRoomsPanel.add(scrollPane);
				managerPanel.add(checkPopularRoomsPanel, BorderLayout.CENTER);
				managerPanel.revalidate();
			}
		});
	}

	private void setUpApplyCouponsButton() {
		this.applyCoupons = new JButton("Issue Coupons");
		this.applyCoupons.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearCenterPanel();
				
			
				try {
					String query2 = "UPDATE Guests SET coupon = true"
							+ " WHERE guest_Id = "
							+ "(SELECT guest_Id FROM Reservation "
							+ "GROUP BY guest_Id "
							+ "HAVING count(guest_Id) > 3);";
					Statement stmt2 = connection.createStatement();
					int rs = stmt2.executeUpdate(query2);
		
					stmt2.close();
					JOptionPane.showMessageDialog(null, "Successfully gave coupon.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				managerPanel.revalidate();
			}
		});
	}

	private void setUpCheckReportButton() {
		this.checkReport = new JButton("Check Report");
		this.checkReport.addActionListener(new ActionListener() {
			JPanel checkReportPanel = null;
			JTextField checkOut = null;
			JTextField checkIn = null;
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clearCenterPanel();
				
				checkReportPanel = new JPanel();
				checkReportPanel.setLayout(new BorderLayout());
				
				JPanel tempPanel = new JPanel();
				checkIn = new JTextField();
				checkIn.setColumns(10);
				JLabel checkInLabel = new JLabel("Check In: (yyyy-mm-dd)");
				
				checkOut = new JTextField();
				checkOut.setColumns(10);
				JLabel checkOutLabel = new JLabel("Check Out: (yyyy-mm-dd)");
				
				JButton checkButton = new JButton("Check Report");
				checkButton.addActionListener(executeCheckAvailability());
				tempPanel.add(checkInLabel);
				tempPanel.add(checkIn);
				tempPanel.add(checkOutLabel);
				tempPanel.add(checkOut); 
				tempPanel.add(checkButton);
				checkReportPanel.add(tempPanel, BorderLayout.NORTH);
				
				BorderLayout layout = (BorderLayout) managerPanel.getLayout();
				if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
					managerPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
				}
								
				managerPanel.add(checkReportPanel, BorderLayout.CENTER);
				managerPanel.revalidate();
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
							model.addColumn("Confirmation ID");
							model.addColumn("Guest ID");
							model.addColumn("Room ID");
							model.addColumn("Total Price");
							model.addColumn("Check In");
							model.addColumn("Check Out");
							model.addColumn("Archived Date");
							
							try {
								String query = "SELECT * FROM Archive  WHERE check_in >= '" + checkIn.getText() + "' "
										+ "AND check_out <= '" + checkOut.getText() + "'";

								Statement stmt = connection.createStatement();
								ResultSet rs = stmt.executeQuery(query);
								int i = 0;
								while (rs.next()) {
									model.addRow(new Object[0]);
									model.setValueAt(rs.getInt("confirmation"), i, 0);
									model.setValueAt(rs.getInt("guest_Id"), i, 1);
									model.setValueAt(rs.getInt("room_id"), i, 2);
									model.setValueAt(rs.getInt("total_price"), i, 3);
									model.setValueAt(rs.getDate("check_in"), i, 4);
									model.setValueAt(rs.getDate("check_out"), i, 5);
									model.setValueAt(rs.getDate("updatedAt"), i, 6);
									i++;
								}
								tempPanel.add(scrollPane);
								checkReportPanel.add(tempPanel, BorderLayout.CENTER);
								checkReportPanel.revalidate();
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

	private void setUpAddRooms() {
		this.addRoom = new JButton("Add Room");
		this.addRoom.addActionListener(new ActionListener() {

			JTextField room = null;

			JTable table = null;
			JScrollPane scrollPane = null;

			DefaultTableModel model = null;

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearCenterPanel();
				JPanel addRoomPanel = new JPanel();
				JLabel roomLabel = new JLabel("Type of Room:");

				room = new JTextField();
				room.setColumns(10);

				table = new JTable();
				table.setDefaultEditor(Object.class, null);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(table);

				model = (DefaultTableModel) table.getModel();
				model.addColumn("ID");
				model.addColumn("Status");
				model.addColumn("Room Type");

				try {
					String query2 = "SELECT * FROM Room";
					Statement stmt2 = connection.createStatement();
					ResultSet rs = stmt2.executeQuery(query2);

					int i = 0;
					while (rs.next()) {
						model.addRow(new Object[0]);
						model.setValueAt(rs.getInt("room_Id"), i, 0);
						model.setValueAt(rs.getBoolean("status"), i, 1);
						model.setValueAt(rs.getString("type_of_room"), i, 2);
						i++;
					}

					stmt2.close();
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}

				JButton addRoom = new JButton("Add Room");
				addRoom.addActionListener(executeAddRoom());

				addRoomPanel.add(roomLabel);
				addRoomPanel.add(room);
				addRoomPanel.add(addRoom);

				addRoomPanel.add(scrollPane);

				managerPanel.add(addRoomPanel, BorderLayout.CENTER);
				managerPanel.revalidate();

			}

			private ActionListener executeAddRoom() {
				// TODO Auto-generated method stub
				return new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (room.getText().equals(""))	{
							JOptionPane.showMessageDialog(null, "Please fill in all the Fields", "Error",
									JOptionPane.ERROR_MESSAGE);
						} else {
							try {
								String query = "INSERT INTO Room(type_of_room) VALUES('"
										+ room.getText() + "');";

								Statement stmt = connection.createStatement();
								stmt.executeUpdate(query);
								stmt.close();

								String query2 = "SELECT * FROM Room";
								Statement stmt2 = connection.createStatement();
								ResultSet rs = stmt2.executeQuery(query2);

								int i = model.getRowCount();
								while (rs.next()) {
									model.addRow(new Object[0]);
									model.setValueAt(rs.getInt("room_id"), i, 0);
									model.setValueAt(rs.getBoolean("status"), i, 1);
									model.setValueAt(rs.getString("type_of_room"), i, 2);
									i++;
								}

								stmt2.close();
								JOptionPane.showMessageDialog(null, "Successfully added room.", "Success",
										JOptionPane.INFORMATION_MESSAGE);
							} catch (Exception exp) {
								JOptionPane.showMessageDialog(null, "Cannot add Room without first adding to RoomType", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				};
			}
		});
	}
	private void setUpCheckEmployees() {
		this.checkEmployees = new JButton("Check Employees");
		this.checkEmployees.addActionListener(new ActionListener() {
			JTable table = null;
			JScrollPane scrollPane = null;
	
			DefaultTableModel model = null;
			@Override
			public void actionPerformed(ActionEvent e) {
				clearCenterPanel();
				
				JPanel checkEmployeesPanel = new JPanel();
				table = new JTable();
				table.setDefaultEditor(Object.class, null);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(table);
		
				model = (DefaultTableModel) table.getModel();
				model.addColumn("First Name");
				model.addColumn("Last Name");
		
				try {
					String query2 = "SELECT first_name, last_name FROM Manager UNION ALL SELECT first_name, last_name FROM Staff";
					Statement stmt2 = connection.createStatement();
					ResultSet rs = stmt2.executeQuery(query2);
		
					int i = 0;
					while (rs.next()) {
						model.addRow(new Object[0]);
						model.setValueAt(rs.getString("first_name"), i, 0);
						model.setValueAt(rs.getString("last_name"), i, 1);
						i++;
					}
		
					stmt2.close();
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				checkEmployeesPanel.add(scrollPane);
				managerPanel.add(checkEmployeesPanel, BorderLayout.CENTER);
				managerPanel.revalidate();
			}
		});
	}
	private void setUpArchive()
	{
		this.archive = new JButton("Archive");
		this.archive.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				clearCenterPanel();
				try {
					CallableStatement cs = connection.prepareCall("{call RelationToBeArchived()}");
					JOptionPane.showMessageDialog(null, "Successfully added to Archive", "Success",
							JOptionPane.INFORMATION_MESSAGE);
		
				} catch (Exception exp) {
					JOptionPane.showMessageDialog(null, exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
	}
	private void clearCenterPanel() {
		BorderLayout layout = (BorderLayout) managerPanel.getLayout();
		if (layout.getLayoutComponent(BorderLayout.CENTER) != null) {
			managerPanel.remove(layout.getLayoutComponent(BorderLayout.CENTER));
		}
	}
}
