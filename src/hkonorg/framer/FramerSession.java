package hkonorg.framer;

import java.util.ArrayList;

public class FramerSession {

	private final ArrayList<Picture> pictures = new ArrayList<Picture>();
	private int currentFrameWidth = 0;
	private Scaler scaler;

	public FramerSession(Scaler frameScaler) {
		this.scaler = frameScaler;
	}
	
	void addAndScaleFrame(Picture picture, int margin) {
		scaler.scale(picture);
		pictures.add(picture);
		currentFrameWidth += margin;
	}
	
	public int getCurrentFrameWidth() {
		return currentFrameWidth;
	}

	public void clear() {
		pictures.clear();
		currentFrameWidth = 0;
	}
}
