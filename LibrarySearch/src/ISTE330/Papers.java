package ISTE330;
import java.util.ArrayList;
/**
 * Paper class represents a Paper object.
 * Class implements a Database table.
 */

/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 *
 */
public class Papers implements DatabaseTable {

	/**
	 * Default Constructor Creates a "Default" object
	 */
	public Papers() {
		this.set_title("Basket Weaving 101");
		this.set_abstract("Basket Weaving, the lucrative college major you need to invest in!");
		this.set_citation("Baskets were Woven");
	}

	/**
	 * Single Parameter Constructor Creates Paper object and assigns a given
	 * paperID
	 * 
	 * @param _paperID
	 */
	public Papers(String _paperID) {
		this.set_paperID(_paperID);
		this.set_title("Basket Weaving 101");
		this.set_abstract("Basket Weaving, the lucrative college major you need to invest in!");
		this.set_citation("Baskets were Woven");
	}

	/**
	 * Parameterized constructor Creates a Paper object and assigns given values
	 * to each attribute of Object.
	 * 
	 * @param _paperID
	 * @param _title
	 * @param _abstract
	 * @param _citation
	 */
	public Papers(String _paperID, String _title, String _abstract, String _citation) {
		this.set_paperID(_paperID);
		this.set_title(_title);
		this.set_abstract(_abstract);
		this.set_citation(_citation);
	}

	/**
	 * @return the _paperID
	 */
	public String get_paperID() {
		return _paperID;
	}

	/**
	 * @param _paperID
	 *            the _paperID to set
	 */
	public void set_paperID(String _paperID) {
		this._paperID = _paperID;
	}

	/**
	 * @return the _title
	 */
	public String get_title() {
		return _title;
	}

	/**
	 * @param _title
	 *            the _title to set
	 */
	public void set_title(String _title) {
		this._title = _title;
	}

	/**
	 * @return the _abstract
	 */
	public String get_abstract() {
		return _abstract;
	}

	/**
	 * @param _abstract
	 *            the _abstract to set
	 */
	public void set_abstract(String _abstract) {
		this._abstract = _abstract;
	}

	/**
	 * @return the _citation
	 */
	public String get_citation() {
		return _citation;
	}

	/**
	 * @param _citation
	 *            the _citation to set
	 */
	public void set_citation(String _citation) {
		this._citation = _citation;
	}

	/**
	 * Will create an SQL query string in order to insert the object's
	 * attributes into the database as a new record.
	 * ***************************NOT FINISHED*************************
	 * 
	 * @return true if insertion was sucessful, false otherwise.
	 * @throws LibraryException 
	 */
	@Override
	public boolean insert() throws LibraryException {
		boolean result = false;

		String sql = "INSERT INTO " + Papers.TABLE + " (title, abstract, citation, id)"
				+ " VALUES(?,?,?,?)";
		// Ensure we aren't inserting an empty object
		if (!this.isEmpty()) {
			DatabaseManager databaseManager = DatabaseManager.getInstance();
			result = DatabaseManager.setData(sql, this.getMyValues(false));
		} // end if
		return result;
	}//end insert

	@Override
	public boolean fetch() throws LibraryException {
		boolean result = false;
		/*
		 * Get an instance of the DatabaseManager class, create an SQL query to
		 * pass to the Database and create a table to store the retrieved
		 * information
		 */
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		String sql = "SELECT id, title, abstract, citation FROM " + Papers.TABLE + " WHERE id=?";
		ArrayList<ArrayList<String>> getData = new ArrayList<ArrayList<String>>();

		// Store the data obtained from query
		getData = databaseManager.getData(sql, this.getMyValues(true), false);

		/*
		 * If one row is returned Fill in object's values with database values
		 */
		if (getData.size() == 1) {
			this.set_paperID(getData.get(0).get(0));
			this.set_title(getData.get(0).get(1));
			this.set_abstract(getData.get(0).get(2));
			this.set_citation(getData.get(0).get(3));
			result = true;
		} // end if
		return result;
	}// end fetch()

