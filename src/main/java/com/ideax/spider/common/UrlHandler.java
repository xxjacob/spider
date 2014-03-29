package com.ideax.spider.common;

import org.jsoup.nodes.Element;

public class UrlHandler implements Handler {

	public void deal(Element node) {
		if (node == null)
			return;
		String url = node.text().trim();
		
		
	}

}
