-- Designer: zhiwei zhang
-- Description: ISSUE#24392 固定资产管理V1.0（支持对内部各类固定资产进行日常维护）


INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'fixedAsset' AS scope,10000 AS code,'zh_CN' AS locale,'分类名称已存在' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10001 AS code,'zh_CN' AS locale,'存在资产属于此分类，请清空后再删除。' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10002 AS code,'zh_CN' AS locale,'资产编号已经存在' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10003 AS code,'zh_CN' AS locale,'父分类不存在' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10004 AS code,'zh_CN' AS locale,'上传了空文件' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10005 AS code,'zh_CN' AS locale,'没有资产分类，请先创建资产分类' AS text UNION ALL

SELECT 'fixedAsset' AS scope,100000 AS code,'zh_CN' AS locale,'保存资产时出错' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100001 AS code,'zh_CN' AS locale,'请输入编号' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100002 AS code,'zh_CN' AS locale,'编号：请输入不多于20字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100003 AS code,'zh_CN' AS locale,'分类：系统不存在此分类' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100004 AS code,'zh_CN' AS locale,'请输入名称' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100005 AS code,'zh_CN' AS locale,'名称：请输入不多于20字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100006 AS code,'zh_CN' AS locale,'规格：请输入不多于50字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100007 AS code,'zh_CN' AS locale,'单价：请输入数字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100008 AS code,'zh_CN' AS locale,'单价：最大支持999,999,999.99' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100009 AS code,'zh_CN' AS locale,'购买时间：格式错误，请输入xxxx-xx-xx' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100010 AS code,'zh_CN' AS locale,'所属供应商：请输入不多于50字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100013 AS code,'zh_CN' AS locale,'其他：请输入不多于200字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100014 AS code,'zh_CN' AS locale,'请输入状态' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100021 AS code,'zh_CN' AS locale,'状态为使用中的领用时间必填，使用部门、使用人至少填写一项' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100016 AS code,'zh_CN' AS locale,'部门不存在或格式错误:上下级部门间用"/"隔开，且从最上级部门开始，例如"左邻/深圳研发中心/研发部"' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100017 AS code,'zh_CN' AS locale,'使用人不存在或关联（使用部门）下不存在（使用人）' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100018 AS code,'zh_CN' AS locale,'领用时间：格式错误，请输入xxxx-xx-xx' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100022 AS code,'zh_CN' AS locale,'使用部门、使用人至少填写一项' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100019 AS code,'zh_CN' AS locale,'存放地点：请输入不多于50字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100020 AS code,'zh_CN' AS locale,'备注信息：请输入不多于200字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100023 AS code,'zh_CN' AS locale,'分类：请选择分类' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100024 AS code,'zh_CN' AS locale,'请输入来源' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100025 AS code,'zh_CN' AS locale,'状态为使用中时，使用部门、领用时间必填' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100026 AS code,'zh_CN' AS locale,'使用人填写时，使用部门、领用时间必填' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100027 AS code,'zh_CN' AS locale,'使用部门、领用时间需要同时填写' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100028 AS code,'zh_CN' AS locale,'请输入正确的领用时间' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100029 AS code,'zh_CN' AS locale,'请输入正确的购买时间' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;


INSERT INTO eh_locale_templates(scope,code,locale,description,text,namespace_id)
SELECT r.scope,r.code,r.locale,r.description,r.text,0
FROM(
SELECT 'fixedAsset' AS scope,100012 AS code,'zh_CN' AS locale,'来源仅支持：购入、自建、租赁、捐赠、其他' AS description,'来源仅支持：${addFromList}' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100015 AS code,'zh_CN' AS locale,'状态仅支持：闲置、使用中、维修中、已出售、已报废、遗失' AS description,'状态仅支持：${statusList}' AS text
)r LEFT JOIN eh_locale_templates t ON r.scope=t.scope AND r.code=t.code AND r.locale=t.locale
WHERE t.id IS NULL;


