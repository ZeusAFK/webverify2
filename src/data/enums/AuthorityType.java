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

package data.enums;

import java.util.HashMap;

import tasks.InitializeTask;
import utils.StringUtils;

public class AuthorityType {

	private int id;
	private String handler;
	private String name;
	private String description;
	private int status;

	public AuthorityType(int id) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getAuthorityTypeById(?)", id);
			name = row.get("name").toString();
			handler = row.get("handler").toString();
			description = row.get("description").toString();
			status = Integer.valueOf(row.get("status").toString());
		} catch (Exception e) {
			StringUtils.printWarning("Authority Type information not found or error");
			e.printStackTrace();
		}
	}

	public AuthorityType() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
