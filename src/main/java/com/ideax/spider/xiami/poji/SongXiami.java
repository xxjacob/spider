package com.ideax.spider.xiami.poji;

import java.util.List;
import java.io.Serializable;

/**
 *
 * @author xxjacob
 */
public class SongXiami implements Serializable{

	/** 序列化ID */
	private static final long serialVersionUID = 1L;

	/** id **/
    private Integer id;
	/** name **/
    private String name;
	/** breif **/
    private String breif;
	/** album **/
    private String album;
	/** artist **/
    private String artist;
	/** 1,2,3 **/
    private String featArtist;
	/** 作词人 **/
    private String lyricist;
	/** 作曲人 **/
    private String composer;
	/** in second **/
    private Integer duration;
	/** cover_img **/
    private String coverImg;
	/** xiami_album_link **/
    private String xiamiAlbumLink;
	/** xiami_song_name **/
    private String xiamiSongName;
	/** xiami_song_link **/
    private String xiamiSongLink;
	/** xiami_artist **/
    private String xiamiArtist;
	/** xiami_artist_link **/
    private String xiamiArtistLink;
	/** xiami_lyric **/
    private String xiamiLyric;
	/** xiami_album **/
    private String xiamiAlbum;
	/** xiami_cover_img **/
    private String xiamiCoverImg;
	/** xiami_composer **/
    private String xiamiComposer;
	/** xiami_lyricist **/
    private String xiamiLyricist;
    private String xiamiLyricUrl;
	public String getXiamiLyricUrl() {
		return xiamiLyricUrl;
	}
	public void setXiamiLyricUrl(String xiamiLyricUrl) {
		this.xiamiLyricUrl = xiamiLyricUrl;
	}

	/** listen_num **/
    private Integer listenNum;
	/** share_num **/
    private Integer shareNum;
	/** comment_num **/
    private Integer commentNum;
	/** modify_time **/
    private Integer modifyTime;
	/** create_time **/
    private Integer createTime;
	/** year **/
    private String year;
	/** 0 yes 1 no **/
    private Byte isvbr;
	/** 比特率 **/
    private Integer bitrate;
	/** album_artist **/
    private String albumArtist;
	/** file_name **/
    private String fileName;
	/** pcs_file_name **/
    private String pcsFileName;
	/** file_path **/
    private String filePath;
	/** song_id **/
    private Integer songId;

	
   /**
    * 获取属性:id
    * id
    * @return id
    */
   public Integer getId() {
       return id;
   }
   /**
    * 设置属性:id
    * id
    * @param id
    */
   public void setId(Integer id) {
       this.id = id;
   }
	
   /**
    * 获取属性:name
    * name
    * @return name
    */
   public String getName() {
       return name;
   }
   /**
    * 设置属性:name
    * name
    * @param name
    */
   public void setName(String name) {
       this.name = name;
   }
	
   /**
    * 获取属性:breif
    * breif
    * @return breif
    */
   public String getBreif() {
       return breif;
   }
   /**
    * 设置属性:breif
    * breif
    * @param breif
    */
   public void setBreif(String breif) {
       this.breif = breif;
   }
	
   /**
    * 获取属性:album
    * album
    * @return album
    */
   public String getAlbum() {
       return album;
   }
   /**
    * 设置属性:album
    * album
    * @param album
    */
   public void setAlbum(String album) {
       this.album = album;
   }
	
   /**
    * 获取属性:artist
    * artist
    * @return artist
    */
   public String getArtist() {
       return artist;
   }
   /**
    * 设置属性:artist
    * artist
    * @param artist
    */
   public void setArtist(String artist) {
       this.artist = artist;
   }
	
   /**
    * 获取属性:featArtist
    * 1,2,3
    * @return featArtist
    */
   public String getFeatArtist() {
       return featArtist;
   }
   /**
    * 设置属性:featArtist
    * 1,2,3
    * @param featArtist
    */
   public void setFeatArtist(String featArtist) {
       this.featArtist = featArtist;
   }
	
   /**
    * 获取属性:lyricist
    * 作词人
    * @return lyricist
    */
   public String getLyricist() {
       return lyricist;
   }
   /**
    * 设置属性:lyricist
    * 作词人
    * @param lyricist
    */
   public void setLyricist(String lyricist) {
       this.lyricist = lyricist;
   }
	
   /**
    * 获取属性:composer
    * 作曲人
    * @return composer
    */
   public String getComposer() {
       return composer;
   }
   /**
    * 设置属性:composer
    * 作曲人
    * @param composer
    */
   public void setComposer(String composer) {
       this.composer = composer;
   }
	
