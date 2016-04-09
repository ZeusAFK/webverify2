package tasks;

import utils.StringUtils;
import network.http.HttpRequest;
import data.enums.AssetVerificationResult;
import data.models.ScanAsset;
import data.models.SiteAsset;

public final class AssetIntegrityVerificationTask {

	public static AssetVerificationResult verify(SiteAsset siteAsset, ScanAsset scanAsset) {
		HttpRequest http = new HttpRequest(scanAsset.getUrl());
		String separator = System.getProperty("file.separator");
		String userdir = System.getProperty("user.dir");
		if (siteAsset.getType().isDownload()) {
			if (siteAsset.getType().isBinary()) {
				String scanAssetHash = http.getBinaryContentSha1();
				String siteAssetHash = StringUtils.getFileSha1(userdir + separator + "storage" + separator + siteAsset.getLocation());

				return scanAssetHash.equals(siteAssetHash) ? AssetVerificationResult.Correct : AssetVerificationResult.Modified;
			} else {
				String scanAssetContent = http.getContent();
				String siteAssetContent = StringUtils.getContentFromFile(userdir + separator + "storage" + separator + siteAsset.getLocation());

				return siteAssetContent.equals(scanAssetContent) ? AssetVerificationResult.Correct : AssetVerificationResult.Modified;
			}
		} else {
			return siteAsset.getSize() == scanAsset.getSize() ? AssetVerificationResult.Correct : AssetVerificationResult.Modified;
		}
	}
}
