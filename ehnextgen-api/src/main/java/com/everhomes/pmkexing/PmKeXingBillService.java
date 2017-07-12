package com.everhomes.pmkexing;

import com.everhomes.rest.pmkexing.*;

import java.util.List;

/**
 * Created by xq.tian on 2016/12/26.
 */
public interface PmKeXingBillService {

    /**
     * 获取用户为管理员的公司列表
     */
    List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmAdmin();

    /**
     * 获取账单列表
     */
    ListPmKeXingBillsResponse listPmKeXingBills(ListPmKeXingBillsCommand cmd);

    // ListPmKeXingBillsResponse listPmKeXingBillsByMultiRequest(ListPmKeXingBillsCommand cmd);

    /**
     * 获取账单详情
     */
    PmKeXingBillDTO getPmKeXingBill(GetPmKeXingBillCommand cmd);

    /**
     * 获取物业账单统计信息
     */
    PmKeXingBillStatDTO getPmKeXingBillStat(GetPmKeXingBillStatCommand cmd);
}