   /**
    * 获取属性:duration
    * in second
    * @return duration
    */
   public Integer getDuration() {
       return duration;
   }
   /**
    * 设置属性:duration
    * in second
    * @param duration
    */
   public void setDuration(Integer duration) {
       this.duration = duration;
   }
	
   /**
    * 获取属性:coverImg
    * cover_img
    * @return coverImg
    */
   public String getCoverImg() {
       return coverImg;
   }
   /**
    * 设置属性:coverImg
    * cover_img
    * @param coverImg
    */
   public void setCoverImg(String coverImg) {
       this.coverImg = coverImg;
   }
	
   /**
    * 获取属性:xiamiAlbumLink
    * xiami_album_link
    * @return xiamiAlbumLink
    */
   public String getXiamiAlbumLink() {
       return xiamiAlbumLink;
   }
   /**
    * 设置属性:xiamiAlbumLink
    * xiami_album_link
    * @param xiamiAlbumLink
    */
   public void setXiamiAlbumLink(String xiamiAlbumLink) {
       this.xiamiAlbumLink = xiamiAlbumLink;
   }
	
   /**
    * 获取属性:xiamiSongName
    * xiami_song_name
    * @return xiamiSongName
    */
   public String getXiamiSongName() {
       return xiamiSongName;
   }
   /**
    * 设置属性:xiamiSongName
    * xiami_song_name
    * @param xiamiSongName
    */
   public void setXiamiSongName(String xiamiSongName) {
       this.xiamiSongName = xiamiSongName;
   }
	
   /**
    * 获取属性:xiamiSongLink
    * xiami_song_link
    * @return xiamiSongLink
    */
   public String getXiamiSongLink() {
       return xiamiSongLink;
   }
   /**
    * 设置属性:xiamiSongLink
    * xiami_song_link
    * @param xiamiSongLink
    */
   public void setXiamiSongLink(String xiamiSongLink) {
       this.xiamiSongLink = xiamiSongLink;
   }
	
   /**
    * 获取属性:xiamiArtist
    * xiami_artist
    * @return xiamiArtist
    */
   public String getXiamiArtist() {
       return xiamiArtist;
   }
   /**
    * 设置属性:xiamiArtist
    * xiami_artist
    * @param xiamiArtist
    */
   public void setXiamiArtist(String xiamiArtist) {
       this.xiamiArtist = xiamiArtist;
   }
	
   /**
    * 获取属性:xiamiArtistLink
    * xiami_artist_link
    * @return xiamiArtistLink
    */
   public String getXiamiArtistLink() {
       return xiamiArtistLink;
   }
   /**
    * 设置属性:xiamiArtistLink
    * xiami_artist_link
    * @param xiamiArtistLink
    */
   public void setXiamiArtistLink(String xiamiArtistLink) {
       this.xiamiArtistLink = xiamiArtistLink;
   }
	
   /**
    * 获取属性:xiamiLyric
    * xiami_lyric
    * @return xiamiLyric
    */
   public String getXiamiLyric() {
       return xiamiLyric;
   }
   /**
    * 设置属性:xiamiLyric
    * xiami_lyric
    * @param xiamiLyric
    */
   public void setXiamiLyric(String xiamiLyric) {
       this.xiamiLyric = xiamiLyric;
   }
	
   /**
    * 获取属性:xiamiAlbum
    * xiami_album
    * @return xiamiAlbum
    */
   public String getXiamiAlbum() {
       return xiamiAlbum;
   }
   /**
    * 设置属性:xiamiAlbum
    * xiami_album
    * @param xiamiAlbum
    */
   public void setXiamiAlbum(String xiamiAlbum) {
       this.xiamiAlbum = xiamiAlbum;
   }
	
   /**
    * 获取属性:xiamiCoverImg
    * xiami_cover_img
    * @return xiamiCoverImg
    */
   public String getXiamiCoverImg() {
       return xiamiCoverImg;
   }
   /**
    * 设置属性:xiamiCoverImg
    * xiami_cover_img
    * @param xiamiCoverImg
    */
   public void setXiamiCoverImg(String xiamiCoverImg) {
       this.xiamiCoverImg = xiamiCoverImg;
   }
	
   /**
    * 获取属性:xiamiComposer
    * xiami_composer
    * @return xiamiComposer
    */
   public String getXiamiComposer() {
       return xiamiComposer;
   }
   /**
    * 设置属性:xiamiComposer
    * xiami_composer
    * @param xiamiComposer
    */
   public void setXiamiComposer(String xiamiComposer) {
       this.xiamiComposer = xiamiComposer;
   }
	
   /**
    * 获取属性:xiamiLyricist
    * xiami_lyricist
    * @return xiamiLyricist
    */
   public String getXiamiLyricist() {
       return xiamiLyricist;
   }
   /**
    * 设置属性:xiamiLyricist
    * xiami_lyricist
    * @param xiamiLyricist
    */
   public void setXiamiLyricist(String xiamiLyricist) {
       this.xiamiLyricist = xiamiLyricist;
   }
	
