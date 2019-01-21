
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 
 * @author daniel
 *
 */
public class logServer {

	/**********************************************************/

	private Logger _logger;
	private FileHandler _fh;

	/**********************************************************/

	/**
	 * constructor  logserver
	 * @param name - name of file
	 */
	public logServer(String name) {
		_logger = Logger.getLogger("MyLog");
		try {

			// This block configure the logger with handler and formatter
			_fh = new FileHandler(name + ".log");
			_logger.addHandler(_fh);
			SimpleFormatter formatter = new SimpleFormatter();
			_fh.setFormatter(formatter);

			// the following statement is used to log any messages
			_logger.info("My first log");

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**********************************************************/

	/**
	 * 
	 * @param str - data to write in log
	 */
	public void writeLog(String str) {
		_logger.info(str);
	}

	/**********************************************************/

}
