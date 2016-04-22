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

package data.models;

import java.util.HashMap;

import tasks.InitializeTask;
import utils.StringUtils;

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
			StringUtils.printWarning("Configuration not found in database: " + key);
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
