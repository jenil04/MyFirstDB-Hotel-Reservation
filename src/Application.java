import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

public class Application extends JFrame {
	public static MyFirstDB app = new MyFirstDB();
	public static CardLayout cardLayout = new CardLayout();
	public static JPanel mainPanel = new JPanel();
	
	private JPanel initialPanel;
	private JButton btnSignup;
	private JButton btnLogIn;
	private JRadioButton user; 
	private JRadioButton manager; 
	private JRadioButton staff;  
	
	public Application() {
		this.setTitle("Welcome To MyFirstDB Hotel Management System");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1000, 500);
		
		this.initialPanel = new JPanel();
		this.initialPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.initialPanel.setLayout(null);
		
		this.user = new JRadioButton("User");
		this.user.setBounds(this.getBounds().width/3,25,100,30);    
		this.manager = new JRadioButton("Manager");
		this.manager.setBounds(this.getBounds().width/3,50,100,30);
		this.staff  = new JRadioButton("Staff");
		this.staff.setBounds(this.getBounds().width/3,75,100,30); 
		
		ButtonGroup radioButtonOptions = new ButtonGroup();
		radioButtonOptions.add(this.user);
		radioButtonOptions.add(this.manager);
		radioButtonOptions.add(this.staff);
		
		this.initialPanel.add(this.user);
		this.initialPanel.add(this.manager);
		this.initialPanel.add(this.staff);
		
		this.btnSignup = new JButton("Sign Up");
		this.btnSignup.setBounds(this.getBounds().width/3, 150, 100, 30);
		
		this.btnSignup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(radioButtonOptions.getSelection() == null){
					JOptionPane.showMessageDialog(null, "Choose an Option" ,"Error",JOptionPane.ERROR_MESSAGE);
				} else {
					//TODO 
					SignUp signUp = null;
					if (user.isSelected()) {
						signUp = new SignUp("guests");
					} else if (manager.isSelected()) {
						signUp = new SignUp("manager");
					} else if (staff.isSelected()) {
						signUp = new SignUp("staff");	
					}
					mainPanel.add(signUp.getSignUpPanel(), "signUpScreen");
					cardLayout.show(mainPanel, "signUpScreen");
				}
			}
		});
		
		this.btnLogIn = new JButton("LogIn");
		this.btnLogIn.setBounds(this.getBounds().width/3 + 100, 150, 100, 30);
		
		this.btnLogIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(radioButtonOptions.getSelection() == null){
					JOptionPane.showMessageDialog(null, "Choose an Option" ,"Error",JOptionPane.ERROR_MESSAGE);
				} else {
					LogIn logInWindow = null;
					if (user.isSelected()) {
						logInWindow = new LogIn("guests");
					} else if (manager.isSelected()) {
						logInWindow = new LogIn("manager");
					} else if (staff.isSelected()) {
						logInWindow = new LogIn("staff");
					}
					mainPanel.add(logInWindow.getLogInPanel(),"logInPanel");
					cardLayout.show(mainPanel, "logInPanel");
				}
			}
		});
		this.initialPanel.add(this.btnSignup);
		this.initialPanel.add(this.btnLogIn);
		
		mainPanel.setLayout(cardLayout);
		mainPanel.add(this.initialPanel, "initialScreen");
		
		this.setContentPane(mainPanel);
		cardLayout.show(mainPanel, "initialScreen");
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		app.connect();
		Application application = new Application();
		application.setVisible(true);
	}
}
