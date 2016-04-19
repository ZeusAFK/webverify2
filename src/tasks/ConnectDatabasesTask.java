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

import java.util.InvalidPropertiesFormatException;

import utils.StringUtils;
import data.DatabaseConnection;

public class ConnectDatabasesTask {

	public static boolean execute() {
		try {
			InitializeTask.database = new DatabaseConnection(InitializeTask.configuration.getDatabaseConfiguration());

			return true;
		} catch (InvalidPropertiesFormatException e) {
			StringUtils.printFatalError("Errors in configuration file: " + e.getMessage());
			return false;
		} catch (Exception e) {
			StringUtils.printFatalError("Error while connecting to database server: " + e.getMessage());
			return false;
		}
	}
}