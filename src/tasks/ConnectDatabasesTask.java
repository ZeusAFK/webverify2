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