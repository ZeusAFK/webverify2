package data.models;

public class Link {

	private String url;
	private boolean extern;
	private boolean image;
	private boolean text;
	private int size;
	private boolean active;

	public Link(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public boolean isExtern() {
		return extern;
	}

	public boolean isImage() {
		return image;
	}

	public boolean isText() {
		return text;
	}

	public boolean isActive() {
		return active;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setExtern(boolean extern) {
		this.extern = extern;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public void setText(boolean text) {
		this.text = text;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
