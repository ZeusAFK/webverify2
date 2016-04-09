package tasks;

import java.io.File;
import java.io.IOException;

import main.Configuration;
import data.DatabaseConnection;
import utils.StringUtils;

public class InitializeTask {

	public static Configuration configuration;
	public static DatabaseConnection database;
	public static final String log_file = System.getProperty("user.dir") + System.getProperty("file.separator") + "logs" + System.getProperty("file.separator") + System.currentTimeMillis() + ".log";

	public void execute() {
		System.out.println("Starting webverify2 community edition");
		
		// Creating log file
		try {
			File file = new File(log_file);
			file.createNewFile();
			StringUtils.setLog_file(log_file);
		} catch (IOException e) {
			System.out.println("Error creating log file: " + e.getMessage());
		}

		// Loading configuration
		StringUtils.printInfo("Loading configuration");
		if (LoadConfigurationTask.execute()) {
			StringUtils.printInfo("Configuration load success, version: " + configuration.getApplicationVersion());
		} else {
			StringUtils.printFatalError("Unable to load configuration, configuration file not found or contains errors");
			System.exit(0);
		}

		// Connecting to database server
		StringUtils.printInfo("Connecting with database servers");
		if (ConnectDatabasesTask.execute()) {
			StringUtils.printInfo("Database connection success");
		} else {
			System.exit(0);
		}
	}
}
