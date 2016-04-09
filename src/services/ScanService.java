package services;

import java.util.Date;

import tasks.EmailSiteVerificationResultTask;
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

		SiteVerificationTask verificationTask = new SiteVerificationTask(scan, schedule.isBuild(), schedule.isCrawl(), schedule.getId());
		SiteVerificationResult result = verificationTask.performVerification();

		if (schedule.isBuild()) {
			schedule.getSite().setLastbuild(new Date());
			schedule.setBuild(false);
			schedule.getSite().Persist();
		} else {
			StringUtils.printInfo("Verification completed, links found: " + result.getLinks() + ", created: " + result.getCreated() + ", modified: "
					+ result.getModified() + ", deleted: " + result.getDeleted());
		}

		// TODO: Recompile results
		// TODO: Schedule scan reporting
		EmailSiteVerificationResultTask mailResults = new EmailSiteVerificationResultTask(schedule.getSite(), result);
		mailResults.send();

		onDestroy();
	}

	public ScanSchedule getSchedule() {
		return schedule;
	}
}
