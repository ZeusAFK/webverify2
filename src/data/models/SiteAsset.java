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

import java.util.Date;
import java.util.HashMap;

import data.enums.AssetType;
import tasks.InitializeTask;

public class SiteAsset extends Asset {

	private Site site;

	public SiteAsset(Site site) {
		super();
		this.site = site;
	}

	public SiteAsset(Site site, Asset asset) {
		super();
		this.site = site;
		if (asset != null) {
			super.setType(asset.getType());
			super.setLocation(asset.getLocation());
			super.setUrl(asset.getUrl());
			super.setCreated(asset.getCreated());
			super.setSize(asset.getSize());
			super.setStatus(asset.getStatus());
		}
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public boolean findByLocation(String location) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getSiteAssetBySiteAndLocation(?,?)", site.getId(), location);
			super.setId(Integer.valueOf(row.get("id").toString()));
			super.setType(new AssetType(Integer.valueOf(row.get("type").toString())));
			super.setLocation(row.get("location").toString());
			super.setUrl(row.get("url").toString());
			super.setCreated((Date) row.get("created"));
			super.setSize(Integer.valueOf(row.get("size").toString()));
			super.setStatus(Integer.valueOf(row.get("status").toString()));

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean delete(boolean storage) {
		try {
			super.setStatus(0);
			InitializeTask.database.ExecuteProcedure("deleteSiteAssetById", super.getId());
			if (storage)
				return super.delete();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean persist() {
		if (super.getType().isDownload() && super.getLocation() == null)
			return false;
		if (super.getType() == null)
			return false;

		if (super.getId() > 0) {
			try {
				InitializeTask.database.ExecuteProcedure("updateSiteAssetById", super.getId(), super.getType().getId(), super.getLocation(), super.getUrl(),
						super.getSize(), super.getStatus());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				int id = InitializeTask.database.ExecuteProcedure("createSiteAsset", site.getId(), super.getType().getId(), super.getLocation(),
						super.getUrl(), super.getSize());
				if (id > 0) {
					super.setId(id);
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
