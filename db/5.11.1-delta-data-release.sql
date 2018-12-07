
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:

-- AUTHOR:黄明波
-- REMARK:服务联盟表单修复部分未迁移成功的表单数据
-- REMARK: /yellowPage/transferFlowCaseVals  ownerId填写1802  将返回字符串发给黄明波

-- AUTHOR: xq.tian  20181206
-- REMARK: 将左邻基线和标准版的 ehcore.yml 里的 xss.enabled: true 改成 xss.enabled: false, 其他环境不用修改

-- AUTHOR: 杨崇鑫 2018-12-07
-- REMARK: 缺陷 #42424 智谷汇保证金账单历史数据迁移
-- 1、先备份一下账单数据
-- select * from eh_payment_bill_items where namespace_id=999945;
-- select * from eh_payment_bills where namespace_id=999945; 
-- 2、更新一下保证金计价条款的字段
-- update eh_contract_charging_items set one_time_bill_status=1 where namespace_id=999945 and charging_item_id in (3,4) and contract_id in (5954,6251,6252,6352,6353,6448,6458,6717,7148,7223,7226,7227,7228,7229,7230,7231,7232,7233,7238,7239,7240,7242,7243,7244,7245,7246,7247,7250,7251,7289,7290,7291,7292,7293,7294,7295,7296,7297,7298,7299,7300,7301,7302,7303,7304,7305,7306,7307,7308,7309,7310,7311,7312,7313,7314,7315,7320,7321,7322,7323,7324,7325,7326,7327,7328,7329,7330,7339,7343,7348,7351,7353,7355,7358,7359,7361,7363,7364,7365,7370,7371,7372,7374,7375,7417,7418,7419,7420,7423,7424,7425,7428,7429,7430,7431,7432,7433,8790,8791,8794,8795,8797,8800,8801,8802,8803,8806,8807,8808,9316,9317,9318,9319,9320,9321,9322,9323,9324,9325,9326,9327,9328,9329,9330,9331,9332,9333,9334,9335,9336,9337,9339,9340,9342,9343,9344,9345,9346,9347,9348,9349,9350,9413,9440,9441,9442,9443,9444,9445,9448,9451,9452,9453,9454,9455,9458,9459,9460,9461,9462,9463,9490,9491,9498,9501,9504,9507,9510,9511,9514,9517,9518,9519,9522,9523,9568,9571,9574,9575,9576,9577,9582,9585,9586,9591,9594,9597,9604,9606,9619,9647,9710,10004,10005,10407,10473,10474,10507,10516,10578,10582,10583,10584,10587,10701,10702,10703,10704,10705,10706,10707,10708,10709,10715,10716,10717,10718,10719,10721,10726,10758,10915,11026,11027,11028,11031,11032,11035,11036,11037,11038,11039,11041,11042,11044,11045,11046,11048,11049,11050,11051,11052,11053,11054,11056,11057,11060,11063,11064,11065,11066,11068,11069,11070,11071,11072,11073,11074,11098,11099,11100,11101,11102,11103,11104,11105,11106,11107,11108,11109,11110,11111,11112,11165,11167,11189,11191,11194,11228,11229,11233,11298,11299,11300,11303,11304,11306,11307,11317,11319,11321,11323,11325,11327,11384,11424,11425,11457,11467,11719,11749,11812,11813,11821,11828,11909,11925,11928,11975,11976,11993,12047,12060,12118,14000,14178,14179,14181,14182,14200,14201,14281,14408,14415,14438,14439,14440,14441,14442,14443,14444,14445,14446,14447,14448,14467,14468,14539,14540,14541,14542,14543,14544,14545,14546,14547,14552,14553,14558,14559,14693,14734,14736,14834,14838,14854,14855,14856);
-- 3、根据合同id,自动刷新合同账单
-- 执行接口/contract/autoGeneratingBill
-- namespaceId填：999945
-- contractIds填：5954,6251,6252,6352,6353,6448,6458,6717,7148,7223,7226,7227,7228,7229,7230,7231,7232,7233,7238,7239,7240,7242,7243,7244,7245,7246,7247,7250,7251,7289,7290,7291,7292,7293,7294,7295,7296,7297,7298,7299,7300,7301,7302,7303,7304,7305,7306,7307,7308,7309,7310,7311,7312,7313,7314,7315,7320,7321,7322,7323,7324,7325,7326,7327,7328,7329,7330,7339,7343,7348,7351,7353,7355,7358,7359,7361,7363,7364,7365,7370,7371,7372,7374,7375,7417,7418,7419,7420,7423,7424,7425,7428,7429,7430,7431,7432,7433,8790,8791,8794,8795,8797,8800,8801,8802,8803,8806,8807,8808,9316,9317,9318,9319,9320,9321,9322,9323,9324,9325,9326,9327,9328,9329,9330,9331,9332,9333,9334,9335,9336,9337,9339,9340,9342,9343,9344,9345,9346,9347,9348,9349,9350,9413,9440,9441,9442,9443,9444,9445,9448,9451,9452,9453,9454,9455,9458,9459,9460,9461,9462,9463,9490,9491,9498,9501,9504,9507,9510,9511,9514,9517,9518,9519,9522,9523,9568,9571,9574,9575,9576,9577,9582,9585,9586,9591,9594,9597,9604,9606,9619,9647,9710,10004,10005,10407,10473,10474,10507,10516,10578,10582,10583,10584,10587,10701,10702,10703,10704,10705,10706,10707,10708,10709,10715,10716,10717,10718,10719,10721,10726,10758,10915,11026,11027,11028,11031,11032,11035,11036,11037,11038,11039,11041,11042,11044,11045,11046,11048,11049,11050,11051,11052,11053,11054,11056,11057,11060,11063,11064,11065,11066,11068,11069,11070,11071,11072,11073,11074,11098,11099,11100,11101,11102,11103,11104,11105,11106,11107,11108,11109,11110,11111,11112,11165,11167,11189,11191,11194,11228,11229,11233,11298,11299,11300,11303,11304,11306,11307,11317,11319,11321,11323,11325,11327,11384,11424,11425,11457,11467,11719,11749,11812,11813,11821,11828,11909,11925,11928,11975,11976,11993,12047,12060,12118,14000,14178,14179,14181,14182,14200,14201,14281,14408,14415,14438,14439,14440,14441,14442,14443,14444,14445,14446,14447,14448,14467,14468,14539,14540,14541,14542,14543,14544,14545,14546,14547,14552,14553,14558,14559,14693,14734,14736,14834,14838,14854,14855,14856
-- 点击一次即可，因为需要执行很久，所以肯定会超时


