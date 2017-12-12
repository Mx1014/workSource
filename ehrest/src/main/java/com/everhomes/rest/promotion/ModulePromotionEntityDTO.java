//@formatter:off
package com.everhomes.rest.promotion;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *     <li>id: id</li>
 *     <li>commoNo: 商品id</li>
 *     <li>posterUrl: 封面图url</li>
 *     <li>subject: 标题</li>
 *     <li>description: 内容</li>
 *     <li>metadata: 业务所需的其他数据</li>
 *     <li>infoList: 信息流列表{@link com.everhomes.rest.promotion.ModulePromotionInfoDTO}</li>
 * </ul>
 */
public class ModulePromotionEntityDTO {

    private Long id;
    private String commoNo;
    private String posterUrl;
    private String subject;
    private String description;
    private String metadata;
    @ItemType(ModulePromotionInfoDTO.class)
    private List<ModulePromotionInfoDTO> infoList = new ArrayList<>();

    public String getCommoNo() {
        return commoNo;
    }

    public void setCommoNo(String commoNo) {
        this.commoNo = commoNo;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ModulePromotionInfoDTO> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<ModulePromotionInfoDTO> infoList) {
        this.infoList = infoList;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
