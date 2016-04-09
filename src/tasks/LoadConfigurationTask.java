package tasks;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import main.Configuration;
import utils.StringUtils;

public class LoadConfigurationTask {

	public static boolean execute() {
		Properties properties = new Properties();

		try {
			FileInputStream in = new FileInputStream("config.properties");
			properties.load(in);
		} catch (IOException ex) {
			StringUtils.printWarning("Configuration file not found, loading default configuration (DEVELOPMENT MODE)");
			try {
				properties.load(Configuration.class.getClassLoader().getResourceAsStream("config.properties"));
			} catch (Exception e) {
				return false;
			}
		}

		InitializeTask.configuration = new Configuration(properties);
		return true;
	}
}
