package com.everhomes.yellowPage.faq;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id : 问题id</li>
 * <li>title : 问题标题</li>
 * <li>content : 内容</li>
 * <li>typeId : 类型id</li>
 * <li>typeName : 类型名称</li>
 * <li>solveTimes : 解决次数</li>
 * <li>unSolveTimes : 未解决次数</li>
 * <li>topFlag : 0-非热门 1-热门</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class FAQDTO {
	private Long id;
	private String title;
	private String content;
	private Long typeId;
	private String typeName;
	private Integer solveTimes;
	private Integer unSolveTimes;
	private Byte topFlag;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getSolveTimes() {
		return solveTimes;
	}

	public void setSolveTimes(Integer solveTimes) {
		this.solveTimes = solveTimes;
	}

	public Integer getUnSolveTimes() {
		return unSolveTimes;
	}

	public void setUnSolveTimes(Integer unSolveTimes) {
		this.unSolveTimes = unSolveTimes;
	}

	public Byte getTopFlag() {
		return topFlag;
	}

	public void setTopFlag(Byte topFlag) {
		this.topFlag = topFlag;
	}
}
