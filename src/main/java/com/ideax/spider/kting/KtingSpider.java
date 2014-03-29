package com.ideax.spider.kting;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
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

public class KtingSpider {

	Logger logger = LoggerFactory.getLogger(this.getClass());

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

	final String path = "/home/data/kting/";

	String novelFile = "novel.txt";
	String chapterFile = "chapter.txt";

	int start = 1;
	int end = 1;

	public KtingSpider(int start2, int end2) {
		this.start = start2;
		this.end = end2;
		novelFile = path + "novel_" + start + "-" + end + ".txt";
		chapterFile = path + "chapter_" + start + "-" + end + ".txt";
	}

	public static void main(String args[]) throws IOException {

		int start = 1, end = 1;
		if (args != null && args.length > 1) {
			start = Integer.parseInt(args[0]);
			end = Integer.parseInt(args[1]);
		}

		KtingSpider sp = new KtingSpider(start, end);

		sp.start();
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void start() throws IOException {
		PrintWriter writer = new PrintWriter(new File(novelFile), "UTF-8");
		PrintWriter cwriter = new PrintWriter(new File(chapterFile), "UTF-8");

		logger.info("START...");
		String baseUrl = "http://www.kting.cn/library/-1//r_rank/desc_";
		int blockRetry = 0;
		for (int i = start; i < end; i++) {
			String listUrl = baseUrl + i + ".html";
			Document listdoc = getDocument(listUrl, 5);
			if (listdoc == null) {
				logger.error("get List page error : " + listUrl);
				continue;
			}
			Elements els = listdoc.select("#rectangle_div > ul > li > div > a");
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
				/*--------------获取正文链接--------------*/
				String contentUrl = el.attr("href");
				NovelAudioKting novel = new NovelAudioKting();
				int ktId;
				try {
					ktId = Integer.parseInt(contentUrl.substring(contentUrl.lastIndexOf('/') + 1,
							contentUrl.lastIndexOf('.')));
					novel.setKtId(ktId);
				} catch (Exception e) {
					logger.error("Kting id is invalid continue next: url " + contentUrl);
					continue;
				}

				/*--------------打开正文链接 获取 详细信息--------------*/
				Document contentDoc = getDocument(contentUrl, 5);
				if (contentDoc == null) {
					logger.error("get CONTENT page error : " + contentUrl);
					continue;
				}

				// 类别
				Elements catEls = contentDoc.select(".kting_argetst > a");
				if (catEls != null && catEls.size() > 0) {
					Element catEl = catEls.get(catEls.size() - 1);
					String catLink = catEl.attr("href");
					try {
						int j = catLink.indexOf("library/") + 8;
						int j2 = catLink.indexOf('/', j);
						novel.setCatId(Integer.parseInt(catLink.substring(j, j2)));
						novel.setCatIdSub(Integer.parseInt(catLink.substring(j2 + 1, catLink.indexOf('/', j2 + 1))));
					} catch (Exception e) {
						logger.error("Cannot get CATID : " + catLink);
					}
				}

				// cover
				novel.setCoverImg(getFirstElAttr(contentDoc, ".first_img > img", "src"));

				// detail info
				logger.info("{}: START details: ", ktId);
				Element detailEl = getFirstEl(contentDoc, ".first_con");
				if (detailEl != null) {
					// title
					novel.setName(getFirstElText(detailEl, "h1"));
					novel.setPlayedNum(getFirstElTextNumber(detailEl, ".dts-02"));
					Element dt_page02 = getFirstEl(detailEl, ".dt-page02");
					if (dt_page02 != null) {
						for (Element el1 : dt_page02.children()) {
							String ss[] = el1.text().split("：");
							if (ss.length > 1) {
								if ("章节".equals(ss[0].trim())) {
									String sss[] = ss[1].split("（");
									novel.setChapterNum(intValueSafe(sss[0].replace("章节", "").trim()));
									if (sss.length > 1)
										novel.setTotalTime((int) (floatValueSafe(sss[1].replace("）", "")
												.replace("小时", "").trim()) * 60));
								} else if ("上架时间".equals(ss[0].trim())) {
									novel.setAddDate(ss[1].trim());
								} else if ("作者".equals(ss[0].trim())) {
									if (!ss[1].equals("--")) {
										novel.setAuthor(ss[1].trim());
										novel.setAuthorLink(getFirstElAttr(el1, "a", "href"));
									}
								} else if ("更新时间".equals(ss[0].trim())) {
									novel.setUpdateDate(ss[1].trim());
								} else if ("播音".equals(ss[0].trim())) {
									if (!ss[1].equals("--")) {
										novel.setReader(ss[1].trim());
										novel.setReaderLink(getFirstElAttr(el1, "a", "href"));
									}
								} else if ("作品提供".equals(ss[0].trim())) {

								}
							}
						}
					}
				}

				// tags
				logger.info("{}: START tags: ", ktId);
				Elements tagEls = contentDoc.select(".first_con5 > dl > dd > a");
				if (tagEls != null && tagEls.size() > 0) {
					StringBuilder sb = new StringBuilder();

					for (Element tagel : tagEls) {
						if (sb.length() != 0)
							sb.append(',');
						sb.append(tagel.text());
					}
					novel.setTags(sb.toString());
				}

				// description
				logger.info("{}: START description: ", ktId);
				novel.setDescription(getFirstElText(contentDoc, "#chapterContentIntro"));

				writer.println(Util.mapper.writeValueAsString(novel));
				writer.flush();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// chapters
				logger.info("{}: START CHAPTER: ", ktId);
				Elements chapterEls = contentDoc.select("#hiddenresult dd");
				if (chapterEls != null && chapterEls.size() > 0) {
					for (Element chapterel : chapterEls) {
						int index = 0;
						NovelAudioChapters ch = new NovelAudioChapters();
						for (Element span : chapterel.children()) {
							if (index == 0) {
								ch.setId(getFirstElAttrNumber(span, "input", "value"));
							} else if (index == 1) {
								try {
									ch.setChapterIndex(Integer.parseInt(span.text()));
								} catch (Exception e) {
								}
							} else if (index == 2) {
								ch.setKtName(span.text());
							} else if (index == 3) {
								ch.setKtDuration(span.text());
							}
							index++;
						}
						logger.info("{}\t{}: START CHAPTER: ", ktId, ch.getId());

						String infoUrl = "http://www.kting.cn/play/type/checkPlay/id/" + ktId + "/aid/" + ch.getId()
								+ "/isNotAuto/1/uid/0/t/0.3627744442783296?_=1395855688692";
						String infoStr = Util.getHttpResponseString(infoUrl, "get", null, mp3headers);
						try {
							JsonNode infoNode = Util.mapper.readTree(infoStr);
							JsonNode dataNode = infoNode.get("data");
							if (dataNode != null) {
								String mp3Url = dataNode.get("audio").asText();
								ch.setKtStreamUrl(mp3Url);
							} else {
								logger.warn("cannot get mp3 url :" + infoStr);
							}
						} catch (Exception e) {
							logger.error("return malformed :" + infoStr);
						}
						cwriter.println(Util.mapper.writeValueAsString(ch));
						cwriter.flush();
						logger.info("{}\t{}: DONE CHAPTER: ", ktId, ch.getId());
					}
				}

				// int chapterPage = 1;
				// logger.info("{} : START CHAPTERS {}", ktId, chapterPage);
				// while (true) {
				// logger.info("{} : DOING PAGE: {}", ktId, chapterPage);
				// String chapterUrl =
				// "http://ktapi.kting.cn/api.php?act=book_article_type_all_new&rt=jsonp&size=10&uid=1512568&token=00f966e1118c5d80375aaa2642d14c0f&book_id="
				// + ktId
				// + "&pid="
				// + chapterPage
				// +
				// "&t=0.6130354113411158&jsonpcallback=jQuery172010503127728588879_1395765092021&_=1395767536888";
				//
				// String str = Util.getHttpResponseString(chapterUrl, "get",
				// null, headers);
				// if (StringUtils.isNotBlank(str)) {
				// JsonNode node = null;
				// try {
				// node = Util.mapper.readTree(str.substring(str.indexOf('(') +
				// 1, str.lastIndexOf(')')));
				// } catch (Exception e) {
				// logger.error("return malformed :" + str);
				// break;
				// }
				// JsonNode asnode = node.get("article");
				// if (asnode != null) {
				// ArrayNode arr = asnode.isArray() ? (ArrayNode) asnode : null;
				// if (arr != null) {
				// for (JsonNode a : arr) {
				// NovelAudioChapters ch = new NovelAudioChapters();
				// ch.setId(a.get("id").asInt());
				// ch.setChapterIndex(a.get("section_index").asInt());
				// ch.setKtId(ktId);
				//
				// String infoUrl =
				// "http://www.kting.cn/play/type/checkPlay/id/" + ktId +
				// "/aid/"
				// + ch.getId() +
				// "/isNotAuto/1/uid/0/t/0.3627744442783296?_=1395855688692";
				// String infoStr = Util.getHttpResponseString(infoUrl, "get",
				// null, mp3headers);
				// try {
				// JsonNode infoNode = Util.mapper.readTree(infoStr.substring(
				// str.indexOf('(') + 1, infoStr.lastIndexOf(')')));
				// JsonNode dataNode = infoNode.get("data");
				// if (dataNode != null) {
				// String mp3Url = dataNode.get("audio").asText();
				// ch.setKtStreamUrl(mp3Url);
				// }
				// } catch (Exception e) {
				// logger.error("return malformed :" + infoStr);
				// }
				// cwriter.println(Util.mapper.writeValueAsString(ch));
				// logger.info("{} : DOING PAGE: {}", ktId, chapterPage);
				//
				// }
				// }
				// } else {
				// break;
				// }
				// } else {
				// break;
				// }
				// chapterPage++;
				// }
				// logger.info("{} : END CHAPTERS {}", ktId, chapterPage);

			}
		}

		writer.close();
		cwriter.close();
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

	private Document getDocument(String url, int retryTime) {
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

}
