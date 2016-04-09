package data.collections;

import java.util.ArrayList;

import utils.StringUtils;
import data.models.ScanAsset;

@SuppressWarnings("serial")
public class ScanAssetsCollection extends ArrayList<ScanAsset> {

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
}
