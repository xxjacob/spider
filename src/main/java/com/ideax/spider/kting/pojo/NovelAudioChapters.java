package com.ideax.spider.kting.pojo;

import java.util.List;
import java.io.Serializable;

/**
 *
 * @author xxjacob
 */
public class NovelAudioChapters implements Serializable{

	/** 序列化ID */
	private static final long serialVersionUID = 1L;

	/** id **/
    private Integer id;
	/** kt_id **/
    private Integer ktId;
	/** chapter_index **/
    private Integer chapterIndex;
	/** kt_cid **/
    private Integer ktCid;
	/** kt_stream_url **/
    private String ktStreamUrl;
	/** local_file_path **/
    private String localFilePath;
    
    private String ktName;
    private String ktDuration;

	
   public String getKtDuration() {
		return ktDuration;
	}
	public void setKtDuration(String ktDuration) {
		this.ktDuration = ktDuration;
	}
public String getKtName() {
		return ktName;
	}
	public void setKtName(String ktName) {
		this.ktName = ktName;
	}
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
    * 获取属性:ktId
    * kt_id
    * @return ktId
    */
   public Integer getKtId() {
       return ktId;
   }
   /**
    * 设置属性:ktId
    * kt_id
    * @param ktId
    */
   public void setKtId(Integer ktId) {
       this.ktId = ktId;
   }
	
   /**
    * 获取属性:chapterIndex
    * chapter_index
    * @return chapterIndex
    */
   public Integer getChapterIndex() {
       return chapterIndex;
   }
   /**
    * 设置属性:chapterIndex
    * chapter_index
    * @param chapterIndex
    */
   public void setChapterIndex(Integer chapterIndex) {
       this.chapterIndex = chapterIndex;
   }
	
   /**
    * 获取属性:ktCid
    * kt_cid
    * @return ktCid
    */
   public Integer getKtCid() {
       return ktCid;
   }
   /**
    * 设置属性:ktCid
    * kt_cid
    * @param ktCid
    */
   public void setKtCid(Integer ktCid) {
       this.ktCid = ktCid;
   }
	
   /**
    * 获取属性:ktStreamUrl
    * kt_stream_url
    * @return ktStreamUrl
    */
   public String getKtStreamUrl() {
       return ktStreamUrl;
   }
   /**
    * 设置属性:ktStreamUrl
    * kt_stream_url
    * @param ktStreamUrl
    */
   public void setKtStreamUrl(String ktStreamUrl) {
       this.ktStreamUrl = ktStreamUrl;
   }
	
   /**
    * 获取属性:localFilePath
    * local_file_path
    * @return localFilePath
    */
   public String getLocalFilePath() {
       return localFilePath;
   }
   /**
    * 设置属性:localFilePath
    * local_file_path
    * @param localFilePath
    */
   public void setLocalFilePath(String localFilePath) {
       this.localFilePath = localFilePath;
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