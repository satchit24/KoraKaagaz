package processing.handlers;

import networking.INotificationHandler;
import processing.ClientBoardState;
import processing.utility.*;

<<<<<<< HEAD
public class PortHandler implements INotificationHandler {
=======
/**
 * This class handles Port number of the Board Server received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class PortHandler implements INotificationHandler{
>>>>>>> ca2577c6c9ae0841541caed5b84aecd066607141
	
		public void onMessageReceived(String message) {
			Port portNumber = new Port(Integer.parseInt(message));
			ClientBoardState.portNumber = portNumber;
		}
}
