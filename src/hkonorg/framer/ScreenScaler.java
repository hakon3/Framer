package hkonorg.framer;

import android.graphics.Bitmap;

public class ScreenScaler implements Scaler {	

	private int screenWidth;
	private int screenHeight;

	public ScreenScaler(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}

	@Override
	public void scale(Picture picture) {

		// optimization, let picture lazy load the bitmap correctly scaled at once... e.g store only path to the picture in object "picture"
		Bitmap bitmap = picture.getBitmap(); 

		// optimization / more correct - use density based scaling, so it will show correct on all types of display...
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);
		
		picture.setBitmap(scaledBitmap);
		
	}
}
