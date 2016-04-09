package data.models;

import java.io.File;
import java.util.Date;

import data.enums.AssetType;

public class Asset {

	private int id;
	private AssetType type;
	private String location;
	private String url;
	private Date created;
	private int size;
	private int status;

	public Asset() {
		id = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AssetType getType() {
		return type;
	}

	public void setType(AssetType type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean delete() {
		File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "storage" + System.getProperty("file.separator")
				+ location);
		return file.delete();
	}
}
