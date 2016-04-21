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

import java.io.File;
import java.io.IOException;

import main.Configuration;
import data.DatabaseConnection;
import data.models.ConfigurationItem;
import utils.StringUtils;

public class InitializeTask {

	public static Configuration configuration;
	public static DatabaseConnection database;
	public static final String log_file = System.getProperty("user.dir") + System.getProperty("file.separator") + "logs" + System.getProperty("file.separator")
			+ System.currentTimeMillis() + ".log";

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

		int max_scan_threads = Integer.valueOf(new ConfigurationItem("max_scan_threads").getValue());

		StringUtils.printInfo("Max scan threads set to " + max_scan_threads);
	}
}
