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

package tasks;

import java.util.Date;

import utils.StringUtils;
import network.http.HttpRequest;
import data.enums.AssetType;
import data.models.Asset;
import data.models.Link;

public class LinkToAssetConverter {

	private int id;
	private String type;

	public LinkToAssetConverter(int id, String type) {
		this.id = id;
		this.type = type;
	}

	public Asset getAsset(Link link, boolean store) {
		HttpRequest http = new HttpRequest(link.getUrl());
		if (http.Exists()) {
			String contentType = http.getContentType();
			AssetType type = new AssetType(contentType);
			if (type.getId() == 0) {
				return null;
			}
			String location = StringUtils.getSha1("site:" + link.getUrl() + ":" + this.type + ":" + id);

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
