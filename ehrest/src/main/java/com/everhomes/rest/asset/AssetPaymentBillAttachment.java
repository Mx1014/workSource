//@formatter:off
package com.everhomes.rest.asset;
/**
 * @author created by ycx
 * @date 下午10:22:32
 */

/**
 *<ul>
 * <li>filename:附件名称</li>
 * <li>uri:附件uri</li>
 * <li>url:附件url</li>
 *</ul>
 */
public class AssetPaymentBillAttachment {
    private String filename;
    private String uri;
    private String url;
    
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
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
}