-- AUTHOR: 黄鹏宇 2018-12-07
-- 1.请先刷一次db/search/enterpriseCustomer.sh
-- 2.刷新ES客户数据
-- 然后执行接口customer/syncEnterpriseCustomerIndex
-- 3：然后将招商客户的统计所需数据初始化
-- 最后请执行接口invitedCustomer/initCustomerStatusToDB（此方法每个环境只能执行一次，该方法为异步方法，点击后会立刻返回成功);

-- AUTHOR: 丁建民 20181207
-- 1.执行 issue-44230(合同报表数据迁移问题)的sql,在下面
-- 2.同步es /contract/syncContracts
-- 3.调用接口/contract/reportForm/excuteContractReportFormJob 生成合同统计数据，时间比较长

-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR:
-- REMARK:

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V7.5
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10022', 'zh_CN', '此账单不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'assetv2', '10023', 'zh_CN', '该账单不是已出账单');

-- AUTHOR: 张智伟
-- REMARK: issue-42126
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'meetingMessage' AS scope,100008 AS code,'zh_CN' AS locale,'您已不是参会人' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100009 AS code,'zh_CN' AS locale,'您已不是会务人' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100010 AS code,'zh_CN' AS locale,'您已被指定为会务人' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;


-- AUTHOR:吴寒
-- REMARK: 支付授权module修改
UPDATE  eh_service_modules SET client_handler_type = 2, HOST = NULL WHERE id= 79880000 ;

