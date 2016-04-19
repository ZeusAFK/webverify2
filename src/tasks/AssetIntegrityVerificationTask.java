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
