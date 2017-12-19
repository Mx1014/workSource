package com.everhomes.point;

import com.everhomes.util.StringHelper;

import java.util.List;

public class PointCommodityList {

    private List<PointCommodity> rows;

    public List<PointCommodity> getRows() {
        return rows;
    }

    public void setRows(List<PointCommodity> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
