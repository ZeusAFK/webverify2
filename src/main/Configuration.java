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

package main;

import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import data.Database;

public class Configuration {

	public final int version = 1;

	private Database database;
	private Properties properties;

	public Configuration(Properties properties) {
		this.properties = properties;
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