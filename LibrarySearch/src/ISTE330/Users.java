package ISTE330;

import java.util.ArrayList;

/**
 * User class, represents a User object. 
 * Class implements a Database Table.
 */

/**
 * @author Aurko Mallick
 * 
 */
public class Users implements DatabaseTable {

	/**
	 * Default Constructor Creates an "default" object
	 */
	public Users() {
		this.set_userID("000");
		this.set_fName("Public");
		this.set_lName("User");
		this.set_email("abc@efg.edu");
		this.set_password("password");
		this.set_userLevel(0);
	}

	/**
	 * Single parameter Constructor Creates User object and assigns given userID
	 * 
	 * @param _userID
	 */
	public Users(String _userID) {
		this.set_userID(_userID);
		this.set_fName("John");
		this.set_lName("Smith");
		this.set_email("abc@efg.edu");
		this.set_password("password");
		this.set_userLevel(0);
	}
	
	/**
	 * Parameterized Constructor Creates User object and assigns given values to
	 * each attribute of Object excluding the userID
	 * 
	 * @param _userID
	 * @param _fName
	 * @param _lName
	 * @param _email
	 * @param _password
	 * @param _userLevel
	 */
	public Users(String _fName, String _lName, String _email, String _password, int _userLevel) {
		this.set_fName(_fName);
		this.set_lName(_lName);
		this.set_email(_email);
		this.set_password(_password);
		this.set_userLevel(_userLevel);
	}

	/**
	 * Parameterized Constructor Creates User object and assigns given values to
	 * each attribute of Object.
	 * 
	 * @param _userID
	 * @param _fName
	 * @param _lName
	 * @param _email
	 * @param _password
	 * @param _userLevel
	 */
	public Users(String _userID, String _fName, String _lName, String _email, String _password, int _userLevel) {
		this.set_userID(_userID);
		this.set_fName(_fName);
		this.set_lName(_lName);
		this.set_email(_email);
		this.set_password(_password);
		this.set_userLevel(_userLevel);
	}

	/**
	 * @return the _userID
	 */
	public String get_userID() {
		return _userID;
	}

	/**
	 * @param i
	 *            the _userID to set
	 */
	public void set_userID(String i) {
		this._userID = i;
	}

	/**
	 * @return the _fName
	 */
	public String get_fName() {
		return _fName;
	}

	/**
	 * @param _fName
	 *            the _fName to set
	 */
	public void set_fName(String _fName) {
		this._fName = _fName;
	}

	/**
	 * @return the _lName
	 */
	public String get_lName() {
		return _lName;
	}

	/**
	 * @param _lName
	 *            the _lName to set
	 */
	public void set_lName(String _lName) {
		this._lName = _lName;
	}

	/**
	 * @return the _password
	 */
	public String get_password() {
		return _password;
	}

	/**
	 * @param _password
	 *            the _password to set
	 */
	public void set_password(String _password) {
		this._password = _password;
	}

	/**
	 * @return the _email
	 */
	public String get_email() {
		return _email;
	}

	/**
	 * @param _email
	 *            the _email to set
	 */
	public void set_email(String _email) {
		this._email = _email;
	}

	/**
	 * @return the _userLevel
	 */
	public int get_userLevel() {
		return _userLevel;
	}

