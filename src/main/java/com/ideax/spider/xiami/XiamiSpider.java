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
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.baidu.inf.iis.bcs.response.BaiduBCSResponse;
import com.ideax.spider.common.Util;
import com.ideax.spider.xiami.pojo.SongXiami;

public class XiamiSpider {

    static Logger logger = LoggerFactory.getLogger(XiamiSpider.class);

    public static Map<String, String> headers = new HashMap<String, String>();
    static {

        headers.put(
                "Cookie",
                "_unsign_token=04570114b0dbb7421c061588a62dabf3; bdshare_firstime=1389614046560; __gads=ID=3d44fac221bb2403:T=1389614076:S=ALNI_MZdNR1gmMpQebOFw-Ma2bV4dnNwzQ; Hm_lvt_3d143f0a07b6487f65609d8411e5464f=1393057948,1393253732,1393303827,1393335532; ab=0; member_auth=0jqbT4gZvmhv0KSSTIk0dicYt%2BzTHDSEkYoD3bR85AF1IIoANoTwlKuQRA1P3CSVkY2RtGsATg; user=1667446%22%E5%BE%90%E6%AC%A3%22images%2Favatar_new%2F33%2F34%2F1667446%2F1667446_1305879917_1.jpg%220%222789%22%3Ca+href%3D%27%2Fwebsitehelp%23help9_3%27+%3Ela%3C%2Fa%3E%220%2224%228173%22922d26080f%221394870464; ahtena_is_show=false; seiya_time=1395467190359%7C1395658904950; player_opencount=0; xiami_playlist_for_hao123=3381901%2C1771979714%2C1770986640%2C1771627825%2C1771889373%2C1770153579%2C1768995853%2C1771949597%2C1772130321%2C1769274193%2C180264%2C2079853%2C146898%2C17302%2C2079912%2C2124065%2C2138604%2C1771734083%2C174652%2C76280%2C1768944774%2C1771760408%2C137794%2C9910%2C1769271562%2C1770454888%2C2077237%2C383794%2C1771638197%2C1772001102%2C1769819214%2C1771829247%2C1769962750%2C165990%2C382776%2C1770752283%2C; recent_tags=%E5%8F%A4%E9%A3%8E+%E4%BA%AC%E5%89%A7+; t_sign_auth=1; pnm_cku822=093fCJmZk4PGRVHHxtNZngkZ3k%2BaC52PmgTKQ%3D%3D%7CfyJ6Zyd9OWAmanoqanktYRA%3D%7CfiB4D15%2BZH9geTp%2FJyN8OzVqKw4OEABJWV5aa0I%3D%7CeSRiYjNhIHA2cmI9f242d2MocTN0NnZlMXNtOHpvJHI5ZSBhdCVzCA%3D%3D%7CeCVoaEARTxFRCR5MFBAnIjxnIyAbChBSR14XI3B%2BUG4%3D%7CeyR8C0gHRQBBBxJHHwlJDBNWA10DRQkDVRMGWAAbXgUqHg%3D%3D%7CeiJmeiV2KHMvangudmM6eXk%2BAA%3D%3D; CNZZDATA921634=cnzz_eid%3D201959541-1389614053-%26ntime%3D1396068742%26cnzz_a%3D1%26ltime%3D1396068662878%26rtime%3D27; CNZZDATA2629111=cnzz_eid%3D750791331-1389614053-%26ntime%3D1396068742%26cnzz_a%3D1%26ltime%3D1396068663041%26rtime%3D21; __utma=251084815.1212903984.1389614024.1395994650.1396068663.52; __utmb=251084815.2.10.1396068663; __utmc=251084815; __utmz=251084815.1395897757.48.16.utmcsr=360.fm|utmccn=(referral)|utmcmd=referral|utmcct=/; _xiamitoken=3216f18932e945795bcb9432cd73e95c");
        headers.put("Referer", "http://rank.kting.cn/top_book/3_1.html");
    }

