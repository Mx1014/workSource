package com.everhomes.payment.zhuzong;

import java.util.List;

/**
 * UserID : 账号
 * ResultID : 结果编号 1 失败 2 没有信息 3 已解绑 4 已销户 5 不存在
 */
public class ZhuzongConsumeResponse {
    private String UserID;
    private String ResultID;
    private String PageIndex;
    private String PageSize;
    private String TotalCount;
    private List<ZhuzongConsumeDate> DataList;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getResultID() {
        return ResultID;
    }

    public void setResultID(String resultID) {
        ResultID = resultID;
    }

    public String getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(String pageIndex) {
        PageIndex = pageIndex;
    }

    public String getPageSize() {
        return PageSize;
    }

    public void setPageSize(String pageSize) {
        PageSize = pageSize;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public List<ZhuzongConsumeDate> getDataList() {
        return DataList;
    }

    public void setDataList(List<ZhuzongConsumeDate> dataList) {
        DataList = dataList;
    }
}