-- AUTHOR: tangcen 2018年12月5日
-- REMARK: 添加房源招商的权限
SET @id = (select max(id) from eh_service_module_privileges);
-- 新增房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38101, '新增房源', 80, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38101, 0, '楼宇资产管理 新增房源', '楼宇资产管理 业务模块权限', NULL);
-- 编辑房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38102, '编辑房源', 90, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38102, 0, '楼宇资产管理 编辑房源', '楼宇资产管理 业务模块权限', NULL);
-- 删除房源权限
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38103, '删除房源', 100, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38103, 0, '楼宇资产管理 删除房源', '楼宇资产管理 业务模块权限', NULL);
-- 查看所有的预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38104, '查看所有的预定记录', 150, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38104, 0, '楼宇资产管理 查看所有的预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 新增预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38105, '新增预定记录', 160, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38105, 0, '楼宇资产管理 新增预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 编辑预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38106, '编辑预定记录', 170, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38106, 0, '楼宇资产管理 编辑预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 删除预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38107, '删除预定记录', 180, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38107, 0, '楼宇资产管理 删除预定记录', '楼宇资产管理 业务模块权限', NULL);
-- 取消预定记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38108, '取消预定记录', 190, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38108, 0, '楼宇资产管理 取消预定记录', '楼宇资产管理 业务模块权限', NULL);	
-- 删除楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38109, '删除楼宇', 30, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38109, 0, '楼宇资产管理 删除楼宇', '楼宇资产管理 业务模块权限', NULL);	
-- 新增楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38110, '新增楼宇', 10, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38110, 0, '楼宇资产管理 新增楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 编辑楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38111, '编辑楼宇', 20, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38111, 0, '楼宇资产管理 编辑楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 导入楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38112, '导入楼宇', 40, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38112, 0, '楼宇资产管理 导入楼宇', '楼宇资产管理 业务模块权限', NULL);	
-- 导出楼宇
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38113, '导出楼宇', 50, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38113, 0, '楼宇资产管理 导出楼宇', '楼宇资产管理 业务模块权限', NULL);
-- 编辑项目
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38114, '编辑项目', 1, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38114, 0, '楼宇资产管理 编辑项目', '楼宇资产管理 业务模块权限', NULL);
-- 楼宇排序
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38115, '楼宇排序', 60, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38115, 0, '楼宇资产管理 楼宇排序', '楼宇资产管理 业务模块权限', NULL);
-- 拆分房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38116, '拆分房源', 130, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38116, 0, '楼宇资产管理 拆分房源', '楼宇资产管理 业务模块权限', NULL);
-- 合并房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38117, '合并房源', 140, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38117, 0, '楼宇资产管理 合并房源', '楼宇资产管理 业务模块权限', NULL);
-- 查看房源授权价记录
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38118, '查看房源授权价记录', 200, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38118, 0, '楼宇资产管理 查看房源授权价记录', '楼宇资产管理 业务模块权限', NULL);
-- 新增授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38119, '新增授权价', 210, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38119, 0, '楼宇资产管理 新增授权价', '楼宇资产管理 业务模块权限', NULL);
-- 编辑授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38120, '编辑授权价', 220, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38120, 0, '楼宇资产管理 编辑授权价', '楼宇资产管理 业务模块权限', NULL);
-- 删除授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38121, '删除授权价', 230, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38121, 0, '楼宇资产管理 删除授权价', '楼宇资产管理 业务模块权限', NULL);
-- 查看房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38122, '查看房源', 70, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38122, 0, '楼宇资产管理 查看房源', '楼宇资产管理 业务模块权限', NULL);
-- 批量导入授权价
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38123, '批量导入授权价', 240, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38123, 0, '楼宇资产管理 批量导入授权价', '楼宇资产管理 业务模块权限', NULL);
-- 上传房源附件
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38124, '上传房源附件', 260, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38124, 0, '楼宇资产管理 上传房源附件', '楼宇资产管理 业务模块权限', NULL);
-- 删除房源附件
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38125, '删除房源附件', 270, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38125, 0, '楼宇资产管理 删除房源附件', '楼宇资产管理 业务模块权限', NULL);
-- 按房源导出
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38126, '按房源导出', 280, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38126, 0, '楼宇资产管理 按房源导出', '楼宇资产管理 业务模块权限', NULL);
-- 按楼宇导入房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38127, '按楼宇导入房源', 110, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38127, 0, '楼宇资产管理 按楼宇导入房源', '楼宇资产管理 业务模块权限', NULL);
-- 按楼宇导出房源
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38128, '按楼宇导出房源', 120, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38128, 0, '楼宇资产管理 按楼宇导出房源', '楼宇资产管理 业务模块权限', NULL);
-- 房源管理
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES (@id:=@id+1 , 38000, 0, 38129, '房源管理', 250, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) 
	VALUES (38129, 0, '楼宇资产管理 房源管理', '楼宇资产管理 业务模块权限', NULL);
	
