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

import java.util.Date;
import java.util.Observable;

import tasks.InitializeTask;
import utils.StringUtils;
import data.enums.ScanScheduleStatus;

public class ScanSchedule extends Observable {

	private int id;
	private Site site;
	private int interval;
	private Date lastScan;
	private Date limit;
	private int priority;
	private User user;
	private boolean build;
	private boolean crawl;
	private Date created;
	private ScanScheduleStatus status;

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

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Date getLimit() {
		return limit;
	}

	public void setLimit(Date limit) {
		this.limit = limit;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isBuild() {
		return build;
	}

	public void setBuild(boolean build) {
		this.build = build;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public ScanScheduleStatus getStatus() {
		return status;
	}

	public void setStatus(ScanScheduleStatus status) {
		this.status = status;
		update();
	}

	public Date getLastScan() {
		return lastScan;
	}

	public void setLastScan(Date lastScan) {
		this.lastScan = lastScan;
		update();
	}

	public boolean isCrawl() {
		return crawl;
	}

	public void setCrawl(boolean crawl) {
		this.crawl = crawl;
	}

	public void Persist() {
		if (id > 0) {
			try {
				InitializeTask.database.ExecuteProcedure("updateSchanScheduleById", id, interval, lastScan, limit, priority, build, crawl, status.getValue());
			} catch (Exception e) {
				StringUtils.printWarning("Error while persist data for ScanSchedule: " + id);
				e.printStackTrace();
			}
		}
		update();
	}

	public void update() {
		setChanged();
		notifyObservers();
	}
}
