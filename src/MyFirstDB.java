
/**
 * MyFirstDB Hotel Reservation
 * 
 * Brendan Kao
 * Yash Parikh
 * Jenil Thakker
 * Roy Vannakittikun
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

/**
 * In order to use this, you have to download the Connector/J driver and add its
 * .jar file to your build path. You can find it here:
 * 
 * http://dev.mysql.com/downloads/connector/j/
 * 
 * You will see the following exception if it's not in your class path:
 * 
 * java.sql.SQLException: No suitable driver found for
 * jdbc:mysql://localhost:3306/
 * 
 * To add it to your class path: 1. Right click on your project 2. Go to Build
 * Path -> Add External Archives... 3. Select the file
 * mysql-connector-java-5.1.24-bin.jar NOTE: If you have a different version of
 * the .jar file, the name may be a little different.
 * 
 * You will get the following exception if the credentials are wrong:
 * 
 * java.sql.SQLException: Access denied for user 'userName'@'localhost' (using
 * password: YES)
 * 
 * You will instead get the following exception if MySQL isn't installed, isn't
 * running, or if your serverName or portNumber are wrong:
 * 
 * java.net.ConnectException: Connection refused
 */
public class MyFirstDB {

	/** The name of the MySQL account to use (or empty for anonymous) */
	private final String userName = "root";

	/** The password for the MySQL account (or empty for anonymous) */
	private final String password = "password123";

	/** The name of the computer running MySQL */
	private final String serverName = "localhost";

	/** The port of the MySQL server (default is 3306) */
	private final int portNumber = 3306;

	/** The name of the database */
	private final String dbName = "hotelmanagement";

	private Connection conn;
	private String currentUsername = "";
	private int currentUserId = 0;

