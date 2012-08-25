package hkonorg.framer;

import android.graphics.Bitmap;

public class Picture {
	private Bitmap bitmap;

	public Picture(Bitmap bitmap){
		this.setBitmap(bitmap);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
}
