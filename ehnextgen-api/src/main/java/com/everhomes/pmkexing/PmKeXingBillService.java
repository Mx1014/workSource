package com.everhomes.pmkexing;

import com.everhomes.asset.GetLeaseContractBillOnFiPropertyRes;
import com.everhomes.asset.GetLeaseContractReceivableRes;
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

    /**
     * 对接一碑系统，在后台查询正中会的账单列表， by Wentian.Wang
     * @param ownerId 园区id
     * @param contractNum   合同编号（不使用）
     * @param dateStrBegin  开始日期
     * @param dateStrEnd    截止日期
     * @param fiProperty    收款单位
     * @param billStatus    账单状态
     * @param targetName    客户名称
     * @param targetId      客户id（不使用）
     * @param pageSize      页大小
     * @param pageOffSet    页码
     * @return              远程接口请求的json的dto
     */
    GetLeaseContractBillOnFiPropertyRes getFiPropertyBills(Long ownerId, String contractNum, String dateStrBegin, String dateStrEnd, String fiProperty, Byte billStatus, String targetName, Long targetId, Integer pageSize, Integer pageOffSet);

    /**
     * 根据拼接的账单id来查询对应客户的fiCategory维度的账单 by Wentian.Wang
     */
    GetLeaseContractReceivableRes listFiCategoryBills(String billId, Long ownerId, Integer pageOffSet, Integer pageSize);

    /**
     * 根据用户获得所有fiProperty维度的账单 by Vladimir Putin
     */
    GetLeaseContractBillOnFiPropertyRes getAllFiPropertyBills(Integer namespaceId, Long ownerId, Long targetId, String targetType,Byte isPay);
}
