package data.models;

import java.util.HashMap;

import tasks.InitializeTask;

public class ConfigurationItem {

	private int id;
	private String key;
	private String name;
	private String description;
	private String value;
	private int status;

	public ConfigurationItem(String key) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getConfigurationByKey(?)", key);
			this.key = row.get("key").toString();
			this.id = Integer.valueOf(row.get("id").toString());
			this.name = row.get("name").toString();
			this.description = row.get("description").toString();
			this.value = row.get("value").toString();
			this.status = Integer.valueOf(row.get("status").toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
