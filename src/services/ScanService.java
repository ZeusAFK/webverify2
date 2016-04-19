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

package services;

import java.util.Date;

import network.http.HttpRequest;
import tasks.SiteVerificationTask;
import utils.StringUtils;
import data.collections.ScanAssetsCollection;
import data.enums.ScanStatus;
import data.models.Scan;
import data.models.ScanSchedule;
import data.models.SiteVerificationResult;

public class ScanService extends AbstractService implements Runnable {

	private ScanSchedule schedule;
	private Scan scan;
	@SuppressWarnings("unused")
	private ScanAssetsCollection assets;

	public ScanService(ScanSchedule schedule) {
		this.schedule = schedule;
		scan = new Scan();
		scan.setCreated(new Date());
		scan.setSite(schedule.getSite());
		scan.setStatus(ScanStatus.Created);
		assets = new ScanAssetsCollection();
	}

	@Override
	public void onInit() {
		StringUtils.printInfo("Starting scan service for: " + schedule.getSite().getUrl());

		scan.setStart(new Date());
		scan.setStatus(ScanStatus.Running);
		ScanScheduleService.getInstance().notifyScanStarting(this);
	}

	@Override
	public void onDestroy() {
		StringUtils.printInfo("Stopping scan service for: " + schedule.getSite().getUrl());
		scan.setEnd(new Date());
		ScanScheduleService.getInstance().notifyScanCompleted(this);
	}

	@Override
	public void run() {
		onInit();

		schedule.getSite().loadAssets();
		if (schedule.isBuild()) {
			StringUtils.printInfo("Site build enabled: " + schedule.getSite().getUrl());
			schedule.getSite().getAssets().deleteAll(true);
		}

		HttpRequest request = new HttpRequest(schedule.getSite().getUrl());
		String ipAddress = request.getIpAddress();

		if (!schedule.isBuild()) {
			if (!ipAddress.equals(schedule.getSite().getIp())) {
				StringUtils.printWarning("Site ip address not match: " + schedule.getSite().getIp() + " original, " + ipAddress + " now");
			} else {
				StringUtils.printInfo("Site ip address match: " + ipAddress);
			}
		} else {
			StringUtils.printInfo("Site ip address: " + ipAddress);
		}

		SiteVerificationTask verificationTask = new SiteVerificationTask(scan, schedule.isBuild(), schedule.isCrawl(), schedule.getId());
		SiteVerificationResult result = verificationTask.performVerification();

		if (schedule.isBuild()) {
			schedule.getSite().setIp(ipAddress);
			schedule.getSite().setLastbuild(new Date());
			schedule.setBuild(false);
			schedule.getSite().Persist();
		} else {
			StringUtils.printInfo("Verification completed, links found: " + result.getLinks() + ", created: " + result.getCreated() + ", modified: "
					+ result.getModified() + ", deleted: " + result.getDeleted());
		}

		// TODO: Recompile results
		// TODO: Schedule scan reporting
		if (!schedule.isBuild()) {
			// TODO: This report is only for demo, implement a better new one.
			// EmailSiteVerificationResultTask mailResults = new
			// EmailSiteVerificationResultTask(schedule.getSite(), result);
			// mailResults.send();
		}

		onDestroy();
	}

	public ScanSchedule getSchedule() {
		return schedule;
	}
}
