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

import tasks.InitializeTask;
import data.enums.AssetType;

public class ScanAsset extends Asset {

	private Scan scan;

	public ScanAsset(Scan scan, Asset asset) {
		super();
		this.scan = scan;
		if (asset != null) {
			super.setType(asset.getType());
			super.setLocation(asset.getLocation());
			super.setUrl(asset.getUrl());
			super.setCreated(asset.getCreated());
			super.setSize(asset.getSize());
			super.setStatus(asset.getStatus());
		}
	}

	public Scan getScan() {
		return scan;
	}

	public void setScan(Scan scan) {
		this.scan = scan;
	}

	public boolean findByLocation(String location) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getScanAssetByScanAndLocation(?,?)", scan.getId(), location);
			super.setId(Integer.valueOf(row.get("id").toString()));
			super.setType(new AssetType(Integer.valueOf(row.get("type").toString())));
			super.setLocation(row.get("location").toString());
			super.setUrl(row.get("url").toString());
			super.setCreated((Date) row.get("created"));
			super.setSize(Integer.valueOf(row.get("size").toString()));
			super.setStatus(Integer.valueOf(row.get("status").toString()));

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(boolean storage) {
		if (storage)
			return super.delete();
		return true;
	}

	public boolean persist() {
		if (super.getType().isDownload() && super.getLocation() == null)
			return false;
		if (super.getType() == null)
			return false;

		if (super.getId() > 0) {
			try {
				InitializeTask.database.ExecuteProcedure("updateScanAssetById", super.getId(), super.getType().getId(), super.getLocation(), super.getUrl(),
						super.getSize(), super.getStatus());
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		} else {
			try {
				int id = InitializeTask.database.ExecuteProcedure("createScanAsset", scan.getId(), super.getType().getId(), super.getLocation(),
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