	/**
	 * Get a new database connection
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", this.userName);
		connectionProps.put("password", this.password);

		conn = DriverManager.getConnection(
				"jdbc:mysql://" + this.serverName + ":" + this.portNumber + "/" + this.dbName + "?useSSL=false",
				connectionProps);

		return conn;
	}

	/**
	 * Run a SQL command which does not return a recordset:
	 * CREATE/INSERT/UPDATE/DELETE/DROP/etc.
	 * 
	 * @throws SQLException
	 *             If something goes wrong
	 */
	public boolean executeUpdate(Connection conn, String command) throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(command); // This will throw a SQLException if it
											// fails
			return true;
		} finally {

			// This will run whether we throw an exception or not
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	/**
	 * Connect to MySQL and do some stuff.
	 */
	public void connect() {

		// Connect to MySQL
		conn = null;
		try {
			conn = this.getConnection();
			System.out.println("Connected to database");
		} catch (SQLException e) {
			System.out.println("ERROR: Could not connect to the database");
			e.printStackTrace();
			return;
		}

	}

	public void displayAvailableRooms() {
		try {
			String query = "SELECT * FROM room WHERE status=true";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("Room_ID	Type");

			while (rs.next()) {
				int id = rs.getInt("room_id");
				String type = rs.getString("type_of_room");
				System.out.println(id + "	" + type);
			}
			System.out.println("Book a room with: booking (roomID#)");
			stmt.close();
		} catch (SQLException e) {
			System.out.println("SQL exception occured" + e);
		}
	}

	public void login(String user, String pass) {
		try {
			// String query = "SELECT * FROM user";
			String query = "SELECT * FROM guests WHERE username='" + user + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			// System.out.println("ID username password");
			if (!rs.next()) {
				System.out.println("Username does not exist!");
			} else {
				do {
					int id = rs.getInt("guest_Id");
					String username = rs.getString("username");
					String password = rs.getString("password");
					// System.out.println(id + " " + username + " " + password);

					if (password.equals(pass)) {
						currentUsername = username;
						currentUserId = id;
						System.out.println("Login successful!");
					} else {
						System.out.println("Invalid username and password combination.");
					}
				} while (rs.next());
			}
			stmt.close();

		} catch (SQLException e) {
			System.out.println("SQL exception occured" + e);
		}
	}

	public void signup(String user, String pass, String name, String lastname, String email, int guests) {
		try {
			String query = "INSERT INTO guests (first_name, last_name, username, password, cus_email, num_of_guests) VALUES('"
					+ name + "', '" + lastname + "', '" + user + "', '" + pass + "', '" + email + "', " + guests + ");";
			this.executeUpdate(conn, query);
			System.out.println("Signup successful!");

		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	// TODO:
	// Modify check in and check out, currently set to current time.
	// Generate better confirmation number
	public void bookRoomId(int roomId) {
		try {
			String checkRoom = "SELECT * FROM room WHERE room_id=" + roomId + "";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkRoom);
			if (!rs.next()) {
				System.out.println("Room ID does not exist.");
			} else {
				do {
					int id = rs.getInt("room_id");
					boolean status = rs.getBoolean("status");
					String type = rs.getString("type_of_room");

					if (status) {
						long millis = System.currentTimeMillis();
						Date date = new Date(millis);

						Random rand = new Random();
						int n = rand.nextInt(50) + 1;

						String query = "INSERT INTO reservation (confirmation, guest_id, room_id, check_in, check_out) VALUES("
								+ n + ", '" + currentUserId + "', '" + roomId + "', '" + date + "', '" + date + "');";
						this.executeUpdate(conn, query);
						String query2 = "UPDATE room SET status=false WHERE room_id=" + roomId + ";";
						this.executeUpdate(conn, query2);
						System.out.println("Room type: " + type);
						System.out.println("Room ID: " + roomId);
						System.out.println("Booked successfully!");

					} else {
						System.out.println("Room ID already booked.");
						return;
					}
				} while (rs.next());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	public boolean isLoggedIn() {
		if (!currentUsername.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * Connect to the DB and do some stuff
	 */
	public static void main(String[] args) {
		MyFirstDB app = new MyFirstDB();
		app.connect();

		System.out.println("Welcome to MyFirstDB Hotel Reservation");
		System.out.println("Created by Brendan Kao, Yash Parikh, Jenil Thakker, Roy Vannakittikun");
		Scanner userInput = new Scanner(System.in);
		while (true) {
			System.out.println("Please enter a command or type help.");
			String input = userInput.nextLine();
			// System.out.println("input is '" + input + "'");

			if (input.startsWith("signup")) {
				String[] parameters = input.split(" ");
				if (input.contains(" ") && parameters.length == 7) {
					String username = parameters[1];
					String password = parameters[2];
					String firstname = parameters[3];
					String lastname = parameters[4];
					String email = parameters[5];
					int guests = Integer.parseInt(parameters[6]);
					System.out.println("Username: " + username);
					System.out.println("Password: " + password);
					System.out.println("First name: " + firstname);
					System.out.println("Last name: " + lastname);
					System.out.println("Email: " + email);
					System.out.println("# of guests: " + guests);
					System.out.println("Registering... ");
					app.signup(username, password, firstname, lastname, email, guests);
				} else {
					System.out.println("Use case: signup username password firstname lastname email #guests");
				}
			}

			if (input.startsWith("login")) {
				String[] parameters = input.split(" ");
				if (input.contains(" ") && parameters.length == 3) {

					String username = parameters[1];
					String password = parameters[2];
					System.out.println("Username: " + username);
					System.out.println("Password: " + password);
					System.out.println("Logging in... ");
					app.login(username, password);
				} else {
					System.out.println("Use case: login username password");
				}
			}

			if (input.startsWith("booking")) {
				String[] parameters = input.split(" ");
				if (app.isLoggedIn()) {
					if (parameters.length == 2 && parameters[1].equals("available")) {
						System.out.println("Displaying all available rooms.");
						app.displayAvailableRooms();
					} else if (parameters.length == 2 && isNumeric(parameters[1])) {
						app.bookRoomId(Integer.parseInt(parameters[1]));
					} else {
						System.out.println("Booking Commands:");
						System.out.println("booking available	-Display list of available rooms.");
						System.out.println("booking (roomID#)	-Book room's id.");
					}
				} else {
					System.out.println("Please log in first!");
				}
			}

			if (input.startsWith("help")) {
				System.out.println("signup	-Guest sign up");
				System.out.println("login	-Guest login");
				System.out.println("booking	-Guest booking system");
				System.out.println("");
			}

			if (input.startsWith("exit")) {
				System.exit(0);
			}

		}

	}

	public static boolean isNumeric(String s) {
		return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

}