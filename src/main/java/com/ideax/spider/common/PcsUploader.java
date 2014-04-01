package com.ideax.spider.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * baidu pcs uploader
 * 
 * @author xuxin03
 * @since 2014年4月1日
 */
public class PcsUploader {

    Logger logger = LoggerFactory.getLogger(Util.class);

    String token = "21.295c30d2cd96a2623cb4a0e0b63181b4.2592000.1398915791.3456334463-1038428";

    String bucket = "/apps/guitar/";

    CloseableHttpClient httpclient;

    public PcsUploader() {
        httpclient = HttpClients
                .custom()
                // disable reuse
                .setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE)
                // set agent name
                .setUserAgent(
                        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36")
                // disable cookie
                .disableCookieManagement()
                // .disableRedirectHandling()
                // socket config, 10s read timeout
                .setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(600000).build()).build();
    }

    public void upload(String filename, File file) {
        String path = null;
        try {
            path = URLEncoder.encode(bucket + filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        StringBuilder urlbu = new StringBuilder();
        urlbu.append("https://pcs.baidu.com/rest/2.0/pcs/file").append("?method=upload").append("&path=")
                .append(path).append("&access_token=").append(token).append("&ondup=overwrite");
        System.out.println(urlbu);
        doUpload(urlbu.toString(), file, filename, null);
    }

    public static void main(String[] args) throws Exception {
        // D:\\baiduyundownload\\current_music\\getlucky.mp3
        new PcsUploader().upload("get lucky.mp3", new File("D:\\soundmanagerv297a-20131201.zip"));
    }

    private String doUpload(String url, File f, String filename, Map<String, String> header) {

        HttpPost post = new HttpPost(url);
        PcsUploadEntity entity = new PcsUploadEntity(f, filename);
        post.setEntity(entity);
        if (header != null) {
            for (Entry<String, String> en : header.entrySet()) {
                post.setHeader(en.getKey(), en.getValue());
            }
        }
        CloseableHttpResponse resp;
        try {
            resp = httpclient.execute(post);
            if (!(resp.getStatusLine().getStatusCode() == 200)) {
                String str = EntityUtils.toString(resp.getEntity(), "UTF-8");
                logger.error("ERROR : report app xml result , response error : status=[{}] , {}", resp.getStatusLine()
                        .getStatusCode(), str);
                return null;
            } else
                return EntityUtils.toString(resp.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * Apache的multipartEntity不能上传，格式好像有点区别，不兼容， 艹。。。 照着php sdk 改了一个
     * 
     * @author xuxin03
     * @since 2014年4月1日
     */
    public static class PcsUploadEntity extends AbstractHttpEntity {

        PcsInputStream pis;

        PcsUploadEntity(File file, String filename) {
            try {
                pis = new PcsInputStream(file, filename);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            setContentType("multipart/form-data; boundary=" + pis.boundary);
        }

        public boolean isRepeatable() {
            return false;
        }

        public long getContentLength() {
            return pis.length;
        }

        public InputStream getContent() throws IOException, IllegalStateException {
            return pis;
        }

        public void writeTo(OutputStream outstream) throws IOException {
            IOUtils.copy(pis, outstream);
            pis.close();
        }

        public boolean isStreaming() {
            return true;
        }

    }

    public static class PcsInputStream extends InputStream {
        final int buffLen = 4096;
        byte[] prefix;
        FileInputStream fis;
        byte[] buff = new byte[buffLen];
        byte[] suffix;
        int index;
        int readLen = -1;
        int status = 0; // 0 prefix , 1 file , 2 suffix
        String boundary;

        long length;

        PcsInputStream(File f, String filename) throws FileNotFoundException {
            fis = new FileInputStream(f);
            boundary = Util.md5Encoding(System.currentTimeMillis() + "");
            StringBuilder sb = new StringBuilder();
            sb.append("--").append(boundary).append("\r\n");
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(filename).append("\"\r\n");
            sb.append("Content-Type: application/octet-stream\r\n\r\n");
            try {
                prefix = sb.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                prefix = sb.toString().getBytes();
            }
            sb = new StringBuilder();
            sb.append("\r\n").append("--").append(boundary).append("\r\n");
            try {
                suffix = sb.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                suffix = sb.toString().getBytes();
            }

            length = prefix.length + f.length() + suffix.length;
        }

        @Override
        public int read() throws IOException {
            if (status == 0) {
                if (index < prefix.length) {
                    return prefix[index++];
                } else {
                    status = 1;
                    index = 0; // for file buff
                }
            }

            if (status == 1) {
                if (readLen == -1 || index >= readLen) {
                    index = 0;
                    readLen = fis.read(buff);
                    if (readLen == -1) {
                        index = 0;
                        status = 2;
                    } else{
                        return buff[index++];
                    }
                } else {
                    return buff[index++];
                }
            }

            if (status == 2) {
                if (index < suffix.length) {
                    System.out.println(" -4- ");
                    return suffix[index++];
                }
            }

            return -1;
        }

        public void close() throws IOException {
            fis.close();
        }

    }
}
