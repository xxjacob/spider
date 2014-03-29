package com.ideax.spider.kting.pojo;

import java.util.List;
import java.io.Serializable;

/**
 *
 * @author xxjacob
 */
public class NovelAudioKting implements Serializable{

	@Override
	public String toString() {
		return "NovelAudioKting [id=" + id + ", ktId=" + ktId + ", name=" + name + ", playedNum=" + playedNum
				+ ", catId=" + catId + ", catIdSub=" + catIdSub + ", coverImg=" + coverImg + ", chapterNum="
				+ chapterNum + ", totalTime=" + totalTime + ", author=" + author + ", authorLink=" + authorLink
				+ ", reader=" + reader + ", readerLink=" + readerLink + ", addDate=" + addDate + ", updateDate="
				+ updateDate + ", tags=" + tags + ", description=" + description + ", keys=" + keys + "]";
	}

	/** 序列化ID */
	private static final long serialVersionUID = 1L;

	/** id **/
    private Integer id;
	/** kt_id **/
    private Integer ktId;
	/** name **/
    private String name;
	/** played_num **/
    private Integer playedNum;
	/** cat_id **/
    private Integer catId;
	/** cat_id_sub **/
    private Integer catIdSub;
	/** cover_img **/
    private String coverImg;
	/** chapter_num **/
    private Integer chapterNum;
	/** hour **/
    private Integer totalTime;
	/** author **/
    private String author;
	/** author_link **/
    private String authorLink;
	/** reader **/
    private String reader;
	/** reader_link **/
    private String readerLink;
	/** add_date **/
    private String addDate;
	/** update_date **/
    private String updateDate;
	/** tags **/
    private String tags;
	/** description **/
    private String description;

	
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
    * 获取属性:playedNum
    * played_num
    * @return playedNum
    */
   public Integer getPlayedNum() {
       return playedNum;
   }
   /**
    * 设置属性:playedNum
    * played_num
    * @param playedNum
    */
   public void setPlayedNum(Integer playedNum) {
       this.playedNum = playedNum;
   }
	
   /**
    * 获取属性:catId
    * cat_id
    * @return catId
    */
   public Integer getCatId() {
       return catId;
   }
   /**
    * 设置属性:catId
    * cat_id
    * @param catId
    */
   public void setCatId(Integer catId) {
       this.catId = catId;
   }
	
   /**
    * 获取属性:catIdSub
    * cat_id_sub
    * @return catIdSub
    */
   public Integer getCatIdSub() {
       return catIdSub;
   }
   /**
    * 设置属性:catIdSub
    * cat_id_sub
    * @param catIdSub
    */
   public void setCatIdSub(Integer catIdSub) {
       this.catIdSub = catIdSub;
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
    * 获取属性:chapterNum
    * chapter_num
    * @return chapterNum
    */
   public Integer getChapterNum() {
       return chapterNum;
   }
   /**
    * 设置属性:chapterNum
    * chapter_num
    * @param chapterNum
    */
   public void setChapterNum(Integer chapterNum) {
       this.chapterNum = chapterNum;
   }
	
   /**
    * 获取属性:totalTime
    * hour
    * @return totalTime
    */
   public Integer getTotalTime() {
       return totalTime;
   }
   /**
    * 设置属性:totalTime
    * hour
    * @param totalTime
    */
   public void setTotalTime(Integer totalTime) {
       this.totalTime = totalTime;
   }
	
   /**
    * 获取属性:author
    * author
    * @return author
    */
   public String getAuthor() {
       return author;
   }
   /**
    * 设置属性:author
    * author
    * @param author
    */
   public void setAuthor(String author) {
       this.author = author;
   }
	
   /**
    * 获取属性:authorLink
    * author_link
    * @return authorLink
    */
   public String getAuthorLink() {
       return authorLink;
   }
   /**
    * 设置属性:authorLink
    * author_link
    * @param authorLink
    */
   public void setAuthorLink(String authorLink) {
       this.authorLink = authorLink;
   }
	
   /**
    * 获取属性:reader
    * reader
    * @return reader
    */
   public String getReader() {
       return reader;
   }
   /**
    * 设置属性:reader
    * reader
    * @param reader
    */
   public void setReader(String reader) {
       this.reader = reader;
   }
	
   /**
    * 获取属性:readerLink
    * reader_link
    * @return readerLink
    */
   public String getReaderLink() {
       return readerLink;
   }
   /**
    * 设置属性:readerLink
    * reader_link
    * @param readerLink
    */
   public void setReaderLink(String readerLink) {
       this.readerLink = readerLink;
   }
	
   /**
    * 获取属性:addDate
    * add_date
    * @return addDate
    */
   public String getAddDate() {
       return addDate;
   }
   /**
    * 设置属性:addDate
    * add_date
    * @param addDate
    */
   public void setAddDate(String addDate) {
       this.addDate = addDate;
   }
	
   /**
    * 获取属性:updateDate
    * update_date
    * @return updateDate
    */
   public String getUpdateDate() {
       return updateDate;
   }
   /**
    * 设置属性:updateDate
    * update_date
    * @param updateDate
    */
   public void setUpdateDate(String updateDate) {
       this.updateDate = updateDate;
   }
	
   /**
    * 获取属性:tags
    * tags
    * @return tags
    */
   public String getTags() {
       return tags;
   }
   /**
    * 设置属性:tags
    * tags
    * @param tags
    */
   public void setTags(String tags) {
       this.tags = tags;
   }
	
   /**
    * 获取属性:description
    * description
    * @return description
    */
   public String getDescription() {
       return description;
   }
   /**
    * 设置属性:description
    * description
    * @param description
    */
   public void setDescription(String description) {
       this.description = description;
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