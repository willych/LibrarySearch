package ISTE330;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
/**
 * Database Manager class interacts with the database.
 * The class is responsible for handling all interactions
 * with the Connection class, sending SQL queries and
 * returning the query results to the appropriate classes.
 */

/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 *
 */
public final class DatabaseManager {
	/**
	 * Database Manager Constructor class sets the appropriate connection
	 * values;
	 * 
	 * @param _username
	 *            Username for database
	 * @param _password
	 *            Password for database
	 * @param _url
	 *            URL to access database
	 * @param _driver
	 *            SQL driver to access database
	 */
	public DatabaseManager(String _username, String _password, String _url, String _driver) {
		this._username = _username;
		this._password = _password;
		this._url = _url;
		this._driver = _driver;
	}// end Constructor

	public static DatabaseManager getInstance() {
		if (instance == null) {
			synchronized (DatabaseManager.class) {
				if (instance == null) {
					instance = new DatabaseManager();
				} // End if
			} // End Synchronized
		} // End if
		return instance;
	}// end getInstance()

	/**
	 * Method returns the current state of autoCommit
	 * 
	 * @return
	 */
	public boolean getAutoCommitStatus() {
		return this._autoCommitStatus;
	}

	/**
	 * Starts a Connection to the database
	 * 
	 * @return boolean value represents whether connection was successful or not
	 * @throws LibraryException
	 */
	public boolean start() throws LibraryException {
		boolean wasSuccessful = false;
		try {
			Class.forName(this._driver);
			DatabaseManager._conn = DriverManager.getConnection(this._url, this._username, this._password);
			wasSuccessful = true;
		} catch (ClassNotFoundException e) {
			throw new LibraryException(e);
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // End catch
		return wasSuccessful;
	}// End start()

	/**
	 * Closes a Connection to the database
	 * 
	 * @return boolean value representing whether close was successful or not
	 * @throws LibraryException
	 */
	public boolean close() throws LibraryException {
		boolean wasSuccessful = false;
		try {
			if (DatabaseManager._conn != null) { // make sure connection already
													// exists
				DatabaseManager._conn.close(); // if it does then close it
				wasSuccessful = true;
			} // end if
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // end Catch
		return wasSuccessful;
	}// end close()

	/**
	 * Function begins a SQL transaction
	 * 
	 * @return wasSuccessful true if transaction was started, false otherwise.
	 * @throws LibraryException
	 */
	public boolean startTrans() throws LibraryException {
		boolean wasSuccessful = false;
		// Make sure system isn't already in a transaction.
		if (this.getAutoCommitStatus()) {
			try {
				// set Auto commit false to begin transaction.
				DatabaseManager._conn.setAutoCommit(false);
				this._autoCommitStatus = false;
				wasSuccessful = true;
			} catch (SQLException e) {
				throw new LibraryException(e);
			} // end catch
		}
		return wasSuccessful;
	}// end startTrans()

	/**
	 * Function ends a transaction Method will commit the transaction to the
	 * database and set the autoCommit status to true
	 * 
	 * @return wasSuccessful true if transaction was closed, false otherwise.
	 * @throws LibraryException
	 */
	public boolean endTrans() throws LibraryException {
		boolean wasSuccessful = false;
		// Make sure system is in a transaction
		if (!this.getAutoCommitStatus()) {
			try {
				DatabaseManager._conn.commit();
				DatabaseManager._conn.setAutoCommit(true);
				this._autoCommitStatus = true;
				wasSuccessful = true;
			} catch (SQLException e) {
				throw new LibraryException(e);
			} // end catch
		} // end if
		return wasSuccessful;
	}// end endTrans()

	/**
	 * Function commits a rollback on a transaction
	 * 
	 * @return wasSuccessful returns true if rollback was successful, false
	 *         otherwise.
	 * @throws LibraryException
	 */
	public boolean rollback() throws LibraryException {
		boolean wasSuccessful = false;
		try {
			// Rollback transaction
			DatabaseManager._conn.rollback();
			wasSuccessful = true;
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // end catch
		return wasSuccessful;
	}// end rollback()

	/**
	 * Describes the structure of the table, listing it's attribute names and
	 * value types.
	 * 
	 * @param table
	 *            The database table to view
	 * @return 2D Collection of values pertaining to the given table.
	 * @throws LibraryException
	 */
	public ArrayList<ArrayList<String>> describeTable(String table) throws LibraryException {
		PreparedStatement statement;
		// 2D arrayList to hold the sql result
		ArrayList<ArrayList<String>> queryResult = new ArrayList<ArrayList<String>>();
		String sql = "SELECT * FROM " + table;
		try {
			statement = DatabaseManager._conn.prepareStatement(sql);
			ResultSet result = statement.executeQuery(sql);
			ResultSetMetaData metadata = result.getMetaData();
			int numcols = metadata.getColumnCount();

			// Create Header
			queryResult.add(new ArrayList<String>());
			queryResult.get(0).add("Field Name");
			queryResult.get(0).add("Column Name");
			// Fill in fieldname - column type rows
			for (int i = 1; i <= numcols; i++) {
				queryResult.add(new ArrayList<String>());
				queryResult.get(i).add(metadata.getColumnName(i));
				queryResult.get(i).add(this.sqlDataType(metadata.getColumnType(i)));
			} // End for
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // End catch
		return queryResult;
	}// End describeTable()

	/**
	 * Modifies data inside the database is currently used by the program to
	 * handle all SQL queries pertaining to Inserting, Updating, and Deleting.
	 * 
	 * @param sql
	 *            SQL string to pass to database
	 * @param values
	 *            an Arraylist of Values to modify upon
	 * @return return true if rows in database were successfully changed.
	 * @throws LibraryException
	 */
	public static boolean setData(String sql, ArrayList<String> values) throws LibraryException {
		int rowsChanged = 0;
		PreparedStatement statement = prepare(sql, values);
		// In case statement is null
		if (statement != null) {
			rowsChanged = executeStatement(statement);
		}
		// see if any rows were modified/affected
		if (rowsChanged > 0) {
			return true;
		} // end if
		return false;
	}// end setData()
	
	public static Users searchUser(String userName, String password) throws LibraryException{
		String sql = "SELECT * FROM users WHERE email ="+userName+", password = " +password;
		ArrayList<String> myArr = new ArrayList<String>();
		myArr.add(userName);
		myArr.add(password);
		ArrayList<ArrayList<String>> userValues = select(sql,myArr);
		if(userValues.size() == 1){
			/*return new Users(userValues.get(0).get(0),userValues.get(0).get(1),userValues.get(0).get(2),userValues.get(0).get(3),userValues.get(0).get(4),
					userValues.get(0).get(5),Integer.parseInt(userValues.get(0).get(6)))*/
		}
		return null;
	}

	/**
	 * getAllData function returns all the data inside a single table in a
	 * database.
	 * 
	 * @param table
	 *            Parameter represents which table to get information from
	 * @param showHeader
	 *            boolean value to represent whether to display the table header
	 *            or not
	 * @return Return an 2D ArrayList of all the values in the table.
	 * @throws LibraryException
	 */
	public ArrayList<ArrayList<String>> getAllData(String table, boolean showHeader) throws LibraryException {
		/*
		 * Create an SQL SELECT statement based on the given table and send it
		 * to the select function to execute and store results from the query.
		 */
		String sql = "SELECT * FROM " + table;
		ArrayList<ArrayList<String>> data = DatabaseManager.select(sql, null);
		// Check whether to display the header information in the result table
		if (showHeader) {
			/*
			 * prepare the previous sql statement and pass the result to the
			 * getHeader function to get the appropriate header values. Then
			 * combine the header and data arrays into one table and return the
			 * result.
			 */
			PreparedStatement statement = prepare(sql, null);
			ArrayList<String> header = getHeader(statement);
			return this.combineArray(header, data);
		}
		// return data without the header information
		return data;
	}// end getAllData()

	public ArrayList<ArrayList<String>> getData(String sql, ArrayList<String> values, boolean showHeader)
			throws LibraryException {
		/*
		 * Pass the given SQL string to the select function to execute and store
		 * the results from the query.
		 */
		ArrayList<ArrayList<String>> data = DatabaseManager.select(sql, values);
		// Check whether to display the header information in the result table;
		if (showHeader == true) {
			/*
			 * Prepare the given SQL statement and pass the result to the
			 * getHeader function to get the appropriate header values. Then
			 * combine the header and data arrays into one table and return the
			 * result.
			 */
			PreparedStatement statement = prepare(sql, null);
			ArrayList<String> header = getHeader(statement);
			return this.combineArray(header, data);
		}
		// return data without the header information
		return data;
	}// end getAllData()

	/**
	 * printResult() method prints the information inside the array
	 * 
	 * @param queryResult
	 * @return String version format of arraylist
	 */
	public String printResult(ArrayList<ArrayList<String>> queryResult, boolean showHeader) {
		String result = "";
		int column = 0;
		int row = 0;
		//
		for (row = 0; row < queryResult.size(); row++) {
			for (column = 0; column < queryResult.get(row).size(); column++) {
				result += "|" + String.format("%25s", (queryResult.get(row).get(column)));
			} // end inner for loop
			if (row == 0 && showHeader == true) {
				result += "\n";
			} // end if
			result += "|\n";
		} // end outer for loop
			// Display the header
		if (showHeader == true) {
			result += (row - 1) + " rows in set.";
		} else {
			result += (row) + " rows in set.";
		} // end else

		return result;
	}// End toString()

	/**
	 * Takes an SQL string and an ArrayList of values to add to sql statement
	 * 
	 * @param sql
	 *            Sql statement to prepare
	 * @param values
	 *            column values to set values too
	 * @return statement the prepared statement
	 * @throws LibraryException
	 */
	private static PreparedStatement prepare(String sql, ArrayList<String> values) throws LibraryException {
		PreparedStatement statement = null;
		try {
			statement = _conn.prepareStatement(sql);
			// Loop through the arraylist and fill in values into the string
			if (values != null) {
				for (int i = 0; i < values.size(); i++) {
					statement.setString((i + 1), values.get(i));
				} // end for
			} // end if
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // end catch
		return statement;
	}// end prepare()

	/**
	 * Function executes a prepared statement
	 * 
	 * @param statement
	 *            SQL query to execute.
	 * @return Integer value representing the number of rows affected/changed
	 * @throws LibraryException
	 */
	private static int executeStatement(PreparedStatement statement) throws LibraryException {
		// number of rows changed
		int rowsChanged = 0;

		try {
			rowsChanged = statement.executeUpdate();
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // end catch
		return rowsChanged;
	}// end executeStatement()

	/**
	 * Function executes an SQL SELECT statement and returns the result inside a
	 * 2D ArrayList
	 * 
	 * @param sql
	 *            SQL SELECT String to execute
	 * @param values
	 *            ArrayList of Strings containing values relevant to the SQL
	 *            Query
	 * @return 2D ArrayList containing the result of the query.
	 * @throws LibraryException
	 */
	static ArrayList<ArrayList<String>> select(String sql, ArrayList<String> values) throws LibraryException {
		PreparedStatement statement;
		ArrayList<ArrayList<String>> queryResult = new ArrayList<ArrayList<String>>();

		try {
			// prepare the SQL statement
			statement = prepare(sql, values);
			/*
			 * Excecute the SQL Query and retrieve the metadata from the result
			 * inorder to determine the number of columns in table.
			 */
			ResultSet result = statement.executeQuery();
			ResultSetMetaData metadata = result.getMetaData();
			int numCols = metadata.getColumnCount();
			int row = 0;

			/*
			 * Add the data from the SQL Query Result into the Table.
			 */
			while (result.next()) {
				// Create a new row in the table
				queryResult.add(new ArrayList<String>());
				int count = 1;
				// Populate the rows
				while (count <= numCols) {
					/*
					 * If no value was found in the current cell of the
					 * database, Insert a String value of "NULL" as a
					 * placeholder.
					 */
					if (result.getObject(count) == null) {
						queryResult.get(row).add("NULL");
					} // end if
						// Add value of current cell to table
					queryResult.get(row).add(result.getString(count));
					count++;// Go to next column in row
				} // End while loop for column
				row++;
			} // end while loop for row
		} catch (SQLException e) {
			throw new LibraryException(e);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new LibraryException(e);
		} // end catch
		return queryResult;
	}// End select()

	/**
	 * Function creates a savepoint
	 * 
	 * @param savePointReference
	 *            string value to reference the savepoint
	 * @return savepoint
	 * @throws DLException
	 */
	@SuppressWarnings("unused")
	private Savepoint createSavePoint(String savePointReference) throws LibraryException {
		Savepoint savepoint = null;

		try {
			savepoint = DatabaseManager._conn.setSavepoint(savePointReference);
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // end catch

		return savepoint;
	}// end createSavePoint()

	private static ArrayList<String> getHeader(PreparedStatement statement) throws LibraryException {
		ArrayList<String> header = new ArrayList<String>();
		ResultSet result = null;

		try {
			result = statement.executeQuery();
			ResultSetMetaData metadata = result.getMetaData();
			int numCols = metadata.getColumnCount();
			// Loop to add column names
			for (int i = 1; i <= numCols; i++) {
				header.add(metadata.getColumnName(i));
			} // end for
		} catch (SQLException e) {
			throw new LibraryException(e);
		} // end catch
		return header;
	}// end getHeader()

	/**
	 * Combines two arraylists into one array list.
	 * 
	 * @param header
	 *            represents the array list storing the attribute names.
	 * @param body
	 *            represents the array storing the data values.
	 * @return combined array containing both attribute names and tuple values.
	 */
	private ArrayList<ArrayList<String>> combineArray(ArrayList<String> header, ArrayList<ArrayList<String>> body) {
		ArrayList<ArrayList<String>> finalArray = new ArrayList<ArrayList<String>>();
		;

		// Add header to Array
		finalArray.add(new ArrayList<String>());
		for (int col = 0; col < header.size(); col++) {
			finalArray.get(0).add(header.get(col));
		}

		// Add body to Array
		for (int row = 1; row <= body.size(); row++) {
			finalArray.add(new ArrayList<String>());
			for (int col = 0; col < body.get(row - 1).size(); col++) {
				finalArray.get(row).add(body.get(row - 1).get(col));
			} // end inner
		} // end outer
		return finalArray;
	}// end combineArray

	/**
	 * converts the sql integer data type to it's corresponding string value.
	 * 
	 * @param value
	 *            int value of data type
	 * @return data type name
	 */
	private String sqlDataType(int value) {
		String type = "";
		switch (value) {
		case -7:
			type = "BIT";
			break;
		case -6:
			type = "TINYINT";
			break;
		case -5:
			type = "BIGINT";
			break;
		case -4:
			type = "LONGVARBINARY";
			break;
		case -3:
			type = "VARBINARY";
			break;
		case -2:
			type = "BINARY";
			break;
		case -1:
			type = "LONGVARCHAR";
			break;
		case -0:
			type = "NULL";
			break;
		case 1:
			type = "CHAR";
			break;
		case 2:
			type = "NUMERIC";
			break;
		case 3:
			type = "DECIMAL";
			break;
		case 4:
			type = "INTEGER";
			break;
		case 5:
			type = "SMALLINT";
			break;
		case 6:
			type = "FLOAT";
			break;
		case 7:
			type = "REAL";
			break;
		case 8:
			type = "DOUBLE";
			break;
		case 12:
			type = "VARCHAR";
			break;
		case 39:
			type = "C0pYp4574";
			break;
		case 42:
			type = "HAL9000";
			break;
		case 91:
			type = "DATE";
			break;
		case 92:
			type = "TIME";
			break;
		case 93:
			type = "TIMESTAMP";
			break;
		case 1027:
			type = "HOW DID YOU EVEN GET HERE!?";
			break;
		default:
			type = ("OTHER type: " + value);
			break;
		}
		return type;
	}// End sqlDataType

	/*
	 * Instance Variables
	 */
	private static Connection _conn = null;
	private String _username = "";
	private String _password = "";
	private String _url = "";
	private String _driver = "";
	private boolean _autoCommitStatus = true;
	private static volatile DatabaseManager instance;

	/*
	 * Instance of DatabaseManager Class
	 */
	private DatabaseManager() {
	}
}
