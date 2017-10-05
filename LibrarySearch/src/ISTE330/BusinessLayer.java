package ISTE330;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Business layer class acts as the business layer
 * and will handle validation of input as well as
 * handling user privilege.
 * 
 * Current Business rules dictate:
 * 		- Decides which database/Driver to use.
 * 		- Decides what method of hashing is used (SHA256)
 * 		- Only Admins may add, update, and delete other users.
 * 		- Users can update their own values, except deciding privilege level. 
 * 		- Only Faculty and Admins may add, update, and delete papers.
 * 		- Faculty may update and delete their own papers.
 * 		- Every user can Search and View papers and user log.
 */

/*
 * TODO:
 * businesslayer Constructor
 * updatePaper
 * deletePaper
 */

/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 *
 */
public class BusinessLayer {
	/*
	 * ERROR TESTING
	 */
	public static void main(String args[]) {
		/*
		 * Hal: password Guy: hunter2
		 * 
		 */
		/*
		 * Error testing variables
		 */
		String usernameTrue = "zxy432@rit.edu";
		String usernameFalse = "something@dotedu";
		BusinessLayer myConn = new BusinessLayer();
		String username = "hal@rit.edu";
		String password = "password";
		String username1 = "abc@rit.edu";
		String password1 = myConn.hashPassword("Pa124124afan2");
		String newUserName = "jdx123@rit.edu";
		String newPassword = myConn.hashPassword("JohnsPass23");
		ArrayList<String> myPaper = new ArrayList<String>();
		myPaper.add("Test Title");
		myPaper.add("Test Abstract");
		myPaper.add("Test Citation");
		myPaper.add("3");
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add("Artificial Intelligence");
		System.out.println(myConn.hashPassword("hunter2") + "\n");
		/*
		 * test hasPassword
		 * 
		 * System.out.println(myConn.hashPassword(password));
		 * 
		 * 
		 * User name and password validation test
		 * 
		 * //System.out.println(myConn.isValidPassword(password1));
		 * //System.out.println(myConn.isValidUsername(username1));
		 * 
		 * 
		 * Login test
		 * 
		 * if(myConn.logIn(username, password)){ System.out.println(
		 * "Successful Login!\n"+ myConn.getCurrentUser().toString(true)); }
		 * 
		 * 
		 * Add User Test
		 * 
		 * ArrayList<String> addUserArr = new ArrayList<String>();
		 * addUserArr.add("Jane"); addUserArr.add("Doe");
		 * addUserArr.add(username1); addUserArr.add(password1);
		 * if(myConn.addUser(addUserArr, 3)){ System.out.println(
		 * "\nSuccessfully added user"); }
		 * 
		 * 
		 * UpdateUser admin
		 * 
		 * ArrayList<String> updateUserArr = new ArrayList<String>();
		 * updateUserArr.add("John"); updateUserArr.add("Doe");
		 * updateUserArr.add(newUserName); updateUserArr.add(newPassword);
		 * updateUserArr.add("5"); if(myConn.updateUser(updateUserArr, 1)){
		 * System.out.println("\nSuccessfully updated user"); }
		 * 
		 * 
		 * 
		 * logout
		 * 
		 * if(myConn.logout()){ System.out.println("\nSuccessfully logged out!"
		 * ); }
		 * 
		 * 
		 * Student login
		 * 
		 * if(myConn.logIn(newUserName, newPassword)){ System.out.println(
		 * "\nSuccessful Login as new user!\n"+
		 * myConn.getCurrentUser().toString(true));
		 * 
		 * 
		 * Update student values
		 * 
		 * updateUserArr = new ArrayList<String>(); updateUserArr.add("Bob");
		 * updateUserArr.add("Doe"); updateUserArr.add(username1);
		 * updateUserArr.add(password1); if(myConn.updateUser(updateUserArr,
		 * 1)){// student can't change privilege System.out.println(
		 * "\nSuccessfully updated student values!\n"+
		 * myConn.getCurrentUser().toString(true)); }
		 * 
		 * 
		 * logout
		 * 
		 * if(myConn.logout()){ System.out.println("\nSuccessfully logged out!"
		 * ); }
		 * 
		 * }
		 */
		/*
		 * Admin Login
		 * 
		 * if (myConn.logIn(username, password)) { System.out.println(
		 * "Successful Login!\n" + myConn.getCurrentUser().toString(true)); }
		 * 
		 * 
		 * Delete User test
		 * 
		 * System.out.println(myConn.deleteUser("5"));
		 * 
		 * 
		 * SearchTable test
		 * 
		 * System.out.println(myConn.searchTable("O", 1).toString());
		 * System.out.println(myConn.searchTable("te", 2).toString());
		 * 
		 */
		if (myConn.logIn(username, password)) {
			System.out.println("Successful Login!\n" + myConn.getCurrentUser().toString(true));
		}

		/*
		 * Add paper
		 */
		System.out.println(myConn.addPaper(myPaper, keywords));

		System.out.println(myConn.searchTable("", 0).toString());

		/*
		 * Delete paper
		 */
		System.out.println(myConn.deletePaper("6"));

		/*
		 * logout
		 */
		if (myConn.logout()) {
			System.out.println("\nSuccessfully logged out!");
		}
	}// end Main

