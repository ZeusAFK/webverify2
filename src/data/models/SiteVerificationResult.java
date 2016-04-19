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

public class SiteVerificationResult {

	private int links;
	private int created;
	private int modified;
	private int deleted;

	public SiteVerificationResult() {
		links = 0;
		created = 0;
		modified = 0;
		deleted = 0;
	}

	public int getLinks() {
		return links;
	}

	public int getCreated() {
		return created;
	}

	public int getModified() {
		return modified;
	}

	public int getDeleted() {
		return deleted;
	}

	public boolean hasLinks() {
		return links > 0;
	}

	public boolean hasCreated() {
		return created > 0;
	}

	public boolean hasModified() {
		return modified > 0;
	}

	public boolean hasDeleted() {
		return deleted > 0;
	}

	public void setLinks(int links) {
		this.links = links;
	}

	public void setCreated(int created) {
		this.created = created;
	}

	public void setModified(int modified) {
		this.modified = modified;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public void addCreated() {
		created++;
	}

	public void addModified() {
		modified++;
	}

	public void addDeleted() {
		deleted++;
	}
}
