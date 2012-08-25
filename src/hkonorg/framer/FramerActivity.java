package hkonorg.framer;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class FramerActivity extends Activity {
	public static final int CAMERA_REQUEST = 1888;     
    int frameWidth = 0;  
    int frameWidthIncrease = 50;
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();    
    CameraWrapper cameraWrapper;
    private FramerSession framerSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.main);        
        
        WindowManager winMgr = (WindowManager)getSystemService(WINDOW_SERVICE);
        int width = winMgr.getDefaultDisplay().getWidth();
        int height = winMgr.getDefaultDisplay().getHeight();
        
        cameraWrapper = new CameraWrapper(this);
        ScreenScaler frameScaler = new ScreenScaler(width, height);
        framerSession = new FramerSession(frameScaler);        
                
        Button clrButton = (Button) this.findViewById(R.id.clrButton);
        clrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {	
            	reset();
            }
        });
    }
    
	private void reset() {
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.mainLayout);
    	for(ImageView view : imageViews) {
    		layout.removeView(view);
    	}
    	
    	imageViews.clear();
    	framerSession.clear();
    	frameWidth = 0;
	}  

    @Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {	
    	if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
    		cameraWrapper.takePicture();
    	}    	
		return super.onKeyUp(keyCode, event);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST) {  
            Bitmap photo = cameraWrapper.getImage();
            Picture picture = new Picture(photo);
            
            updateFrameModel(picture, frameWidth);           
            updateFrameView(picture);
            frameWidth = frameWidthIncrease; 
        }  
    }
    
    private void updateFrameModel(Picture picture, int margin) {
		framerSession.addAndScaleFrame(picture, margin);
	}

	private void updateFrameView(Picture picture){
    	RelativeLayout layout = (RelativeLayout)this.findViewById(R.id.mainLayout);
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT );
    	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    	int frameWidth = framerSession.getCurrentFrameWidth();
    	params.setMargins(frameWidth, frameWidth, frameWidth, frameWidth );
    	
    	ImageView imageView = new ImageView(this);
    	imageView.setScaleType(ScaleType.CENTER);   
    	imageView.setImageBitmap(picture.getBitmap());   
    	imageView.setBackgroundColor(Color.BLUE);
    	
    	layout.addView(imageView, params);
    	imageViews.add(imageView);    	
    }
}