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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LogIn extends JFrame {

	private String typeOfLogIn;
	private JPanel logInPanel;
	private JTextField userName;
	private JTextField password;
	private JButton logInButton;
	private Connection connection;
	
	public LogIn(String typeOfLogIn) {
		this.connection = Application.app.getConn();
		this.typeOfLogIn = typeOfLogIn;
		
		this.logInPanel = new JPanel();
		this.logInPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.logInPanel.setLayout(null);
		
		this.setUpUserNameField();
		this.setUpPasswordField();
		this.setUpLoginButton();
	}

	public JPanel getLogInPanel() {
		return this.logInPanel;
	}

	private void setUpPasswordField() {
		this.userName = new JTextField();
		this.userName.setBounds(188, 50, 100, 20);
		this.userName.setColumns(10);
		
		JLabel userNameLabel = new JLabel("User Name");
		userNameLabel.setBounds(70, 50, 85, 15);
		this.logInPanel.add(this.userName);
		this.logInPanel.add(userNameLabel);
	}

	private void setUpUserNameField() {
		this.password = new JTextField();
		this.password.setBounds(185, 100, 100, 20);
		this.password.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(70, 100, 85, 15);
		this.logInPanel.add(this.password);
		this.logInPanel.add(passwordLabel);
	}
	
	private void setUpLoginButton() {
		this.logInButton = new JButton("Log In");
		this.logInButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill in all the Fields", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					executeLogIn();
				}
			}
		});
		this.logInButton.setBounds(125, 180, 85, 20);
		this.logInPanel.add(this.logInButton);
	}
	
	private boolean isEmpty() {
		return (this.userName.getText().equals("")) || 
				(this.password.getText().equals(""));
	}
	
	private void executeLogIn() {
		try {
			String query = "Select * from " + this.typeOfLogIn
							+ " Where userName = '" + this.userName.getText()
							+ "' and password = '" + this.password.getText() + "';";
			
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (!rs.next()) {
				JOptionPane.showMessageDialog(null, "UserName and/or Password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				if (this.typeOfLogIn.equals("guests")) {
					this.loadUserPanel(rs.getInt("guest_Id"),
										rs.getString("first_name"),
										rs.getString("last_name"),
										rs.getInt("num_of_guests"));
					
				} else if (this.typeOfLogIn.equals("manager")) {
					this.loadManagerPanel(rs.getInt("manager_Id"),
											rs.getString("first_name"),
											rs.getString("last_name"));
				} else {
					this.loadStaffPanel(rs.getInt("staff_Id"),
											rs.getString("first_name"),
											rs.getString("last_name"));
				}
				stmt.close();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void loadUserPanel(int guestId, String fname, String lname, int numOfGuests) {
		User user = new User(guestId, fname, lname, numOfGuests);
		Application.mainPanel.add(user.getUserPanel(), "userSession");
		Application.cardLayout.show(Application.mainPanel, "userSession");
	}
	
	private void loadManagerPanel(int managerId, String fname, String lname) {
		Manager manager = new Manager(managerId, fname, lname);
		Application.mainPanel.add(manager.getManagerPanel(), "managerSession");
		Application.cardLayout.show(Application.mainPanel, "managerSession");
	} 
	
	private void loadStaffPanel(int staffId, String fname, String lname) {
		Staff staff = new Staff(staffId, fname, lname);
		Application.mainPanel.add(staff.getStaffPanel(), "staffSession");
		Application.cardLayout.show(Application.mainPanel, "staffSession");
	}
	
}
