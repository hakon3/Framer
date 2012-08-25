package hkonorg.framer;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class CameraWrapper {

	private Activity activity;
	private Uri fileUri;

	public CameraWrapper(Activity activity) {
		this.activity = activity;
	}

	public void takePicture() {
		File photo;

		try {
			photo = createTemporaryFile("temppic", ".jpg");
		} catch (IOException ex) {
			Log.v("FramerAPP!!!", "Can't create file to take picture!");
			return;
		}

		fileUri = Uri.fromFile(photo);
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		activity.startActivityForResult(intent, FramerActivity.CAMERA_REQUEST);
	}

	private File createTemporaryFile(String filename, String filextension)
			throws IOException {
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
		if (tempDir.exists() == false) {
			tempDir.mkdir();
		}

		return File.createTempFile(filename, filextension, tempDir);
	}

	// htc desire 480*800
	public Bitmap getImage() {
		ContentResolver contentResolver = activity.getContentResolver();
		contentResolver.notifyChange(fileUri, null);

		Bitmap bitmap = decodeSampledBitmap(fileUri.getPath(), 480, 800);
		return bitmap;
	}

	// gibbed from stackoverflow
	// decodes with width and height heuristic for better view in specified
	// viewport
	private static Bitmap decodeSampledBitmap(String filename, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
}
