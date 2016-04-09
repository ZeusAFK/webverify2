package tasks;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.StringUtils;
import utils.ThreadUtils;
import network.http.HttpRequest;
import data.collections.LinksCollection;
import data.models.Link;
import data.models.Site;

public class LinkGrabberTask {

	private Link base_link;
	private String url;
	private LinksCollection links;
	private Site site;

	public LinkGrabberTask(Site site, Link currentLink) {
		this.base_link = currentLink;
		this.site = site;
		links = new LinksCollection();
		links.add(currentLink);
	}

	public boolean CheckLink(boolean crawl) {
		url = base_link.getUrl();
		base_link.setExtern(checkExternal(base_link.getUrl()));

		if (base_link.isExtern()) {
			StringUtils.printInfo("External url: " + url);
			return false;
		}

		base_link.setActive(checkIsActive(base_link.getUrl()));

		if (!base_link.isActive()) {
			StringUtils.printWarning("Inactive url: " + url);
			return false;
		} else {
			StringUtils.printInfo("Active url: " + url);
		}

		if (checkIsSource(base_link.getUrl()) && !crawl) {
			StringUtils.printInfo("Skipping url: " + url);
			return false;
		}

		base_link.setText(checkIsText(base_link.getUrl()));
		base_link.setImage(!base_link.isText());

		int contentSize = checkSize(base_link.getUrl());

		if (!base_link.isText()) {
			base_link.setSize(contentSize);
			StringUtils.printInfo("Resource url: " + url + " [" + contentSize + " bytes]");
		}

		if (base_link.isText() && crawl) {
			return GrabLinks(base_link.getUrl());
		}

		return true;
	}

	public LinksCollection getLinks() {
		return links;
	}

	private boolean checkExternal(String url) {
		try {
			return !new URL(url).getHost().equals(new URL(site.getUrl()).getHost());
		} catch (MalformedURLException e) {
			return true;
		}
	}

	private boolean checkIsText(String url) {
		return new HttpRequest(url).getContentType().startsWith("text/");
	}

	private int checkSize(String url) {
		return new HttpRequest(url).getContentLength();
	}

	private boolean checkIsActive(String url) {
		return new HttpRequest(url).Exists();
	}

	private boolean checkIsSource(String url) {
		return new HttpRequest(url).getContentType().toLowerCase().contains("html");
	}

	private boolean GrabLinks(String url) {
		ArrayList<String> found = new ArrayList<String>();

		int attempts = 0;
		Document doc = null;
		while (attempts < 10) {
			try {
				doc = Jsoup.connect(url).get();
				break;
			} catch (IOException e) {
				attempts++;
				StringUtils.printWarning("Net timeout: " + url + " waiting " + attempts + " seconds");
				ThreadUtils.sleepSeconds(attempts);
			}
		}

		if (attempts >= 10) {
			StringUtils.printWarning("Net timeout: " + url + " no more attempts allowed");
			return false;
		}

		int contentSize = doc.toString().length();
		base_link.setSize(contentSize);
		StringUtils.printInfo("Crawling url: " + url + " [" + contentSize + " chars]");

		Elements alinks = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		for (Element src : media) {
			found.add(src.tagName().equals("img") ? src.attr("abs:src") : src.attr("abs:src"));
		}

		for (Element link : imports) {
			found.add(link.attr("abs:href"));
		}

		for (Element link : alinks) {
			found.add(link.attr("abs:href"));
		}

		for (String lUrl : found) {
			if (lUrl.startsWith("/") || !lUrl.toLowerCase().startsWith("http")) {
				StringUtils.printInfo("Malformed url: " + lUrl);
				if (StringUtils.countMatches(url, lUrl) > 2) {
					StringUtils.printWarning("Possible path loop detected, skipping url");
					continue;
				} else {
					try {
						if (url.endsWith(lUrl)) {
							url = url.replace(lUrl, "");
						}
						URL uurl = new URL(url);
						String path = uurl.getFile().substring(0, uurl.getFile().lastIndexOf('/'));
						if (path.equals("")) {
							lUrl = uurl.getProtocol() + "://" + uurl.getHost() + lUrl;
						} else {
							lUrl = uurl.getProtocol() + "://" + uurl.getHost() + path + lUrl;
						}
					} catch (Exception e) {
						try {
							URL uurl = new URL(url);
							lUrl = uurl.getProtocol() + "://" + uurl.getHost() + lUrl;
						} catch (MalformedURLException e1) {
							lUrl = url + lUrl;
						}
					}
				}
				StringUtils.printInfo("Fixed url: " + lUrl);
			}

			links.add(new Link(lUrl), true);
		}
		return true;
	}
}
