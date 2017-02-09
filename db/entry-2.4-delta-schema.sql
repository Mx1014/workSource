ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `contract_id` BIGINT COMMENT 'eh_contracts id';


要修改的接口:
techpark/entry/listApplyEntrys 查询和返回都增加楼栋字段   测试点:1.看能否按照楼栋过滤,2.查出楼栋的名称是否正确.
http://techpark-beta.zuolin.com/evh/yellowPage/getYellowPageTopic 创客空间增加楼栋 测试点:看增加表内有楼栋id

/techpark/entry/applyEntry 续租 增加 合同id


要加的表-  
黄页加楼栋字段, 
申请加关联楼栋的新表, 
申请表 eh_enterprise_op_requests 加合同id 