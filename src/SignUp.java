import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SignUp extends JFrame {

	private String typeOfSignUp;
	private JPanel signUpPanel;
	private JTextField firstName;
	private JTextField lastName;
	private JTextField userName;
	private JTextField password;
	private JTextField email;
	private JTextField numOfGuests;
	private JButton register;
	private Connection connection;
	
	public SignUp(String typeOfSignUp) {
		this.connection = Application.app.getConn();
		this.typeOfSignUp = typeOfSignUp;
		
		this.signUpPanel = new JPanel();
		this.signUpPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.signUpPanel.setLayout(null);
		
		if (this.typeOfSignUp.equalsIgnoreCase("Guests")) {
			this.setUpGuestsSignUp();
		} else if (this.typeOfSignUp.equalsIgnoreCase("Manager") || this.typeOfSignUp.equalsIgnoreCase("Staff")) {
			this.setUpEmployeeSignUp();
		}
	}
	
	private void setUpGuestsSignUp() {
		this.setUpFirstNameField();
		this.setUpLastNameField();
		this.setUpUserNameField();
		this.setUpPasswordField();
		this.setUpEmailField();
		this.setUpNumGuestsField();
		this.setUpSignUpButton();
	}
	
	private void setUpEmployeeSignUp() {
		this.setUpFirstNameField();
		this.setUpLastNameField();
		this.setUpUserNameField();
		this.setUpPasswordField();
		this.setUpSignUpButton();
	}

	public JPanel getSignUpPanel() {
		return signUpPanel;
	}
	
	private void setUpFirstNameField() {
		this.firstName = new JTextField();
		this.firstName.setBounds(185, 10, 100, 20);
		this.firstName.setColumns(10);
		
		JLabel firstNameLabel = new JLabel("First Name");
		firstNameLabel.setBounds(70, 10, 85, 15);
		this.signUpPanel.add(this.firstName);
		this.signUpPanel.add(firstNameLabel);
	}
	
	private void setUpLastNameField() {
		this.lastName = new JTextField();
		this.lastName.setBounds(185, 40, 100, 20);
		this.lastName.setColumns(10);
		
		JLabel lastNameLabel = new JLabel("Last Name");
		lastNameLabel.setBounds(70, 40, 85, 15);
		this.signUpPanel.add(this.lastName);
		this.signUpPanel.add(lastNameLabel);
	}
	
	private void setUpUserNameField() {
		this.userName = new JTextField();
		this.userName.setBounds(185, 70, 100, 20);
		this.userName.setColumns(10);
		
		JLabel userNameLabel = new JLabel("User Name");
		userNameLabel.setBounds(70, 70, 85, 15);
		this.signUpPanel.add(this.userName);
		this.signUpPanel.add(userNameLabel);
	}
	
	private void setUpPasswordField() {
		this.password = new JTextField();
		this.password.setBounds(185, 100, 100, 20);
		this.password.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(70, 100, 85, 15);
		this.signUpPanel.add(this.password);
		this.signUpPanel.add(passwordLabel);
	}
	
	private void setUpEmailField() {
		this.email = new JTextField();
		this.email.setBounds(185, 130, 100, 20);
		this.email.setColumns(10);
		
		JLabel emailLabel = new JLabel("Email ID");
		emailLabel.setBounds(70, 130, 85, 15);
		this.signUpPanel.add(this.email);
		this.signUpPanel.add(emailLabel);
	}
	
	private void setUpNumGuestsField() {
		this.numOfGuests = new JTextField();
		this.numOfGuests.setBounds(185, 160, 100, 20);
		this.numOfGuests.setColumns(10);
		
		JLabel numOfGuestsLabel = new JLabel("# of Guests");
		numOfGuestsLabel.setBounds(70, 160, 85, 15);
		this.signUpPanel.add(this.numOfGuests);
		this.signUpPanel.add(numOfGuestsLabel);
	}
	
	private void setUpSignUpButton() {
		this.register = new JButton("Sign Up");
		this.register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {	
				if(isEmpty(typeOfSignUp)) {
					JOptionPane.showMessageDialog(null, "Please fill in all the Fields" ,"Error",JOptionPane.ERROR_MESSAGE);
				} else {
					executeSignUp();
				}
			}
		});
		this.register.setBounds(125, 200, 85, 20);
		this.signUpPanel.add(this.register);
	}
	
	private boolean isEmpty(String typeOfSignUp) {
		boolean temp = (this.firstName.getText().equals("")) ||
						(this.lastName.getText().equals("")) ||
						(this.userName.getText().equals("")) || 
						(this.password.getText().equals(""));
		
		if (typeOfSignUp.equals("guests")) {
			temp = temp ||
					(this.email.getText().equals("")) ||
					(this.numOfGuests.getText().equals(""));
		}
		return temp;
	}
	
	private void executeSignUp() {
		String query = "";
		if (this.typeOfSignUp.equalsIgnoreCase("guests")) {
			int numOfGuests = Integer.parseInt(this.numOfGuests.getText());
			query = "INSERT INTO Guests "
					+ " (first_name, last_name, username, password, cus_email, num_of_guests) "
					+ "VALUES('"
					+ this.firstName.getText() + "', '" + this.lastName.getText() 
					+ "', '" + this.userName.getText() + "', '" + this.password.getText()
					+ "', '" + this.email.getText() + "', " + numOfGuests + ");";
		} else {
			query = "INSERT INTO " + this.typeOfSignUp
					+ " (first_name, last_name, username, password) "
					+ "VALUES('"
					+ this.firstName.getText() + "', '" + this.lastName.getText() 
					+ "', '" + this.userName.getText() + "', '" + this.password.getText() + "');";
		}

		try {
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate(query);
			
			if (this.typeOfSignUp.equals("guests")) {
				this.loadUserPanel();
			} else if (this.typeOfSignUp.equals("manager")) {
				this.loadManagerPanel();
			} else {
				this.loadStaffPanel();
			}
			stmt.close();
		} catch (Exception e) {
			if (e.getMessage().contains("Duplicate entry")) {
				JOptionPane.showMessageDialog(null," Pick a different User Name","Error",JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null,e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void loadUserPanel() {
		User user = new User();
		Application.mainPanel.add(user.getUserPanel(), "userSession");
		Application.cardLayout.show(Application.mainPanel, "userSession");
	}
	
	private void loadManagerPanel() {
		Manager manager = new Manager();
		Application.mainPanel.add(manager.getManagerPanel(), "managerSession");
		Application.cardLayout.show(Application.mainPanel, "managerSession");
	} 
	
	private void loadStaffPanel() {
		Staff staff = new Staff();
		Application.mainPanel.add(staff.getStaffPanel(), "staffSession");
		Application.cardLayout.show(Application.mainPanel, "staffSession");
	}
}