-- AUTHOR: tangcen 2018年12月5日
-- REMARK: 添加房源日志模板
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '1', 'zh_CN', '房源事件', '创建房源', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '2', 'zh_CN', '房源事件', '删除房源', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '3', 'zh_CN', '房源事件', '修改${display}:由${oldData}更改为${newData}	', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '4', 'zh_CN', '房源拆分、合并计划事件', '创建房源合并计划，生效时间为${dateBegin}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '5', 'zh_CN', '房源拆分、合并计划事件', '创建房源拆分计划，生效时间为${dateBegin}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '6', 'zh_CN', '房源拆分、合并计划事件', '删除房源合并计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '7', 'zh_CN', '房源拆分、合并计划事件', '删除房源拆分计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '8', 'zh_CN', '房源拆分、合并计划事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '9', 'zh_CN', '房源拆分、合并计划事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '10', 'zh_CN', '房源授权价事件', '创建房源授权价', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '11', 'zh_CN', '房源授权价事件', '删除房源授权价', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '12', 'zh_CN', '房源授权价事件', '修改${display}:由${oldData}更改为${newData}', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '13', 'zh_CN', '房源预定事件', '创建房源预定计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '14', 'zh_CN', '房源预定事件', '删除房源预定计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '15', 'zh_CN', '房源预定事件', '取消房源预定计划', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('address.tracking', '16', 'zh_CN', '房源预定事件', '修改${display}:由${oldData}更改为${newData}', '0');	

-- AUTHOR: tangcen 2018年12月5日
-- REMARK: 添加房源日志tab
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) 
	VALUES (1010, 'asset_management', '0', '/1010', '房源日志', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
	
-- AUTHOR: tangcen 2018年12月5日
-- REMARK: 资产管理的管理配置页面添加默认的tab卡
set @item_id = (select max(id) from `eh_var_field_group_scopes`);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) 
	VALUES (((@item_id:=@item_id+1)), 0, 'asset_management', 1010, '房源日志', 1, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);

-- AUTHOR:  谢旭双
-- REMARK: 修改会议预定消息内容
UPDATE eh_locale_strings SET text = REPLACE(text,'您','你') WHERE scope IN ('meetingErrorCode', 'meetingMessage');
UPDATE eh_locale_templates SET text = REPLACE(text,'您','你'),description=REPLACE(description,'您','你')  WHERE scope='meetingMessage';

-- AUTHOR: tangcen
-- REMARK: 添加导入房源授权价时的出错提示
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'address', '40001', 'zh_CN', '收费项名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'address', '40002', 'zh_CN', '授权金额不能为空');

-- AUTHOR:丁建民 20181205
-- REMARK: issue-37007 合同报表相关
SET @eh_configurations = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ((@eh_configurations := @eh_configurations + 1), 'schedule.contractstatics.cronexpression', '0 30 2 * * ?', '合同报表定时任务', '0', NULL, '1');


SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '30001', 'zh_CN', '请输入正确的查询时间');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '30002', 'zh_CN', '请输入查询项目');

-- AUTHOR:  张智伟
-- REMARK: issue-43865 web端参数传错，数据修复
UPDATE eh_meeting_invitations SET source_type='MEMBER_DETAIL' WHERE source_type='source_user';

-- AUTHOR:  吴寒
-- REMARK: 福利v1.0:文字模板脚本
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'welfare.msg', 1, 'zh_CN', '发福利消息', '$你收到了${subject},快去查看吧!', 0);
-- 模块配置脚本
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('273000','企业福利','310000','/100/310000/79880000','1','3','2','10','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','module','0','2',NULL,NULL,NULL,NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79884000','企业福利','48000000',NULL,'enterprise-welfare','1','2','/40000010/48000000/79884000','park','8','273000','3','system','module','2','1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79885000','企业福利','16040000',NULL,'enterprise-welfare','1','2','/23000000/16040000/79885000','zuolin','8','273000','3','system','module','2','1');

-- AUTHOR:丁建民 20181205
-- REMARK: issue-44220 
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'contract', '10016', 'zh_CN', '调整周期 不能为0');

