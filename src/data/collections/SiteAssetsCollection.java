package data.collections;

import java.util.ArrayList;

import utils.StringUtils;
import data.models.SiteAsset;

@SuppressWarnings("serial")
public class SiteAssetsCollection extends ArrayList<SiteAsset> {

	public SiteAsset findByUrl(String url) {
		for (SiteAsset asset : this) {
			if (asset.getUrl().equals(url))
				return asset;
		}
		return null;
	}

	public void deleteAll(boolean storage) {
		for (SiteAsset asset : this) {
			if (!asset.delete(storage)) {
				StringUtils.printWarning("Error while deleting asset: " + asset.getId() + " in " + asset.getLocation());
			}
		}
		this.clear();
	}

	public void delete(int index, boolean storage) {
		SiteAsset asset = (SiteAsset) this.get(index);
		if (!(asset).delete(storage)) {
			StringUtils.printWarning("Error while deleting asset: " + asset.getId() + " in " + asset.getLocation());
		} else {
			this.remove(asset);
		}
	}

	public boolean persistAll() {
		for (SiteAsset asset : this) {
			asset.persist();
		}
		return true;
	}

	public boolean persist(int index) {
		SiteAsset asset = (SiteAsset) this.get(index);
		asset.persist();
		return true;
	}
}
