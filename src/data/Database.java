package data;

import java.util.ArrayList;

public class Database {

	private String ip;
	private String db_name;
	private String db_user;
	private String db_password;
	private int db_port;
	private boolean use_ssh;
	private String ssh_host;
	private String ssh_user;
	private String ssh_password;
	private int ssh_port;
	private ArrayList<String> statements;

	public Database() {
		statements = new ArrayList<String>();
	}

	public Database(String ip, String user, String password, String name, int port) {
		statements = new ArrayList<String>();
		this.ip = ip;
		this.db_user = user;
		this.db_password = password;
		this.db_name = name;
		this.db_port = port;
	}

	public Database(String ip, String db_user, String db_password, String name, int port, boolean use_ssh, String ssh_host, String ssh_user, String ssh_password, int ssh_port) {
		statements = new ArrayList<String>();
		this.ip = ip;
		this.db_user = db_user;
		this.db_password = db_password;
		this.db_name = name;
		this.db_port = port;
		this.use_ssh = use_ssh;
		this.ssh_host = ssh_host;
		this.ssh_user = ssh_user;
		this.ssh_password = ssh_password;
		this.ssh_port = ssh_port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return db_name;
	}

	public void setName(String name) {
		this.db_name = name;
	}

	public String getUser() {
		return db_user;
	}

	public void setUser(String user) {
		this.db_user = user;
	}

	public String getPassword() {
		return db_password;
	}

	public void setPassword(String password) {
		this.db_password = password;
	}

	public int getPort() {
		return db_port;
	}

	public void setPort(int port) {
		this.db_port = port;
	}

	public boolean getSshStatus() {
		return use_ssh;
	}

	public void setSshStatus(boolean enabled) {
		this.use_ssh = enabled;
	}

	public String getSshHost() {
		return ssh_host;
	}

	public void setSshHost(String host) {
		this.ssh_host = host;
	}

	public String getSshUser() {
		return ssh_user;
	}

	public void setSshUser(String user) {
		this.ssh_user = user;
	}

	public String getSshPassword() {
		return ssh_password;
	}

	public void setSshPassword(String password) {
		this.ssh_password = password;
	}

	public int getSshPort() {
		return ssh_port;
	}

	public void setSshPort(int port) {
		this.ssh_port = port;
	}

	public int addStatement(String statement) {
		statements.add(statement);
		return statements.size() - 1;
	}

	public ArrayList<String> getStatements() {
		return statements;
	}
}