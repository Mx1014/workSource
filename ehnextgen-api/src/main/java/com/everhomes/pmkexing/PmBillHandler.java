package com.everhomes.pmkexing;

import com.everhomes.rest.pmkexing.*;

import java.util.List;

/**
 *
 * Created by xq.tian on 2016/12/27.
 */
interface PmBillHandler {

    /**
     * 初始化handler
     * @see com.everhomes.rest.pmkexing.PmBillVendor
     * @return  返回该handler所能处理的域空间的物业费用的 PmBillVendor,
     *           可以返回多个 PmBillVendor ,表示该handler可以处理多个域空间的物业缴费
     */
    PmBillVendor[] initHandler();

    /**
     * 获取用户为管理员的公司列表
     */
    List<ListOrganizationsByPmAdminDTO> listOrganizationsByPmOwner();

    /**
     * 获取账单列表
     */
    ListPmKeXingBillsResponse listPmBills(ListPmKeXingBillsCommand cmd);

    /**
     * 获取账单详情
     */
    PmKeXingBillDTO getPmBill(GetPmKeXingBillCommand cmd);

    /**
     * 获取物业账单统计信息
     */
    PmKeXingBillStatDTO getPmBillStat(GetPmKeXingBillStatCommand cmd);
}
