package com.everhomes.rest.rentalv2;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.rentalv2.admin.AttachmentConfigDTO;
import com.everhomes.rest.rentalv2.admin.RentalOpenTimeDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <ul>
 * <li>rentalSiteId：场所id</li>
 * <li>siteName：场所名称</li>
 * <li>resourceCounts：场所数量</li>
 * <li>introduction：详情</li>
 * <li>notice：备注文字</li>
 * <li>address：地址</li>
 * <li>longitude：经纬度</li>
 * <li>latitude：经纬度</li>
 * <li>contactPhonenum：电话号码</li>
 * <li>	discountType：     	折扣信息：0不打折 1满减优惠2比例折扣	</li>
 * <li>	fullPrice：        	满XX元	</li>
 * <li>	cutPrice：         	减XX元	</li>
 * <li>	discountRatio：    	折扣比例	</li>
 * <li>	rentalType： 0: 按小时预定  1-半天 2-天 3-支持晚上的半天	</li>
 * <li>	rentalStep：     步长，每次最少预定多少个单元格（目前默认都是1）</li>
 * <li>	exclusiveFlag：   是否为独占资源0否 1 是	</li>
 * <li>	autoAssign：       	是否动态分配 1是 0否	</li>
 * <li>	multiUnit：        	是否允许预约多个场所 1是 0否	</li>
 * <li>	multiTimeInterval：	是否允许预约多个时段 1是 0否	</li>
 * <li>	cancelFlag：       	是否允许取消 1是 0否	</li>
 * <li>	needPay：          	是否需要支付 1是 0否	</li>
 * <li>anchorTime：规则日期</li>
 * <li>siteDays：每一天的单元格，参考{@link com.everhomes.rest.rentalv2.RentalSiteDayRulesDTO}</li> 
 * <li>sitePics：详情图片多张，参考{@link com.everhomes.rest.rentalv2.RentalSitePicDTO}</li> 
 * <li>attachments：附件列表，参考{@link com.everhomes.rest.rentalv2.AttachmentDTO} </li> 
 * <li>siteItems：商品列表，参考{@link com.everhomes.rest.rentalv2.SiteItemDTO}</li> 
 * </ul>
 */
public class FindRentalSiteMonthStatusByWeekCommandResponse {

    private Long rentalSiteId;
    private String siteName;
    private Double resourceCounts;
    private String introduction;
    private String notice;
    private String address;
    private java.lang.Double longitude;
    private java.lang.Double latitude;
    private String contactPhonenum;
    private java.lang.Byte       discountType;
    private java.math.BigDecimal fullPrice;
    private java.math.BigDecimal cutPrice;
    private java.lang.Double     discountRatio;
    private java.lang.Byte       rentalType;
    private java.lang.Integer    rentalStep;
    private java.lang.Byte       exclusiveFlag;
    private java.lang.Byte       autoAssign;
    private java.lang.Byte       multiUnit;
    private java.lang.Byte       multiTimeInterval;
    private java.lang.Byte       cancelFlag;
    private java.lang.Byte       needPay;
    private Long anchorTime;
    @ItemType(RentalSiteDayRulesDTO.class)
    private List<RentalSiteDayRulesDTO> siteDays;
    @ItemType(RentalSitePicDTO.class)
    private List<RentalSitePicDTO> sitePics;
    @ItemType(AttachmentConfigDTO.class)
    private List<AttachmentConfigDTO> attachments;
    @ItemType(SiteItemDTO.class)
    private List<SiteItemDTO> siteItems;
    private String openTimes;

    public Long getRentalSiteId() {
        return rentalSiteId;
    }

    public void setRentalSiteId(Long rentalSiteId) {
        this.rentalSiteId = rentalSiteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public Double getResourceCounts() {
        return resourceCounts;
    }

    public void setResourceCounts(Double resourceCounts) {
        this.resourceCounts = resourceCounts;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getContactPhonenum() {
        return contactPhonenum;
    }

    public void setContactPhonenum(String contactPhonenum) {
        this.contactPhonenum = contactPhonenum;
    }

    public Byte getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Byte discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(BigDecimal fullPrice) {
        this.fullPrice = fullPrice;
    }

    public BigDecimal getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(BigDecimal cutPrice) {
        this.cutPrice = cutPrice;
    }

    public Double getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(Double discountRatio) {
        this.discountRatio = discountRatio;
    }

    public Byte getRentalType() {
        return rentalType;
    }

    public void setRentalType(Byte rentalType) {
        this.rentalType = rentalType;
    }

    public Integer getRentalStep() {
        return rentalStep;
    }

    public void setRentalStep(Integer rentalStep) {
        this.rentalStep = rentalStep;
    }

    public Byte getExclusiveFlag() {
        return exclusiveFlag;
    }

    public void setExclusiveFlag(Byte exclusiveFlag) {
        this.exclusiveFlag = exclusiveFlag;
    }

    public Byte getAutoAssign() {
        return autoAssign;
    }

    public void setAutoAssign(Byte autoAssign) {
        this.autoAssign = autoAssign;
    }

    public Byte getMultiUnit() {
        return multiUnit;
    }

    public void setMultiUnit(Byte multiUnit) {
        this.multiUnit = multiUnit;
    }

    public Byte getMultiTimeInterval() {
        return multiTimeInterval;
    }

    public void setMultiTimeInterval(Byte multiTimeInterval) {
        this.multiTimeInterval = multiTimeInterval;
    }

    public Byte getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(Byte cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public Byte getNeedPay() {
        return needPay;
    }

    public void setNeedPay(Byte needPay) {
        this.needPay = needPay;
    }

    public Long getAnchorTime() {
        return anchorTime;
    }

    public void setAnchorTime(Long anchorTime) {
        this.anchorTime = anchorTime;
    }

    public List<RentalSiteDayRulesDTO> getSiteDays() {
        return siteDays;
    }

    public void setSiteDays(List<RentalSiteDayRulesDTO> siteDays) {
        this.siteDays = siteDays;
    }

    public List<RentalSitePicDTO> getSitePics() {
        return sitePics;
    }

    public void setSitePics(List<RentalSitePicDTO> sitePics) {
        this.sitePics = sitePics;
    }

    public List<AttachmentConfigDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentConfigDTO> attachments) {
        this.attachments = attachments;
    }

    public List<SiteItemDTO> getSiteItems() {
        return siteItems;
    }

    public void setSiteItems(List<SiteItemDTO> siteItems) {
        this.siteItems = siteItems;
    }

    public String getOpenTimes() {
        return openTimes;
    }

    public void setOpenTimes(String openTimes) {
        this.openTimes = openTimes;
    }
}
