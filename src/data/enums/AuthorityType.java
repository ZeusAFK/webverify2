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
