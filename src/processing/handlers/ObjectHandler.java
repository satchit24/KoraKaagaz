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
				
				IBoardObjectOperation boardOperationType = boardObject.getOperation();
				BoardObjectOperationType boardOp = boardOperationType.getOperationType();
				
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
							boardOperationType,
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
					break;
				case ROTATE:
					
					Angle angleOfRotation = ((
							RotateOperation)
							boardOperationType).
							getAngle();
					
					ParameterizedOperationsUtil.rotationUtil(
							boardObject,
							newUserId,
							angleOfRotation
					);
					break;
				case COLOR_CHANGE:
					
					Intensity newIntensity = ((
							ColorChangeOperation)
							boardOperationType).
							getIntensity();
					
					ParameterizedOperationsUtil.colorChangeUtil(
							boardObject,
							newUserId,
							newIntensity
					);
					break;
				default:
					
					logger.log(
							ModuleID.PROCESSING,
							LogLevel.ERROR,
							"[#" + Thread.currentThread().getId() + "] "+
							"Undefined Operation Type" + boardOp
					);
					break;
				}
		} catch (ClassNotFoundException e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] " +
					"BoardObject Class not found while deserializing"
			);
			
		} catch (IOException e) {
			logger.log(
					ModuleID.PROCESSING,
					LogLevel.ERROR,
					"[#" + Thread.currentThread().getId() + "] " +
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
