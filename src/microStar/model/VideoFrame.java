package microStar.model;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class VideoFrame implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	String source;
	String destination;
	ImageIcon frame;
	boolean connected;
	
	public VideoFrame() {
		source = null;
		destination = null;
		frame = null;
		connected = false;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public ImageIcon getFrame() {
		return frame;
	}

	public void setFrame(ImageIcon frame) {
		this.frame = frame;
	}

	public boolean getConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	
	
}