    public static Map<String, String> mp3headers = new HashMap<String, String>();
    static {
        mp3headers.put("Accept-Encoding", "gzip,deflate,sdch");
        mp3headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        mp3headers.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4");
        mp3headers.put("Cache-Control", "max-age=0");
        mp3headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        mp3headers
                .put("Cookie",
                        "_unsign_token=04570114b0dbb7421c061588a62dabf3; __gads=ID=3d44fac221bb2403:T=1389614076:S=ALNI_MZdNR1gmMpQebOFw-Ma2bV4dnNwzQ; ab=0; member_auth=0jqbT4gZvmhv0KSSTIk0dicYt%2BzTHDSEkYoD3bR85AF1IIoANoTwlKuQRA1P3CSVkY2RtGsATg; user=1667446%22%E5%BE%90%E6%AC%A3%22images%2Favatar_new%2F33%2F34%2F1667446%2F1667446_1305879917_1.jpg%220%222789%22%3Ca+href%3D%27%2Fwebsitehelp%23help9_3%27+%3Ela%3C%2Fa%3E%220%2224%228173%22922d26080f%221394870464; player_opencount=0; xiami_playlist_for_hao123=3381901%2C1771979714%2C1770986640%2C1771627825%2C1771889373%2C1770153579%2C1768995853%2C1771949597%2C1772130321%2C1769274193%2C180264%2C2079853%2C146898%2C17302%2C2079912%2C2124065%2C2138604%2C1771734083%2C174652%2C76280%2C1768944774%2C1771760408%2C137794%2C9910%2C1769271562%2C1770454888%2C2077237%2C383794%2C1771638197%2C1772001102%2C1769819214%2C1771829247%2C1769962750%2C165990%2C382776%2C1770752283%2C; t_sign_auth=1; auth_key=Y2ZkODViN2QyMDU2ZGRhZWRhYzFiMWVlNjU0NzFiMzctLzM1Mi8yMzM1Mi8xNjgwODkvMjA3Mzg0MV8yMDcxODc1X2wubXAzLTEzOTYxMzc2MDAtMC1udWxs; __utma=251084815.1212903984.1389614024.1396068663.1396074179.53; __utmb=251084815.3.10.1396074180; __utmc=251084815; __utmz=251084815.1395897757.48.16.utmcsr=360.fm|utmccn=(referral)|utmcmd=referral|utmcct=/; _xiamitoken=3216f18932e945795bcb9432cd73e95c");
        mp3headers.put("Referer", "http://www.xiami.com/play?ids=/song/playlist/id/2073841/object_name/default/object_id/0");
        mp3headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
    }
    
