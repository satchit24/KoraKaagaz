package processing;

import java.util.ArrayList;

import processing.boardobject.BoardObject;
import processing.utility.*;
/*
 * @author Satchit Desai
 * @reviewer Himanshu Jain
 */
public class Reset {
	
	public static BoardObject screenReset(ObjectId newObjId, Timestamp newTimestamp, UserId newUserId, boolean reset) {
		
		Dimension dimension = ClientBoardState.boardDimension;
		
		int rows = dimension.numRows;
		
		int cols = dimension.numCols;
		
		ArrayList<Pixel> pixels = new ArrayList<Pixel>();
		
		for(int i = 0; i < rows; i++) {
			
			for(int j = 0; j < cols; j++) {
				
				Intensity whiteIntensity = new Intensity(255,255,255);
				
				Position whitePosition = new Position(i,j);
				
				Pixel whitePixel = new Pixel(whitePosition,
											whiteIntensity);
				
				pixels.add(whitePixel);
				
			}
		}
		
		BoardObject resetObj = new BoardObject(	pixels,
												newObjId,
												newTimestamp,
												newUserId,
												reset
												);
		
		return resetObj;
	}
}