	/**
	 * Method will validate whether the userLevel meets required range or not
	 * 
	 * @param _userLevel
	 *            the _userLevel to set
	 * @return true if mutator was successful, false otherwise.
	 */
	public boolean set_userLevel(int _userLevel) {
		if (_userLevel >= 0 && _userLevel <= 3) {
			this._userLevel = _userLevel;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Will create an SQL query string in order to insert the object's
	 * attributes into the database as a new record.
	 * 
	 * @return result true if insertion was sucessful, false otherwise.
	 * @throws LibraryException
	 */
	public boolean insert() throws LibraryException {
		boolean result = false;

		String sql = "INSERT INTO " + Users.TABLE + " (fName, lName, email, password, userLevel, id )"
				+ " VALUES(?,?,?,?,?,?)";
		// Ensure we aren't inserting an empty object
		if (!this.isEmpty()) {
			DatabaseManager databaseManager = DatabaseManager.getInstance();
			result = DatabaseManager.setData(sql, this.getMyValues(false));
		} // end if
		return result;
	}// end insert

	/**
	 * Will retrieve all row information stored in database and will update the
	 * object's values based on the values retrieved from the database.
	 * 
	 * @return Returns true if fetch was successful, false otherwise.
	 * @throws LibraryException
	 */
	public boolean fetch() throws LibraryException {
		boolean result = false;
		/*
		 * Get an instance of the DatabaseManager class, create an SQL query to
		 * pass to the Database and create a table to store the retrieved
		 * information
		 */
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		String sql = "SELECT * FROM " + Users.TABLE + " WHERE id=?";
		ArrayList<ArrayList<String>> getData = new ArrayList<ArrayList<String>>();

		// Store the data obtained from query
		getData = databaseManager.getData(sql, this.getMyValues(true), false);

		/*
		 * If one row is returned Fill in object's values with database values
		 */
		if (getData.size() == 1) {
			this.set_fName(getData.get(0).get(1));
			this.set_lName(getData.get(0).get(2));
			this.set_password(getData.get(0).get(3));
			this.set_email(getData.get(0).get(4));
			this.set_userLevel(Integer.parseInt(getData.get(0).get(5)));
			result = true;
		} // end if
		return result;
	}// end fetch()

	/**
	 * Will update the database values based on this object's values.
	 * 
	 * @return result return true if update was successful, false otherwise.
	 * @throws LibraryException
	 */
	public boolean update() throws LibraryException {
		boolean result = false;

		String sql = "UPDATE " + Users.TABLE + " SET " + "fName=?, " + "lName=?, "  + "email=?, " + "password=?, "
				+ "userlevel=? " + "WHERE id=?";

		// Ensure object isn't empty
		if (!this.isEmpty()) {
			DatabaseManager databaseManager = DatabaseManager.getInstance();
			result = DatabaseManager.setData(sql, this.getMyValues(false));
		} // end if
		return result;
	}// end update

	/**
	 * Will remove a row from the database based on the User's ID
	 * 
	 * @return result Return true if delete was successful, false otherwise.
	 * @throws LibraryException
	 */
	public boolean delete() throws LibraryException {
		boolean result = false;
		/*// Only Faculty and Admins can delete
		if (this.get_userLevel() == 0 || this.get_userLevel() == 1) {
			return result;
		} // end if*/
		String sql = "DELETE FROM " + Users.TABLE + " WHERE id=?";
		// Ensure Object isn't empty before deleting
		if (!this.isEmpty()) {
			/*
			 * Get an instance of the DatabaseManager Class and pass the sql to
			 * the DatabaseManager to class in order to execute the query.
			 */
			DatabaseManager databaseManager = DatabaseManager.getInstance();
			result = DatabaseManager.setData(sql, this.getMyValues(true));
			System.out.println("test");
		} // end if
		return result;
	}// end delete()

	/**
	 * toString function that will returns a String description of the object's
	 * values
	 * 
	 * @param displayPass
	 *            if true, display password
	 * @return userString description of this object.
	 */
	public String toString(boolean displayPass) {
		String userString = "";
		userString += "User_ID: " + this.get_userID() + "\nUser First_Name: " + this.get_fName() + "\nUser Last_Name: "
				+ this.get_lName() + "\nUser email: " + this.get_email() + "\nUser level: "
				+ this.userAuthLevel(this.get_userLevel());
		if (displayPass) {
			userString += "\nUser Password: " + this.get_password();
		}
		return userString;
	}// end toString

	/**
	 * isEmpty to check if User object is empty or not. As it stands, the object
	 * is empty if any attribute in the object contains a null or empty string
	 * value. User level checks to ensure it proper user levels are entered.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (!(this.get_fName().equals(null) || this.get_fName().equals(""))) {
			return false;
		} // end First name check
		if (!(this.get_lName().equals(null) || this.get_lName().equals(""))) {
			return false;
		} // end Last name check
		if (!(this.get_email().equals(null) || this.get_email().equals(""))) {
			return false;
		} // end email check
		if (!(this.get_password().equals(null) || this.get_password().equals(""))) {
			return false;
		} // end password check
		if (!(this.get_userLevel() < 0)) {
			return false;
		} // end userlevel check
		return true;
	}// end isEmpty()

	/**
	 * Equals method checks to see if two objects are the same the parameter
	 * will take an object to compare the object against its values.
	 * 
	 * @param obj
	 * @return true if they are equal, false otherwise.
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} // end object comparison
		if (obj == null) {
			return false;
		} // end null object check
		if (getClass() != obj.getClass()) {
			return false;
		} // end class check

		Users object = (Users) obj;
		if ((!this.get_userID().equals(object.get_userID()))) {
			return false;
		} // end userID check
		if (!this.get_fName().equals(object.get_fName())) {
			return false;
		} // end firstName check
		if (!(this.get_lName().equals(object.get_lName()))) {
			return false;
		}
		if (!this.get_email().equals(object.get_email())) {
			return false;
		} // end email check
		if (!this.get_password().equals(object.get_password())) {
			return false;
		} // end password check
		if (!(this.get_userLevel() == object.get_userLevel())) {
			return false;
		} // end user level check
		return true;
	}// end equals()

	/**
	 * getMyValues method inserts all the values of this object's attributes
	 * into an arrayList
	 * 
	 * @param idOnly
	 *            parameter determines whether the arrayList should only contain
	 *            userID or not
	 * @return myVal returns the created ArrayList of values
	 */
	private ArrayList<String> getMyValues(boolean idOnly) {
		ArrayList<String> myVal = new ArrayList<String>();
		if (!idOnly) {
			myVal.add(this.get_fName());
			myVal.add(this.get_lName());
			myVal.add(this.get_email());
			myVal.add(this.get_password());
			myVal.add(this.get_userLevel() + "");
		}
		myVal.add(this.get_userID());
		return myVal;
	}// end myValues

	/**
	 * Function determines the String value of the User's Authentication Level.
	 * 
	 * @param ul
	 *            integer value representing the User's authentication level.
	 * @return authenticationLevel String value containing the name of user's
	 *         privelage level.
	 */
	private String userAuthLevel(int ul) {
		String authenticationLevel = "";
		switch (ul) {
		case 0:
			authenticationLevel = "Public User";
			break;
		case 1:
			authenticationLevel = "Student";
			break;
		case 2:
			authenticationLevel = "Faculty";
			break;
		case 3:
			authenticationLevel = "System Administrator";
			break;
		default:// Should not be reached, must be error somewhere to reach this
				// far
			authenticationLevel = "OTHER :" + ul;
			break;
		}// end switch case
		return authenticationLevel;
	}// end userAuthLevel

	/*
	 * Instance Variables
	 */
	private String _userID;
	private String _fName;
	private String _lName;
	private String _email;
	private String _password;
	/*
	 * User level - -1: Empty/NotSet
	 * User level - 0: Public 
	 * User level - 1: Student 
	 * User level - 2: Faculty 
	 * User level - 3: Admin
	 */
	private int _userLevel = -1;
	/*
	 * Database table to reference.
	 */
	private final static String TABLE = "users";
}
