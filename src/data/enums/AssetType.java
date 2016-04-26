/**
Copyright 2016 ZeusAFK

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
			binary = Integer.valueOf(row.get("binary").toString()) == 1;
			status = Integer.valueOf(row.get("status").toString());
		} catch (Exception e) {
			StringUtils.printWarning("Asset type information not found: " + contentType);
			id = 0;
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