INSERT INTO eh_fixed_asset_default_categories(id,name,parent_id,path,default_order,creator_uid,create_time)
SELECT r.id,r.name,r.parent_id,r.path,r.default_order,0,NOW() FROM(
SELECT 1 AS id,'土地、房屋及构筑物' AS name,0 AS parent_id,'/1' AS path,1 AS default_order UNION ALL
SELECT 2 AS id,'通用设备' AS name,0 AS parent_id,'/2' AS path,2 AS default_order UNION ALL
SELECT 3 AS id,'专用设备' AS name,0 AS parent_id,'/3' AS path,3 AS default_order UNION ALL
SELECT 4 AS id,'交通运输设备' AS name,0 AS parent_id,'/4' AS path,4 AS default_order UNION ALL
SELECT 5 AS id,'电器设备' AS name,0 AS parent_id,'/5' AS path,5 AS default_order UNION ALL
SELECT 6 AS id,'电子产品及通信设备' AS name,0 AS parent_id,'/6' AS path,6 AS default_order UNION ALL
SELECT 7 AS id,'仪器仪表、计量标准器具及量具、衡器' AS name,0 AS parent_id,'/7' AS path,7 AS default_order UNION ALL
SELECT 8 AS id,'文艺体育设备' AS name,0 AS parent_id,'/8' AS path,8 AS default_order UNION ALL
SELECT 9 AS id,'图书文物及陈列品' AS name,0 AS parent_id,'/9' AS path,9 AS default_order UNION ALL
SELECT 10 AS id,'家具用具及其他类' AS name,0 AS parent_id,'/10' AS path,10 AS default_order
)r LEFT JOIN eh_fixed_asset_default_categories t ON r.id=t.id
WHERE t.id IS NULL;


INSERT INTO eh_service_modules(id,name,parent_id,path,type,level,STATUS,create_time,creator_uid,operator_uid,action_type,multiple_flag,module_control_type,default_order)
VALUE(59000,'固定资产管理',50000,'/50000/59000',1,2,2,NOW(),0,0,72,0,'org_control',0);

-- 增加固定资产模块与域空间的关联信息
SET @id = (SELECT MAX(id) FROM eh_service_module_scopes);
INSERT INTO eh_service_module_scopes(id,namespace_id,module_id,module_name,owner_type,owner_id,apply_policy)
VALUE(@id+1,1,59000,'固定资产管理','EhNamespaces',1,2);

-- 新增菜单配置
INSERT INTO eh_web_menus(id,name,parent_id,data_type,leaf_flag,status,path,type,sort_num,module_id,level,condition_type,category)
SELECT r.id,r.name,r.parent_id,r.data_type,r.leaf_flag,r.status,r.path,r.type,r.sort_num,r.module_id,r.level,'system','module' FROM(
SELECT 16041800 AS id,'固定资产管理' AS name,16040000 AS parent_id,'permanent-asset' AS data_type,1 AS leaf_flag,2 AS status,'/16000000/16040000/16041800' AS path,'zuolin' AS type,18 AS sort_num,59000 AS module_id,3 AS level UNION ALL
SELECT 48180000 AS id,'固定资产管理' AS name,48000000 AS parent_id,'permanent-asset' AS data_type,1 AS leaf_flag,2 AS status,'/48000000/48180000' AS path,'park' AS type,18 AS sort_num,59000 AS module_id,2 AS level UNION ALL
SELECT 72180000 AS id,'固定资产管理' AS name,72000000 AS parent_id,'permanent-asset' AS data_type,1 AS leaf_flag,2 AS status,'/72000000/72180000' AS path,'organization' AS type,18 AS sort_num,59000 AS module_id,2 AS level
)r LEFT JOIN eh_web_menus m ON r.id=m.id
WHERE m.id IS NULL;

-- End by: zhiwei zhang

-- 住总报修地址 by st.zheng
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) VALUES ( 'pmtask.zhuzong.url', 'http://139.129.204.232:8007/LsInterfaceServer', '物业报修', '0');
-- by zheng 初始化communityId
update `eh_lease_promotions`  right join `eh_lease_buildings` on `eh_lease_promotions`.building_id = `eh_lease_buildings`.id
set `eh_lease_promotions`.community_id = `eh_lease_buildings`.community_id
where `eh_lease_promotions`.building_id>0;

update `eh_service_modules` set module_control_type = 'community_control' where id = 40100;

