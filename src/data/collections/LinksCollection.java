package data.collections;

import java.util.ArrayList;

import data.models.Link;

@SuppressWarnings("serial")
public class LinksCollection extends ArrayList<Link> {

	public Link findByUrl(String url) {
		for (Link link : this) {
			if (link.getUrl().equals(url))
				return link;
		}
		return null;
	}

	public boolean add(Link link, boolean override) {
		if (override) {
			if (this.getByUrl(link.getUrl()) == null) {
				this.add(link);
				return true;
			} else {
				return false;
			}
		} else {
			this.add(link);
			return true;
		}
	}

	public Link getByUrl(String url) {
		for (Link link : this) {
			if (link.getUrl().equals(url)) {
				return link;
			}
		}
		return null;
	}
}
