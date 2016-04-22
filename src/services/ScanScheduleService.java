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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import data.collections.ScanSchedulesCollection;
import data.collections.ScanServicesCollection;
import data.enums.ScanScheduleStatus;
import data.models.ConfigurationItem;
import data.models.ScanSchedule;
import data.models.Site;
import data.models.User;
import tasks.InitializeTask;
import utils.StringUtils;
import utils.ThreadUtils;

public class ScanScheduleService extends AbstractService implements Runnable {

	public static boolean running;
	public int CURRENT_SCAN_THREADS;

	private static ScanSchedulesCollection schedules;
	private static ScanServicesCollection scans;

	public ScanScheduleService() {
		onInit();
	}

	@Override
	public void onInit() {
		running = true;
		schedules = new ScanSchedulesCollection();
		scans = new ScanServicesCollection();
		CURRENT_SCAN_THREADS = 0;
		StringUtils.printInfo("Scan schedule service started");
	}

	@Override
	public void onDestroy() {
		running = false;
		StringUtils.printInfo("Scan schedule service started");
	}

	@Override
	public void run() {
		while (running) {
			updateSchedulesList();
			for (ScanSchedule schedule : schedules.getCollection()) {
				if (schedule.getStatus() != ScanScheduleStatus.Scanning) {
					if (!schedule.getLimit().after(new Date())) {
						StringUtils.printInfo("Scan schedule date limit expired: " + schedule.getId() + " url: " + schedule.getSite().getUrl());
						continue;
					}

					int minutes_last_scan = ((int) ((new Date().getTime() / 60000) - (schedule.getLastScan().getTime() / 60000)));

					if (schedule.getInterval() > minutes_last_scan) {
						continue;
					} else {
						StringUtils.printInfo("Last scan was " + minutes_last_scan + " minutes ago and interval is set to " + schedule.getInterval()
								+ " minutes. Starting scan service.");
					}

					if (CURRENT_SCAN_THREADS < Integer.valueOf(new ConfigurationItem("max_scan_threads").getValue())) {
						ScanService scan = new ScanService(schedule);
						new Thread(scan).start();
					} else {
						StringUtils.printInfo("Scan services are busy... if you want try increasing CURRENT_SCAN_THREADS in config file");
						ThreadUtils.sleepSeconds(10);
					}
				}
			}
			ThreadUtils.sleepSeconds(60);
		}
	}

	public void updateSchedulesList() {
		StringUtils.printInfo("Updating scan schedule list");

		try {
			ArrayList<HashMap<String, Object>> results = InitializeTask.database.getResulSetMapped("CALL getScanScheduleActiveList()");
			for (HashMap<String, Object> result : results) {

				int id = Integer.valueOf(result.get("id").toString());
				Date created = (Date) result.get("created");
				Date lastscan = (Date) result.get("lastscan");
				int interval = Integer.valueOf(result.get("interval").toString());
				Date limit = (Date) result.get("limit");
				int priority = Integer.valueOf(result.get("priority").toString());
				Site site = new Site(Integer.valueOf(result.get("site").toString()));
				ScanScheduleStatus status = ScanScheduleStatus.fromValue(Integer.valueOf(result.get("status").toString()));
				User user = new User(Integer.valueOf(result.get("site").toString()));
				boolean build = Integer.valueOf(result.get("build").toString()) == 1;
				boolean crawl = Integer.valueOf(result.get("crawl").toString()) == 1;

				boolean newSchedule = false;
				ScanSchedule schedule = schedules.getById(id);
				if (schedule == null || schedule.equals(null)) {
					schedule = new ScanSchedule();
					schedules.add(schedule);
					newSchedule = true;
				}
				schedule.setInterval(interval);
				schedule.setLimit(limit);
				schedule.setPriority(priority);
				schedule.setLastScan(lastscan);
				schedule.setBuild(build);
				schedule.setCrawl(crawl);

				if (newSchedule || status == ScanScheduleStatus.Deleted) {
					schedule.setId(id);
					schedule.setSite(site);
					schedule.setUser(user);
					schedule.setCreated(created);
					schedule.setStatus(status);
				}

				if (schedule.getStatus() == ScanScheduleStatus.Completed) {
					schedule.setStatus(ScanScheduleStatus.Scheduled);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyScanStarting(ScanService scan) {
		scans.add(scan);
		scan.getSchedule().setStatus(ScanScheduleStatus.Scanning);
		CURRENT_SCAN_THREADS++;
	}

	public void notifyScanCompleted(ScanService scan) {
		scan.getSchedule().setStatus(ScanScheduleStatus.Completed);
		scan.getSchedule().setLastScan(new Date());
		scan.getSchedule().Persist();
		scans.remove(scan);
		CURRENT_SCAN_THREADS--;
	}

	public boolean isRunning() {
		return running;
	}

	public ScanSchedulesCollection getSchedules() {
		return schedules;
	}

	public ScanServicesCollection getScans() {
		return scans;
	}

	public static ScanScheduleService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final ScanScheduleService instance = new ScanScheduleService();
	}
}
