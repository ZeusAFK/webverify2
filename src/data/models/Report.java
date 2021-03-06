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
