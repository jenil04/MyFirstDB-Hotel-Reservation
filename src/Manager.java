import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

	/*
	 * public void actionPerformed(ActionEvent e) { try { String query =
	 * "INSERT INTO Staff(first_name, last_name, username, password) VALUES('" +
	 * firstName.getText() + "', '" + lastName.getText() + "', '" +
	 * userName.getText() + "', '" + password.getText() + "');";
	 * 
	 * Statement stmt = connection.createStatement(); stmt.executeUpdate(query);
	 * stmt.close();
	 * 
	 * JOptionPane.showMessageDialog(null, "Successfully added staff.",
	 * "Success", JOptionPane.INFORMATION_MESSAGE); } catch (Exception exp) {
	 * JOptionPane.showMessageDialog(null, exp.getMessage(), "Error",
	 * JOptionPane.ERROR_MESSAGE); } }
	 */

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
