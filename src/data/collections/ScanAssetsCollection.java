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

package data.collections;

import java.util.ArrayList;

import utils.StringUtils;
import data.models.ScanAsset;

@SuppressWarnings("serial")
public class ScanAssetsCollection extends ArrayList<ScanAsset> {

	public ScanAsset findByUrl(String url) {
		for (ScanAsset asset : this) {
			if (asset.getUrl().equals(url))
				return asset;
		}
		return null;
	}

	public void deleteAll(boolean storage) {
		for (ScanAsset asset : this) {
			if (!asset.delete(storage)) {
				StringUtils.printWarning("Error while deleting asset: " + asset.getId() + "in " + asset.getLocation());
			}
		}
	}

	public void delete(int index, boolean storage) {
		ScanAsset asset = (ScanAsset) this.get(index);
		if (!(asset).delete(storage)) {
			StringUtils.printWarning("Error while deleting asset: " + asset.getId() + "in " + asset.getLocation());
		}
	}

	public boolean persistAll() {
		for (ScanAsset asset : this) {
			asset.persist();
		}
		return true;
	}

	public boolean persist(int index) {
		ScanAsset asset = (ScanAsset) this.get(index);
		asset.persist();
		return true;
	}
}