    public static Map<String, String> downloadheaders = new HashMap<String, String>();
    static {
        downloadheaders.put("Accept-Encoding", "gzip,deflate,sdch");
        downloadheaders.put("Accept", "*/*");
        downloadheaders.put("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4");
        downloadheaders.put("Cache-Control", "max-age=0");
        downloadheaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        downloadheaders
                .put("Cookie",
                        "_unsign_token=04570114b0dbb7421c061588a62dabf3; bdshare_firstime=1389614046560; __gads=ID=3d44fac221bb2403:T=1389614076:S=ALNI_MZdNR1gmMpQebOFw-Ma2bV4dnNwzQ; Hm_lvt_3d143f0a07b6487f65609d8411e5464f=1393057948,1393253732,1393303827,1393335532; ab=0; member_auth=0jqbT4gZvmhv0KSSTIk0dicYt%2BzTHDSEkYoD3bR85AF1IIoANoTwlKuQRA1P3CSVkY2RtGsATg; user=1667446%22%E5%BE%90%E6%AC%A3%22images%2Favatar_new%2F33%2F34%2F1667446%2F1667446_1305879917_1.jpg%220%222789%22%3Ca+href%3D%27%2Fwebsitehelp%23help9_3%27+%3Ela%3C%2Fa%3E%220%2224%228173%22922d26080f%221394870464; ahtena_is_show=false; seiya_time=1395467190359%7C1395658904950; player_opencount=0; xiami_playlist_for_hao123=3381901%2C1771979714%2C1770986640%2C1771627825%2C1771889373%2C1770153579%2C1768995853%2C1771949597%2C1772130321%2C1769274193%2C180264%2C2079853%2C146898%2C17302%2C2079912%2C2124065%2C2138604%2C1771734083%2C174652%2C76280%2C1768944774%2C1771760408%2C137794%2C9910%2C1769271562%2C1770454888%2C2077237%2C383794%2C1771638197%2C1772001102%2C1769819214%2C1771829247%2C1769962750%2C165990%2C382776%2C1770752283%2C; recent_tags=%E5%8F%A4%E9%A3%8E+%E4%BA%AC%E5%89%A7+; t_sign_auth=1; pnm_cku822=230fCJmZk4PGRVHHxtNZngkZ3k%2BaC52PmgTKQ%3D%3D%7CfyJ6Zyd9OWAma3oraX0obx4%3D%7CfiB4D15%2BZH9geTp%2FJyN8OzVqKw4OEABJWV5aa0I%3D%7CeSRiYjNhIHA2cmI8f281c2Ymfz16OnxrNXNnPHtjJ303ZiBndCB2DQ%3D%3D%7CeCVoaEARTxBTCxxCGh5VDRNMHUZyWVxIB1QDPShqfWxzOBU8%7CeyR8C0gHRQBBBxJHHwlJDBNWA10DRQkDVRMGWAAbXgUqHg%3D%3D%7CeiJmeiV2KHMvangudmM6eXk%2BAA%3D%3D; CNZZDATA921634=cnzz_eid%3D201959541-1389614053-%26ntime%3D1396068742%26cnzz_a%3D4%26sin%3Dnone%26ltime%3D1396068662878%26rtime%3D27; CNZZDATA2629111=cnzz_eid%3D750791331-1389614053-%26ntime%3D1396068742%26cnzz_a%3D4%26sin%3Dnone%26ltime%3D1396068663041%26rtime%3D21; __utma=251084815.1212903984.1389614024.1395994650.1396068663.52; __utmb=251084815.5.10.1396068663; __utmc=251084815; __utmz=251084815.1395897757.48.16.utmcsr=360.fm|utmccn=(referral)|utmcmd=referral|utmcct=/; _xiamitoken=3216f18932e945795bcb9432cd73e95c; sec=53365782f56d4b2a278642bb478b55a2aba36493");
        downloadheaders.put("Referer", "http://alisec.xiami.com/checkcodev3.php?apply=xiami&http_referer=http://www.xiami.com/song/playlist/id/2073841/object_name/default/object_id/0?t=1396070081856");
        downloadheaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
    }

    final String path = "/home/data/xiami/";
    final String mp3Path = "/home/data/xiami/mp3/";

    String xiamiFile = "xiamisong.txt";

    int start = 1;
    int end = 1;

    BaiduBCS baiduBCS = null;

    public XiamiSpider(int start2, int end2) {
        this.start = start2;
        this.end = end2;
        xiamiFile = path + "xiamisong_" + start + "-" + end + ".txt";

        //
        BCSCredentials credentials = new BCSCredentials(accessKey, secretKey);
        baiduBCS = new BaiduBCS(credentials, host);
        // baiduBCS.setDefaultEncoding("GBK");
        baiduBCS.setDefaultEncoding("UTF-8"); // Default UTF-8
    }