-- AUTHOR:丁建民 20181207
-- REMARK: issue-44230(合同报表数据迁移问题)
UPDATE eh_contracts as aa inner join (
SELECT t1.id, t1.update_time,t1.create_time from eh_contracts as t1, eh_contracts as t2 WHERE t1.id=t2.id and t1.`status` in (2, 10) and t1.update_time is NULL
) as tt  ON aa.id=tt.id SET aa.`update_time`=tt.create_time;

-- AUTHOR:梁燕龙
-- REMARK:修改模块数据
UPDATE eh_service_modules SET name = '园区电子报-定制' WHERE id = 10500;
UPDATE eh_service_modules SET name = '行业协会-定制' WHERE id = 10760;
UPDATE eh_service_modules SET name = '短信推送' WHERE id = 12200;
UPDATE eh_service_modules SET name = '物业报修' WHERE id = 20100;
UPDATE eh_service_modules SET name = '缴费管理' WHERE id = 20400;
UPDATE eh_service_modules SET app_type = 0 WHERE id = 20500;
UPDATE eh_service_modules SET name = '企业客户管理' WHERE id = 21100;
UPDATE eh_service_modules SET app_type = 0 WHERE id = 21400;
UPDATE eh_service_modules SET name = '科技园合同管理-定制' WHERE id = 32500;
UPDATE eh_service_modules SET name = '孵化器入驻-定制' WHERE id = 36000;
UPDATE eh_service_modules SET name = '园区地图-定制' WHERE id = 40070;
UPDATE eh_service_modules SET name = '资源预约' WHERE id = 40400;
UPDATE eh_service_modules SET name = '快递服务-定制' WHERE id = 40700;
UPDATE eh_service_modules SET name = '企业人才-定制' WHERE id = 40730;
UPDATE eh_service_modules SET name = '车辆管理-定制' WHERE id = 40900;
UPDATE eh_service_modules SET name = '门禁APP用户端' WHERE id = 41000;
UPDATE eh_service_modules SET name = '园区班车-定制' WHERE id = 41015;
UPDATE eh_service_modules SET name = '一键上网-定制' WHERE id = 41100;
UPDATE eh_service_modules SET name = '储能一卡通-定制' WHERE id = 41200;
UPDATE eh_service_modules SET name = '园区文件管理-定制' WHERE id = 41500;
UPDATE eh_service_modules SET name = '园区审批-定制' WHERE id = 41600;
UPDATE eh_service_modules SET name = '政务服务-定制' WHERE id = 41900;
UPDATE eh_service_modules SET name = '门禁钥匙管理-定制' WHERE id = 42000;
UPDATE eh_service_modules SET name = '组织管理' WHERE id = 50100;
UPDATE eh_service_modules SET name = '会议管理' WHERE id = 53000;
UPDATE eh_service_modules SET name = '招商管理' WHERE id = 150020;
UPDATE eh_service_modules SET app_type = 0 WHERE id = 200000;
UPDATE eh_service_modules SET name = 'H5快讯-定制' WHERE id = 10800001;
UPDATE eh_service_modules SET name = '文档管理' WHERE id = 55000;
UPDATE eh_service_modules SET name = '北环门禁-定制' WHERE id = 272000;
UPDATE eh_service_modules SET name = '待办事项-定制' WHERE id = 56300;
UPDATE eh_service_modules SET name = '园区支付授权-定制' WHERE id = 200000;
UPDATE eh_service_modules SET name = '企业支付授权' WHERE id = 79880000;
UPDATE eh_service_modules SET name = '左邻会议' WHERE id = 50700;
INSERT INTO eh_service_modules(id, name, parent_id, path, type, level, status, default_order, create_time,
                               instance_config,operator_uid,creator_uid,description,multiple_flag, module_control_type,access_control_type,
                               menu_auth_flag,category,app_type,client_handler_type)
    VALUES (274000,'同事圈',50000,'/100/50000/274000',1,3,2,100,now(),'',0,0,'',1,'org_control',2,1,'module',0,0);
-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-standard
-- DESCRIPTION: 此SECTION只在左邻标准版执行的脚本

-- AUTHOR:梁燕龙 20181120
-- REMARK: 将移动端工作台的通讯录合我的任务移动到企业办公中
SET @id = (SELECT IFNULL(MAX(id),0) from eh_service_module_entries);
SET @categoryId = (SELECT IFNULL(id,0) from eh_app_categories WHERE name = '企业办公' AND location_type = 1);
INSERT INTO eh_service_module_entries (id,module_id, module_name, entry_name, terminal_type, location_type, scene_type, app_category_id, default_order)
VALUES (@id := @id+1,50100,'组织架构','通讯录',1,1,1,@categoryId,1);
INSERT INTO eh_service_module_entries (id,module_id, module_name, entry_name, terminal_type, location_type, scene_type, app_category_id, default_order)
VALUES (@id := @id+1,13000,'任务管理','我的任务',1,1,1,@categoryId,2);

