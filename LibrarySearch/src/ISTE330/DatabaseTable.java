package ISTE330;
/**
 * 
 */

/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 *
 */
public interface DatabaseTable {
	public boolean insert() throws LibraryException; // Insert values to database

	public boolean fetch() throws LibraryException; // Update object's values with database values

	public boolean update() throws LibraryException; // Update database values with object's values

	public boolean delete() throws LibraryException; // Delete object from database
}
