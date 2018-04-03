// @formatter:off
package com.everhomes.rest.contentserver;

import com.everhomes.util.StringHelper;

/**
 * <p>把文件（图片、语音、视频、普通文件）上传到Content Server的结果格式为：</p>
 * <p>
 * {<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"errorCode": 0,<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"errorDescription": "",<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"errorScope": "",<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"name": "Uploaded",<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"size": 1M,<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"response": {<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"uri": "cs://1/image/aW1hZ2UvTVRvNVlqZzNZV1ZoTlRCak9URXdaV0kxWkdZMFl6Tm1NekJoTkdVNFpUVTVNQQ",<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"url": "http://alpha-cs.lab.everhomes.com:5000/image/aW1hZ2UvTVRvNVlqZzNZV1ZoTlRCak9URXdaV0kxWkdZMFl6Tm1NekJoTkdVNFpUVTVNQQ?token=GqUI2YCwa8pskPeRW5a8s44JzwpyjrGEc4SFplFBE_Cs_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVpe8vpyBgs2yAaZsgczdlIo"<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;},<br />
 * &nbsp;&nbsp;&nbsp;&nbsp;"version": ""<br />
 * }<br />
 * </p>
 * <ul>
 * <li>errorCode: 来自于Content Server的错误码</li>
 * <li>errorDescription: 来自于Content Server的错误描述</li>
 * <li>name: 文件名称</li>
 * <li>size: 文件大小</li>
 * <li>originalName: 文件源名称</li>
 * <li>response: 文件URL、URI信息，参考{@link com.everhomes.rest.contentserver.CsFileLocationDTO}</li>
 * <li>version: 版本</li>
 * </ul>
 */
public class UploadCsFileResponse {
    private Integer errorCode;
    private String errorDescription;
    private String name;
    private String size;
    private String originalName;
    private CsFileLocationDTO response;
    private String version;
    
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public CsFileLocationDTO getResponse() {
        return response;
    }

    public void setResponse(CsFileLocationDTO response) {
        this.response = response;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
