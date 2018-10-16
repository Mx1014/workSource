//@formatter:off
package com.everhomes.rest.asset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

/**
 *<ul>
 * <li>sortField: 用于排序的字段(比如：应收=amount_receivable、已收=amount_received、待收=amount_owed)</li>
 * <li>sortType: desc：表示按倒序排序(即：从大到小排序)，asc:表示按正序排序(即：从小到大排序)</li>
 *</ul>
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
