//@formatter:off
package com.everhomes.rest.asset;


import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 * <li>dateStr:账期</li>
 * <li>contractNO:合同号</li>
 * <li>addressName: 楼栋门牌名称</li>
 * <li>noticeTel:催缴电话</li>
 * <li>targetName:客户名称</li>
 * <li>targetId:客户id</li>
 * <li>targetType:客户type</li>
 * <li>billGroupDTOList:账单组列表，参考{@link com.everhomes.rest.asset.BillGroupDTO}</li>
 *</ul>
 */
public class ListNotSettledBillDetailResponse {
    private String dateStr;
    private String contractNO;
    private String addressName;
    private String noticeTel;
    private String targetName;
    private String targetType;
    private Long targetId;
    @ItemType(BillGroupDTO.class)
    private List<BillGroupDTO> billGroupDTOList;


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getContractNO() {
        return contractNO;

    }

    public void setContractNO(String contractNO) {
        this.contractNO = contractNO;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getNoticeTel() {
        return noticeTel;
    }

    public void setNoticeTel(String noticeTel) {
        this.noticeTel = noticeTel;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public List<BillGroupDTO> getBillGroupDTOList() {
        return billGroupDTOList;
    }

    public void setBillGroupDTOList(List<BillGroupDTO> billGroupDTOList) {
        this.billGroupDTOList = billGroupDTOList;
    }

    public ListNotSettledBillDetailResponse() {

    }
}
