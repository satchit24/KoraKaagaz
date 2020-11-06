package processing;

import java.util.ArrayList;

import processing.boardobject.BoardObject;
import processing.utility.*;
/**
 * Reset Class clears the screen
 * ables undo and redo
 * but not select delete and rotate
 * 
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 */
public class Reset {
	
	/*
	 *  The function makes a white filled rectangle object
	 * covers the whole screen with undo enabled
	 * 
	 * @param Reset flag so reset object is not selected
	 * deleted rotated color changed
	 */
	public static void screenReset(UserId newUserId,boolean reset) {
		
		/* Get BoardDimensions from UI */
		Dimension dimension = ClientBoardState.boardDimension;
		
		int rows = dimension.numRows;
		
		int cols = dimension.numCols;
		
		Intensity whiteIntensity = new Intensity(255,255,255);
		
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		/* Traverse through screen and assign position
		 * and white intensity to each pixel
		 */
		for(int i = 0; i < rows; i++) {
			
			for(int j = 0; j < cols; j++) {
				
				Position whitePosition = new Position(i,j);
				
				Pixel whitePixel = new Pixel(whitePosition, whiteIntensity);
				
				pixels.add(whitePixel);
				
			}
		}
		
		Timestamp newTimestamp = Timestamp.getCurrentTime();
		
		ObjectId newObjId = new ObjectId(newUserId, newTimestamp);
		
		/* Creates Object of the white rectangle acting as clear screen mask */
		BoardObject resetObj = 
				new BoardObject(pixels, newObjId, newTimestamp, newUserId, reset);
		
		//Insert BoardObject in the Map
		ClientBoardState.maps.insertObjectIntoMaps(resetObj);
				
		//Push BoardObject in undo stack
		stackUtil(resetObj);
	}
	
	private static void stackUtil(BoardObject newObj) {
		
		UndoRedo.pushIntoStack(newObj);
	
	}
}
