//@formatter:off
package com.everhomes.rest.asset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class ReSortCmd {
    private String sortField;
    private String sortType;

    public ReSortCmd() {
        super();
    }

    public static List<ReSortCmd> sort(String sortField, String sortType) {
        ReSortCmd sort = new ReSortCmd();
        sort.setSortField(sortField);
        sort.setSortType(sortType);

        List<ReSortCmd> sorts = new ArrayList<ReSortCmd>();
        sorts.add(sort);
        return sorts;
    }

    public static List<ReSortCmd> getDefault() {
        ReSortCmd sort = new ReSortCmd();
        sort.setSortField("defalutOrder");
        sort.setSortType("asc");

        List<ReSortCmd> sorts = new ArrayList<ReSortCmd>();
        sorts.add(sort);
        return sorts;
    }

    public ReSortCmd(String sortField, String sortType) {
        this.sortField = sortField;
        this.sortType = sortType;
    }

    public String getSortField() {
        return sortField;
    }
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
    public String getSortType() {
        return sortType;
    }
    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
