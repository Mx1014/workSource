package com.everhomes.rest.user;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.contentserver.ContentCacheConfigDTO;
import com.everhomes.rest.launchpadbase.IndexDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>contentServer: 内容服务器地址。如果用户未登录，则此字段不返回</li>
 *     <li>uploadUrlInBrowser: 通过 pc 上传文件的 url 链接地址</li>
 *     <li>paymentPlatform: 0旧支付  1新支付</li>
 *     <li>scanForLogonServer: 扫码登录服务器地址</li>
 *     <li>accessPoints: borderServer 的链接地址。如果用户未登录，则此地址不返回</li>
 *     <li>contentCacheConfig: 资源缓存配置 {@link com.everhomes.rest.contentserver.ContentCacheConfigDTO}</li>
 *     <li>securityPayServer: 支付双向安全校验</li>
 *     <li>indexDtos: 主页签信息{@link IndexDTO}</li>
 *     <li>customIndexDtos: 定制版主页签信息{@link IndexDTO}</li>
 *     <li>indexFlag: 是否使用主页签，请参考{@link com.everhomes.rest.common.TrueOrFalseFlag}</li>
 *     <li>clientAddressMode : 客户端项目地址模式  0 普通模式  1 图片模式</li>
 *     <li>authPopupConfig: 注册后是否引导用户进行认证，1为是，0为否</li>
 * </ul>
 */
public class SystemInfoResponse {

    private String contentServer;
    private String uploadUrlInBrowser;
    private Long paymentPlatform;
    private String scanForLogonServer;

    private Integer addressDialogStyle;

    @ItemType(String.class)
    private List<String> accessPoints;

    private Byte myPublishFlag;


    private Integer sceneShowType;

    //default: https://secpay.zuolin.com
    private String securityPayServer;

    private ContentCacheConfigDTO contentCacheConfig;

    private List<IndexDTO> indexDtos;

    private List<IndexDTO> customIndexDtos;

    private Byte indexFlag;

    private Integer clientAddressMode;

    private Byte authPopupConfig;


    public List<IndexDTO> getCustomIndexDtos() {
        return customIndexDtos;
    }

    public void setCustomIndexDtos(List<IndexDTO> customIndexDtos) {
        this.customIndexDtos = customIndexDtos;
    }

    public Byte getIndexFlag() {
        return indexFlag;
    }

    public void setIndexFlag(Byte indexFlag) {
        this.indexFlag = indexFlag;
    }

    public Byte getAuthPopupConfig() {
        return authPopupConfig;
    }

    public void setAuthPopupConfig(Byte authPopupConfig) {
        this.authPopupConfig = authPopupConfig;
    }

    public String getSecurityPayServer() {
        return securityPayServer;
    }

    public void setSecurityPayServer(String securityPayServer) {
        this.securityPayServer = securityPayServer;
    }

    public ContentCacheConfigDTO getContentCacheConfig() {
        return contentCacheConfig;
    }

    public void setContentCacheConfig(ContentCacheConfigDTO contentCacheConfig) {
        this.contentCacheConfig = contentCacheConfig;
    }

    public String getContentServer() {
        return contentServer;
    }

    public void setContentServer(String contentServer) {
        this.contentServer = contentServer;
    }

    public String getUploadUrlInBrowser() {
        return uploadUrlInBrowser;
    }

    public void setUploadUrlInBrowser(String uploadUrlInBrowser) {
        this.uploadUrlInBrowser = uploadUrlInBrowser;
    }

    public List<String> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<String> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public Long getPaymentPlatform() {
        return paymentPlatform;
    }

    public void setPaymentPlatform(Long paymentPlatform) {
        this.paymentPlatform = paymentPlatform;
    }

    public List<IndexDTO> getIndexDtos() {
        return indexDtos;
    }

    public void setIndexDtos(List<IndexDTO> indexDtos) {
        this.indexDtos = indexDtos;
    }

    public Integer getAddressDialogStyle() {
        return addressDialogStyle;
    }

    public void setAddressDialogStyle(Integer addressDialogStyle) {
        this.addressDialogStyle = addressDialogStyle;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getScanForLogonServer() {
        return scanForLogonServer;
    }

    public void setScanForLogonServer(String scanForLogonServer) {
        this.scanForLogonServer = scanForLogonServer;
    }

    public Byte getMyPublishFlag() {
        return myPublishFlag;
    }

    public void setMyPublishFlag(Byte myPublishFlag) {
        this.myPublishFlag = myPublishFlag;
    }

    public Integer getSceneShowType() {
        return sceneShowType;
    }

    public void setSceneShowType(Integer sceneShowType) {
        this.sceneShowType = sceneShowType;
    }

	public Integer getClientAddressMode() {
		return clientAddressMode;
	}

	public void setClientAddressMode(Integer clientAddressMode) {
		this.clientAddressMode = clientAddressMode;
	}
}
