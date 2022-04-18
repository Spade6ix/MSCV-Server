package microStar.model;

import java.io.Serializable;

import javax.swing.ImageIcon;

public class VideoFrame implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	String source;
	String destination;
	ImageIcon frame;
	
	public VideoFrame() {
		source = null;
		destination = null;
		frame = null;
	}
	
	public VideoFrame(VideoFrame obj) {
		source = obj.getSource();
		destination = obj.getDestination();
		frame = obj.getFrame();
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
	
}