	/**
	 * Function determines what the paper's ID is from the database based on its other values.
	 * @throws LibraryException
	 */
	public void generateID() throws LibraryException{
		/*
		 * Get an instance of the DatabaseManager class, create an SQL query to
		 * pass to the Database and create a table to store the retrieved
		 * information
		 */
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		String sql = "SELECT id FROM " + Papers.TABLE 
				+ " WHERE title=? && abstract = ?";
		ArrayList<ArrayList<String>> getData = new ArrayList<ArrayList<String>>();
		// Store the data obtained from query
		getData = databaseManager.getData(sql, this.getMyValues(), false);
		/*
		 * If one row is returned Fill in object's values with database values
		 */
		if (getData.size() == 1) {
			this.set_paperID(getData.get(0).get(0));
		}
	}
	/**
	 * Will update the database values based on this object's values.
	 * ***************************NOT FINISHED*************************
	 * 
	 * @return return true if update was successful, false otherwise.
	 * @throws LibraryException 
	 */
	@Override
	public boolean update() throws LibraryException {
		boolean result = false;
		String sql = "UPDATE " + Papers.TABLE + " SET " + "title=?, " + "abstract=?, " + "citation=?, " + "WHERE id=?";

		// Ensure object isn't empty
		if (!this.isEmpty()) {
			DatabaseManager databaseManager = DatabaseManager.getInstance();
			result = DatabaseManager.setData(sql, this.getMyValues(false));
		} // end if
		return result;
	}// end update

	/**
	 * Will remove from the database any data corresponding to object's id
	 * 
	 * @return Return true if delete was successful, false otherwise.
	 * @throws LibraryException 
	 */
	@Override
	public boolean delete() throws LibraryException {
		boolean result = false;
		String sql = "DELETE FROM " + Papers.TABLE + " WHERE id=?";
		// Ensure Object isn't empty before deleting

		if (!this.isEmpty()) {
			/*
			 * Get an instance of the DatabaseManager Class and pass the sql to
			 * the DatabaseManager to class in order to execute the query.
			 */
			
			DatabaseManager databaseManager = DatabaseManager.getInstance();
			result = DatabaseManager.setData(sql, this.getMyValues(true));
			
			
		} // end if
		return result;
	}

	/**
	 * toString function that will return a String description of the object's
	 * values
	 * 
	 * @param displayPaperID
	 *            if true, display password
	 * @return paperString description of this object.
	 */
	public String toString(boolean displayPaperID) {
		String paperString = "";
		if (displayPaperID) {
			paperString += "PaperID: " + this.get_paperID() + "\n";
		}
		paperString += "Title: " + this.get_title() + "\nAbstract : " + this.get_abstract() + "\nCitation :"
				+ this.get_citation();
		return paperString;
	}// end toString

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Papers [_paperID=" + _paperID + ", _title=" + _title + ", _abstract=" + _abstract + ", _citation="
				+ _citation + "]";
	}

	/**
	 * isEmpty to check if Paper object is empty or not. As it stands the Object
	 * is empty if any attributes in the object contains a null or empty string
	 * value.
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (!(this.get_title().equals(null) || this.get_title().equals(""))) {
			return false;
		} // end title check
		if (!(this.get_abstract().equals(null) || this.get_abstract().equals(""))) {
			return false;
		} // end abstract check
		if (!(this.get_citation().equals(null) || this.get_citation().equals(""))) {
			return false;
		} // end citation check
		return true;
	}// end isEmpty()

	/**
	 * Equals method checks to see if two objects are the same. The parameter
	 * will take an object to compare the object against it's values.
	 * 
	 * @param obj
	 *            represents object to compare
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

		Papers object = (Papers) obj;
		if (!this.get_paperID().equals(object.get_paperID())) {
			return false;
		} // end paperID check
		if (!this.get_title().equals(object.get_title())) {
			return false;
		} // end title check
		if (!this.get_abstract().equals(object.get_abstract())) {
			return false;
		} // end abstract check
		if (!this.get_citation().equals(object.get_citation())) {
			return false;
		} // end citation check
		return true;
	}// end equals()

	/**
	 * getMyValues method inserts all the values of this object's attributes into
	 * an ArrayList
	 * 
	 * @param idOnly
	 *            idOnly parameter determines whether the ArrayList should only
	 *            contain userID or not
	 * @return myVal returns the created ArrayList of values
	 */
	private ArrayList<String> getMyValues(boolean idOnly) {
		ArrayList<String> myVal = new ArrayList<String>();
		if (!idOnly) {
			myVal.add(this.get_title());
			myVal.add(this.get_abstract());
			myVal.add(this.get_citation());
		}
		myVal.add(this.get_paperID());
		return myVal;
	}// end myValues

	/**
	 * getMyValues method inserts all the values of this object's attributes into
	 * an ArrayList
	 * 
	 * @return myVal returns the created ArrayList of values
	 */
	private ArrayList<String> getMyValues() {
		ArrayList<String> myVal = new ArrayList<String>();
	
		myVal.add(this.get_title());
		myVal.add(this.get_abstract());
	
		return myVal;
	}// end myValues
	
	/*
	 * Instance Variables
	 */
	private String _paperID;
	private String _title = "";
	private String _abstract = "";
	private String _citation = "";
	/*
	 * Database table to reference
	 */
	private final static String TABLE = "papers";

}
