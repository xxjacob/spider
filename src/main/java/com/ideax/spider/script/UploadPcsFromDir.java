package com.ideax.spider.script;

import java.io.File;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideax.spider.common.PcsUploader;

public class UploadPcsFromDir {
    static Logger logger = LoggerFactory.getLogger(UploadPcsFromDir.class);

    public static void main(String[] args) throws Exception {
        String token = "21.295c30d2cd96a2623cb4a0e0b63181b4.2592000.1398915791.3456334463-1038428";
        String bucket = "/apps/guitar/";

        if (args == null || args.length != 1) {
            return;
        }

        File dir = new File(args[0]);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println(args[0] + " is not dir");
            return;
        }

        PcsUploader uploader = new PcsUploader(token, bucket);

        File[] files = dir.listFiles();
        Arrays.sort(files);
        for (File f : files) {
            String fn = f.getName();
            if (fn.endsWith(".mp3")) {
                // pcs 不允许这些字符
                fn = fn.replace("*", "").replace("?", "").replace(":", "");
                uploader.upload(fn, f);
                logger.info("done upload : " + fn);
            } else
                logger.info("skip : " + fn);
        }

    }
}
