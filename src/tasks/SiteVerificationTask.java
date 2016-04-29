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
import data.collections.LinksCollection;
import data.enums.AssetVerificationResult;
import data.models.Asset;
import data.models.Link;
import data.models.Scan;
import data.models.ScanAsset;
import data.models.SiteAsset;
import data.models.SiteVerificationResult;

public class SiteVerificationTask {

	private Scan scan;
	private SiteVerificationResult result;
	private LinksCollection links;
	private boolean storeAssets;
	private boolean crawl;
	private int scheduleId;

	public SiteVerificationTask(Scan scan, boolean storeAssets, boolean crawl, int scheduleId) {
		this.scan = scan;
		this.storeAssets = storeAssets;
		this.crawl = crawl;
		this.result = new SiteVerificationResult();
		this.scheduleId = scheduleId;
		// links = new LinksCollection();
		links = scan.getCrawlLinks();
	}

	public SiteVerificationResult performVerification() {
		performLinkScanning(new Link(scan.getSite().getUrl()), true);

		StringUtils.printInfo("Links found in " + scan.getSite().getUrl() + ": " + links.size());

		LinkToAssetConverter converterSchedule = new LinkToAssetConverter(scheduleId, "schedule");
		LinkToAssetConverter converterScan = new LinkToAssetConverter(scan.getId(), "scan");

		if (storeAssets) {
			if (links.size() > 0) {
				StringUtils.printInfo("Site build enabled, storing assets from links found");
				for (Link link : links.getCollection()) {
					if (link.isActive() && !link.isExtern()) {
						Asset asset = converterSchedule.getAsset(link, storeAssets);
						if (asset == null) {
							continue;
						}
						scan.getSite().getAssets().add(new SiteAsset(scan.getSite(), asset));
					}
				}
				StringUtils.printInfo("Saving stored assets information into database");
				scan.getSite().getAssets().persistAll();
			} else {
				StringUtils.printWarning("Site build enabled but no links found by crawler");
			}
		} else {
			StringUtils.printInfo("Starting links verification");
			LinksCollection siteLinks = new LinksCollection();
			for (Link link : links.getCollection()) {
				if (!link.isExtern()) {
					Asset asset = converterScan.getAsset(link, true);
					if (asset == null) {
						continue;
					}
					ScanAsset scanAsset = new ScanAsset(scan, asset);
					SiteAsset siteAsset = new SiteAsset(scan.getSite());

					scan.getAssets().add(scanAsset);

					String location = StringUtils.getSha1("site:" + link.getUrl() + ":schedule:" + scheduleId);

					if (!siteAsset.findByLocation(location)) {
						this.result.addCreated();
						StringUtils.printInfo("Created: " + link.getUrl());
					} else if (siteLinks.add(link, false) && AssetIntegrityVerificationTask.verify(siteAsset, scanAsset) == AssetVerificationResult.Modified) {
						this.result.addModified();
						StringUtils.printInfo("Modified: " + link.getUrl());
					} else {
						StringUtils.printInfo("Correct: " + link.getUrl());
					}
				}
			}

			StringUtils.printInfo("Saving scan assets information into database");
			scan.getAssets().persistAll();

			if (siteLinks.size() != scan.getSite().getAssets().size()) {
				for (SiteAsset asset : scan.getSite().getAssets()) {
					if (siteLinks.findByUrl(asset.getUrl()) == null) {
						this.result.addDeleted();
						StringUtils.printInfo("Deleted: " + asset.getUrl());
					}
				}
			}
		}

		this.result.setLinks(links.size());

		return result;
	}

	private void performLinkScanning(Link currentLink, boolean crawl) {
		LinkGrabberTask task = new LinkGrabberTask(scan.getSite(), currentLink);
		if (task.CheckLink(crawl)) {
			LinksCollection task_links = task.getLinks();
			for (Link link : task_links.getCollection()) {
				if (links.add(link, true)) {
					performLinkScanning(link, this.crawl);
				}
			}
		}
	}
}
