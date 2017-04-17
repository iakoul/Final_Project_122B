package main;

import java.util.Scanner;
import java.io.Console; //password test
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ConsoleApp {

	public static void main(String[] args) {
		
		//Incorporate mySQL driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (final Exception e) {
			System.out.println("mySQL driver was not loaded");
			System.out.println(e.getMessage());
		}
		
		Connection connection = null;
		
		while(true){
			System.out.println("Welcome to the Movie and Star Database");
			Scanner scanner = new Scanner(System.in);
			
			boolean loggedIn = false;
			while(!loggedIn) {
				System.out.println("To quit, enter quit for username and press enter for password");
				//Connect to database
				try {
					connection = DriverManager.getConnection("jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false","root","mysqlpass");
				} catch (final Exception e) {
					System.out.println("Failed to establish connection with database with error " + e.getMessage());
				}
				
				System.out.println("Please enter your username");
				String username = scanner.nextLine().trim();
				
				//testing password input
				String password = "";
				try {
					Console console = System.console();
					char[] pwd = console.readPassword("Please enter your password");
					password = String.valueOf(pwd);
				} catch (Exception e) {
					System.out.println("Hidden passwrod does not work in IDE, please reenter password");
					password = scanner.nextLine().trim();
				}
				

				if (username.equals("quit") && password.equals("")) {
					try {
						connection.close();
					} catch (SQLException e) {
						System.out.println("Failed to close connection with databse with error " + e.getMessage());
					}
					System.out.println("Good bye");
					scanner.close();
					return;
				} else {
					try {
						String prepQuery = "SELECT c.first_name, c.last_name FROM customers c WHERE c.email = ? AND c.password = ?";
						PreparedStatement pstmt = connection.prepareStatement(prepQuery);
						pstmt.setString(1, username);
						pstmt.setString(2, password);
						ResultSet results = pstmt.executeQuery();
						if (results.next()) {
							System.out.println("Welcome, " + results.getString(1) + " " + results.getString(2));
							loggedIn = true;
						} else {
							System.out.println("Incorrect username or password");
							loggedIn = false;
						}
					} catch (SQLException e) {
						System.out.println("Select statement failed with code " + e.getMessage());
					}
				}
				clearConsole();
			}
			
			while(loggedIn) {
				System.out.println("Please select one of these options");
				System.out.println("1. Search movies of a given star");
				System.out.println("2. Insert new star into database");
				System.out.println("3. Insert new customer into database");
				System.out.println("4. Delete customer from database");
				System.out.println("5. Print out database metadata");
				System.out.println("6. Execute custom SQL command");
				System.out.println("7. Log out");
				System.out.println("8. Exit");
				
				int selection = scanner.nextInt();
				scanner.nextLine();
				switch (selection) {
					case 1:
						//Search movies given star name
						searchMovies(connection);
						break;
					case 2:
						//Insert star into database
						insertStar(connection);
						break;
					case 3:
						//Insert customer into database
						insertCustomer(connection);
						break;
					case 4:
						//Remove customer from database
						removeCustomer(connection);
						break;
					case 5:
						//Print metadata
						printMetadata(connection);
						break;
					case 6:
						//Allow executing custom SQL
						customSQL(connection);
						break;
					case 7:
						//log out
						try {
							connection.close();
						} catch (SQLException e) {
							System.out.println("Failed to close DB connection with error " + e.getMessage());
						}
						loggedIn = false;
						break;
					case 8:
						//exit
						scanner.close();
						return;
					default:
						System.out.println("Invalid selection");
				}
				clearConsole();
			}
		}
	}
	
	public static void clearConsole() {
		/*
		//Does not currently work too well, scrapping this for now
		int screenLines = 25;
		for (int i = 0; i < screenLines; i++) {
	        System.out.println();
	    }
	    */
	}
	
	public static void searchMovies(Connection connection) {
		clearConsole();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter the star's first name or press enter to leave blank");
		String firstName = scanner.nextLine().trim();
		System.out.println("Please enter the star's last name or press enter to leave blank");
		String lastName = scanner.nextLine().trim();
		System.out.println("Please enter the star's ID or enter -1 to skip");
		int starId = scanner.nextInt();
		scanner.nextLine();
		
		try {
			String prepQuery = "SELECT m.id, m.title, m.year, m.director, m.banner_url, m.trailer_url FROM movies m, stars s, stars_in_movies sm WHERE sm.movie_id = m.id AND sm.star_id = s.id";
			
			if (starId != -1) {
				prepQuery += " AND s.id = ?";
			}
			if (!firstName.equals("")) {
				prepQuery += " AND s.first_name = ?";
			}
			if (!lastName.equals("")) {
				prepQuery += " AND s.last_name = ?";
			}
			prepQuery += ";";
			
			PreparedStatement pstmt = connection.prepareStatement(prepQuery);
			int starIdPresent = 0;
			int firstNamePresent = 0;
			if (starId != -1) {
				pstmt.setInt(1, starId);
				starIdPresent = 1;
			}
			if (!firstName.equals("")) {
				pstmt.setString(1 + starIdPresent, firstName);
				firstNamePresent = 1;
			}
			if (!lastName.equals("")) {
				pstmt.setString(1 + starIdPresent + firstNamePresent, lastName);
			}
			
			ResultSet result = pstmt.executeQuery();
			
			System.out.println("Results");
			while(result.next()) {
				System.out.println("ID = " + result.getInt(1));
				System.out.println("Title = " + result.getString(2));
				System.out.println("Year = " + result.getString(3));
				System.out.println("Director = " + result.getString(4));
				System.out.println("Banner URL = " + result.getString(5));
				System.out.println("Trailer URL = " + result.getString(6));
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("Select statement failed with code " + e.getMessage());
		}
	}
	
	public static void insertStar(Connection connection) {
		clearConsole();
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter the following information in order to add the star to the database");
		System.out.println("Last name, if star has only one name, enter that here");
		String lastName = scanner.nextLine().trim();
		System.out.println("First name, if star has only one name, press enter here");
		String firstName = scanner.nextLine().trim();
		System.out.println("Date of Birth, YYYY/MM/DD");
		String dob = scanner.nextLine().trim();
		System.out.println("Enter a URL to a photo or press enter");
		String photoUrl = scanner.nextLine().trim();
		
		try {
			String prepQuery = "INSERT INTO stars VALUES(?, ?, ?, ?, ?)";

			PreparedStatement pstmt = connection.prepareStatement(prepQuery);
			
			pstmt.setNull(1, Types.INTEGER);
			
			pstmt.setString(2, firstName);
			pstmt.setString(3, lastName);
			
			//Date conversion found on stack overflow and modified by me
			java.sql.Date sqlDateDob = null;
			try {
			    DateFormat dateformat;
			    java.util.Date utilDateDob = null;
			    dateformat = new SimpleDateFormat("yyyy/MM/dd");
			    utilDateDob = dateformat.parse(dob);
			    sqlDateDob = new java.sql.Date(utilDateDob.getTime());
			} catch (Exception e) {
				System.out.println("Error parsing dob, possible incorrect format");
			}
			
			pstmt.setDate(4, sqlDateDob);
			
			pstmt.setString(5, photoUrl);
			
			System.out.println(pstmt);
			
			System.out.println("Number of rows affected: " + pstmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("Insert statement failed with code " + e.getMessage());
		}
	}
	
	public static void insertCustomer(Connection connection) {
		clearConsole();
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter customer's last name, if customer only has one name, enter it here");
		String lastName = scanner.nextLine().trim();
		System.out.println("Enter customer's first name or press enter to leave blank");
		String firstName = scanner.nextLine().trim();
		System.out.println("Enter customer's credit card ID");
		String ccId = scanner.nextLine().trim();
		System.out.println("Enter customer's address");
		String address = scanner.nextLine().trim();
		System.out.println("Enter customer's email address");
		String email = scanner.nextLine().trim();
		System.out.println("Enter customer's password");
		String password = scanner.nextLine().trim();
		
		
		try {
			String prepQueryCheckCC = "SELECT * FROM creditcards cc WHERE cc.id = ?";
			
			PreparedStatement pstmtCheck = connection.prepareStatement(prepQueryCheckCC);
			pstmtCheck.setString(1, ccId);
			ResultSet results = pstmtCheck.executeQuery();
			if (results.next() && results.getString(1).equals(ccId) && results.getString(2).equals(firstName) && results.getString(3).equals(lastName)) {
				String prepQueryInsert = "INSERT INTO customers VALUES(?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmtInsert = connection.prepareStatement(prepQueryInsert);
				pstmtInsert.setNull(1, Types.INTEGER);
				pstmtInsert.setString(2, firstName);
				pstmtInsert.setString(3, lastName);
				pstmtInsert.setString(4, ccId);
				pstmtInsert.setString(5, address);
				pstmtInsert.setString(6, email);
				pstmtInsert.setString(7, password);
				
				System.out.println("Number of rows affected: " + pstmtInsert.executeUpdate());
				
			} else {
				System.out.println("Credit Card number was unable to be validated, user was not added");
			}
		} catch (SQLException e) {
			System.out.println("Customer entry failed with error " + e.getMessage());
		}
	}
	
	public static void removeCustomer(Connection connection) {
		clearConsole();
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter customer's ID number or enter -1 to skip");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter customer's first name, leave blank if deleting by ID");
		String firstName = scanner.nextLine().trim();
		System.out.println("Enter customer's last name, leave blank if deleting by ID");
		String lastName = scanner.nextLine().trim();
		
		try {
			String prepQuery = "DELETE FROM customers WHERE id = ? OR (first_name = ? AND last_name = ?)";
			
			PreparedStatement pstmt = connection.prepareStatement(prepQuery);
			
			if (id != -1) {
				pstmt.setInt(1, id);
				pstmt.setNull(2, Types.VARCHAR);
				pstmt.setNull(3, Types.VARCHAR);
			} else if (firstName.equals("") || lastName.equals("")) {
				System.out.println("If deleting by name, both first and last name need to be provided");
				return;
			} else {
				pstmt.setNull(1, Types.INTEGER);
				pstmt.setString(2, firstName);
				pstmt.setString(3, lastName);
			}
			System.out.println("Number of rows affected: " + pstmt.executeUpdate());
		} catch (SQLException e) {
			System.out.println("Failed to delete entry with error " + e.getMessage());
		}
	}
	
	public static void printMetadata(Connection connection) {
		clearConsole();
		try {
			DatabaseMetaData mdata = connection.getMetaData();
			ResultSet results = mdata.getTables(null, null, "%",null);
			System.out.println("Table Metadata:");
			while (results.next()) {
				System.out.println("Table name: " + results.getString(3));
				ResultSetMetaData resultMd = results.getMetaData();
				for (int i = 1; i <= resultMd.getColumnCount(); i++) {
					//Don't know if results are correct
					System.out.println(" Column name: " + resultMd.getColumnName(i) + " Column type: " + resultMd.getColumnTypeName(i));
				}
			}
		} catch (SQLException e) {
			System.out.println("Failed to retrieve metadata with error " + e.getMessage());
		}
	}
	
	public static void customSQL(Connection connection) {
		clearConsole();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter a valid select, insert, update, or delete mySQL statement, complete with closing semicolon, but with no newline characters");
		String query = scanner.nextLine().trim();
		try {
			Statement stmt = connection.createStatement();
			ResultSet results = null;
			if (query.startsWith("S") || query.startsWith("s")) { //select statements
				results = stmt.executeQuery(query);
				ResultSetMetaData resultMd = results.getMetaData();
				while (results.next()) {
					for (int i = 1; i <= resultMd.getColumnCount(); i++) {
						System.out.println("Column " + i + " " + results.getString(i));
					}
					System.out.println();
				}
			} else if (query.startsWith("I") || query.startsWith("i") || query.startsWith("U") || query.startsWith("u") || query.startsWith("D") || query.startsWith("d")) { //insert, update, or delete statements
				System.out.println("Number of rows affected: " + stmt.executeUpdate(query));
			} else {
				System.out.println("Query type not recognized");
			}
		} catch (SQLException e) {
			System.out.println("The statement failed to run correctly with error " + e.getMessage());
		}
	}
}