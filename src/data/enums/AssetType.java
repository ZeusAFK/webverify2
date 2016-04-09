package data.enums;

import java.util.HashMap;

import tasks.InitializeTask;
import utils.StringUtils;

public class AssetType {

	private int id;
	private String name;
	private String mime;
	private String description;
	private boolean download;
	private boolean binary;
	private int status;

	public AssetType(int id) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getAssetTypeById(?)", id);
			id = Integer.valueOf(row.get("id").toString());
			name = row.get("name").toString();
			mime = row.get("mime").toString();
			description = row.get("description").toString();
			download = Integer.valueOf(row.get("download").toString()) == 1;
			binary = Integer.valueOf(row.get("binary").toString()) == 1;
			status = Integer.valueOf(row.get("status").toString());
		} catch (Exception e) {
			StringUtils.printWarning("Error while loading asset type information: " + id);
			e.printStackTrace();
		}
	}

	public AssetType(String contentType) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getAssetTypeByMime(?)", contentType);
			id = Integer.valueOf(row.get("id").toString());
			name = row.get("name").toString();
			mime = row.get("mime").toString();
			description = row.get("description").toString();
			download = Integer.valueOf(row.get("download").toString()) == 1;
			status = Integer.valueOf(row.get("status").toString());
		} catch (Exception e) {
			StringUtils.printWarning("Error while loading asset type information: " + contentType);
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isDownload() {
		return download;
	}

	public boolean isBinary() {
		return binary;
	}

	public void setBinary(boolean binary) {
		this.binary = binary;
	}

	public void setDownload(boolean download) {
		this.download = download;
	}
}
