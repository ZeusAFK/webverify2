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

import utils.StringUtils;
import data.collections.ReportsCollection;
import data.enums.ReportStatus;
import data.models.Report;
import data.models.ReportHandler;

public class ReportService extends AbstractService implements Runnable {

	private static ReportsCollection reports;
	private static boolean running;

	public ReportService() {
		onInit();
	}

	@Override
	public void onInit() {
		reports = new ReportsCollection();
		running = true;

		StringUtils.printInfo("Reporting service started");
	}

	@Override
	public void onDestroy() {
		running = false;
		StringUtils.printInfo("Reporting service stopped");
	}

	@Override
	public void run() {
		while (running) {
			for (int i = 0; i < reports.size(); i++) {
				Report report = reports.get(i);
				if (report.getStatus() == ReportStatus.Scheduled) {
					report.setStatus(new ReportHandler().execute(report) ? ReportStatus.Sended : ReportStatus.Failed);
				}
			}
		}
	}

	public static ReportService getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final ReportService instance = new ReportService();
	}
}
