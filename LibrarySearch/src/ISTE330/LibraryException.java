package ISTE330;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * LibraryExeption class is a custom exception
 * class that will handle and keep a log
 * of Exceptions encountered by the program.s
 */

/**
 * @author Willy Choi
 * @author Austin Levesque
 * @author Aurko Mallick
 * @author Nick Taber
 *
 */
@SuppressWarnings("serial")
public class LibraryException extends Exception {
	/**
	 * Default Constructor sends a default message back
	 */
	public LibraryException() {
		super(DEFAULT_MESSAGE);
	}// end Default Constructor
	
	/**
	 * Single paramater LibraryException Constructor takes
	 * a string message and prints it on the terminal. 
	 * This function is mainly used by other classes 
	 * if they want to display a custom error message
	 * to the terminal.
	 * @param message
	 */
	public LibraryException(String message){
		System.out.println(message);
	}

	/**
	 * Single Parameter LibraryException Constructor takes a given exception and
	 * sends the exception through the log function to keep a record of the log
	 * 
	 * @param e
	 *            Exception to handle
	 */
	public LibraryException(Exception e) {
		super(DEFAULT_MESSAGE);
		this.e = e;
		// this.log() // write to log file.
	}// End SingleParameter Constructor

	/**
	 * Paramterized Constructor takes a given exception, followed by a 2D List
	 * of Strings and sends the exception to the log function to record the
	 * exception as well as the contents in the collection.
	 * 
	 * @param e
	 *            Exception to handle.
	 * @param list
	 *            2D collection of string values/queries.
	 */
	public LibraryException(Exception e, List<ArrayList<String>> list) {
		super(DEFAULT_MESSAGE);
		this.e = e;
		this.list = list;
		// this.log(); // write to logfile
	}// end Parameterized COnstructor

	/**
	 * Log function will
	 * @return
	 */
	private boolean log() {
		try {
			// Import the System's date and time
			String currTime = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
			// Log file for output
			File logFile = new File(logFileDir);

			// create writer and append if the file already exists
			this.writer = new BufferedWriter(new FileWriter(logFile, true));

			// create new entry into file and add date stamp
			this.writer.write(this.newLine + "New log entry during: " + currTime + this.newLine);
			// If Exception was passed into constructor
			if (!this.e.equals(null)) {
				// Record the exception type
				this.writer.write("Exception Type: " + this.e.toString() + this.newLine);

				// Store printStackTrace
				StringWriter error = new StringWriter();
				this.e.printStackTrace(new PrintWriter(error));
				String stackTrace = error.toString();
				this.writer.write(stackTrace);
			}

			// If 2D list was passed into contructor add it to the file
			if (!this.list.isEmpty()) {
				// Outter Collection
				for (ArrayList<String> array : list) {
					// Inner Collection
					for (String line : array) {
						this.writer.write(line + this.newLine);
					} // End Inner
						// Beging new Line
					this.writer.write(this.newLine + this.newLine);
				} // end outer
			} // end If

			// If message constructor was called
			if (!this.message.isEmpty()) {
				this.writer.write(this.message + this.newLine);
			} // end if

		} catch (Exception e) {
			System.out.println("Failed to log message.");
			return false;
		} finally {
			// close BufferedWriter
			try {

				writer.close();
			} catch (Exception e) {
				// Can't really do anything about this.
			} // end Catch
		} // End finally
		return true;
	}// end Log()

	/*
	 * Instance Variables
	 */
	private List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	private Exception e;
	public final static String DEFAULT_MESSAGE = "Operation could not be completed.";
	private BufferedWriter writer = null;
	private String message = "";
	private String logFileDir = "LibraryLogFile.txt";// Director/Location of the log file
	/*
	 * Line seperator value across all systems.
	 */
	private static String newLine = System.getProperty("line.separator");
}// End class