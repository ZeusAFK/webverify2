package data.models;

import java.util.Date;
import java.util.HashMap;

import tasks.InitializeTask;
import utils.StringUtils;
import data.enums.AuthorityType;

public class User {

	private int id;
	private String name;
	private String firstname;
	private String lastname;
	private String password;
	private AuthorityType authority;
	private Date lastaccess;
	private Date created;
	private int status;

	public User(int id) {
		try {
			HashMap<String, Object> row = InitializeTask.database.getRowMapped("CALL getUserById(?)", id);
			name = row.get("name").toString();
			firstname = row.get("firstname").toString();
			lastname = row.get("lastname").toString();
			password = row.get("password").toString();
			authority = new AuthorityType(Integer.valueOf(row.get("authority").toString()));
			lastaccess = (Date) row.get("lastaccess");
			created = (Date) row.get("created");
			status = Integer.valueOf(row.get("status").toString());
		} catch (Exception e) {
			StringUtils.printWarning("User information not found or error");
			e.printStackTrace();
		}
	}

	public User() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AuthorityType getAuthority() {
		return authority;
	}

	public void setAuthority(AuthorityType authority) {
		this.authority = authority;
	}

	public Date getLastaccess() {
		return lastaccess;
	}

	public void setLastaccess(Date lastaccess) {
		this.lastaccess = lastaccess;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
