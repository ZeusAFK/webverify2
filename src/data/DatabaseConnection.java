package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.vngx.jsch.JSch;
import org.vngx.jsch.Session;
import org.vngx.jsch.config.SessionConfig;
import org.vngx.jsch.exception.JSchException;

import utils.StringUtils;

public class DatabaseConnection {

	private Connection connection;
	private Statement statement;
	private Database database;
	JSch jsch;

	public DatabaseConnection(Database database) throws Exception {
		this.database = database;
		Connect();
	}

	private boolean Connect() throws Exception {
		if (connection == null) {
			boolean ssh_connected = false;
			int lport = 0;
			int rport = database.getPort();

			if (database.getSshStatus()) {
				while (lport < 1024) {
					lport = new Random().nextInt(65535);
				}
				jsch = JSch.getInstance();
				Map<String, String> config = new HashMap<String, String>();
				config.put("StrictHostKeyChecking", "no");

				try {
					Session jschSession = jsch.createSession(database.getSshUser(), database.getSshHost(), database.getSshPort(), new SessionConfig(config));
					jschSession.connect(database.getSshPassword().getBytes());
					StringUtils.printInfo("SSH Tunnel creation success");

					lport = jschSession.setPortForwardingL(lport, database.getIp(), rport);

					StringUtils.printInfo("Redirecting port " + rport + " to " + lport);
					ssh_connected = true;
				} catch (JSchException e) {
					throw new JSchException("Error while creating SSH Tunnel: " + e.getMessage());
				}
			}
			DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
			String connectionString = "";
			if (ssh_connected && database.getSshStatus()) {
				connectionString = "jdbc:mysql://localhost:" + lport + "/" + database.getName() + "?zeroDateTimeBehavior=convertToNull";
			} else {
				connectionString = "jdbc:mysql://" + database.getIp() + ":" + database.getPort() + "/" + database.getName()
						+ "?zeroDateTimeBehavior=convertToNull";
			}
			connection = DriverManager.getConnection(connectionString, database.getUser(), database.getPassword());
			if (database.getSshStatus()) {
				StringUtils.printInfo("Connected to " + database.getName() + " version " + getVersion() + " on " + database.getSshHost());
			} else {
				StringUtils.printInfo("Connected to " + database.getName() + " version " + getVersion() + " on " + database.getIp());
			}
			prepareStatements();
			return true;
		}
		return false;
	}

	private ArrayList<PreparedStatement> preparedStatements;

	private void prepareStatements() throws Exception {
		StringUtils.printInfo("Creating prepared statements to database " + database.getName());

		preparedStatements = new ArrayList<PreparedStatement>();
		for (String statement : database.getStatements()) {
			preparedStatements.add(prepareStatement(statement));
		}
	}

	public PreparedStatement getPreparedStatement(int statement) {
		return preparedStatements.get(statement);
	}

	public boolean CheckConnection() throws Exception {
		if (!isConnected()) {
			StringUtils.printWarning("Database connection lost " + database.getName() + ", reconnecting attempt...");
			int attempts = 0;
			while (attempts++ < 10) {
				connection = null;
				try {
					if (Connect()) {
						StringUtils.printInfo("Database " + database.getName() + " reconnected");
						return true;
					} else {
						StringUtils.printWarning("Error while trying to reconnect with database " + database.getName() + ", attempt " + attempts + " of 5");
						Thread.sleep(10000);
					}
				} catch (Exception e) {
					StringUtils.printWarning("Error while trying to reconnect with database " + database.getName() + ", attempt " + attempts + " of 5");
					Thread.sleep(10000);
				}
			}
			StringUtils.printWarning("Error while reconnecting with database " + database.getName());
			System.exit(0);
			return false;
		} else {
			return true;
		}
	}

	public boolean isConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public String getVersion() throws Exception {
		CheckConnection();
		statement = connection.createStatement();
		ResultSet result = statement.executeQuery("SELECT VERSION();");
		if (result.next()) {
			return result.getString(1);
		}
		return "No database server found, fix it first!";
	}

	public PreparedStatement prepareStatement(String query) throws Exception {
		CheckConnection();
		return connection.prepareStatement(query);
	}

	private void bindStatementParams(PreparedStatement statement, Object... args) throws SQLException {
		int i = 1;
		for (Object arg : args) {
			if (arg instanceof Date) {
				statement.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
			} else if (arg instanceof Integer) {
				statement.setInt(i++, (Integer) arg);
			} else if (arg instanceof Long) {
				statement.setLong(i++, (Long) arg);
			} else if (arg instanceof Double) {
				statement.setDouble(i++, (Double) arg);
			} else if (arg instanceof Float) {
				statement.setFloat(i++, (Float) arg);
			} else if (arg instanceof Byte) {
				statement.setByte(i++, (byte) arg);
			} else if (arg instanceof byte[]) {
				statement.setBytes(i++, (byte[]) arg);
			} else {
				statement.setString(i++, String.valueOf(arg));
			}
		}
	}

