package tasks;

import java.util.Date;

import utils.StringUtils;
import network.http.HttpRequest;
import data.enums.AssetType;
import data.models.Asset;
import data.models.Link;

public class LinkToAssetConverter {

	private int scheduleId;

	public LinkToAssetConverter(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Asset getAsset(Link link, boolean store) {
		HttpRequest http = new HttpRequest(link.getUrl());
		if (http.Exists()) {
			String contentType = http.getContentType();
			AssetType type = new AssetType(contentType);
			String location = StringUtils.getSha1("site:" + link.getUrl() + ":schedule:" + scheduleId);

			Asset asset = new Asset();
			asset.setCreated(new Date());
			asset.setLocation(location);
			asset.setUrl(link.getUrl());
			asset.setSize(link.getSize());
			asset.setStatus(1);
			asset.setType(type);

			if (store && asset.getType().isDownload()) {
				StringUtils.printInfo("Downloading " + link.getUrl() + " into " + location);
				String separator = System.getProperty("file.separator");
				http.saveToFile(System.getProperty("user.dir") + separator + "storage" + separator + location);
			}

			return asset;
		} else {
			return null;
		}
	}
}
