package com.ideax.spider.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideax.spider.common.Util;
import com.ideax.spider.xiami.pojo.SongXiami;

public class SaveXiamiSongToDB {

    static Logger logger = LoggerFactory.getLogger(SaveXiamiSongToDB.class);

    public static void main(String[] args) throws Exception {
//        if (args == null || args.length != 1) {
//            return;
//        }

        File file = new File("D:\\2.txt");
        if (!file.exists() || !file.isFile()) {
            System.err.println(args[0] + " is not file");
            return;
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String s = null;

        Class.forName("com.mysql.jdbc.Driver");
        Driver d = DriverManager.getDriver("jdbc:mysql://211.149.200.132:8306/test3?useUnicode=true&characterEncoding=UTF8");
        Properties prop = new Properties();
        prop.setProperty("user", "xxjacob");
        prop.setProperty("password", "1395630");
        Connection conn = d.connect("jdbc:mysql://211.149.200.132:8306/test3?useUnicode=true&characterEncoding=UTF8", prop);
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `test3`.`song_xiami`(" + "`name`, "
                + "`duration`," + "`xiami_album_link`, `xiami_song_name`, `xiami_song_link`, "
                + "`xiami_artist`, `xiami_artist_link`, `xiami_lyric`, `xiami_album`, "
                + "`xiami_cover_img`, `xiami_composer`, `xiami_lyricist`, `xiami_lyric_url`, "
                + "`xiami_download_url`, `listen_num`, " + "`modify_time`, `create_time`, "
                + "`file_path`, `pcs_file_name`)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement lii_pstmt = conn.prepareStatement("SELECT LAST_INSERT_ID()");

        PreparedStatement song_pstmt = conn.prepareStatement("INSERT INTO `test3`.`song` (`name`,`album`, `artist`, "
                + "`duration`, `lyricist`, `composer`, `listen_num`, `lyric`, `modify_time`, "
                + "`create_time`, `pcs_filename`, `sx_id`, `cover_img`)VALUES " + "(?,?,?,?,?,?,?,?,?,?,?,?,?);");

        PreparedStatement update_pstmt = conn.prepareStatement("UPDATE `test3`.`song_xiami` SET song_id = ? WHERE id = ?;");
        while ((s = reader.readLine()) != null) {
            int now = (int) (System.currentTimeMillis() / 1000L);
            SongXiami song = Util.mapper.reader(SongXiami.class).readValue(s);
            pstmt.setString(1, song.getXiamiSongName() == null ? "" : song.getXiamiSongName());
            pstmt.setInt(2, song.getDuration() == null ? 0 : song.getDuration());
            pstmt.setString(3, song.getXiamiAlbumLink() == null ? "" : song.getXiamiAlbumLink());
            pstmt.setString(4, song.getXiamiSongName() == null ? "" : song.getXiamiSongName());
            pstmt.setString(5, song.getXiamiSongLink() == null ? "" : song.getXiamiSongLink());
            pstmt.setString(6, song.getXiamiArtist() == null ? "" : song.getXiamiArtist());
            pstmt.setString(7, song.getXiamiArtistLink() == null ? "" : song.getXiamiArtistLink());
            pstmt.setString(8, song.getXiamiLyric() == null ? "" : song.getXiamiLyric());
            pstmt.setString(9, song.getXiamiAlbum() == null ? "" : song.getXiamiAlbum());
            pstmt.setString(10, song.getXiamiCoverImg() == null ? "" : song.getXiamiCoverImg());
            pstmt.setString(11, song.getXiamiComposer() == null ? "" : song.getXiamiComposer());
            pstmt.setString(12, song.getXiamiLyricist() == null ? "" : song.getXiamiLyricist());
            pstmt.setString(13, song.getXiamiLyricUrl() == null ? "" : song.getXiamiLyricUrl());
            pstmt.setString(14, song.getXiamiDownloadUrl() == null ? "" : song.getXiamiDownloadUrl());
            pstmt.setInt(15, song.getListenNum() == null ? 0 : song.getListenNum());
            pstmt.setInt(16, now);
            pstmt.setInt(17, now);
            pstmt.setString(18, song.getFileName() == null ? "" : song.getFileName());
            pstmt.setString(19, song.getPcsFileName() == null ? "" : song.getPcsFileName());
            int num = pstmt.executeUpdate();

            if (num == 0) {
                logger.error("xiamisong insert error");
                return;
            }
            ResultSet rs = lii_pstmt.executeQuery();
            int xmid = 0;
            if (rs.next()) {
                xmid = rs.getInt(1);
                logger.info("xiamisong inserted : " + xmid);
            } else {
                logger.error("xiamisong last id error");
                return;
            }
            rs.close();
            // .prepareStatement("INSERT INTO `test3`.`song` (`name`,`album`, `artist`, "
            // +
            // "`duration`, `lyricist`, `composer`, `listen_num`, `lyric`, `modify_time`, "
            // + "`create_time`, `pcs_filename`, `sx_id`, `cover_img`)VALUES "
            // + "(?,?,?,?,?,?,?,?,?,?,?,?,?);");
            song_pstmt.setString(1, song.getXiamiSongName() == null ? "" : song.getXiamiSongName());
            song_pstmt.setString(2, song.getXiamiAlbum() == null ? "" : song.getXiamiAlbum());
            song_pstmt.setString(3, song.getXiamiArtist() == null ? "" : song.getXiamiArtist());
            song_pstmt.setInt(4, song.getDuration() == null ? 0 : song.getDuration());
            song_pstmt.setString(5, song.getXiamiArtist() == null ? "" : song.getXiamiArtist());
            song_pstmt.setString(6, song.getXiamiComposer() == null ? "" : song.getXiamiComposer());
            song_pstmt.setInt(7, song.getListenNum() == null ? 0 : song.getListenNum());
            if (StringUtils.isNotBlank(song.getXiamiLyricUrl())) {
                String lrc = Util.getHttpResponseString(song.getXiamiLyricUrl(), null);
                song_pstmt.setString(8, StringUtils.isBlank(lrc) ? "" : lrc);
            }else{
            	song_pstmt.setString(8, "");
            }
            song_pstmt.setInt(9, now);
            song_pstmt.setInt(10, now);
            song_pstmt.setString(11, song.getPcsFileName() == null ? "" : song.getPcsFileName());
            song_pstmt.setInt(12, xmid);
            song_pstmt.setString(13, song.getXiamiCoverImg() == null ? "" : song.getXiamiCoverImg());
            song_pstmt.execute();
            
            rs = lii_pstmt.executeQuery();
            int songId = 0;
            if (rs.next()) {
                songId = rs.getInt(1);
                logger.info("song inserted : " + songId);
            } else {
                logger.error("song last id error");
                return;
            }
            
            update_pstmt.setInt(1, songId);
            update_pstmt.setInt(2, xmid);
            update_pstmt.execute();
        }
        conn.close();
        reader.close();
    }
}