	public Object[] getRow(String query, Object... args) throws Exception {
		PreparedStatement pStmt = prepareStatement(query);
		return getRow(pStmt, args);
	}

	public Object[] getRow(PreparedStatement pStmt, Object... args) throws Exception {
		CheckConnection();
		bindStatementParams(pStmt, args);
		ResultSet rs = pStmt.executeQuery();
		ResultSetMetaData metadata = rs.getMetaData();
		Object[] row = null;
		if (rs.next()) {
			row = new Object[metadata.getColumnCount()];
			for (int i = 0; i < metadata.getColumnCount(); i++) {
				row[i] = rs.getObject(i + 1);
			}
		}
		rs.close();
		return row;
	}

	public HashMap<String, Object> getRowMapped(String query, Object... args) throws Exception {
		PreparedStatement pStmt = prepareStatement(query);
		return getRowMapped(pStmt, args);
	}

	public HashMap<String, Object> getRowMapped(PreparedStatement pStmt, Object... args) throws Exception {
		CheckConnection();
		HashMap<String, Object> row = new HashMap<String, Object>();
		bindStatementParams(pStmt, args);
		ResultSet rs = pStmt.executeQuery();
		ResultSetMetaData metadata = rs.getMetaData();
		while (rs.next()) {
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				row.put(metadata.getColumnName(i), rs.getObject(i));
			}
		}
		rs.close();
		return row;
	}

	public ArrayList<Object[]> getResultSet(String query, Object... args) throws Exception {
		PreparedStatement pStmt = prepareStatement(query);
		return getResultSet(pStmt, args);
	}

	public ArrayList<Object[]> getResultSet(PreparedStatement pStmt, Object... args) throws Exception {
		CheckConnection();
		ArrayList<Object[]> result = new ArrayList<Object[]>();
		bindStatementParams(pStmt, args);
		ResultSet rs = pStmt.executeQuery();
		ResultSetMetaData metadata = rs.getMetaData();
		while (rs.next()) {
			Object[] row = new Object[metadata.getColumnCount()];
			for (int i = 0; i < metadata.getColumnCount(); i++) {
				row[i] = rs.getObject(i + 1);
			}
			result.add(row);
		}
		rs.close();
		return result;
	}

	public ArrayList<HashMap<String, Object>> getResulSetMapped(String query, Object... args) throws Exception {
		PreparedStatement pStmt = prepareStatement(query);
		return getResulSetMapped(pStmt, args);
	}

	public ArrayList<HashMap<String, Object>> getResulSetMapped(PreparedStatement pStmt, Object... args) throws Exception {
		CheckConnection();
		ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		bindStatementParams(pStmt, args);
		ResultSet rs = pStmt.executeQuery();
		ResultSetMetaData metadata = rs.getMetaData();
		while (rs.next()) {
			HashMap<String, Object> row = new HashMap<String, Object>();
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				row.put(metadata.getColumnName(i), rs.getObject(i));
			}
			result.add(row);
		}
		rs.close();
		return result;
	}

	private CallableStatement prepareCallableStatement(String storedProcedure, int args) throws SQLException {
		if (args < 1) {
			return null;
		}
		String cStmt = "{ call " + storedProcedure + " ( ?";

		for (int i = 1; i < args; i++) {
			cStmt += ", ?";
		}
		cStmt += ", ? ) }";

		return connection.prepareCall(cStmt);
	}

	public int ExecuteProcedure(String storedProcedure, Object... args) throws Exception {
		CheckConnection();
		int i = 1;

		CallableStatement cStmt = prepareCallableStatement(storedProcedure, args.length);

		for (Object arg : args) {
			if (arg.getClass().getName().toString().equals("java.util.Date")) {
				cStmt.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
			} else if (arg instanceof Timestamp) {
				cStmt.setTimestamp(i++, (Timestamp) arg);
			} else if (arg instanceof Integer) {
				cStmt.setInt(i++, (Integer) arg);
			} else if (arg instanceof Byte) {
				cStmt.setByte(i++, (Byte) arg);
			} else if (arg instanceof Long) {
				cStmt.setLong(i++, (Long) arg);
			} else if (arg instanceof Double) {
				cStmt.setDouble(i++, (Double) arg);
			} else if (arg instanceof Float) {
				cStmt.setFloat(i++, (Float) arg);
			} else if (arg instanceof Short) {
				cStmt.setShort(i++, (Short) arg);
			} else if (arg instanceof Boolean) {
				cStmt.setInt(i++, ((Boolean) arg) ? 1 : 0);
			} else {
				cStmt.setString(i++, (String) arg);
			}
		}
		cStmt.registerOutParameter(i, Types.INTEGER);
		cStmt.execute();
		return cStmt.getInt(i);
	}

	public int ExecuteQuery(String query, Object... args) throws Exception {
		PreparedStatement pStmt = prepareStatement(query);
		bindStatementParams(pStmt, args);
		return pStmt.executeUpdate();
	}
}
