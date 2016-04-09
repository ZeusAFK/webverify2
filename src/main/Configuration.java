package main;

import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import utils.StringUtils;
import data.Database;

public class Configuration {

	public final int version = 1;
	public final int MAX_SCANS_THREADS;

	private Database database;
	private Properties properties;

	public Configuration(Properties properties) {
		this.properties = properties;
		MAX_SCANS_THREADS = Integer.valueOf(properties.getProperty("max_scan_threads"));
		StringUtils.printInfo("Max scan threads set to " + MAX_SCANS_THREADS);
	}

	public int getApplicationVersion() {
		return version;
	}

	public Database getDatabaseConfiguration() throws InvalidPropertiesFormatException {
		if (database != null)
			return database;
		try {
			String db_ip = properties.getProperty("db_ip");
			String db_name = properties.getProperty("db_name");
			String db_user = properties.getProperty("db_user");
			String db_password = properties.getProperty("db_password");
			int db_port = Integer.valueOf(properties.getProperty("db_port"));
			boolean ssh = properties.getProperty("ssh").equals("1");
			String ssh_host = properties.getProperty("ssh_host");
			String ssh_user = properties.getProperty("ssh_user");
			String ssh_password = properties.getProperty("ssh_password");
			int ssh_port = Integer.valueOf(properties.getProperty("ssh_port"));

			if (!ssh) {
				database = new Database(db_ip, db_user, db_password, db_name, db_port);
			} else {
				database = new Database(db_ip, db_user, db_password, db_name, db_port, ssh, ssh_host, ssh_user, ssh_password, ssh_port);
			}

			return database;
		} catch (Exception e) {
			throw new InvalidPropertiesFormatException("Error loading database configuration: " + e.getMessage());
		}
	}
}