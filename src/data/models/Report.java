package data.models;

import java.util.Date;

import data.enums.ReportStatus;

public class Report {

	private int id;
	private Scan scan;
	private ReportConfiguration configuration;
	private Date created;
	private ReportStatus status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Scan getScan() {
		return scan;
	}

	public void setScan(Scan scan) {
		this.scan = scan;
	}

	public ReportConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ReportConfiguration configuration) {
		this.configuration = configuration;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}

}
