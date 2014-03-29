package com.ideax.spider.xiami;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.ideax.spider.common.Util;
import com.ideax.spider.kting.pojo.NovelAudioChapters;
import com.ideax.spider.kting.pojo.NovelAudioKting;
import com.ideax.spider.xiami.poji.SongXiami;

public class XiamiSpider {

	static Logger logger = LoggerFactory.getLogger(XiamiSpider.class);

	public static Map<String, String> headers = new HashMap<String, String>();
	static {

		headers.put(
				"Cookie",
				"kt_token_expir=1395846575; kt_token=00f966e1118c5d80375aaa2642d14c0f%2A1512568; username=%E6%88%91%E5%B0%B1%E7%9C%8B%E7%9C%8B_%E4%B8%8D%E8%AF%B4%E8%AF%9D; phone_aulit=0; email_aulit=1; is_ace=0; Hm_lvt_3b863cf5c37d78d1e58a6c3196ad1226=1395759254,1395759288; Hm_lpvt_3b863cf5c37d78d1e58a6c3196ad1226=1395761484; CNZZDATA1000045199=1492302134-1395759255-http%253A%252F%252Fwww.baidu.com%252F%7C1395759255");
		headers.put("Referer", "http://rank.kting.cn/top_book/3_1.html");
	}

	public static Map<String, String> mp3headers = new HashMap<String, String>();
	static {

		mp3headers
				.put("Cookie",
						"kt_token_expir=1395846575; Hm_lvt_3b863cf5c37d78d1e58a6c3196ad1226=1395759254,1395759288,1395845856; Hm_lpvt_3b863cf5c37d78d1e58a6c3196ad1226=1395855688");
		mp3headers.put("Referer", "http://rank.kting.cn/top_book/3_1.html");
	}

	final String path = "/home/data/xiami/";

	String xiamiFile = "xiamisong.txt";

	int start = 1;
	int end = 1;

	public XiamiSpider(int start2, int end2) {
		this.start = start2;
		this.end = end2;
		xiamiFile = path + "xiamisong_" + start + "-" + end + ".txt";
	}

