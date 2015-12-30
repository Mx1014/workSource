// @formatter:off
package com.everhomes.rest.link;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>sourceType: 对应的资源类型  参考{@link com.everhomes.rest.link.LinkSourceType}</li>
 * <li>title：link的标题</li>
 * <li>author：作者</li>
 * <li>coverUri：封面uri</li>
 * <li>contentType：link的类型  参考{@link com.everhomes.rest.link.LinkContentType}</li>
 * <li>content：link的内容</li>
 * <li>contentAbstract：link简介</li> 
 * </ul>
 */
public class CreateLinkCommand {
	@NotNull
	private Byte     sourceType;
	@NotNull
	private String   title;
	private String   author;
	private String   coverUri;
	private String   contentType;
	private String   content;
	private String   contentAbstract;
	

    public CreateLinkCommand() {
    }
    
   

    public Byte getSourceType() {
		return sourceType;
	}



	public void setSourceType(Byte sourceType) {
		this.sourceType = sourceType;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getAuthor() {
		return author;
	}



	public void setAuthor(String author) {
		this.author = author;
	}



	public String getCoverUri() {
		return coverUri;
	}



	public void setCoverUri(String coverUri) {
		this.coverUri = coverUri;
	}



	public String getContentType() {
		return contentType;
	}



	public void setContentType(String contentType) {
		this.contentType = contentType;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getContentAbstract() {
		return contentAbstract;
	}



	public void setContentAbstract(String contentAbstract) {
		this.contentAbstract = contentAbstract;
	}



	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