-- AUTHOR: 梁燕龙 20181123
-- REMARK: 删除原来的用户编辑数据
DELETE from eh_user_apps WHERE location_type = 1 OR location_type = 2;
DELETE from eh_user_app_flags WHERE location_type = 1 OR location_type = 2;

-- AUTHOR:梁燕龙 20181207
-- REMARK: 初始化标准版工作台数据
SET @versionCode = '201812070000';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @newsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10800 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);
SET @meetingAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 53000 AND `namespace_id` = 2);
SET @workFlowAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 13000 AND `namespace_id` = 2);
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"groupId\":1,\"groupName\":\"容器\","title":"容器","titleFlag":0,"titleStyle":101,"titleSize":1,"titleMoreFlag":0,\"columnCount\":4,\"defaultOrder\":0,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0,\"itemShowNum\":8.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Fold\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31059\",\"rowCount\":2.0,\"noticeCount\":2.0,\"style\":1,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":1,\"separatorHeight\":16,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"任务管理\","title":"任务管理","titleFlag":1,\"titleUrl\":\"http://10.1.10.96:5000/image/aW1hZ2UvTVRveE1XTTJZMkV3T0dOaE5tRXdNamczTXpGbE5EYzFObVkzWm1ObFlURm1OUQ?token=Yv6KLSwtNo3CIcOxq8Un08lDTtx0Jdm34cLLMr14_wus_udZWepmGMDY7SgjhNvBmt9M5AX9Y-IX7hHEdaExVhtQDaIAv1jXj79MDeAPBIk\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31060\",\"moduleId\":13000,\"appId\":',@workFlowAppid,',\"clientHandlerType\":0,\"maxShowNum\":3.0,\"routerPath\":\"/index\",\"routerQuery\":\"appId=',@workFlowAppId,'&moduleId=13000&clientHandlerType=0&locationType=1&sceneType=1&displayName=%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86\",\"router\":\"zl://workflow/index?appId=',@workFlowAppId,'&moduleId=13000&clientHandlerType=0&locationType=1&sceneType=1&displayName=%E4%BB%BB%E5%8A%A1%E7%AE%A1%E7%90%86\"},\"separatorFlag\":1,\"separatorHeight\":16,\"style\":\"Default\",\"widget\":\"CardExtension\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"今日会议安排\","title":"今日会议安排","titleFlag":1,\"titleUrl\":\"\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31061\",\"moduleId\":53000,\"appId\":',@meetingAppId,',\"clientHandlerType\":0,\"routerPath\":\"/index\",\"router\":\"zl://meeting-reservation/index?appId=',@meetingAppId,'&moduleId=53000&clientHandlerType=0&locationType=1&sceneType=2&displayName=%E4%BC%9A%E8%AE%AE&url=http%3A%2F%2Foa.zuolin.com%2Fmobile%2Fstatic%2Fcoming_soon%2Findex.html\",\"routerQuery\":\"appId=',@meetingAppId,'&moduleId=53000&clientHandlerType=0&locationType=1&sceneType=2&displayName=%E4%BC%9A%E8%AE%AE&url=http%3A%2F%2Foa.zuolin.com%2Fmobile%2Fstatic%2Fcoming_soon%2Findex.html\"},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"CardExtension\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\",\"style\":\"Shape\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0,"autoScroll":1,"showDots":1},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\","title":"容器","titleFlag":0,"titleStyle":101,"titleSize":1,"titleMoreFlag":0,\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\","title":"商品精选","titleFlag":1,"titleStyle":101,"titleSize":1,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":8,\"groupName\":\"园区快讯\","title":"园区快讯","titleFlag":1,"titleStyle":101,"titleSize":1,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10800,\"appId\":', @newsAppId, ',\"actionType\":48,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"NewsListView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\","title":"活动","titleFlag":1,"titleStyle":101,"titleSize":1,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\","title":"论坛","titleFlag":1,"titleStyle":101,"titleSize":1,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;

