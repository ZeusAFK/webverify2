package data.models;

import java.util.Date;

import data.enums.ScanStatus;

public class Scan {

	private int id;
	private Site site;
	private Date start;
	private Date end;
	private int ocurrences;
	private Date created;
	private ScanStatus status;

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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public int getOcurrences() {
		return ocurrences;
	}

	public void setOcurrences(int ocurrences) {
		this.ocurrences = ocurrences;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public ScanStatus getStatus() {
		return status;
	}

	public void setStatus(ScanStatus status) {
		this.status = status;
	}

}
