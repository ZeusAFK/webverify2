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

package data.collections;

import java.util.ArrayList;
import java.util.Observable;

import data.models.Link;

public class LinksCollection extends Observable {

	private ArrayList<Link> collection;

	public LinksCollection() {
		collection = new ArrayList<Link>();
	}

	public Link findByUrl(String url) {
		for (Link link : collection) {
			if (link.getUrl().equals(url))
				return link;
		}
		return null;
	}

	public boolean add(Link link, boolean override) {
		if (override) {
			if (this.getByUrl(link.getUrl()) == null) {
				collection.add(link);
				update();
				return true;
			} else {
				return false;
			}
		} else {
			collection.add(link);
			return true;
		}
	}

	public Link getByUrl(String url) {
		for (Link link : collection) {
			if (link.getUrl().equals(url)) {
				return link;
			}
		}
		return null;
	}

	public int size() {
		return collection.size();
	}

	public ArrayList<Link> getCollection() {
		return collection;
	}
	
	public void update(){
		setChanged();
		notifyObservers();
	}
}
