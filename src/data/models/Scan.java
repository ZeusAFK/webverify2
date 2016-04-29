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
import data.collections.LinksCollection;
import data.collections.ScanAssetsCollection;
import data.enums.ScanStatus;

public class Scan extends Observable {

	private int id;
	private Site site;
	private Date start;
	private Date end;
	private String ip;
	private int ocurrences;
	private Date created;
	private ScanStatus status;
	private ScanAssetsCollection assets;
	private LinksCollection crawlLinks;

	public Scan() {
		assets = new ScanAssetsCollection();
		crawlLinks = new LinksCollection();
		status = ScanStatus.Created;
	}

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
		update();
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
		update();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		update();
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
		update();
	}

	public ScanStatus getStatus() {
		return status;
	}

	public void setStatus(ScanStatus status) {
		this.status = status;
		update();
	}

	public void Persist() {
		if (id == 0) {
			try {
				id = InitializeTask.database.ExecuteProcedure("createScanEntry", site.getId(), start, ip, ocurrences, status.getValue());
			} catch (Exception e) {
				StringUtils.printWarning("Error while persist data for scan: " + id);
				e.printStackTrace();
			}
		} else {
			try {
				InitializeTask.database.ExecuteProcedure("updateScanEntry", id, site.getId(), start, end, ip, ocurrences, status.getValue());
			} catch (Exception e) {
				StringUtils.printWarning("Error while persist data for scan: " + id);
				e.printStackTrace();
			}
		}
		update();
	}

	public ScanAssetsCollection getAssets() {
		return assets;
	}

	public LinksCollection getCrawlLinks() {
		return crawlLinks;
	}

	public void update() {
		setChanged();
		notifyObservers();
	}
}
