package com.ideax.spider.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * spider's
 * 
 * @author
 * 
 */
public class Gossamer {

	Map<String, Handler> handlers = new HashMap<String, Handler>();

	Map<String, String> headers = null;

	Connection con;

	public Gossamer(String startUrl) {
		con = HttpConnection.connect(startUrl);
		assert (handlers != null);
	}
	
	public void addHandler(String path, Handler handler){
		this.handlers.put(path, handler);
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
		if (headers != null) {
			for (Entry<String, String> a : headers.entrySet()) {
				con.header(a.getKey(), a.getValue());
			}
		}
	}

	public void start() {
		try {
			Document doc = con.get();
			for (Entry<String, Handler> en : handlers.entrySet()) {
				Elements els = doc.select(en.getKey());
				if (els != null && els.size() > 0) {
					for (Element el : els) {
						en.getValue().deal(el);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
