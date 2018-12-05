
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:
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

-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:
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
-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本
-- --------------------- SECTION END jinmao ------------------------------------------