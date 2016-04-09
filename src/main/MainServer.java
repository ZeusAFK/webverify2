package main;

import services.ReportService;
import services.ScanScheduleService;
import tasks.InitializeTask;

public class MainServer {

	public static void main(String[] args) {
		// Setup
		new InitializeTask().execute();

		// Services
		new Thread(ReportService.getInstance()).start();
		new Thread(ScanScheduleService.getInstance()).start();
	}

}
