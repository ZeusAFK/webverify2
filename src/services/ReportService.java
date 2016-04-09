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
