package com.everhomes.rest.contract;

public interface ContractExportPaymentBillCode {
    String SCOPE = "export.paymentbill";
    //表头信息
    String DATESTR="14001";  //账单时间
    String BILLGROUPNAME="12002"; //账单组
    String BILLITEMLISTMSG="12003";  //收费项信息
    String TARGETNAME="12004";   //客户名称
    String TARGETTYPE="12005";   //客户类型
    String PAYMENTSTATUS="12006";    //订单状态
    String PAYMENTTYPE="12007";  //支付方式
    String AMOUNTRECEIVED="12008";   //实收金额
    String AMOUNTRECEIVABLE="12009"; //应收金额
    String AMOUNTEXEMPTION="12010";  //减免
    String AMOUNTSUPPLEMENT="12011"; //增收
    String PAYMENTORDERNUM="13012";  //订单编号
    String PAYTIME="12013";  //缴费时间
    String PAYERTEL="12014"; //缴费人电话
    String PAYERNAME="12015";    //缴费人
    String ADDRESSES="14016";    //楼栋门牌
    //数据标签
    String PERSON="20001";    //个人客户
    String ORGANIZATION="20002";    //企业客户
    String COMPLETED="20003";   //已完成
    String WECHAT="20004";   //微信
    String ALIPAY="20005";   //支付宝
    String PUBLICTRANSFER="20006";   //对公转账

    //表名
    String TABLENAME="30001";

}