update `eh_general_forms` set template_text = '[{"dataSourceType":"USER_NAME","dynamicFlag":1,"fieldDisplayName":"用户姓名","fieldExtra":"{\"limitWord\":10}","fieldName":"USER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"USER_PHONE","dynamicFlag":1,"fieldDisplayName":"手机号码","fieldExtra":"{\"limitWord\":11}","fieldName":"USER_PHONE","fieldType":"INTEGER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROJECT_NAME","dynamicFlag":1,"fieldDisplayName":"项目名称","fieldExtra":"{\"limitWord\":20}","fieldName":"LEASE_PROJECT_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_BUILDING","dynamicFlag":1,"fieldDisplayName":"楼栋名称","fieldExtra":"{\"limitWord\":20}","fieldName":"LEASE_PROMOTION_BUILDING","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_APARTMENT","dynamicFlag":1,"fieldDisplayName":"门牌号码","fieldExtra":"{\"limitWord\":20}","fieldName":"LEASE_PROMOTION_APARTMENT","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_DESCRIPTION","dynamicFlag":0,"fieldDisplayName":"备注说明","fieldName":"LEASE_PROMOTION_DESCRIPTION","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOM_DATA","dynamicFlag":0,"fieldName":"CUSTOM_DATA","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"visibleType":"HIDDEN"}]'
where owner_type = 'EhLeasePromotions';

update `eh_general_forms` set template_text = '[{"dataSourceType":"USER_NAME","dynamicFlag":1,"fieldDisplayName":"用户姓名","fieldExtra":"{\"limitWord\":10}","fieldName":"USER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"USER_PHONE","dynamicFlag":1,"fieldDisplayName":"手机号码","fieldExtra":"{\"limitWord\":11}","fieldName":"USER_PHONE","fieldType":"INTEGER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROJECT_NAME","dynamicFlag":1,"fieldDisplayName":"项目名称","fieldExtra":"{\"limitWord\":20}","fieldName":"LEASE_PROJECT_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_BUILDING","dynamicFlag":1,"fieldDisplayName":"楼栋名称","fieldExtra":"{\"limitWord\":20}","fieldName":"LEASE_PROMOTION_BUILDING","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_DESCRIPTION","dynamicFlag":0,"fieldDisplayName":"备注说明","fieldName":"LEASE_PROMOTION_DESCRIPTION","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOM_DATA","dynamicFlag":0,"fieldName":"CUSTOM_DATA","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"visibleType":"HIDDEN"}]'
where owner_type = 'EhBuildings';

update `eh_general_forms` set template_text = '[{"dataSourceType":"USER_NAME","dynamicFlag":1,"fieldDisplayName":"用户姓名","fieldExtra":"{\"limitWord\":10}","fieldName":"USER_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"USER_PHONE","dynamicFlag":1,"fieldDisplayName":"手机号码","fieldExtra":"{\"limitWord\":11}","fieldName":"USER_PHONE","fieldType":"INTEGER_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROJECT_NAME","dynamicFlag":1,"fieldDisplayName":"项目名称","fieldExtra":"{\"limitWord\":20}","fieldName":"LEASE_PROJECT_NAME","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"LEASE_PROMOTION_DESCRIPTION","dynamicFlag":0,"fieldDisplayName":"备注说明","fieldName":"LEASE_PROMOTION_DESCRIPTION","fieldType":"MULTI_LINE_TEXT","renderType":"DEFAULT","requiredFlag":0,"validatorType":"TEXT_LIMIT","visibleType":"EDITABLE"},{"dataSourceType":"CUSTOM_DATA","dynamicFlag":0,"fieldName":"CUSTOM_DATA","fieldType":"SINGLE_LINE_TEXT","renderType":"DEFAULT","requiredFlag":1,"visibleType":"HIDDEN"}]'
where owner_type = 'EhLeaseProjects';

update `eh_locale_templates` set text = '[{"key":"预约楼栋","value":"${applyBuilding}","entityType":"list"},{"key":"姓名","value":"${applyUserName}","entityType":"list"},{"key":"手机号","value":"${contactPhone}","entityType":"list"},{"key":"申请来源","value":"${sourceType}","entityType":"list"},{"key":"备注","value":"${description}","entityType":"multi_line"}]'
where scope = 'expansion' and code = '2';

