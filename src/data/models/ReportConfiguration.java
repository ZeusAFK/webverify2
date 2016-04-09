package data.models;

import java.util.Date;

import data.enums.ReportType;

public class ReportConfiguration {

	private int id;
	private Site site;
	private ReportType type;
	private Scan target;
	private Date created;
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
	}

	public Scan getTarget() {
		return target;
	}

	public void setTarget(Scan target) {
		this.target = target;
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