	/**
	 * Class constructor will set a new connection to the database.
	 */
	public BusinessLayer() {
		this.url = "jdbc:mysql://127.0.0.1/484project";
		this.driver = "com.mysql.jdbc.Driver";
		this.myDatabase = new DatabaseManager("root", "student", this.url, this.driver);
		try {
			myDatabase.start();
		} catch (LibraryException e) {
			new LibraryException("Could not open connection to database");
		} // end catch
	}// end Constructor

	/**
	 * Function will take a username and password and determine if that
	 * combination was
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean logIn(String username, String password) {
		// Hash the password
		password = this.hashPassword(password);
		/*
		 * Check to see if a valid username and password was entered if valid,
		 * then set the current user's values to the ones retrieved from
		 * database
		 */
		if (this.isValidUsername(username) && this.isValidPassword(password)) {
			String sql = "SELECT * FROM users WHERE email = ? && password = ?";
			// 2d arraylist to hold resultset of select call
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
			/*
			 * Arraylist of strings to hold values
			 */
			ArrayList<String> values = new ArrayList<String>();
			values.add(username);
			values.add(password);
			try {
				// search database for rows pertaining to values
				result = myDatabase.getData(sql, values, false);

				// if a row was found
				if (result.size() == 1) {
					// Assign the database values to the current user
					this.currentUser = new Users(result.get(0).get(0), // UserID
							result.get(0).get(1), // firstName
							result.get(0).get(2), // lastName
							result.get(0).get(3), // email
							result.get(0).get(4), // password
							Integer.parseInt(result.get(0).get(5))); // userLevel
					// Successful login
					return true;
				} // end if
			} catch (LibraryException e) {
				// Send message to terminal
				new LibraryException("Couldn't log in successfully");
			} // end catch
		} // end if
		return false;
	}// end login()

	/**
	 * Function acts as a "log off" feature it will remove all references to the
	 * currentLogged user object.
	 * 
	 * @return
	 */
	public boolean logout() {
		this.currentUser = null;
		return true;
	}// end logout()

	/**
	 * Function will search the database in a specified table based on the given
	 * parameters.
	 * 
	 * @param keyword
	 *            the String value to search on
	 * @param category
	 *            The category type to search by -1 - returns ID( ONLY for use
	 *            with local functions) 0 - Default View, 1 - Search Users, 2 -
	 *            Paper by title, 3 - paper by author, 4 - paper by keywords
	 * @return 2D arraylist of values containing all found rows.
	 */
	public ArrayList<ArrayList<String>> searchTable(String keyword, int category) {
		// 2D arraylist to hold results
		ArrayList<ArrayList<String>> searchResult = new ArrayList<ArrayList<String>>();
		// SQL string to search based on given keyword
		String sql = "";
		keyword = "%" + keyword + "%";
		// Arraylist to hold values to send to the setData function
		ArrayList<String> values = new ArrayList<String>();
		switch (category) {
		case -1:
			/*
			 * Split the string by the comma to determine the user and paper id
			 */
			String[] idArr = keyword.split(",");
			sql = "SELECT users.id " + "FROM users " + "INNER JOIN authorship ON users.id = authorship.usersid "
					+ "INNER JOIN papers ON papers.id = authorship.paperid " + "WHERE users.id = ? AND papers.id = ?";
			break;
		case 0:
			sql = "SELECT users.lname, users.fName, papers.title, papers.id " + "FROM users "
					+ "INNER JOIN authorship ON users.id = authorship.usersid "
					+ "INNER JOIN papers ON papers.id = authorship.paperid";
			break;
		case 1:
			sql = "SELECT users.lname, users.fname, users.email "
					+ "FROM users WHERE users.fName LIKE ? OR users.lname LIKE ? ";
			values.add(keyword);
			values.add(keyword);
			break;
		case 2:
			sql = "SELECT users.lname, users.fName, papers.title, papers.id " + "FROM users "
					+ "INNER JOIN authorship ON users.id = authorship.usersid "
					+ "INNER JOIN papers ON papers.id = authorship.paperid " + "" + "WHERE papers.title LIKE ?";
			values.add(keyword);
			break;
		case 3:
			sql = "SELECT users.lname, users.fName, papers.title, papers.id " + "FROM users "
					+ "INNER JOIN authorship ON users.id = authorship.usersid "
					+ "INNER JOIN papers ON papers.id = authorship.paperid "
					+ "WHERE users.fName LIKE ? OR users.lname LIKE ? ";
			values.add(keyword);
			values.add(keyword);
			break;
		case 4:
			sql = "SELECT users.lname, users.fName, papers.title, papers.id FROM users "
					+ "INNER JOIN authorship ON users.id = authorship.usersid "
					+ "INNER JOIN papers ON papers.id = authorship.paperid "
					+ "INNER JOIN paper_keywords ON paper_keywords.id = papers.id "
					+ "WHERE paper_keywords.keyword LIKE ?";
			values.add(keyword);
			break;
		default:
			System.out
					.println("Entered unaccepted category number." + "\n0 - Author" + "\n1 - Keyword" + "\n2 - title");
			break;
		}
		try {
			searchResult = myDatabase.getData(sql, values, false);
		} catch (LibraryException e) {
			new LibraryException("Failed to get data.");
		}
		return searchResult;
	}

	/**
	 * Function creates a new paper and will add it to the database, if keywords
	 * are given then it will assign the references to appropriate keywords in
	 * the database.
	 * 
	 * @param content
	 *            arraylist of strings containing information about the paper
	 *            values should be: 0 - title, 1 - abstract, 2 - citation 3 -
	 *            author (Only)
	 * @param keywords
	 * @return
	 */
	public boolean addPaper(ArrayList<String> content, ArrayList<String> keywords) {
		boolean wasSuccess = false;
		// Check if user has the correct privileges
		if (this.currentUser.get_userLevel() > 1) {
			// Create a temporary paper
			Papers tempPaper = new Papers();
			tempPaper.set_title(content.get(0));
			tempPaper.set_abstract(content.get(1));
			tempPaper.set_citation(content.get(2));
			/*
			 * Insert paper to the database and then determine it's ID
			 */
			try {
				wasSuccess = tempPaper.insert();
				// if successfully inserted and was given keywords
				if (keywords.size() > 0 && wasSuccess) {
					tempPaper.generateID(); // get the id from the database
					// and add the keywords to the paper keywords in the
					// database
					wasSuccess = this.addKeyword(tempPaper.get_paperID(), keywords);
				} // end if
					// If the user is a faculty they will be set as the author
				if (this.currentUser.get_userLevel() == 2) {
					wasSuccess = this.addAuthorShip(this.currentUser.get_userID(), tempPaper.get_paperID());
				} else {// If a admin is inserting the paper they should know
						// the author's ID
					wasSuccess = this.addAuthorShip(content.get(3), tempPaper.get_paperID());
				} // end else

			} catch (LibraryException e) {
				new LibraryException("Adding paper Process failed");
				return false;
			}
		}
		return wasSuccess;
	}

	/**
	 * Function adds authorship inside the database.
	 * 
	 * @param authorID
	 *            represents the author's ID
	 * @param paperID
	 *            represents the paper's ID
	 * @return
	 */
	public boolean addAuthorShip(String authorID, String paperID) {
		String sql = "INSERT INTO authorship VALUES (?,?)";
		ArrayList<String> values = new ArrayList<String>();
		values.add(authorID);
		values.add(paperID);
		try {
			myDatabase.setData(sql, values);
			return true;
		} catch (LibraryException e) {
			new LibraryException("Setting authorship failed");
		}
		return false;
	}

	/**
	 * Function will add a new user to the database. The paramter accepts an
	 * arrayList of the new users values it will accept user values with or
	 * without a userID and a userLevel which deteremines its authorization
	 * level.
	 * 
	 * @param userVal
	 *            Attributes pertaining to the user, 0 - first name, 1 -
	 *            lastname, 2 - email, 3 - password, 4(optional) - userID
	 * 
	 * @param userLevel
	 *            - authorization level of new user
	 * @return
	 */
	public boolean addUser(ArrayList<String> userVal, int userLevel) {
		// Check if user has the correct privileges
		if (this.currentUser.get_userLevel() > 2) {
			/*
			 * Assign a temporary user Check if the userID was given or not then
			 * create that user object based on given values, will also hash the
			 * password upon assigning.
			 */
			Users tempUser;
			// User ID given
			if (userVal.size() == 5) {
				tempUser = new Users(userVal.get(4), userVal.get(0), userVal.get(1), userVal.get(2),
						this.hashPassword(userVal.get(3)), userLevel);
				// User ID not given
			} else if (userVal.size() == 4) {
				tempUser = new Users(userVal.get(0), userVal.get(1), userVal.get(2), this.hashPassword(userVal.get(3)),
						userLevel);
			} else {
				// Problem with data insertion from presentation layer
				System.out.println("Improper Arraylist size");
				return false;
			} // end userVal checks

			/*
			 * Another check to ensure proper email and password was entered.
			 */
			if (this.isValidUsername(tempUser.get_email()) && this.isValidPassword(tempUser.get_password())) {
				// Insert the temporary user to the database
				try {
					return tempUser.insert();
				} catch (LibraryException e) {
					new LibraryException("Inserting user to database failed");
				} // end catch
			} // end if
		} // end if
		return false;
	}// end addUser()

	/**
	 * Function will add a keyword to a paper in the database.
	 * 
	 * @param id
	 *            represents the paper id that will be assigned keywords
	 * @param keyword
	 *            holds a collection of keywords that will be assigned to the
	 *            paper.
	 * @return
	 */
	public boolean addKeyword(String id, ArrayList<String> keywords) {
		String sql = "INSERT INTO paper_keywords VALUES ";
		for (int i = 0; i < keywords.size(); i++) {
			if(i < keywords.size()-1)
				sql += " (" + id + ",?),";
			else
				sql += " (" + id + ",?)";	
		}
		try {
			return myDatabase.setData(sql, keywords);
		} catch (LibraryException e) {
			new LibraryException("Keyword insertion failed");
		}
		return false;
	}// end keywords

	public boolean updatePaper(String title, String abstractText, ArrayList<String> keywords) {
		return false;
	}

	/**
	 * Function will update a user's values
	 * 
	 * @param userValues
	 *            Attributes pertaining to the user, 0 - first name, 1 -
	 *            lastname, 2 - email, 3 - password, 4 - userID(admins)
	 * @param UserID
	 * @return
	 */
	public boolean updateUser(ArrayList<String> userVal, int userLevel) {
		if (this.currentUser.get_userLevel() > 2) {
			Users tempUser;
			/*
			 * Assign a temporary User, check if proper arraylist was given
			 * create the user based on those given values
			 */
			if (userVal.size() == 5) {
				// (id, firstname, lastname, email, password, userlevel)
				tempUser = new Users(userVal.get(4), userVal.get(0), userVal.get(1), userVal.get(2),
						this.hashPassword(userVal.get(3)), userLevel);
				// Update that user.
				try {
					return tempUser.update();
				} catch (LibraryException e1) {
					new LibraryException("User Update Failed");
				}
			} else {
				// Problem with data insertion from presentation layer
				System.out.println("Improper Arraylist size");
			} // end else

		} else {// other users
			/*
			 * Update the current user's values and then update the database
			 */
			try {
				this.currentUser.set_fName(userVal.get(0));
				this.currentUser.set_lName(userVal.get(1));
				this.currentUser.set_email(userVal.get(2));
				this.currentUser.set_password(userVal.get(3));
				return this.currentUser.update();
			} catch (LibraryException e) {
				new LibraryException("Failed to update");
			}
		}
		return false;
	}// end updateUser

	/**
	 * Function will delete a user from the database since this function is only
	 * carried out by the system or database administrators, we will be deleting
	 * a value based on the userID value, which the administrators should have
	 * access to.
	 * 
	 * @return
	 */
	public boolean deleteUser(String id) {
		/*
		 * Create an SQL prepared statement add the given id to the arraylist
		 */
		String sql = "DELETE FROM users WHERE id=?";
		ArrayList<String> values = new ArrayList<String>();
		values.add(id);
		// Check if user has appropriate privileges.
		if (this.currentUser.get_userLevel() > 2) {
			// Call the database layer's data modification function
			try {
				myDatabase.setData(sql, values);
				return true;
			} catch (LibraryException e) {
				new LibraryException("Unable to Delete");
			} // end catch
		} // end if
		return false;
	}// end delete

	/**
	 * ***************************NOT FINISHED*************************
	 * 
	 * @return
	 */
	public boolean deletePaper(String id) {
		// Check if the current user is an admin, or the author of the paper.
		if ((this.currentUser.get_userLevel() == 3)
				|| (this.searchTable((currentUser.get_userID() + "," + id), -1).size() == 1)) {
			// create a fake paper object based on the id
			Papers tempPapers = new Papers(id);
			try {
				return tempPapers.delete();
			} catch (LibraryException e) {
				new LibraryException("Failed to delete paper");
			} // end catch
		} // end if
		return false;
	}// end deletePaper

	/**
	 * Function will retrieve all rows from a specified table.
	 * 
	 * @param table
	 *            represents the table to retrieve information from
	 * @param showHeader
	 *            boolean value to show if header is to be displayed or not.
	 * @return the retrieved display table
	 */
	public ArrayList<ArrayList<String>> getTableRows(String table, boolean showHeader) {
		// 2D arraylist to hold the retrieved information
		ArrayList<ArrayList<String>> displayTable = new ArrayList<ArrayList<String>>();
		try {
			displayTable = myDatabase.getAllData(table, showHeader);
		} catch (LibraryException e) {
			new LibraryException("Select all rows failed");
		} // end catch
		return displayTable;
	}// end getTableRows()

	/**
	 * Function returns a user object which references the currently logged
	 * user.
	 * 
	 * @return
	 */
	public Users getCurrentUser() {
		if (this.currentUser == null) {
			System.out.println("No current User");
		}
		return this.currentUser;
	}

	/**
	 * Function closes the connection to the database
	 * 
	 * @return
	 */
	public boolean closeConnection() {
		try {
			// Log the user out if they haven't been already before closing
			// connection
			if (this.currentUser != null) {
				this.logout();
			}
			myDatabase.close();
			return true;
		} catch (LibraryException e) {
			// send message to terminal
			new LibraryException("Closing Connection failed");
		}
		return false;
	}

	/**
	 * Function will check to see if the user name entered was valid or not.
	 * 
	 * @param username
	 * @return
	 */
	private boolean isValidUsername(String username) {
		// REGEX string pattern that the username must follow
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(username);

		// check if username entered matches proper username pattern.
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Function will check to see if the user name entered was valid or not.
	 * 
	 * @param username
	 * @return
	 */
	private boolean isValidPassword(String password) {
		final String PASSWORD_PATTERN = "^(?=.*/d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{5,20}$";

		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		// check if entered password matches the appropriate password pattern.
		if (matcher.matches()) {
			return true;
		}
		return true;
	}

	/**
	 * Function will check to see if the first name entered was valid or not
	 * 
	 * @param firstname
	 * @return
	 */
	private boolean isValidFirstName(String firstname) {
		final String FIRSTNAME_PATTERN = "([A-Z]|[a-z]){2,20}";

		Pattern pattern = Pattern.compile(FIRSTNAME_PATTERN);
		Matcher matcher = pattern.matcher(firstname);
		// Check if the entered first name matches the appropriate firstname
		// password.
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Function will check to see if the lastname entered was valid or not
	 * 
	 * @param firstname
	 * @return
	 */
	private boolean isValidLastName(String lastname) {
		final String LASTNAME_PATTERN = "([A-Z]|[a-z]){2,20}";

		Pattern pattern = Pattern.compile(LASTNAME_PATTERN);
		Matcher matcher = pattern.matcher(lastname);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * hashPassword is used to hash a password before doing anything else to it.
	 * Function will take a string value and then hash it through a [HASHTYPE]
	 * function and return the new string value.
	 * 
	 * @param password
	 * @return
	 */
	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");

			md.update(password.getBytes("UTF-8"));
			BigInteger digest = new BigInteger(1, md.digest());
			password = digest + "";
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return password;
	}

	/*
	 * Instance Variables
	 */
	private String url;
	private String driver;
	private Users currentUser = null;
	private DatabaseManager myDatabase = null;
}// end Class
