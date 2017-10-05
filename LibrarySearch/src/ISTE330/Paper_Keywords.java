package ISTE330;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Paper_Keywords class represents a paper keyword collection
 */
/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 */
public class Paper_Keywords implements DatabaseTable {

	private HashMap<String, String> Map = new HashMap<String, String>();

	public Paper_Keywords(){
		fetch(); //populates the HashMap.
	}
	/**
	 * Adds a string to current collection of keywords
	 * @param keyword
	 * @return
	 * @throws LibraryException
	 */
	private boolean addKeyWord(String keyword){
		ArrayList<String> blank = new ArrayList<String>();
		DatabaseManager databaseManager = DatabaseManager.getInstance(); //datbase manager
		ArrayList<ArrayList<String>> data = null;
		try {
			data = databaseManager.select("SELECT LAST_INSERT_ID()", blank);
		} catch (LibraryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //get next auto_increment value.
		Map.put(data.get(0).get(0), keyword);//put the keyword in the map at the next auto_increment value.
		return true;
	}
	

	public boolean insert(String keyword) {
		if(!Map.containsKey(keyword)){
			addKeyWord(keyword);
		}
		ArrayList<String> params = new ArrayList<String>();
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		params.add(keyword);
		String sql = "INSERT INTO paper_keywords (key) VALUES(?)";
		try {
			databaseManager.setData(sql,params);
		} catch (LibraryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean fetch() {
		DatabaseManager databaseManager = DatabaseManager.getInstance();
		ArrayList<ArrayList<String>> data;
		try {
			data = databaseManager.getAllData("paper_keywords",false);
		for(int i = 0; i < data.size(); i++){
			Map.put(data.get(i).get(0), data.get(i).get(1));
		}} catch (LibraryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete() {
		// TODO Auto-generated method stub
		return false;
	}
	public ArrayList<ArrayList<String>> getAllData(){
		ArrayList<ArrayList<String>> Data = new ArrayList<ArrayList<String>>();
		for(String key: Map.keySet()) {
			Data.get(Integer.parseInt(key)).add(key);//Add Key to ArrayList
			Data.get(Integer.parseInt(key)).add(Map.get(key));//Add Value to ArrayList
		}
		return Data;
	}
	@Override
	public boolean insert() throws LibraryException {
		// TODO Auto-generated method stub
		return false;
	}
}