   /**
    * 获取属性:listenNum
    * listen_num
    * @return listenNum
    */
   public Integer getListenNum() {
       return listenNum;
   }
   /**
    * 设置属性:listenNum
    * listen_num
    * @param listenNum
    */
   public void setListenNum(Integer listenNum) {
       this.listenNum = listenNum;
   }
	
   /**
    * 获取属性:shareNum
    * share_num
    * @return shareNum
    */
   public Integer getShareNum() {
       return shareNum;
   }
   /**
    * 设置属性:shareNum
    * share_num
    * @param shareNum
    */
   public void setShareNum(Integer shareNum) {
       this.shareNum = shareNum;
   }
	
   /**
    * 获取属性:commentNum
    * comment_num
    * @return commentNum
    */
   public Integer getCommentNum() {
       return commentNum;
   }
   /**
    * 设置属性:commentNum
    * comment_num
    * @param commentNum
    */
   public void setCommentNum(Integer commentNum) {
       this.commentNum = commentNum;
   }
	
   /**
    * 获取属性:modifyTime
    * modify_time
    * @return modifyTime
    */
   public Integer getModifyTime() {
       return modifyTime;
   }
   /**
    * 设置属性:modifyTime
    * modify_time
    * @param modifyTime
    */
   public void setModifyTime(Integer modifyTime) {
       this.modifyTime = modifyTime;
   }
	
   /**
    * 获取属性:createTime
    * create_time
    * @return createTime
    */
   public Integer getCreateTime() {
       return createTime;
   }
   /**
    * 设置属性:createTime
    * create_time
    * @param createTime
    */
   public void setCreateTime(Integer createTime) {
       this.createTime = createTime;
   }
	
   /**
    * 获取属性:year
    * year
    * @return year
    */
   public String getYear() {
       return year;
   }
   /**
    * 设置属性:year
    * year
    * @param year
    */
   public void setYear(String year) {
       this.year = year;
   }
	
   /**
    * 获取属性:isvbr
    * 0 yes 1 no
    * @return isvbr
    */
   public Byte getIsvbr() {
       return isvbr;
   }
   /**
    * 设置属性:isvbr
    * 0 yes 1 no
    * @param isvbr
    */
   public void setIsvbr(Byte isvbr) {
       this.isvbr = isvbr;
   }
	
   /**
    * 获取属性:bitrate
    * 比特率
    * @return bitrate
    */
   public Integer getBitrate() {
       return bitrate;
   }
   /**
    * 设置属性:bitrate
    * 比特率
    * @param bitrate
    */
   public void setBitrate(Integer bitrate) {
       this.bitrate = bitrate;
   }
	
   /**
    * 获取属性:albumArtist
    * album_artist
    * @return albumArtist
    */
   public String getAlbumArtist() {
       return albumArtist;
   }
   /**
    * 设置属性:albumArtist
    * album_artist
    * @param albumArtist
    */
   public void setAlbumArtist(String albumArtist) {
       this.albumArtist = albumArtist;
   }
	
   /**
    * 获取属性:fileName
    * file_name
    * @return fileName
    */
   public String getFileName() {
       return fileName;
   }
   /**
    * 设置属性:fileName
    * file_name
    * @param fileName
    */
   public void setFileName(String fileName) {
       this.fileName = fileName;
   }
	
   /**
    * 获取属性:pcsFileName
    * pcs_file_name
    * @return pcsFileName
    */
   public String getPcsFileName() {
       return pcsFileName;
   }
   /**
    * 设置属性:pcsFileName
    * pcs_file_name
    * @param pcsFileName
    */
   public void setPcsFileName(String pcsFileName) {
       this.pcsFileName = pcsFileName;
   }
	
   /**
    * 获取属性:filePath
    * file_path
    * @return filePath
    */
   public String getFilePath() {
       return filePath;
   }
   /**
    * 设置属性:filePath
    * file_path
    * @param filePath
    */
   public void setFilePath(String filePath) {
       this.filePath = filePath;
   }
	
   /**
    * 获取属性:songId
    * song_id
    * @return songId
    */
   public Integer getSongId() {
       return songId;
   }
   /**
    * 设置属性:songId
    * song_id
    * @param songId
    */
   public void setSongId(Integer songId) {
       this.songId = songId;
   }

	/**
     * 需要更新时的代码，keys 代表主键list
     */
	private List<Long> keys;
		
	public List<Long> getKeys() {
		return keys;
	}
	
	public void setIds(List<Long> keys) {
		this.keys = keys;
	}
}