-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END guangda -------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本

-- AUTHOR:梁燕龙 20181207
-- REMARK: 更新会员信息时，推送消息给用户
SET @id = (SELECT max(id) from eh_locale_templates);
INSERT INTO eh_locale_templates(id, scope, code, locale, description, text, namespace_id)
    VALUES (@id := @id +1 , 'ruian.message', 1,'zh_CN','瑞安升级会员时，推送消息给用户','您已成为${levelName}会员',0);
-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本

-- AUTHOR: 黄明波
-- REMARK: 上海金茂语音识别对接
SET @max_id := (select ifnull(max(id), 0) from eh_xfyun_match);
SET @vendor := 'ZUOLINIOS';
SET @service := 'zuolin';
SET @namespace_id := 0;
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wuyebaoxiu', '物业报修', 20100, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wuyekefu', '物业客服', 40300, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wuyejiaofei', '物业缴费', 20400, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'fangkeyuyue', '访客预约', 41800, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'tingchejiaofei', '停车缴费', 40800, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'pinzhihecha', '品质核查', 20600, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'shequhuodong', '社区活动', 10600, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'ziyuanyuyue', '资源预约', 40400, 0, NULL, NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodegongdan', '我的工单', NULL, 1, 'zl://workflow/tasks', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodedianpu', '我的店铺', NULL, 1, 'zl://browser/i?url=${home.url}/mobile/static/stay_tuned/index.html', 2, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodefabu', '我的发布', NULL, 1, 'zl://user-publish/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodeshoucang', '我的收藏', NULL, 1, 'zl://user-collection/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodebaoming', '我的报名', NULL, 1, 'zl://user-apply/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'sousuo', '搜索', NULL, 1, 'zl://search/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'saoyisao', '扫一扫', NULL, 1, 'zl://scan/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'xiaoxi', '消息', NULL, 1, 'zl://message/index', NULL, NULL);
INSERT INTO `eh_xfyun_match` (`id`, `namespace_id`, `vendor`, `service`, `intent`, `description`, `module_id`, `type`, `default_router`, `client_handler_type`, `access_control_type`) VALUES (@max_id := @max_id + 1, @namespace_id, @vendor, concat(@vendor, '.', @service), 'wodedizhi', '我的地址', NULL, 1, 'zl://address/index', NULL, NULL);

-- 添加测试token
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('xfyun.tpp.testToken', '341a5441a2ac8c2f', '讯飞测试token', 0, NULL, 0);


-- 添加测试停车场
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.host', 'http://10.1.10.37:80', '接口地址', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.appkey', '880076901009202', 'appkey', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO.secretKey', '880076901009202', 'secretKey', 999925, NULL, 1);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO2.host', 'http://10.1.10.37:80', '接口地址', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO2.appkey', '880076901009202', 'appkey', 999925, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.hkws.HKWS_SHJINMAO2.secretKey', '880076901009202', 'secretKey', 999925, NULL, 1);

set @max_lots_id := (select ifnull(max(id),0)  from eh_parking_lots);
set @namespace_id := 999925;
set @community_id := 240111044832061113;
set @parking_name := '金茂雅苑一期停车场';
set @parking_vendor := 'HKWS_SHJINMAO';
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`) 
VALUES ((@max_lots_id := @max_lots_id + 1), 'community', @community_id,  @parking_name,  @parking_vendor, '', 2, 1, now(), @namespace_id, '{"expiredRechargeFlag":0,"monthlyDiscountFlag":0,"tempFeeDiscountFlag":0}', '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0,"monthRechargeFlag":0}', right(@max_lots_id, 3), @max_lots_id, NULL, '["tempfee","monthRecharge"]');

set @community_id := 240111044832061114;
set @parking_name := '金茂雅苑二期停车场';
set @parking_vendor := 'HKWS_SHJINMAO2';
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`) 
VALUES ((@max_lots_id := @max_lots_id + 1), 'community', @community_id,  @parking_name,  @parking_vendor, '', 2, 1, now(), @namespace_id, '{"expiredRechargeFlag":0,"monthlyDiscountFlag":0,"tempFeeDiscountFlag":0}', '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0,"monthRechargeFlag":0}', right(@max_lots_id, 3), @max_lots_id, NULL, '["tempfee","monthRecharge"]');








-- --------------------- SECTION END jinmao ------------------------------------------