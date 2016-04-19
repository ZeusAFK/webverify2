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

package data.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import data.collections.SiteAssetsCollection;
import data.enums.AssetType;
import tasks.InitializeTask;
import utils.StringUtils;

public class Site {

	private int id;
	private String name;
	private String url;
	private String ip;
	private String description;
	private User user;
	private Date lastbuild;
	private Date created;
	private int status;
	private SiteAssetsCollection assets;

	public Site(int id) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getSiteById(?)", id);
			this.id = Integer.valueOf(row.get("id").toString());
			name = row.get("name").toString();
			url = row.get("url").toString();
			ip = row.get("ip").toString();
			description = row.get("description").toString();
			user = new User(Integer.valueOf(row.get("user").toString()));
			lastbuild = (Date) row.get("lastbuild");
			created = (Date) row.get("created");
			status = Integer.valueOf(row.get("status").toString());
			assets = new SiteAssetsCollection();
		} catch (Exception e) {
			StringUtils.printWarning("Site information not found or error");
			e.printStackTrace();
		}
	}

	public Site() {

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getLastbuild() {
		return lastbuild;
	}

	public void setLastbuild(Date lastbuild) {
		this.lastbuild = lastbuild;
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

	public void Persist() {
		if (id > 0) {
			try {
				InitializeTask.database.ExecuteProcedure("updateSiteById", id, name, url, ip, description, lastbuild, status);
			} catch (Exception e) {
				StringUtils.printWarning("Error while persist data for site: " + id);
				e.printStackTrace();
			}
		}
	}

	public SiteAssetsCollection getAssets() {
		return assets;
	}

	public boolean loadAssets() {
		try {
			assets = new SiteAssetsCollection();
			ArrayList<HashMap<String, Object>> results = InitializeTask.database.getResulSetMapped("CALL getSiteAssets(?)", id);
			for (HashMap<String, Object> result : results) {
				SiteAsset asset = new SiteAsset(this);
				asset.setId(Integer.valueOf(result.get("id").toString()));
				asset.setSite(this);
				asset.setLocation(result.get("location").toString());
				asset.setUrl(result.get("url").toString());
				asset.setCreated((Date) result.get("created"));
				asset.setStatus(Integer.valueOf(result.get("status").toString()));
				asset.setType(new AssetType(Integer.valueOf(result.get("type").toString())));
				assets.add(asset);
			}
		} catch (Exception e) {
			StringUtils.printFatalError("Error while loading site assets: " + url);
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
