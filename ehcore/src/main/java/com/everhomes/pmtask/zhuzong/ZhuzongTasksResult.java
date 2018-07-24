package com.everhomes.pmtask.zhuzong;


import java.util.List;

public class ZhuzongTasksResult {
    private List<ZhuzongTasksData> datas;
    private Integer total;
    private Integer currentpage;
    private Integer totalpage;

    public List<ZhuzongTasksData> getDatas() {
        return datas;
    }

    public void setDatas(List<ZhuzongTasksData> datas) {
        this.datas = datas;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(Integer currentpage) {
        this.currentpage = currentpage;
    }

    public Integer getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(Integer totalpage) {
        this.totalpage = totalpage;
    }
}
