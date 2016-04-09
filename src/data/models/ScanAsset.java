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
		// TODO: Persist ScanAsset into database
		return false;
	}
}