	public static void main(String args[]) throws IOException {

//		int start = 1, end = 1;
//		if (args != null && args.length > 1) {
//			start = Integer.parseInt(args[0]);
//			end = Integer.parseInt(args[1]);
//		}
//
//		XiamiSpider sp = new XiamiSpider(start, end);
//		sp.start();
		
		System.out.println(decodeXiamiUrl("4h%2Ff.moFF2%911F6E22m3teD99edde5511%55ut3Fmixim16F5%772747_pFhy13d5%9cd6335EEltA%5li.%%92E25%1%_7l3a_%26975d43297E--lp%2.eac22%56F626537.%uk3d514E786-66%%n"));
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void start() throws IOException {
		PrintWriter writer = new PrintWriter(new File(xiamiFile), "UTF-8");

		logger.info("START...");
		String baseUrl = "http://www.xiami.com/artist/tag/%E6%AC%A7%E7%BE%8E/page/";
		int blockRetry = 0;
		for (int i = start; i < end; i++) {
			String listUrl = baseUrl + i;
			logger.info("START artist..." + listUrl);
			Document listdoc = getDocument(listUrl, 5);
			if (listdoc == null) {
				logger.error("get List page error : " + listUrl);
				continue;
			}
			Elements els = listdoc.select(".buddy > a");
			if (els == null || els.size() == 0) {
				logger.error("Content has no result , END? or BLOCKED? see: " + listdoc.toString());
				blockRetry++;
				if (blockRetry > 10) {
					logger.error("Retry 10 times give up, exit at : " + listUrl);
					blockRetry = 0;
					break; // 不抓了，到头了
				} else {
					i--;
					continue;
				}
			}

			for (Element el : els) {
				/*--------------歌手id--------------*/
				String contentUrl = el.attr("href");
				int artistid;
				try {
					int j = contentUrl.lastIndexOf('?');
					artistid = Integer.parseInt(contentUrl.substring(contentUrl.lastIndexOf('/') + 1, j > 0 ? j
							: contentUrl.length()));
				} catch (Exception e) {
					logger.error("artist id is invalid continue next: url " + contentUrl);
					continue;
				}

				/*--------------获取歌曲的hot 歌曲-------------*/
				for (int hoti = 1; hoti < 6; hoti++) {
					String hotListUrl = "http://www.xiami.com/artist/top/id/" + artistid + "/page/" + hoti;
					Document hotListDoc = getDocument(hotListUrl, 5);

					Elements songLineEls = hotListDoc.select(".track_list > tr");

					if (songLineEls != null && songLineEls.size() > 0) {
						for (Element songLineEl : songLineEls) {
							SongXiami song = new SongXiami();
							song.setXiamiArtistLink(contentUrl);
							String songLink = getFirstElAttr(songLineEl, ".song_name > a", "href");

							if (!songLink.startsWith("http")) {
								songLink = "http://www.xiami.com" + songLink;
							}
							int songid = 0;
							try {
								int j = songLink.lastIndexOf('?');
								songid = Integer.parseInt(songLink.substring(songLink.lastIndexOf('/') + 1, j > 0 ? j
										: songLink.length()));
							} catch (Exception e) {
								logger.error("songid is invalid continue next: url " + songLink);
								continue;
							}

							int songHot = getFirstElTextNumber(songLineEl, ".song_hot");
							song.setListenNum(songHot);
							song.setXiamiSongLink(songLink);

							// 只抓试听超1w的 精品
							if (songHot > 10000) {

								// catchDetailInfoFromXiami(song); //
								// 不抓歌曲页面啦，下面xml信息好全啊

								String mp3XmlUrl = "http://www.xiami.com/song/playlist/id/" + songid
										+ "/object_name/default/object_id/0?t=" + System.currentTimeMillis();

								String mp3XmlStr = Util.getHttpResponseString(mp3XmlUrl, "get", null, mp3headers);
								org.dom4j.Document mp3Xml;
								try {
									mp3Xml = DocumentHelper.parseText(mp3XmlStr);
								} catch (DocumentException e1) {
									logger.error("not xml..." + mp3XmlStr);
									continue;
								}
								org.dom4j.Element root = mp3Xml.getRootElement();
								org.dom4j.Element track = root.element("track");
								if (track != null) {
									song.setXiamiSongName(track.element("title").getTextTrim());
									song.setXiamiAlbum(track.element("album_name").getTextTrim());
									song.setXiamiAlbumLink(track.element("album_id").getTextTrim());
									song.setXiamiArtist(track.element("artist").getTextTrim());
									song.setXiamiArtistLink(track.element("artist_url").getTextTrim());
									song.setXiamiLyricUrl(track.element("lyric").getTextTrim());
									song.setXiamiCoverImg(track.element("pic").getTextTrim());
									try {
										song.setDuration(Integer.parseInt(track.element("length").getText()));
									} catch (Exception e) {
										logger.error("no duration..." + mp3XmlStr);
										continue;
									}
									
									String surl = track.element("location").getTextTrim();
									
								}
							}

							writer.println(Util.mapper.writeValueAsString(song));
							writer.flush();
						}
					} else {
						break;
					}

				}

			}
		}
		writer.close();
	}

	public float floatValueSafe(String s) {
		try {
			return Float.parseFloat(s);
		} catch (Exception e) {
			return 0.0f;
		}
	}

	public int intValueSafe(String s) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
	}

	public Element getFirstEl(Element parent, String path) {
		Elements coverEls = parent.select(path);
		if (coverEls != null && coverEls.size() > 0) {
			return coverEls.get(0);
		}
		return null;
	}

	public String getFirstElAttr(Element parent, String path, String attr) {
		Element el = getFirstEl(parent, path);
		if (el != null)
			return el.attr(attr);
		return "";
	}

	public String getFirstElText(Element parent, String path) {
		Element el = getFirstEl(parent, path);
		if (el != null)
			return el.text().trim();
		return "";
	}

