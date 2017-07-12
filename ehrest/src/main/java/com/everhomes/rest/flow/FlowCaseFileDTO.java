package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>url: url</li>
 * <li>fileName: fileName</li>
 * <li>fileSize: fileSize</li> 
 * 
 * </ul>
 * @author janson
 *
 */
public class FlowCaseFileDTO {
	private String uri;
	private String url;
	private String fileName;
	private Integer fileSize;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getFileSize() {
		return fileSize;
	}
	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	} 
	
}
