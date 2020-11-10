package processing.handlers;

import java.io.IOException;
import java.util.ArrayList;

import infrastructure.validation.logger.*;
import processing.threading.*;
import processing.utility.*;
import processing.*;
import processing.boardobject.*;
import networking.INotificationHandler;

/**
 * This class handles Board Object received from the server
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 *
 */

public class ObjectHandler implements INotificationHandler{
	
	private static ILogger logger = LoggerFactory.getLoggerInstance();
	
	public static void handleBoardObject(String message) {
		
		BoardObject boardObject;
		
		try {
				boardObject = (BoardObject)Serialize.deSerialize(message);
				
				IBoardObjectOperation iBoardOp = boardObject.getOperation();
				BoardObjectOperationType boardOp = iBoardOp.getOperationType();
				
				UserId newUserId = boardObject.getUserId();
				
				switch(boardOp) {
				
				case CREATE:
					
					ArrayList<Pixel> newPixel = boardObject.getPixels();
					ObjectId newObjId = boardObject.getObjectId();
					Timestamp newTimestamp = boardObject.getTimestamp();
					boolean newReset = boardObject.isResetObject();
					ArrayList<Pixel> newPrevPixel = boardObject.getPrevIntensity();
					
					CurveBuilder.drawCurve(
							newPixel,
							iBoardOp,
							newObjId,
							newTimestamp,
							newUserId,
							newPrevPixel,
							newReset
					);
					
					CommunicateChange.provideChanges(newPixel, newPrevPixel);
					break;
				case DELETE:
					
					SelectDelete.delete(boardObject, newUserId);
					
				case ROTATE:
					
					ParameterizedOperationsUtil.rotationUtil(boardObject, newUserId, angleOfRotation);
					
				case COLOR_CHANGE:
					
					
					ParameterizedOperationsUtil.colorChangeUtil(boardObject, newUserId, newIntensity);
					
				default:
					
					logger.log(
							ModuleID.PROCESSING,
							LogLevel.ERROR,
							"Undefined Operation Type" + boardOp
					);
					
				}
		} catch (ClassNotFoundException e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"BoardObject Class not found while deserializing"
			);
			
		} catch (IOException e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"BoardObject IO not found while deserializing"
			);
			
		}
		
	}
	
	public void onMessageReceived(String message) {
	
		HandleBoardObject runnable = new HandleBoardObject(message);
		Thread objectHandler = new Thread(runnable);
		objectHandler.start();
		
	}
}