	public int getFirstElTextNumber(Element parent, String path) {
		Element el = getFirstEl(parent, path);
		if (el != null) {
			try {
				return Integer.parseInt(el.text().trim());
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return 0;
	}

	public int getFirstElAttrNumber(Element parent, String path, String attr) {
		Element el = getFirstEl(parent, path);
		if (el != null)
			try {
				return Integer.parseInt(el.attr(attr));
			} catch (NumberFormatException e) {
				return 0;
			}
		return 0;
	}

	private static Document getDocument(String url, int retryTime) {
		Connection con = HttpConnection.connect(url);
		setHeaders(con);
		con.timeout(30000);
		// 列表页面doc
		Document listdoc = null;
		int retry = 0;
		do {
			try {
				listdoc = con.get();
				break;
			} catch (IOException e) {
				logger.error("GET page IO error: " + url);
				if (retry >= retryTime) {
					logger.error("Retry " + retryTime + " times give up, continue next : " + url);
					break;
				} else {
					retry++;
				}
			}
		} while (true);
		return listdoc;
	}

	public static void setHeaders(Connection con) {
		con.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
		con.header(
				"Cookie",
				"kt_token_expir=1395846575; kt_token=00f966e1118c5d80375aaa2642d14c0f%2A1512568; username=%E6%88%91%E5%B0%B1%E7%9C%8B%E7%9C%8B_%E4%B8%8D%E8%AF%B4%E8%AF%9D; phone_aulit=0; email_aulit=1; is_ace=0; Hm_lvt_3b863cf5c37d78d1e58a6c3196ad1226=1395759254,1395759288; Hm_lpvt_3b863cf5c37d78d1e58a6c3196ad1226=1395761484; CNZZDATA1000045199=1492302134-1395759255-http%253A%252F%252Fwww.baidu.com%252F%7C1395759255");
		con.header("Referer", "http://rank.kting.cn/top_book/3_1.html");
	}

	public static void catchDetailInfoFromXiami(SongXiami song) {

		// 专辑图片
		Document albumDoc = getDocument(song.getXiamiSongLink(), 5);
		if (albumDoc == null) {
			logger.error("CANNOT get song page " + song.getXiamiSongLink());
			return;
		}

		Elements coverels = albumDoc.select(".cdCDcover185");
		if (coverels.size() > 0) {
			Element img = coverels.get(0);
			String src = img.attr("src");
			if (StringUtils.isNotBlank(src))
				song.setXiamiCoverImg(src);
		}
		//
		Elements albuminfo = albumDoc.select("#albums_info");
		Elements albumels = albuminfo.select(".item");
		if (albumels.size() > 0) {
			for (Element item : albumels) {
				String key = item.text();
				String value = null;
				String link = null;
				Element valueEl = item.nextElementSibling();
				if (valueEl != null) {
					try {
						Element div = valueEl.child(0);
						if (div.children().size() > 0) {
							Element a = div.child(0);
							value = a.text();
							link = a.attr("href");
						} else {
							value = div.text();
						}
					} catch (IndexOutOfBoundsException e) {
					}
				}
				if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
					if (key.contains("专辑")) {
						song.setXiamiAlbum(value);
						song.setXiamiAlbumLink(link);
					} else if (key.contains("演唱")) {
						song.setXiamiArtist(value);
						song.setXiamiArtistLink(link);
					} else if (key.contains("词")) {
						song.setXiamiLyricist(value);
					} else if (key.contains("作曲")) {
						song.setXiamiComposer(value);
					}
				}
			}
		}
		//
		Elements lrcs = albumDoc.select(".lrc_main");
		if (lrcs.size() > 0) {
			Element lrc = lrcs.get(0);
			String lrcstr = lrc.html();
			if (StringUtils.isNotBlank(lrcstr)) {
				song.setXiamiLyric(lrcstr);
			}
		}
	}
	
	public static String decodeXiamiUrl(String location){
		String ls = location.substring(0,1);
		int l = Integer.parseInt(ls);
	    String t = location.substring(1);
	    int tn = t.length();
	    int ln = tn/l;
	    int r = tn % l;
	    char [] tex = t.toCharArray();
	    StringBuilder text = new StringBuilder();
	    for(int i=0;i<=ln;i++) {
	        for(int j=0;j<l;j++) {
	            int n = j*ln+i;
	            if(j<r) n += j;
	            else n += r;
	            if(n < tex.length)  text.append(tex[n]);
	            else break;
	        }
	    }
	    try {
			return URLDecoder.decode(text.subSequence(0, tn).toString(), "UTF-8").replace('^', '0').replace('%', '|');
		} catch (UnsupportedEncodingException e) {
		}
	    return "";
	}

}