-- 人事2.6新需求 by ryan 03/21/2018
SET @locale_id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1001', 'zh_CN', '通讯录删除');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1002', 'zh_CN', '通讯录成员列表');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1003', 'zh_CN', '人员档案导入模板');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1004', 'zh_CN', '人员档案列表');
INSERT INTO `eh_locale_strings`(`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '1005', 'zh_CN', '填写须知：\r\n    1、请不要对员工信息类别进行增加、删除或修改，以免无法识别员工信息；\r\n    2、Excel中红色字段为必填字段,黑色字段为选填字段\r\n    3、请不要包含公式，以免错误识别员工信息；\r\n    4、多次导入时，若系统中已存在相同手机号码的员工，将以导入的信息为准；\r\n    5、部门：上下级部门间用‘/’隔开，且从最上级部门开始，例如\\\"左邻/深圳研发中心/研发部\\\"；部门若为空，则自动将成员添加到选择的目录下；\r\n    6、手机：支持国内、国际手机号（国内手机号直接输入手机号即可；国际手机号必须包含加号以及国家地区码，格式示例：“+85259****24”）；\r\n    7、合同公司：合同公司若为空，将默认使用公司全称\r\n    8、若要删除某行信息，请右键行号，选择删除\r\n    9、注意日期格式为 xxxx-xx-xx');

SET @template_id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 5, 'zh_CN', '人事提醒开头', '你好，${contactName}\r\n\r\n\r\n${companyName}近一周需要注意的人事日程如下：\r\n\r\n\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 6, 'zh_CN', '人事转正提醒', '${contactNames} 转正\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 7, 'zh_CN', '人事合同到期提醒', '${contactNames} 合同到期\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 8, 'zh_CN', '人事身份证到期提醒', '${contactNames} 身份证到期\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 9, 'zh_CN', '人事周年提醒', '${contactName} 在${companyName}工作满 ${year} 年\r\n', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'archives.notification', 10, 'zh_CN', '人事生日提醒', '${contactName} ${year} 岁生日\r\n', 0);
-- end by ryan

-- Designer: zhiwei zhang
-- Description: ISSUE#26208 日程1.0


INSERT INTO eh_remind_settings(id,name,offset_day,fix_time,default_order,creator_uid,create_time)
SELECT r.id,r.name,r.offset_day,'09:00',r.default_order,0,NOW() FROM(
SELECT 1 AS id,'当天（09:00）' AS name,0 AS offset_day,1 AS default_order UNION ALL
SELECT 2 AS id,'提前1天（09:00）' AS name,1 AS offset_day,2 AS default_order UNION ALL
SELECT 3 AS id,'提前2天（09:00）' AS name,2 AS offset_day,3 AS default_order UNION ALL
SELECT 4 AS id,'提前3天（09:00）' AS name,3 AS offset_day,4 AS default_order UNION ALL
SELECT 5 AS id,'提前5天（09:00）' AS name,5 AS offset_day,5 AS default_order UNION ALL
SELECT 6 AS id,'提前1周（09:00）' AS name,7 AS offset_day,6 AS default_order
)r LEFT JOIN eh_remind_settings s ON r.id=s.id
WHERE s.id IS NULL;

INSERT INTO eh_configurations(name,value,namespace_id)
SELECT r.name,r.value,r.namespace_id FROM
(
SELECT 'remind.colour.list' AS name,'#B3FF3B30,#FFF58F3E,#FFF2C500,#FF4AD878,#FF00B9EF,#FF4285F4,#B3673AB7' AS value,0 AS namespace_id union all
SELECT 'remind.colour.share' AS name,'#FF6E6E74' AS value,0 AS namespace_id
)r LEFT JOIN eh_configurations c ON r.name=c.name
WHERE c.id IS NULL;


INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'calendarRemind' AS scope,10000 AS code,'zh_CN' AS locale,'分类名称已存在' AS text UNION ALL
SELECT 'calendarRemind' AS scope,10001 AS code,'zh_CN' AS locale,'此日程已被删除' AS text UNION ALL
SELECT 'calendarRemind' AS scope,10002 AS code,'zh_CN' AS locale,'此日程已取消共享' AS text UNION ALL
SELECT 'calendarRemind' AS scope,10003 AS code,'zh_CN' AS locale,'最后一个分类不能删除' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;



-- volgo 添加日程图标.
SET @item_id = (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
VALUES (@item_id := @item_id + 1, '1', '0', '0', '0', '/home', 'Bizs', '日程', '日程', 'cs://1/image/aW1hZ2UvTVRvNE5XWXdPVFl6Wm1SaVpqWmxaVFJpT0RkaE56WXpNR1EyTnpsa1ptSmpZZw', '1', '1', '73', '{"title":"日程"}', '11', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL, '0', NULL,NULL);


-- End by: zhiwei zhang