    public static void main(String args[]) throws IOException {

        int start = 1, end = 2;
        if (args != null && args.length > 1) {
            start = Integer.parseInt(args[0]);
            end = Integer.parseInt(args[1]);
        }

        XiamiSpider sp = new XiamiSpider(start, end);
        sp.start();

        // System.out
        // .println(decodeXiamiUrl("4h%2Ff.moFF2%911F6E22m3teD99edde5511%55ut3Fmixim16F5%772747_pFhy13d5%9cd6335EEltA%5li.%%92E25%1%_7l3a_%26975d43297E--lp%2.eac22%56F626537.%uk3d514E786-66%%n"));
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
                    logger.info("RETRY ...");
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

                    Elements songLineEls = hotListDoc.select(".track_list  tr");

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
                                
                                logger.info("songhot > 10000 ,get song xml :  " + mp3XmlUrl);

                                String mp3XmlStr = Util.getHttpResponseString(mp3XmlUrl, "get", null, mp3headers);
                                if (StringUtils.isBlank(mp3XmlStr)) {
                                    logger.error("get xiami xml content error " + mp3XmlUrl);
                                    continue;
                                }
                                org.dom4j.Document mp3Xml;
                                long t1 = System.currentTimeMillis();
                                try {
                                    mp3Xml = DocumentHelper.parseText(mp3XmlStr);
                                } catch (DocumentException e1) {
                                    logger.error("not xml...be blocked sleep 20s. " + mp3XmlStr);
                                    try {
                                        Thread.sleep(20000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    continue;
                                }
                                logger.info("start analyse  xml");
                                org.dom4j.Element root = mp3Xml.getRootElement();
                                org.dom4j.Element track = root.element("trackList").element("track");
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
                                    String url = decodeXiamiUrl(surl);
                                    song.setXiamiDownloadUrl(url);
                                    logger.info("start download mp3 {}" , url);
                                    if (StringUtils.isNotBlank(url)) {
                                        String filename = song.getXiamiSongName() + " - " + song.getXiamiArtist() + ".mp3";

                                        File localMp3 = new File(mp3Path + filename);
                                        if (!localMp3.exists()) {
                                            boolean downRst = Util.download(url, mp3headers, localMp3);
                                            if (downRst) {
                                                logger.info("download success " + url);
                                                song.setFileName(mp3Path + filename);
                                                uploadToBcs(filename, localMp3);
                                                logger.info("upload to bcs success " + filename);
                                                song.setPcsFileName(filename);
                                            } else {
                                                logger.error("download fail " + url);
                                            }
                                        }
                                    }

                                }else {

                                    logger.error("xml have no track " + mp3XmlStr);
                                }
                                
                                long sleep  = System.currentTimeMillis() - t1;
                                if (sleep < 10000){
                                    logger.error("sleep for  " + (10000 - sleep));
                                    try {
                                        Thread.sleep(10000 - sleep);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            writer.println(Util.mapper.writeValueAsString(song));
                            writer.flush();
                        }
                        if (songLineEls.size() < 20) {
                            logger.error("reach hot list end 'cause this page only have {} songs", songLineEls.size());
                            break;
                        }
                    } else {
                        logger.error(" reach hot list end 'cause this page only have {} songs", 0);
                        break;
                    }

                }

            }
        }
        writer.close();
    }

    static String host = "bcs.duapp.com";
    static String accessKey = "XcMrzzpIgrVlXUOtAEzL3PFz";
    static String secretKey = "BOFBT0GSnoh0cnancoAAhhSTWFVonf8d";
    static String bucket = "fmstore";

    public void uploadToBcs(String bcsFilename, File file) {
        PutObjectRequest request = new PutObjectRequest(bucket, "/" + bcsFilename, file);
        ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setContentType("text/html");
        request.setMetadata(metadata);
        BaiduBCSResponse<ObjectMetadata> response = baiduBCS.putObject(request);

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

    public static String decodeXiamiUrl(String location) {
        String ls = location.substring(0, 1);
        int l = Integer.parseInt(ls);
        String t = location.substring(1);
        int tn = t.length();
        int ln = tn / l;
        int r = tn % l;
        char[] tex = t.toCharArray();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i <= ln; i++) {
            for (int j = 0; j < l; j++) {
                int n = j * ln + i;
                if (j < r)
                    n += j;
                else
                    n += r;
                if (n < tex.length)
                    text.append(tex[n]);
                else
                    break;
            }
        }
        try {
            return URLDecoder.decode(text.subSequence(0, tn).toString(), "UTF-8").replace('^', '0').replace('%', '|');
        } catch (UnsupportedEncodingException e) {
        }
        return "";
    }

}
